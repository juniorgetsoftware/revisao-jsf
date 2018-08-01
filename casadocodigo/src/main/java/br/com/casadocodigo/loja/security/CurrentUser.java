package br.com.casadocodigo.loja.security;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.casadocodigo.loja.daos.SecurityDAO;
import br.com.casadocodigo.loja.exceptions.UsernameNotFoundException;
import br.com.casadocodigo.loja.models.SystemUser;

@Model
public class CurrentUser {

	@Inject
	private HttpServletRequest request;

	@Inject
	private SecurityDAO securityDAO;

	private SystemUser systemUser;

	public SystemUser get() {
		return this.systemUser;
	}

	@PostConstruct
	private void loadSystemUser() throws UsernameNotFoundException {
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			this.systemUser = securityDAO.loadUserByUsername(principal.getName());
		}
	}

	public boolean hasRole(String name) {
		return request.isUserInRole(name);
	}
}