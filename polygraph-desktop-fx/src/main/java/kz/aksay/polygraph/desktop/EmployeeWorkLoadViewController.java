package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.omg.PortableServer.POAManagerPackage.State;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.util.SessionAware;

public class EmployeeWorkLoadViewController implements Initializable, SessionAware {

	private IEmployeeService employeeService = StartingPane.getBean(IEmployeeService.class);
	private IOrderService orderService = StartingPane.getBean(IOrderService.class);
	
	List<Employee> employees;
	List<Order> orders;
	ObservableList<String> employeeNames;
	Map<Order.State, Map<Employee, Integer>> employeeWorkloadAll = new HashMap<Order.State, Map<Employee, Integer>>();
	
	private Map<String, Object> session;
	
	@FXML private BarChart<String, Integer> barChart;
	@FXML private CategoryAxis xAxis;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		employees = employeeService.findAll();
		employeeNames = FXCollections.observableArrayList();
		orders = orderService.findAll();
		
		for(Order.State state : Order.State.values()) {
			if(state.equals(Order.State.PROCESSED) || state.equals(Order.State.NEW) || state.equals(Order.State.PAUSED)) {
				Map<Employee, Integer> employeeWorkload = new HashMap<Employee, Integer>();
				for(Employee employee : employees) {
					String categoryName = createCategoryName(employee, state);
					if(!employeeNames.contains(categoryName)) {
						employeeNames.add(categoryName);
					}
					employeeWorkload.put(employee,  0);
				}
				employeeWorkloadAll.put(state, employeeWorkload);
			}
		}
		
		for(Order order : orders) {
			Employee employee = order.getCurrentExecutor();
			Order.State state = order.getState();
			if(state != null) {
				
				Map<Employee, Integer> employeeWorkload = employeeWorkloadAll.get(state);
				if(employeeWorkload != null && employee != null) {
					Integer count = employeeWorkload.get(employee);
					employeeWorkload.put(employee, count+1);
				}
				
			}
		}
		
		for(Map.Entry<Order.State, Map<Employee, Integer>> employeeWorkloadEntry : employeeWorkloadAll.entrySet()) {
			
			Order.State state = employeeWorkloadEntry.getKey();
			
			XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
			series.setName(state.getName());
			
			Map<Employee, Integer> workloads = employeeWorkloadEntry.getValue();
			
			for(Map.Entry<Employee,Integer> employeeWorkoad : workloads.entrySet()) {
				
				Employee employee = employeeWorkoad.getKey();
				String categoryName = createCategoryName(employee, state);
				Integer count = employeeWorkoad.getValue();
				series.getData().add(new XYChart.Data<String, Integer>(categoryName, count));
			}
			
			barChart.getData().add(series);
		}
		
		xAxis.setCategories(employeeNames);
		
		
	}
	
	private static String createCategoryName(Employee employee, Order.State state) {
		
		StringBuffer sb = new StringBuffer();
		
		if(employee != null) {
			Person person = employee.getPerson();
			if(person != null) {
				if(person.getLastName() != null){
					sb.append(person.getLastName());
				}
				if(person.getFirstName() != null){
					if(sb.length() > 0) sb.append("\n");
					sb.append(person.getFirstName());
				}
				if(person.getMiddleName() != null){
					if(sb.length() > 0) sb.append("\n");
					sb.append(person.getMiddleName());
				}
			}
		}
		
//		if(state != null) {
//			if(sb.length() > 0) sb.append("\n");
//			sb.append(state.getName());
//		}
		
		return sb.toString();
	}

	
}