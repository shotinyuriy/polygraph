package kz.aksay.polygraph.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kz.aksay.polygraph.entity.Subject;

@Repository
public class CustomerDao extends GenericDaoHibernateImpl<Subject, Long> {

	private PersonDao personDao;
	private OrganizationDao organizationDao;
	
/*	@Override
	public Customer read(Long id) {
		Customer customer = personDao.read(id);
		if(customer == null) {
			customer = organizationDao.read(id);
		}
		return customer;
	}*/
	
	public PersonDao getPersonDao() {
		return personDao;
	}
	
	@Autowired
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	public OrganizationDao getOrganizationDao() {
		return organizationDao;
	}
	
	@Autowired
	public void setOrganizationDao(OrganizationDao organizationDao) {
		this.organizationDao = organizationDao;
	}
}
