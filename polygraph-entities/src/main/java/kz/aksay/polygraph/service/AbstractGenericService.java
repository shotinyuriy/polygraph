package kz.aksay.polygraph.service;

import java.io.Serializable;
import java.util.List;

import kz.aksay.polygraph.api.IGenericService;
import kz.aksay.polygraph.dao.GenericDao;

import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractGenericService<T, PK extends Serializable> 
	implements IGenericService<T, PK> {
	
	protected abstract GenericDao<T, PK> getDao();
	
	@Override
	public T find(PK id) {
		return getDao().read(id);
	}
	
	@Override
	public List<T> findAll() {
		return getDao().readAll();
	}
	
	@Override
	public List<T> findAll(int offset, int limit) {
		return getDao().readAll(offset, limit);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T save(T entity) throws Exception {
		
		getDao().createOrUpdate(entity);
		
		return entity;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(T entity) {
		getDao().delete(entity);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteAll() {
		return getDao().deleteAll();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED)
	public List<T> findByExample(T example) {
		return (List<T>) getDao().getSession().createCriteria(getDao().clazz())
				.add(Example.create(example)).list();
	}
}
