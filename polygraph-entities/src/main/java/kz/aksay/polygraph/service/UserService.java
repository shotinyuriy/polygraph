package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.User;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractGenericService<User, Long>
	implements IUserService {
	
	private GenericDao<User, Long> userDao;

	public User findByLogin(String login) {
		User user = null;
		
		if (login != null && !login.isEmpty()) {
			Criteria criteria = getDao().getSession().createCriteria(userDao.clazz());
			criteria.add(Restrictions.like("login", login));
			user = getDao().readUniqueByCriteria(criteria);
		}
		return user;
	}

	public User findByLoginAndPassword(String login, String password) {
		User user = null;
		if (login != null && !login.isEmpty()) {
			Criteria criteria = getDao().getSession().createCriteria(userDao.clazz());
			criteria.add(Restrictions.like("login", login));
			criteria.add(Restrictions.like("password", password));
			user = getDao().readUniqueByCriteria(criteria);
		}
		return user;
	}
	
	public User findByEmployee(Employee employee) {
		User user = null;
		if(employee != null) {
			Criteria criteria = getDao().getSession().createCriteria(User.class);
			criteria.add(Restrictions.eq("employee.id", employee.getId()));
			user = getDao().readUniqueByCriteria(criteria);
		}
		return user;
	}

	@Autowired
	public void setUserDao(GenericDao<User, Long> userDao) {
		this.userDao = userDao;
	}

	@Override
	protected GenericDao<User, Long> getDao() {
		return userDao;
	}
	
}
