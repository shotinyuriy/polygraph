package kz.aksay.polygraph.test.service;

import java.util.Date;

import org.springframework.context.ApplicationContext;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import kz.aksay.polygraph.entity.Family;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.test.ContextUtils;

public class TestPersonService extends Assert {
	
	private ApplicationContext context;
	
	private PersonService personService;
	
	private Date now = new Date();
	
	private Person person;
	
	@BeforeClass
	public void setUp() {
		context = ContextUtils.getApplicationContext();
		personService = context.getBean(PersonService.class);
		person = createPerson();
	}
	
	private Person createPerson() {
		Person person = new Person();
		
		person.setCreatedBy(User.TECH_USER);
		person.setCreatedAt(now);
		person.setFirstName("Тест");
		person.setLastName("Тест");
		person.setMiddleName("Тест");
		person.setBirthDate(now);
		person.setFamily(createFamily());
		personService.save(person);
		return person;
	}
	
	private Family createFamily() {
		Family family = new Family();
		family.setCreatedAt(now);
		family.setCreatedBy(User.TECH_USER);
		return family;
	}
	
	@Test
	public void testSave() {
		person = personService.save(person);
		assertNotNull(person);
		assertNotNull(person.getId());
		
		personService.delete(person);
	}
	
	@AfterClass
	public void cleanUp() {
		
	}
}
