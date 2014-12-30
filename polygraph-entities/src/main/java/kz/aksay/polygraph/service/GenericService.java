package kz.aksay.polygraph.service;

import java.io.Serializable;
import java.util.List;

import kz.aksay.polygraph.dao.GenericDao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericService<T, PK extends Serializable> {
	
	protected abstract GenericDao<T, PK> getDao();
	
	public T find(PK id) {
		return getDao().read(id);
	}
	
	public List<T> findAll() {
		return getDao().readAll();
	}
	
	public List<T> findAll(int offset, int limit) {
		return getDao().readAll(offset, limit);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public T save(T entity) throws Exception {
		
		getDao().createOrUpdate(entity);
		
		return entity;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(T entity) {
		getDao().delete(entity);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteAll() {
		return getDao().deleteAll();
	}
}
