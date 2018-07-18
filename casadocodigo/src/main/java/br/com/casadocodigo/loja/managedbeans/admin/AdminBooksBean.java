package br.com.casadocodigo.loja.managedbeans.admin;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.daos.BookDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.infra.MessageHelper;
import br.com.casadocodigo.loja.models.Book;

@Model
public class AdminBooksBean {

	private Book product = new Book();

	private Part summary;

	@Inject
	private BookDAO bookDAO;
 
	@PostConstruct
	public void loadObjects() {

	}

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private FileSaver fileSaver;

	@Transactional
	public String save() {
		String summaryPath = fileSaver.write("summaries", summary);
		System.out.println(summaryPath);
		product.setSummaryPath(summaryPath);
		bookDAO.save(product);
		messageHelper.addInfoMessage(null, "Livro cadastrado com sucesso!", "").keepMessages();
		clearObjects();
		return "/livros/lista?faces-redirect=true";
	}

	private void clearObjects() {
		this.product = new Book();
	}

	// GETTERS ANS SETTERS

	public Book getProduct() {
		return product;
	}

	public void setProduct(Book product) {
		this.product = product;
	}

	public Part getSummary() {
		return summary;
	}

	public void setSummary(Part summary) {
		this.summary = summary;
	}

}
