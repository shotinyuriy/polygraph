package kz.aksay.polygraph.test.service;

import java.util.Date;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.service.ProducedWorkService;
import kz.aksay.polygraph.service.UserService;
import kz.aksay.polygraph.test.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDesignerBaseScenario {

	private ApplicationContext context;
	
	private UserService userService;
	private EmployeeTypeService employeeTypeService;
	private EmployeeService employeeService;
	private PersonService personService;
	private OrganizationService organizationService;
	private OrderService orderService;
	private ProducedWorkService producedWorkService;
	
	private final String executorLogin = "executorLogin";
	private final String executorPassword = "exectorPassword";

	private Person executorPerson;
	private Employee executorEmployee;
	private User executorUser;
	private Order firstOrder;
	private Organization organizationCustomer;
	private ProducedWork producedWork;

	@BeforeClass
	public void setUp()	{
		context = ContextUtils.getApplicationContext();
		userService = context.getBean(UserService.class);
		employeeTypeService = context.getBean(EmployeeTypeService.class);
		employeeService = context.getBean(EmployeeService.class);
		personService = context.getBean(PersonService.class);
		organizationService = context.getBean(OrganizationService.class);
		orderService = context.getBean(OrderService.class);
	}
	
	@Test
	public void test() {
		createPerson();
		createEmployee();
		createUser();
		createOrganizationCustomer();
		createOrder();
		createProducedWork();
	}

	private void createPerson() {
		executorPerson = new Person();
		executorPerson.setCreatedAt(new Date());
		executorPerson.setCreatedBy(User.TECH_USER);
		executorPerson.setLastName("Тестов");
		executorPerson.setFirstName("Тест");
		executorPerson.setMiddleName("Тестович");
		executorPerson.setBirthDate(new Date());
		personService.save(executorPerson);
	}
	
	
	private void createEmployee() {
		executorEmployee = new Employee();
		executorEmployee.setCreatedAt(new Date());
		executorEmployee.setCreatedBy(User.TECH_USER);
		executorEmployee.setPerson(executorPerson);
		executorEmployee.setType(
				employeeTypeService.findByName(EmployeeType.DefaultNames.DESIGNER));
		employeeService.save(executorEmployee);
	}
	
	private void createUser() {
		executorUser = new User();
		executorUser.setCreatedAt(new Date());
		executorUser.setCreatedBy(User.TECH_USER);
		executorUser.setEmployee(executorEmployee);
		executorUser.setLogin(executorLogin);
		executorUser.setPassword(executorPassword);
		userService.save(executorUser);
	}

	private void createOrganizationCustomer() {
		organizationCustomer = new Organization();
		organizationCustomer.setCreatedAt(new Date());
		organizationCustomer.setCreatedBy(User.TECH_USER);
		organizationCustomer.setFullname("Тестовая Организация");
		organizationCustomer.setInn("1231231230");
		organizationCustomer.setKpp("123123123");
		organizationService.save(organizationCustomer);
	}

	private void createOrder() {
		firstOrder = new Order();
		firstOrder.setCreatedAt(new Date());
		firstOrder.setCreatedBy(User.TECH_USER);
		firstOrder.setCustomer(organizationCustomer);
		firstOrder.setDescription("Описание заказа");
	}

	private void createProducedWork() {
		producedWork = new ProducedWork();
		producedWork.setOrder(firstOrder);
		
	}
}
