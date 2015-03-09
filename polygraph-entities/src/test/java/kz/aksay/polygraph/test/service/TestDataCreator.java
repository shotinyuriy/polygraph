package kz.aksay.polygraph.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;

import sun.security.util.Password;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IFullTextIndexService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IMaterialTypeService;
import kz.aksay.polygraph.api.IOrderFullTextIndexService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Customer;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entity.User.Role;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.FullTextIndexService;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.service.OrderFullTextIndexService;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.service.ProducedWorkService;
import kz.aksay.polygraph.service.UserService;
import kz.aksay.polygraph.service.WorkTypeService;
import kz.aksay.polygraph.test.ContextUtils;
import kz.aksay.polygraph.util.GeneratorUtils;
import kz.aksay.polygraph.util.OrgNameGenerator;
import kz.aksay.polygraph.util.OrgNameGenerator.OrgName;
import kz.aksay.polygraph.util.PersonNameGenerator.FullName;
import kz.aksay.polygraph.util.StringUtils;

public class TestDataCreator {
	
	private static final int PERSON_COUNT = 50;
	
	private ApplicationContext context;
	private IUserService userService;
	private IEmployeeService employeeService;
	private IPersonService personService;
	private IOrganizationService organizationService;
	private IOrderService orderService;
	private IProducedWorkService producedWorkService;
	private IWorkTypeService workTypeService;
	private IMaterialTypeService materialTypeService;
	private IMaterialService materialService;
	private IMaterialConsumptionService materialConsumptionService;
	private IOrderFullTextIndexService orderFullTextIndexService;
	private IFullTextIndexService fullTextIndexService;

	public TestDataCreator(ApplicationContext context) {
		this.context = context;
		setUp();
	}

	public void setUp() {
		userService = context.getBean(IUserService.class);
		employeeService = context.getBean(IEmployeeService.class);
		personService = context.getBean(IPersonService.class);
		organizationService = context.getBean(IOrganizationService.class);
		orderService = context.getBean(IOrderService.class);
		producedWorkService = context.getBean(IProducedWorkService.class);
		workTypeService = context.getBean(IWorkTypeService.class);
		materialTypeService = context.getBean(IMaterialTypeService.class);
		materialService = context.getBean(IMaterialService.class);
		materialConsumptionService = context.getBean(IMaterialConsumptionService.class);
		orderFullTextIndexService = context.getBean(IOrderFullTextIndexService.class);
		fullTextIndexService = context.getBean(IFullTextIndexService.class);
	}
	
	public void createAllEntities() {
		int rolesCount = User.Role.values().length;
		List<Person> employeePeople = createPersonList(User.TECH_USER, rolesCount);
		List<Employee> employees = createEmployees(User.TECH_USER, employeePeople);
		List<User> users = createUsers(User.TECH_USER, employees);
		employeePeople = null;
		employees = null;
		List<Customer> customers = createCustomers(users);
		List<Order> orders = createOrders(users, customers);
		customers = null;
	}
	
	public List<Person> createPersonList(User creator, int personCount) {
		List<Person> persons = new ArrayList<>(personCount);
		for(int i = 0; i < personCount; i++) {
			try {
				persons.add(createPerson(creator));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return persons;
	}
	
	public List<Employee> createEmployees(User creator, List<Person> persons) {
		List<Employee> employees = new ArrayList<>(persons.size());
		for(Person person : persons) {
			try {
				employees.add(createEmployee(creator, person));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return employees;
	}
	
	public List<User> createUsers(User creator, List<Employee> employees) {
		List<User> users = new ArrayList<>(employees.size());
		int roleIndex = 0;
		int roleCount = Role.values().length;
		
		Role role = null;
		for(Employee employee : employees) {
			role = Role.values()[roleIndex];
			String login = generateLogin(employee);
			String password = login;
			try {
				users.add(createUser(creator, employee, login, password, role));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(roleIndex < roleCount-1)
				roleIndex++;
			else
				roleIndex = 0;
		}
		return users;
	}
	
	private String generateLogin(Employee employee) {
		String login = "";
		if(employee != null) {
			Person person = employee.getPerson();
			if(person != null) {
				String fNameEng = StringUtils.rusToEngTranslit(person.getFirstName().substring(0,1));
				String lastNameEng = StringUtils.rusToEngTranslit(person.getLastName());
				login = fNameEng+lastNameEng;
			}
		}
		return login;
	}
	
	private List<Customer> createCustomers(List<User> creators) {
		List<Customer> customers = new ArrayList<>();
		for(User creator : creators) {
			try {
				customers.add( createPerson(creator) );
				
				for(int i = 0; i < 10; i++) {
					customers.add( createOrganizationCustomer(creator) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return customers;
	}
	
	private List<Order> createOrders(List<User> creators, List<Customer> customers) {
		List<Order> orders = new ArrayList<>(customers.size()*3);
		List<WorkType> workTypes = workTypeService.findAll();
		Random r  = new Random();
		for(Customer customer : customers ) {
			for(int i = 0; i < 3; i++) {
				int userIndex = r.nextInt(creators.size());
				User creator = creators.get(userIndex);
				int employeeIndex = r.nextInt(creators.size());
				Employee employee = creators.get(employeeIndex).getEmployee();
				try {
					Order order = createOrder(creator, customer, employee);
					for(WorkType workType : workTypes) {
						createProducedWork(order, workType, employee);
					}
					orders.add( order );
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return orders;
	}

	public Person createPerson(User creator) throws Exception {
		Person executorPerson = new Person();
		executorPerson.setCreatedAt(new Date());
		executorPerson.setCreatedBy(creator);
		FullName fullName = GeneratorUtils.generateFullName();
		executorPerson.setLastName(fullName.getLastName());
		executorPerson.setFirstName(fullName.getFirstName());
		executorPerson.setMiddleName(fullName.getMiddleName());
		executorPerson.setBirthDate(new Date());
		executorPerson = personService.save(executorPerson);
		return executorPerson;
	}
	
	public Person createPersonCustomer(User creator) throws Exception {
		Person customerPerson = new Person();
		customerPerson.setCreatedAt(new Date());
		customerPerson.setCreatedBy(creator);
		FullName fullName = GeneratorUtils.generateFullName();
		customerPerson.setLastName(fullName.getLastName());
		customerPerson.setFirstName(fullName.getFirstName());
		customerPerson.setMiddleName(fullName.getMiddleName());
		customerPerson.setBirthDate(new Date());
		customerPerson= personService.save(customerPerson);
		return customerPerson;
	}
	
	
	public Employee createEmployee(User creator, Person executorPerson) throws Exception {
		Employee executorEmployee = new Employee();
		executorEmployee.setCreatedAt(new Date());
		executorEmployee.setCreatedBy(User.TECH_USER);
		executorEmployee.setPerson(executorPerson);
		executorEmployee = employeeService.save(executorEmployee);
		return executorEmployee;
	}
	
	public User createUser(User creator, Employee executorEmployee, String executorLogin, String executorPassword, Role role) throws Exception {
		User executorUser = new User();
		executorUser.setCreatedAt(new Date());
		executorUser.setCreatedBy(creator);
		executorUser.setEmployee(executorEmployee);
		executorUser.setLogin(executorLogin);
		executorUser.setPassword(executorPassword);
		executorUser.setRole(role);
		executorUser = userService.save(executorUser);
		return executorUser;
	}

	public Organization createOrganizationCustomer(User creator) throws Exception {
		Organization organizationCustomer = new Organization();
		organizationCustomer.setCreatedAt(new Date());
		organizationCustomer.setCreatedBy(creator);
		OrgName orgName = GeneratorUtils.generateOrgName();
		organizationCustomer.setFullname(orgName.getFullName());
		organizationCustomer.setShortname(orgName.getShortName());
		organizationCustomer.setInn(orgName.getNumber());
		organizationCustomer.setKpp("123123123");
		organizationService.save(organizationCustomer);
		return organizationCustomer;
	}
	
	public WorkType createWorkType(User creator, String name) throws Exception {
		WorkType xerocopy = workTypeService.findByName(name);
		if(xerocopy == null) {
			xerocopy = new WorkType();
			xerocopy.setCreatedAt(new Date());
			xerocopy.setCreatedBy(creator);
			xerocopy.setName(name);
			workTypeService.save(xerocopy);
			xerocopy = workTypeService.find(xerocopy.getId());
		}
		return xerocopy;
	}
	
	public MaterialType createMaterialType(User creator, String name) throws Exception {
		MaterialType materialType = materialTypeService.findByName(name);
		if(materialType == null) {
			materialType = new MaterialType();
			materialType.setCreatedAt(new Date());
			materialType.setCreatedBy(creator);
			materialType.setName(name);
			materialType = materialTypeService.save(materialType);
		}
		return materialType;
	}
	
	public Material createMaterial(User creator, MaterialType materialType, String name) throws Exception {
		Material paperA4 = new Material();
		paperA4.setCreatedAt(new Date());
		paperA4.setCreatedBy(creator);
		paperA4.setName(name);
		paperA4.setMaterialType(materialType);
		paperA4 = materialService.save(paperA4);
		return paperA4;
	}
	
	public Order createOrder(User creator, Customer customer, Employee executorEmployee) throws Exception {
		Order firstOrder = new Order();
		firstOrder.setCreatedAt(new Date());
		firstOrder.setCreatedBy(creator);
		firstOrder.setCustomer(customer);
		firstOrder.setCurrentExecutor(executorEmployee);
		firstOrder.setDescription("Описание заказа на ксерокопию");
		firstOrder.setState(Order.State.PROCESSED);
		orderService.save(firstOrder);
		return firstOrder;
	}
	
	public Order createOrderForPerson(Customer customer, Employee executorEmployee) throws Exception {
		Order secondOrder = new Order();
		secondOrder.setCreatedAt(new Date());
		secondOrder.setCreatedBy(User.TECH_USER);
		secondOrder.setCustomer(customer);
		secondOrder.setCurrentExecutor(executorEmployee);
		secondOrder.setDescription("Описание заказа на ксерокопию");
		orderService.save(secondOrder);
		return secondOrder;
	}

	public ProducedWork createProducedWork(Order order, WorkType workType, Employee executorEmployee) throws Exception {
		ProducedWork producedWork = new ProducedWork();
		producedWork.setCreatedAt(new Date());
		producedWork.setCreatedBy(User.TECH_USER);
		producedWork.setOrder(order);
		producedWork.setWorkType(workType);
		producedWork.setExecutor(executorEmployee);
		producedWork.setDirty(true);
		producedWork.setQuantity(1);
		producedWork.setPrice(BigDecimal.valueOf(100));
		producedWork = producedWorkService.save(producedWork);		
		return producedWork;
	}
	
	public MaterialConsumption createMaterialConsumption(Material material, Order order) throws Exception {
		MaterialConsumption copyMaterialConsumption = new MaterialConsumption();
		copyMaterialConsumption.setCreatedAt(new Date());
		copyMaterialConsumption.setCreatedBy(User.TECH_USER);
		copyMaterialConsumption.setMaterial(material);
		copyMaterialConsumption.setOrder(order);
		copyMaterialConsumption.setQuantity(BigDecimal.valueOf(1.0));
		copyMaterialConsumption.setDirty(true);
		copyMaterialConsumption = materialConsumptionService.save(
				copyMaterialConsumption);
		return copyMaterialConsumption;
	}
}
