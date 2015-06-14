package kz.aksay.polygraph.test.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IFullTextIndexService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IOrderFullTextIndexService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IVicariousPowerService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.User.Role;
import kz.aksay.polygraph.entity.report.EmployeeWorkloadReport;
import kz.aksay.polygraph.entity.report.OrderProceedReport;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.integration1c.OrderToXMLExporter;
import kz.aksay.polygraph.test.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDesignerBaseScenario extends Assert {

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
	private IMaterialConsumptionService materialConsumptionService;
	private IOrderFullTextIndexService orderFullTextIndexService; 
	private IFullTextIndexService fullTextIndexService;
	private IContractService contractService;
	private IVicariousPowerService vicariousPowerService;
	private OrderToXMLExporter orderToXMLExporter;
	
	private final String executorLogin = "executorLogin";
	private final String executorPassword = "exectorPassword";

	private Person executorPerson;
	private Employee executorEmployee;
	private User executorUser;
	private Order firstOrder;
	private Organization organizationCustomer;
	private WorkType xerocopy;
	private PaperType gloss2;
	private Paper paperA4;
	private ProducedWork producedWork;
	private MaterialConsumption copyMaterialConsumption;
	private Person customerPerson;
	private Order secondOrder; 
	private Contract contract;
	private VicariousPower vicariousPower;
	private Equipment equipment;
	
	private TestOrderService testOrderService;
	private TestMaterialConsumptionService testMaterialConsumptionService;
	private TestDataCreator testDataCreator; 

	@BeforeClass
	public void setUp()	{
		context = ContextUtils.getApplicationContext();
		userService = context.getBean(IUserService.class);
		employeeService = context.getBean(IEmployeeService.class);
		personService = context.getBean(IPersonService.class);
		organizationService = context.getBean(IOrganizationService.class);
		orderService = context.getBean(IOrderService.class);
		producedWorkService = context.getBean(IProducedWorkService.class);
		workTypeService = context.getBean(IWorkTypeService.class);
		paperTypeService = context.getBean(IPaperTypeService.class);
		paperService = context.getBean(IPaperService.class);
		materialConsumptionService = context.getBean(IMaterialConsumptionService.class);
		orderFullTextIndexService = context.getBean(IOrderFullTextIndexService.class);
		fullTextIndexService = context.getBean(IFullTextIndexService.class);
		testOrderService = new TestOrderService();
		testMaterialConsumptionService = new TestMaterialConsumptionService();
		testMaterialConsumptionService.setUp();
		contractService = context.getBean(IContractService.class);
		vicariousPowerService = context.getBean(IVicariousPowerService.class);
		equipmentService = context.getBean(IEquipmentService.class);
		testDataCreator = new TestDataCreator(this.context);
		orderToXMLExporter = context.getBean(OrderToXMLExporter.class);
	}
	
	@Test
	public void test() throws Exception {
		try {
			testDataCreator.createAllEntities();
			
			createPerson();
			createPersonCustomer();
			createEmployee();
			createEquipment();
			createUser();
			createOrganizationCustomer();
			createContract();
			createVicariousPower();
			createOrder();
			createOrderForPerson();
			testRecreateOrderFullTextIndexes();
			testFindAllOrders();
			createWorkTypeXerocopy();
			createMaterialTypePaper();
			createMaterialPaperA4();
			createProducedWork();
			createMaterialConsumption();
			testFindOrdersByStateAndCurrentExecutor();
			testFindOrdersByStateAndCurrentExecutorAndString();
			testMaterialConsumptionService();
			testCountOfEquipmentUsageByExample();
			employeeService.findAllByUserRole(User.Role.DESIGNER);
			
			testOrderProceedReport();
			testEmployeeWorkloadReport();
			
			orderToXMLExporter.export(orderService.findAll(), new File("test.xml"));
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			deleteAll();
		}
	}
	
	private void testOrderProceedReport() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -12);
		Date dateFrom = calendar.getTime();
		calendar.add(Calendar.MONTH, 12);
		Date dateTo = calendar.getTime();
		
		List<OrderProceedReport> orderProceedReports = orderService.generateProceedReport(dateFrom, dateTo);
		
		assertTrue(orderProceedReports.size() > 0);
		
		for(OrderProceedReport orderProceedReport : orderProceedReports) {
			System.out.println("monthYear "+orderProceedReport.getMonthYear()+" proceedTime "+orderProceedReport.getProcceedTime());
		}
				
	}
	
	private void testEmployeeWorkloadReport() {
		Map<Employee, EmployeeWorkloadReport> report = employeeService.getEmployeesWorkload();
		for(Map.Entry<Employee, EmployeeWorkloadReport> reportEntry : report.entrySet()) {
			Employee employee = reportEntry.getKey();
			EmployeeWorkloadReport empWLRep = reportEntry.getValue();
			System.out.println(" Employee "+employee.getPerson().getFullName());
			System.out.println(" WorkLoad "+empWLRep.getWorkLoadMin()+" "
					+empWLRep.getWorkLoadMax()+" "
					+empWLRep.getAverageWorkLoad());
		}
	}

	private void testCountOfEquipmentUsageByExample() {
		ProducedWork producedWork = new ProducedWork();
		producedWork.setEquipment(equipment);
		producedWork.setFormat(Format.A4);
		equipmentService.countOfUsagesByExample(producedWork);
	}

	private void createVicariousPower() throws Exception {
		vicariousPower = new VicariousPower();
		Calendar calendar = Calendar.getInstance();
		vicariousPower.setCreatedAt(calendar.getTime());
		vicariousPower.setCreatedBy(User.TECH_USER);
		calendar.add(Calendar.MONTH, 1);
		vicariousPower.setEndDate(calendar.getTime());
		vicariousPower.setNumber("123456789");
		vicariousPower.setOrganization(organizationCustomer);
		vicariousPower.setPerson(customerPerson);
		vicariousPowerService.save(vicariousPower);
	}

	private void createContract() throws Exception {
		contract = new Contract();
		Calendar calendar = Calendar.getInstance();
		contract.setCreatedAt(calendar.getTime());
		contract.setCreatedBy(User.TECH_USER);
		calendar.add(Calendar.MONTH, -1);
		contract.setBeginDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 2);
		contract.setEndDate(calendar.getTime());
		contract.setParty1(Organization.FIRMA_SERVER_PLUS);
		contract.setParty2(organizationCustomer);
		contract.setNumber("123456789");
		contractService.save(contract);
	}

	private void createMaterialConsumption() throws Exception {
		copyMaterialConsumption = testDataCreator.createMaterialConsumption(
				paperA4, firstOrder, producedWork, true);
	}

	private void createProducedWork() throws Exception {
		producedWork = testDataCreator.createProducedWork(
				firstOrder, xerocopy, executorEmployee, equipment);
	}

	private void createMaterialPaperA4() throws Exception {
		paperA4 = testDataCreator.createPaper(User.TECH_USER, gloss2, Format.A4);
	}

	private void createMaterialTypePaper() throws Exception {
		gloss2 = testDataCreator.createMaterialType(User.TECH_USER, "БУМАГА");
	}

	private void createWorkTypeXerocopy() throws Exception {
		xerocopy = testDataCreator.createWorkType(
				User.TECH_USER, "КСЕРОКОПИЯ", "XEROCOPYING");
	}

	private void createOrderForPerson() throws Exception {
		secondOrder = testDataCreator.createOrderForPerson(
				customerPerson, executorEmployee);
	}

	private void createOrder() throws Exception {
		firstOrder = testDataCreator.createOrder(new Date(),
				User.TECH_USER, organizationCustomer, executorEmployee, vicariousPower, Order.State.NEW);
	}

	private void createOrganizationCustomer() throws Exception {
		organizationCustomer = testDataCreator.createOrganizationCustomer(User.TECH_USER);
	}

	private void createUser() throws Exception {
		executorUser = testDataCreator.createUser(
				User.TECH_USER, executorEmployee, executorLogin, executorPassword, Role.DESIGNER);
	}

	private void createEmployee() throws Exception {
		executorEmployee = testDataCreator.createEmployee(User.TECH_USER, executorPerson);
	}

	private void createPersonCustomer() throws Exception {
		customerPerson = testDataCreator.createPersonCustomer(User.TECH_USER);
	}

	private void createPerson() throws Exception {
		executorPerson = testDataCreator.createPerson(User.TECH_USER);
	}
	
	private void createEquipment() throws Exception {
		equipment = new Equipment();
		equipment.setCreatedAt(new Date());
		equipment.setCreatedBy(User.TECH_USER);
		equipment.setName("PRINTER-TEST");
		equipment = equipmentService.save(equipment);
	}

	public void testFindAllOrders() {
		testOrderService.setUp();
		List<Order> orders = testOrderService.testFindAll();
		assertNotNull(orders);
		assertTrue(orders.size() >= 2);
		for(Order order : orders) {
			System.out.println(String.format("%s %s %S", order.getId(), order.getCustomer().getFullName(), order.getCurrentExecutor().getId()));
		}
	}
	
	public void testFindOrdersByStateAndCurrentExecutor() {
		Order order = new Order();
		
		order.setCurrentExecutor(executorEmployee);
		order.setState(Order.State.NEW);
		
		List<Order> orders = orderService.findByExample(order);
		
		assertTrue(orders.size() >= 1);
		
		order = new Order();
		
		order.setCurrentExecutor(new Employee());
		order.setState(Order.State.PROCESSING);
		
		orders = orderService.findByExample(order);
		
		assertTrue(orders.size() == 0);
	}
	
	public void testFindOrdersByStateAndCurrentExecutorAndString() {
		Order order = new Order();
		
		List<Order> orders = orderService.findByExampleAndSearchString(order, executorPerson.getFullName());
		
		assertTrue(orders.size() >= 1);
		
		List<Order> orders2 = orderService.findByExampleAndSearchString(order, executorPerson.getFirstName());
		
		assertTrue(orders2.size() >= 1);
		
		order.setState(Order.State.FINISHED);
		List<Order> orders3 = orderService.findByExampleAndSearchString(order, secondOrder.getDescription());
		
		assertTrue(orders3.size() >= 0);
		
		order.setState(null);
		order.setCurrentExecutor(executorEmployee);
		List<Order> orders4 = orderService.findByExampleAndSearchString(order, secondOrder.getDescription());
		
		assertTrue(orders4.size() >= 1);
	}
	
	public void testRecreateOrderFullTextIndexes() {
		try {
			orderFullTextIndexService.recreateOrderFullTextIndexes(firstOrder);
			orderFullTextIndexService.recreateOrderFullTextIndexes(secondOrder);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void testMaterialConsumptionService() {
		List<MaterialConsumption> expected = new ArrayList<>();
		expected.add(copyMaterialConsumption);
		testMaterialConsumptionService.testFindByExample(copyMaterialConsumption, expected);
	}
	
	private void deleteAll() {
		int rowsAffected = 0;
		
		rowsAffected = orderFullTextIndexService.deleteAll();
		rowsAffected = fullTextIndexService.deleteAll();
		
		if(copyMaterialConsumption != null && copyMaterialConsumption.getId() != null) materialConsumptionService.delete(copyMaterialConsumption);
		if(producedWork != null && producedWork.getId() != null) {
			materialConsumptionService.deleteAllByProducedWork(producedWork);
			producedWorkService.delete(producedWork);
		}
		if(paperA4 != null && paperA4.getId() != null) paperService.delete(paperA4);
		if(gloss2 != null && gloss2.getId() != null) {
			paperService.deleteAllByPaperType(gloss2);
			paperTypeService.delete(gloss2);
		}
		if(xerocopy != null && xerocopy.getId() != null) workTypeService.delete(xerocopy);
		if(secondOrder != null && secondOrder.getId() != null) {
			producedWorkService.deleteAllByOrder(secondOrder);
			orderService.delete(secondOrder);
		}
		if(firstOrder != null && firstOrder.getId() != null) {
			producedWorkService.deleteAllByOrder(firstOrder);
			orderService.delete(firstOrder);
		}
		if(contract != null && contract.getId() != null) contractService.delete(contract);
		if(vicariousPower != null && vicariousPower.getId() != null) vicariousPowerService.delete(vicariousPower);
		if(organizationCustomer != null && organizationCustomer.getId() != null) organizationService.delete(organizationCustomer);
		if(executorUser != null && executorUser.getId() != null) userService.delete(executorUser);
		if(executorEmployee != null && executorEmployee.getId() != null) employeeService.delete(executorEmployee);
		if(executorPerson != null && executorPerson.getId() != null) personService.delete(executorPerson);
		if(customerPerson != null && customerPerson.getId() != null) personService.delete(customerPerson);
	}
}
