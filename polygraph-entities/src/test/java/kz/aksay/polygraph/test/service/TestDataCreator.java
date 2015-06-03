package kz.aksay.polygraph.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IFullTextIndexService;
import kz.aksay.polygraph.api.IGenericService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.api.IOrderFullTextIndexService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IVicariousPowerService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.User.Role;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.service.DefaultDataCreationService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.util.AddressGenerator;
import kz.aksay.polygraph.util.Code1cGenerator;
import kz.aksay.polygraph.util.GeneratorUtils;
import kz.aksay.polygraph.util.OrgNameGenerator.OrgName;
import kz.aksay.polygraph.util.PersonNameGenerator.FullName;
import kz.aksay.polygraph.util.StringUtils;

import org.springframework.context.ApplicationContext;

public class TestDataCreator {
	
	private static final int PERSON_COUNT = 50;
	
	private ApplicationContext context;
	private IUserService userService;
	private IEmployeeService employeeService;
	private IEquipmentService equipmentService;
	private IPersonService personService;
	private IOrganizationService organizationService;
	private IOrderService orderService;
	private IProducedWorkService producedWorkService;
	private IWorkTypeService workTypeService;
	private IPaperTypeService paperTypeService;
	private IPaperService paperService;
	private IMaterialService materialService;
	private IMaterialConsumptionService materialConsumptionService;
	private IOrderFullTextIndexService orderFullTextIndexService;
	private IFullTextIndexService fullTextIndexService;
	private IVicariousPowerService vicariousPowerService;
	
	private Random dateEndRealRandom = new Random();
	private Random formatRandom = new Random(System.currentTimeMillis());

	public TestDataCreator(ApplicationContext context) {
		this.context = context;
		setUp();
	}

	public void setUp() {
		userService = context.getBean(IUserService.class);
		employeeService = context.getBean(IEmployeeService.class);
		equipmentService = context.getBean(IEquipmentService.class);
		personService = context.getBean(IPersonService.class);
		organizationService = context.getBean(IOrganizationService.class);
		orderService = context.getBean(IOrderService.class);
		producedWorkService = context.getBean(IProducedWorkService.class);
		workTypeService = context.getBean(IWorkTypeService.class);
		paperTypeService = context.getBean(IPaperTypeService.class);
		paperService = context.getBean(IPaperService.class);
		materialService = context.getBean(IMaterialService.class);
		materialConsumptionService = context.getBean(IMaterialConsumptionService.class);
		orderFullTextIndexService = context.getBean(IOrderFullTextIndexService.class);
		fullTextIndexService = context.getBean(IFullTextIndexService.class);
		vicariousPowerService = context.getBean(IVicariousPowerService.class);
	}
	
	public void createAllEntities() {
		int rolesCount = User.roles.length;
		List<Person> employeePeople = createPersonList(User.TECH_USER, rolesCount+2);
		List<Employee> employees = createEmployees(User.TECH_USER, employeePeople);
		List<User> users = createUsers(User.TECH_USER, employees);
		List<User> executors = new ArrayList<>();
		for(User user : users) {
			if(user.getRole().equals(Role.DESIGNER)) {
				executors.add(user);
			}
		}
		employeePeople = null;
		employees = null;
		List<Subject> customers = createCustomers(users);
		List<Order> orders = createOrders(users, executors, customers);
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
		int roleCount = User.roles.length;
		
		Role role = null;
		for(Employee employee : employees) {
			
			if(roleIndex < roleCount-1)
				role = User.roles[roleIndex];
			else
				role = Role.DESIGNER;
			
			if(!role.equals(Role.ADMIN)) { 
				String login = generateLogin(employee);
				String password = login;
				try {
					users.add(createUser(creator, employee, login, password, role));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			roleIndex++;
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
	
	private List<Subject> createCustomers(List<User> creators) {
		List<Subject> customers = new ArrayList<>();
		for(User creator : creators) {
			try {
				customers.add( createPerson(creator) );
				
				for(int i = 0; i < 10; i++) {
					Organization org = createOrganizationCustomer(creator);
					if(org != null) {
						customers.add(org);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return customers;
	}
	
	private List<Order> createOrders(List<User> creators, List<User> executors, List<Subject> customers) {
		List<Order> orders = new ArrayList<>(customers.size()*3);
		List<WorkType> workTypes = workTypeService.findAll();
		Random r  = new Random();
		for(Subject customer : customers ) {
			for(int i = 0; i < 1; i++) {
				int userIndex = r.nextInt(creators.size());
				User creator = creators.get(userIndex);
				int employeeIndex = r.nextInt(executors.size());
				Employee employee = executors.get(employeeIndex).getEmployee();
				try {
					VicariousPower vicariousPower = null;
					if(customer instanceof Organization) {
						vicariousPower = createVicariousPower((Organization)customer);
					}
					Order.State state = Order.State.values()[r.nextInt(Order.State.values().length)];
					Order order = createOrder(creator, customer, employee, vicariousPower, state);
					for(WorkType workType : workTypes) {
						Equipment equipment = null;
						if(workTypeService.isEquipmentRequired(workType)) {
							int index = formatRandom.nextInt(DefaultDataCreationService.PRINTER_NAMES.length);
							equipment = equipmentService.findByName(DefaultDataCreationService.PRINTER_NAMES[index]);
						}
						createProducedWork(order, workType, employee, equipment);
					}
					orders.add( order );
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return orders;
	}

	private VicariousPower createVicariousPower(Organization customer) throws Exception {
		VicariousPower vicariousPower = new VicariousPower();
		vicariousPower.setCreatedAt(new Date());
		vicariousPower.setCreatedBy(User.TECH_USER);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		vicariousPower.setBeginDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 2);
		vicariousPower.setEndDate(calendar.getTime());
		vicariousPower.setNumber(GeneratorUtils.generateCode(4));
		FullName fullName = GeneratorUtils.generateFullName();
		vicariousPower.setPersonName(fullName.toString());
		vicariousPower.setOrganization(customer);
		vicariousPowerService.save(vicariousPower);
		return vicariousPower;
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
		boolean isCreated = false;
		int attempts = 0;
		
		while(!isCreated && attempts <3) {
			try {
				attempts++;
				Organization organizationCustomer = new Organization();
				organizationCustomer.setCreatedAt(new Date());
				organizationCustomer.setCode1c(Code1cGenerator.generateCode());
				organizationCustomer.setCreatedBy(creator);
				OrgName orgName = GeneratorUtils.generateOrgName();
				FullName director = GeneratorUtils.generateFullName();
				organizationCustomer.setDirectorName(director.toString());
				organizationCustomer.setFullname(orgName.getFullName());
				organizationCustomer.setShortname(orgName.getShortName());
				organizationCustomer.setInn(orgName.getNumber());
				organizationCustomer.setKpp(orgName.getKpp());
				organizationCustomer.setAddress(AddressGenerator.generateAddress());
				organizationService.save(organizationCustomer);
				isCreated = true;
				return organizationCustomer;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public WorkType createWorkType(User creator, String name, String code) throws Exception {
		WorkType xerocopy = workTypeService.findByName(name);
		if(xerocopy == null) {
			xerocopy = new WorkType();
			xerocopy.setCreatedAt(new Date());
			xerocopy.setCreatedBy(creator);
			xerocopy.setName(name);
			xerocopy.setCode(code);
			xerocopy.setCode1c("0003");
			workTypeService.save(xerocopy);
			xerocopy = workTypeService.find(xerocopy.getId());
		}
		return xerocopy;
	}
	
	public PaperType createMaterialType(User creator, String name) throws Exception {
		PaperType paperType = paperTypeService.findByName(name);
		if(paperType == null) {
			paperType = new PaperType();
			paperType.setCreatedAt(new Date());
			paperType.setCreatedBy(creator);
			paperType.setName(name);
			paperType = paperTypeService.save(paperType);
		}
		return paperType;
	}
	
	public Paper createPaper(User creator, PaperType paperType, Format format) throws Exception {
		Paper paperA4 = new Paper();
		paperA4.setCreatedAt(new Date());
		paperA4.setCreatedBy(creator);
		paperA4.setFormat(format);
		paperA4.setType(paperType);
		paperA4.setDensity(250);
		paperA4 = paperService.save(paperA4);
		return paperA4;
	}
	
	public Order createOrder(User creator, Subject customer, 
			Employee executorEmployee, VicariousPower vicariousPower, Order.State state) throws Exception {
		Order firstOrder = new Order();
		firstOrder.setCreatedAt(new Date());
		firstOrder.setCreatedBy(creator);
		firstOrder.setCustomer(customer);
		firstOrder.setCurrentExecutor(executorEmployee);
		firstOrder.setDescription("Описание заказа на ксерокопию");
		firstOrder.setState(state);
		firstOrder.setVicariousPower(vicariousPower);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 3);
		firstOrder.setDateEndPlan(calendar.getTime());
		if(state.equals(Order.State.FINISHED)) {
			int delay = dateEndRealRandom.nextInt(5) - 2;
			calendar.add(Calendar.DAY_OF_YEAR, delay);
			firstOrder.setDateEndReal(calendar.getTime());
		}
			
		orderService.save(firstOrder);
		return firstOrder;
	}
	
	public Order createOrderForPerson(Subject customer, Employee executorEmployee) throws Exception {
		Order secondOrder = new Order();
		secondOrder.setCreatedAt(new Date());
		secondOrder.setCreatedBy(User.TECH_USER);
		secondOrder.setCustomer(customer);
		secondOrder.setCurrentExecutor(executorEmployee);
		secondOrder.setDescription("Описание заказа на ксерокопию");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		secondOrder.setDateEndPlan(calendar.getTime());
		orderService.save(secondOrder);
		return secondOrder;
	}

	public ProducedWork createProducedWork(Order order, WorkType workType, Employee executorEmployee, Equipment equipment) throws Exception {
		ProducedWork producedWork = new ProducedWork();
		producedWork.setCreatedAt(new Date());
		producedWork.setCreatedBy(User.TECH_USER);
		producedWork.setOrder(order);
		producedWork.setWorkType(workType);
		producedWork.setExecutor(executorEmployee);
		producedWork.setEquipment(equipment);
		producedWork.setDirty(true);
		producedWork.setQuantity(1);
		producedWork.setPrice(BigDecimal.valueOf(100));
		if(workTypeService.isEquipmentRequired(workType)) {
			Format[] formats = {Format.A3, Format.A4 };
			Format format = formats[ formatRandom.nextInt(2) ];
			producedWork.setFormat(format);
		}
		
		Set<MaterialConsumption> materialConsumptions = createMaterialConsumptions(producedWork);
		producedWork.setMaterialConsumption(materialConsumptions);
		
		producedWork = producedWorkService.save(producedWork);		
		return producedWork;
	}
	
	private Set<MaterialConsumption> createMaterialConsumptions(
			ProducedWork producedWork) throws Exception {
		Set<MaterialConsumption> materialConsumptions = new HashSet<>();
		
		List<Material> materials = materialService.findMaterialsByWorkType(producedWork.getWorkType());
		if(!materials.isEmpty()) {
			Random r = new Random();
			int index = r.nextInt(materials.size());
			Material material = materials.get(index);
			MaterialConsumption materialConsumption = 
					createMaterialConsumption(material, producedWork.getOrder(), producedWork, false);
			materialConsumptions.add(materialConsumption);
		}
		
		return materialConsumptions;
	}

	public MaterialConsumption createMaterialConsumption(Material material, Order order, ProducedWork producedWork, boolean save) throws Exception {
		MaterialConsumption copyMaterialConsumption = new MaterialConsumption();
		copyMaterialConsumption.setCreatedAt(new Date());
		copyMaterialConsumption.setCreatedBy(User.TECH_USER);
		copyMaterialConsumption.setMaterial(material);
		copyMaterialConsumption.setOrder(order);
		copyMaterialConsumption.setProducedWork(producedWork);
		copyMaterialConsumption.setQuantity(1);
		copyMaterialConsumption.setDirty(true);
		copyMaterialConsumption.setWasted(0);
		if(save) {
			copyMaterialConsumption = materialConsumptionService.save(
					copyMaterialConsumption);
		}
		return copyMaterialConsumption;
	}
}
