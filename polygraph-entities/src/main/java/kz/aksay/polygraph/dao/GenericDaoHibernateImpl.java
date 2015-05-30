package kz.aksay.polygraph.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import kz.aksay.polygraph.exception.InternalLogicException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoHibernateImpl<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	private SessionFactory sessionFactory;

	private Class<T> clazz;

	private Validator validator;

	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if (clazz == null) {

			Class<?> actualClass = this.getClass();
			ParameterizedType pt = (ParameterizedType) actualClass
					.getGenericSuperclass();
			clazz = (Class<T>) pt.getActualTypeArguments()[0];
		}
		return clazz;
	}

	public void validate(T entity) throws ValidationException {
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(entity);
		if (!constraintViolations.isEmpty()) {
			StringBuffer exceptionBuffer = new StringBuffer();
			Iterator<ConstraintViolation<T>> iter = constraintViolations
					.iterator();
			while (iter.hasNext()) {
				ConstraintViolation<T> constraintViolation = iter.next();
				exceptionBuffer.append(constraintViolation.getPropertyPath())
				.append(" ")
				.append(constraintViolation.getMessage());
				if (iter.hasNext()) {
					exceptionBuffer.append("\n");
				}
				throw new ValidationException(exceptionBuffer.toString());
			}
		}
	}

	public T read(PK id) {
		return (T) getSession().get(clazz(), id);
	}

	@SuppressWarnings("unchecked")
	public PK create(T entity) throws Exception {
		validate(entity);
		getSession().saveOrUpdate(entity);
		PK pk = (PK) getSession().save(entity);
		getSession().flush();
		return pk;
	}

	@SuppressWarnings("unchecked")
	public void update(T entity)  throws Exception {
		validate(entity);
		getSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	public void createOrUpdate(T entity)  throws Exception  {
		validate(entity);
		getSession().saveOrUpdate(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	public int deleteAll() {
		return getSession().createQuery("DELETE FROM "+clazz().getName()).
				executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<T> readAll() {
		return (List<T>) getSession().createCriteria(clazz()).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> readAll(int offset, int limit) {
		return (List<T>) getSession().createCriteria(clazz()).setFirstResult(offset).setMaxResults(limit).list();
	}

	@Override
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException he) {
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
		return (List<T>) criteria.list();
	}

	@Override
	public <T> T readUniqueByCriteria(Criteria criteria) {
		return (T) criteria.uniqueResult();
	}

	public Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
