package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.jpa.QueryHints;

import br.com.casadocodigo.loja.models.Book;

public class BookDAO {

	@PersistenceContext
	private EntityManager manager;

	public BookDAO() {

	}

	public BookDAO(EntityManager manager) {
		super();
		this.manager = manager;
	}

	public void save(Book product) {
		manager.persist(product);
	}

	public List<Book> lastReleases() {
		TypedQuery<Book> query = manager
				.createQuery("select b from Book b where b.releaseDate <= now() order by b.id desc", Book.class)
				.setMaxResults(3);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "home");
		return query.getResultList();
	}

	public List<Book> last(int number) {
		TypedQuery<Book> query = manager.createQuery("select b from Book b join fetch b.authors", Book.class)
				.setMaxResults(number);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "home");
		return query.getResultList();
	}

	public List<Book> list() {
		TypedQuery<Book> query = manager.createQuery("SELECT DISTINCT(b) FROM Book b JOIN FETCH b.authors", Book.class);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		return query.getResultList();

	}

	public List<Book> olderBooks() {
		return manager.createQuery("select b from Book b", Book.class).setMaxResults(20).getResultList();
	}

	public Book findById(Integer id) {
		return manager.find(Book.class, id);
	}
}
