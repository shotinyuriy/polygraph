package kz.aksay.polygraph.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoHibernateImpl<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	private SessionFactory sessionFactory;

	private Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if(clazz == null) {
			
			Class<?> actualClass = this.getClass();
			ParameterizedType pt = 
					(ParameterizedType) actualClass.getGenericSuperclass();
			clazz = (Class<T>) pt.getActualTypeArguments()[0];
		}
		return clazz;
	}

	public T read(PK id) {
		return (T) getSession().get(clazz(), id);
	}
	
	@SuppressWarnings("unchecked")
	public PK create(T entity) {	
		getSession().saveOrUpdate(entity);
		return (PK) getSession().save(entity);
	}

	@SuppressWarnings("unchecked")
	public void update(T entity) {
		getSession().update(entity);
	}
	
	@SuppressWarnings("unchecked")
	public void createOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> readAll() {
		return (List<T>) getSession().createCriteria(clazz()).list();
	}

	@Override
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}
		catch(HibernateException he) {
			return sessionFactory.openSession();
		}
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> readByCriteria(Criteria criteria) {
		// TODO Auto-generated method stub
		return (List<T>)criteria.list();
	}
	
	@Override
	public <T> T readUniqueByCriteria(Criteria criteria) {
		return (T)criteria.uniqueResult();
	}

}
