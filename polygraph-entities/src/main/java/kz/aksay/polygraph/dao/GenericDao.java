package kz.aksay.polygraph.dao;

import java.io.Serializable;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;


public interface GenericDao <T, PK extends Serializable> {
	
	public Class<T> clazz();
	
	public PK create(T newEntity) throws Exception;
	
	public void createOrUpdate(T entity)  throws Exception;
	
	public T read(PK pimaryKey);
	
	void update(T entity) throws Exception;
	
	void delete(T entity);
	
	public List<T> readAll();
	
	public List<T> readByCriteria(Criteria criteria);
	
	public <T> T readUniqueByCriteria(Criteria criteria);
	
	public Session getSession();
}
