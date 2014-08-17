package kz.aksay.polygraph.test.service;

import java.util.Date;

import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.test.ContextUtils;
import kz.aksay.polygraph.util.TrowableUtils;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestEmployeeTypeService extends Assert {

	private ApplicationContext context;
	
	private EmployeeTypeService employeeTypeService;
	
	@BeforeClass
	public void setUp() {
		context = ContextUtils.getApplicationContext();
		employeeTypeService = context.getBean(EmployeeTypeService.class);
	}
	
	@Test(expectedExceptions=PropertyValueException.class)
	public void testSaveWithNullName() {
		EmployeeType employeeType = new EmployeeType();
		employeeType.setCreatedAt(new Date());
		employeeType.setCreatedBy(User.TECH_USER);
		employeeType.setName(null);
		employeeTypeService.save(employeeType);
	}
	
	@Test(expectedExceptions=ConstraintViolationException.class)
	public void testSaveWithSameName() {
		EmployeeType employeeType = new EmployeeType();
		employeeType.setCreatedAt(new Date());
		employeeType.setCreatedBy(User.TECH_USER);
		employeeType.setName("А");
		employeeTypeService.save(employeeType);
		
		
		EmployeeType employeeTypeTwink = new EmployeeType();
		employeeTypeTwink.setCreatedAt(new Date());
		employeeTypeTwink.setCreatedBy(User.TECH_USER);
		employeeTypeTwink.setName("А");
		try {
			employeeTypeService.save(employeeTypeTwink);
		} catch(Exception e) {
			Throwable t = e;
			TrowableUtils.findAndThrowCause(t, ConstraintViolationException.class);
		}
	}
}
