package br.com.casadocodigo.loja.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.SystemUser;

public class SystemUserDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void save(SystemUser systemUser) {
		entityManager.persist(systemUser);
	}
}
