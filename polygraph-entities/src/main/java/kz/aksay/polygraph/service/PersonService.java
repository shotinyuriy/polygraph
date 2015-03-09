package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Person;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends AbstractGenericService<Person, Long> 
	implements IPersonService {
	
	private GenericDao<Person, Long> personDao;
	
	public Person find(Long id) {
		return personDao.read(id);
	}
	
	public Person findSimilarPerson(final Person person) {
		Person similarPerson = null;
		
		Criteria criteria = personDao.getSession().createCriteria(Person.class);
		criteria.add(Restrictions.ilike("firstName", person.getFirstName()));
		criteria.add(Restrictions.ilike("middleName", person.getMiddleName()));
		criteria.add(Restrictions.ilike("lastName", person.getLastName()));
		criteria.add(Restrictions.eq("birthDate", person.getBirthDate()));
		criteria.setMaxResults(1);
		
		similarPerson = personDao.readUniqueByCriteria(criteria);
		
		return similarPerson;
	}
	
	@Autowired
	public void setPersonDao(GenericDao<Person, Long> personDao) {
		this.personDao = personDao;
	}

	@Override
	protected GenericDao<Person, Long> getDao() {
		return personDao;
	}
	
}
