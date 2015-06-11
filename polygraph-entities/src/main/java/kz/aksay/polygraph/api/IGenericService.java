package kz.aksay.polygraph.api;

import java.io.Serializable;
import java.util.List;

import kz.aksay.polygraph.dao.GenericDao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IGenericService<T, PK extends Serializable> {
	
	public T find(PK id);
	
	public List<T> findAll();
	
	public List<T> findAll(int offset, int limit);
	
	public T save(T entity) throws Exception;
	
	public void delete(T entity);
	
	public int deleteAll();

	public List<T> findByExample(T example);
}
