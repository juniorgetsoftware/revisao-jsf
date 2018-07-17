package br.com.casadocodigo.loja.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ApplicationScoped
public class MessageHelper {

	@Inject
	private FacesContext facesContext;

	private void addMessage(String id, Severity severity, String summary, String detail) {
		facesContext.addMessage(id, new FacesMessage(severity, summary, detail));
	}
	
	public MessageHelper addInfoMessage(String id, String summary, String detail) {
		this.addMessage(id, FacesMessage.SEVERITY_INFO, summary, detail);
		return this;
	}
	
	public MessageHelper addWarnMessage(String id, String summary, String detail) {
		this.addMessage(id, FacesMessage.SEVERITY_WARN, summary, detail);
		return this;
	}
	
	public MessageHelper addFatalMessage(String id, String summary, String detail) {
		this.addMessage(id, FacesMessage.SEVERITY_FATAL, summary, detail);
		return this;
	}
	
	public MessageHelper addErrorMessage(String id, String summary, String detail) {
		this.addMessage(id, FacesMessage.SEVERITY_ERROR, summary, detail);
		return this;
	}
	
	public void keepMessages() {
		this.facesContext.getExternalContext().getFlash().setKeepMessages(true);
	}

}
