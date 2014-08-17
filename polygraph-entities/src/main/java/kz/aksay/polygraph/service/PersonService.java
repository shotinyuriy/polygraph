package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends GenericService<Person, Long> {
	
	private GenericDao<Person, Long> personDao;
	
	public Person find(Long id) {
		return personDao.read(id);
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
