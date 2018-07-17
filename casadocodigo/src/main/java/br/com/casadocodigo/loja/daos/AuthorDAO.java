package br.com.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import br.com.casadocodigo.loja.models.Author;

public class AuthorDAO {

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	public void save(Author author) {
		manager.persist(author);
	}

	public List<Author> list() {
		return manager.createQuery(
				"SELECT a FROM Author a ORDER BY a.name ASC", Author.class)
			.getResultList();
	}
	
}
