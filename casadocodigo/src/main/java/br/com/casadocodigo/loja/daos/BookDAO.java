package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.Book;

public class BookDAO {

	@PersistenceContext // (unitName = "casadocodigo-persistence-unit")
	private EntityManager manager;

	@Transactional
	public void save(Book product) {
		manager.persist(product);
	}

	public List<Book> list() {
		return manager.createQuery(
				"SELECT DISTINCT(b) FROM Book b JOIN FETCH b.authors", Book.class)
				.getResultList();
	}
}
