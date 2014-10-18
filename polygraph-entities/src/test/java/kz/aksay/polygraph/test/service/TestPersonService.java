package kz.aksay.polygraph.test.service;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
	public void setUp() throws Exception {
		context = ContextUtils.getApplicationContext();
		personService = context.getBean(PersonService.class);
		person = createPerson();
	}
	
	private Person createPerson() throws Exception {
		Person person = new Person();
		
		person.setCreatedBy(User.TECH_USER);
		person.setCreatedAt(now);
		person.setFirstName("Тест");
		person.setLastName("Тест");
		person.setMiddleName("Тест");
		person.setBirthDate(now);
		personService.save(person);
		return person;
	}
	
	@Test
	public void testSave() throws Exception {
		person = personService.save(person);
		assertNotNull(person);
		assertNotNull(person.getId());
		
		personService.delete(person);
	}
	
	@Test
	public void testFildAll() {
		List<Person> allPersons = personService.findAll();
		assertNotNull(allPersons);
	}
	
	@AfterClass
	public void cleanUp() {
		
	}
}
