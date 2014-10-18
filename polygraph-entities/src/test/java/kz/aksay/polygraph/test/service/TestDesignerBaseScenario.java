package kz.aksay.polygraph.test.service;

import java.math.BigDecimal;
import java.util.Date;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.User.Role;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.service.ProducedWorkService;
import kz.aksay.polygraph.service.UserService;
import kz.aksay.polygraph.service.WorkTypeService;
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
	private WorkTypeService workTypeService;
	private MaterialTypeService materialTypeService;
	private MaterialService materialService;
	private MaterialConsumptionService materialConsumptionService;
	
	private final String executorLogin = "executorLogin";
	private final String executorPassword = "exectorPassword";

	private Person executorPerson;
	private Employee executorEmployee;
	private User executorUser;
	private Order firstOrder;
	private Organization organizationCustomer;
	private WorkType xerocopy;
	private MaterialType paper;
	private Material paperA4;
	private ProducedWork producedWork;
	private MaterialConsumption copyMaterialConsumption;

	@BeforeClass
	public void setUp()	{
		context = ContextUtils.getApplicationContext();
		userService = context.getBean(UserService.class);
		employeeTypeService = context.getBean(EmployeeTypeService.class);
		employeeService = context.getBean(EmployeeService.class);
		personService = context.getBean(PersonService.class);
		organizationService = context.getBean(OrganizationService.class);
		orderService = context.getBean(OrderService.class);
		producedWorkService = context.getBean(ProducedWorkService.class);
		workTypeService = context.getBean(WorkTypeService.class);
		materialTypeService = context.getBean(MaterialTypeService.class);
		materialService = context.getBean(MaterialService.class);
		materialConsumptionService = context.getBean(MaterialConsumptionService.class);
	}
	
	@Test
	public void test() throws Exception {
		try {
			createPerson();
			createEmployee();
			createUser();
			createOrganizationCustomer();
			createOrder();
			createWorkTypeXerocopy();
			createMaterialTypePaper();
			createMaterialPaperA4();
			createProducedWork();
			createMaterialConsumption();
		} 
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			deleteAll();
		}
	}

	private void createPerson() throws Exception {
		executorPerson = new Person();
		executorPerson.setCreatedAt(new Date());
		executorPerson.setCreatedBy(User.TECH_USER);
		executorPerson.setLastName("Тестов");
		executorPerson.setFirstName("Тест");
		executorPerson.setMiddleName("Тестович");
		executorPerson.setBirthDate(new Date());
		personService.save(executorPerson);
	}
	
	
	private void createEmployee() throws Exception {
		executorEmployee = new Employee();
		executorEmployee.setCreatedAt(new Date());
		executorEmployee.setCreatedBy(User.TECH_USER);
		executorEmployee.setPerson(executorPerson);
		executorEmployee.setType(
				employeeTypeService.findByName(EmployeeType.DefaultNames.DESIGNER));
		employeeService.save(executorEmployee);
	}
	
	private void createUser() throws Exception {
		executorUser = new User();
		executorUser.setCreatedAt(new Date());
		executorUser.setCreatedBy(User.TECH_USER);
		executorUser.setEmployee(executorEmployee);
		executorUser.setLogin(executorLogin);
		executorUser.setPassword(executorPassword);
		executorUser.setRole(Role.DESIGNER);
		userService.save(executorUser);
	}

	private void createOrganizationCustomer() throws Exception {
		organizationCustomer = new Organization();
		organizationCustomer.setCreatedAt(new Date());
		organizationCustomer.setCreatedBy(User.TECH_USER);
		organizationCustomer.setFullname("Тестовая Организация");
		organizationCustomer.setShortname("Тестовая Организация");
		organizationCustomer.setInn("1231231230");
		organizationCustomer.setKpp("123123123");
		organizationService.save(organizationCustomer);
	}
	
	private void createWorkTypeXerocopy() throws Exception {
		xerocopy = new WorkType();
		xerocopy.setCreatedAt(new Date());
		xerocopy.setCreatedBy(User.TECH_USER);
		xerocopy.setName("КСЕРОКОПИЯ");
		workTypeService.save(xerocopy);
		xerocopy = workTypeService.find(xerocopy.getId());
		System.out.println("XEROCOPY "+xerocopy);
	}
	
	private void createMaterialTypePaper() throws Exception {
		paper = new MaterialType();
		paper.setCreatedAt(new Date());
		paper.setCreatedBy(User.TECH_USER);
		paper.setName("КРАСКА");
		materialTypeService.save(paper);
	}
	
	private void createMaterialPaperA4() throws Exception {
		paperA4 = new Material();
		paperA4.setCreatedAt(new Date());
		paperA4.setCreatedBy(User.TECH_USER);
		paperA4.setName("А4");
		materialService.save(paperA4);
	}

	private void createOrder() throws Exception {
		firstOrder = new Order();
		firstOrder.setCreatedAt(new Date());
		firstOrder.setCreatedBy(User.TECH_USER);
		firstOrder.setCustomer(organizationCustomer);
		firstOrder.setCurrentExecutor(executorEmployee);
		firstOrder.setDescription("Описание заказа на ксерокопию");
		orderService.save(firstOrder);
	}

	private void createProducedWork() throws Exception {
		producedWork = new ProducedWork();
		producedWork.setCreatedAt(new Date());
		producedWork.setCreatedBy(User.TECH_USER);
		producedWork.setOrder(firstOrder);
		producedWork.setWorkType(xerocopy);
		producedWork.setExecutor(executorEmployee);
		producedWorkService.save(producedWork);
	}
	
	private void createMaterialConsumption() throws Exception {
		copyMaterialConsumption = new MaterialConsumption();
		copyMaterialConsumption.setCreatedAt(new Date());
		copyMaterialConsumption.setCreatedBy(User.TECH_USER);
		copyMaterialConsumption.setMaterial(paperA4);
		copyMaterialConsumption.setProducedWork(producedWork);
		copyMaterialConsumption.setQuantity(BigDecimal.valueOf(1.0));
		materialConsumptionService.save(copyMaterialConsumption);
	}
	
	private void deleteAll() {		
		if(copyMaterialConsumption != null) materialConsumptionService.delete(copyMaterialConsumption);
		if(producedWork != null) producedWorkService.delete(producedWork);
		if(paperA4 != null) materialService.delete(paperA4);
		if(paper != null) materialTypeService.delete(paper);
		if(xerocopy != null) workTypeService.delete(xerocopy);
		if(firstOrder != null) orderService.delete(firstOrder);
		if(organizationCustomer != null) organizationService.delete(organizationCustomer);
		if(executorUser != null) userService.delete(executorUser);
		if(executorEmployee != null) employeeService.delete(executorEmployee);
		if(executorPerson != null) personService.delete(executorPerson);
	}
}
