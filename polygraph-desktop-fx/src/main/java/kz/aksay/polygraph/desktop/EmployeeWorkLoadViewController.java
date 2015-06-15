package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.omg.PortableServer.POAManagerPackage.State;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.text.Text;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.report.EmployeeWorkloadReport;
import kz.aksay.polygraph.util.SessionAware;

public class EmployeeWorkLoadViewController implements Initializable, SessionAware {

	private static enum BarType {
		AVG_WORKLOAD,
		FREE_WORKLOAD
	}
	
	private static final Integer WORKLOAD_DAY_LIMIT = 8;
	private IEmployeeService employeeService = StartingPane.getBean(IEmployeeService.class);
	private IOrderService orderService = StartingPane.getBean(IOrderService.class);
	
	private List<Employee> employees;
	private List<Order> orders;
	private ObservableList<String> employeeNames;
	private Map<Employee, EmployeeWorkloadReport> empWorkLoadRep;
	
	private Map<String, Object> session;
	
	@FXML private StackedBarChart<String, Number> barChart;
	@FXML private CategoryAxis xAxis;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		empWorkLoadRep = employeeService.getEmployeesWorkload();
		employeeNames = FXCollections.observableArrayList();
		
		Series<String, Number> lowSeries = new Series<String, Number>();
		Series<String, Number> medSeries = new Series<String, Number>();
		Series<String, Number> highSeries = new Series<String, Number>();
		Series<String, Number> freeSeries = new Series<String, Number>();
		
		lowSeries.setName("Низкая загруженность");
		medSeries.setName("Средняя загруженность");
		highSeries.setName("Высокая загруженность");
		freeSeries.setName("Свободное рабочее время");
		
		for(Entry<Employee, EmployeeWorkloadReport> entry : empWorkLoadRep.entrySet()) {
			Employee employee = entry.getKey();
			EmployeeWorkloadReport employeeWorkloadReport = entry.getValue();
			String categoryName = createCategoryName(employee);
			employeeNames.add(categoryName);
			Double busyWorkload = employeeWorkloadReport.getWorkLoadAvg();
			Double workloadDiff = WORKLOAD_DAY_LIMIT - busyWorkload;
			final Data<String, Number> minData = new Data<String, Number>(categoryName, busyWorkload); 
			final Data<String, Number> maxData = new Data<String, Number>(categoryName, workloadDiff);
			
			if(busyWorkload < 4) {
				lowSeries.getData().add(minData);
			} else if( busyWorkload < 6 ) {
				medSeries.getData().add(minData);
			} else {
				highSeries.getData().add(minData);
			}
			
			freeSeries.getData().add(maxData);
			addLabelListener(minData, busyWorkload, BarType.AVG_WORKLOAD);
			addLabelListener(maxData, workloadDiff, BarType.FREE_WORKLOAD);
		}
		
		xAxis.setCategories(employeeNames);
		
		barChart.getData().addAll(lowSeries, medSeries, highSeries, freeSeries);
	}
	
	private void addLabelListener(final Data<String, Number> data, final Number number, final BarType barType) {
		data.nodeProperty().addListener(new ChangeListener<Node>() {
	        @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
	          if (node != null) {
	            displayLabelForData(data, number);
	          } 
	        }
	      });
	}
	
	private static String createCategoryName(Employee employee) {
		
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
		
		return sb.toString();
	}

	/** places a text label with a bar's value above a bar node for a given XYChart.Data */
	  private void displayLabelForData(XYChart.Data<String, Number> data, final Number number) {
	    final Node node = data.getNode();
	    final Text dataText = new Text(new BigDecimal(number.toString())
	    	.setScale(2, RoundingMode.HALF_UP) + "ч.");
	    node.parentProperty().addListener(new ChangeListener<Parent>() {
	      @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
	        Group parentGroup = (Group) parent;
	        parentGroup.getChildren().add(dataText);
	      }
	    });
	 
	    node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
	      @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
	        dataText.setLayoutX(
	          Math.round(
	            bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
	          )
	        );
	        dataText.setLayoutY(
	          Math.round(
	            bounds.getMinY() + dataText.prefHeight(-1) * 1.0
	          )
	        );
	      }
	    });
	  }
	
}
