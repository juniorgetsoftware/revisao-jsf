package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.Book;

public class BookDAO {

	@PersistenceContext
	private EntityManager manager;

	public BookDAO() {
	}
	
	public BookDAO(EntityManager manager) {
		this.manager = manager;
	}

	@Transactional
	public void save(Book product) {
		manager.persist(product);
	}

	public List<Book> list() {
		return manager.createQuery("SELECT DISTINCT(b) FROM Book b JOIN FETCH b.authors", Book.class).getResultList();
	}

	public List<Book> lastReleases() {
		return manager.createQuery("select b from Book b where b.releaseDate <= now() order by b.id desc", Book.class)
				.setMaxResults(3).getResultList();
	}

	public List<Book> olderBooks() {
		return manager.createQuery("select b from Book b", Book.class).setMaxResults(20).getResultList();
	}

	public Book findById(Integer id) {
		return manager.find(Book.class, id);
	}
}
