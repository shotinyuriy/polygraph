package kz.aksay.polygraph.desktop;

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
		
		Series<String, Number> minSeries = new Series<String, Number>();
		Series<String, Number> maxSeries = new Series<String, Number>();
		minSeries.setName("Минимальная загруженность");
		maxSeries.setName("Максимальная загруженность");
		for(Entry<Employee, EmployeeWorkloadReport> entry : empWorkLoadRep.entrySet()) {
			Employee employee = entry.getKey();
			EmployeeWorkloadReport employeeWorkloadReport = entry.getValue();
			String categoryName = createCategoryName(employee);
			employeeNames.add(categoryName);
			Double workloadDiff = employeeWorkloadReport.getWorkLoadMax() - employeeWorkloadReport.getWorkLoadMin();
			final Data<String, Number> minData = new Data<String, Number>(categoryName, employeeWorkloadReport.getWorkLoadMin()); 
			final Data<String, Number> maxData = new Data<String, Number>(categoryName, workloadDiff);
			minSeries.getData().add(minData);
			maxSeries.getData().add(maxData);
			addLabelListener(minData, employeeWorkloadReport.getWorkLoadMin());
			addLabelListener(maxData, employeeWorkloadReport.getWorkLoadMax());
			
			System.out.println(employee.getId()+ " min "+employeeWorkloadReport.getWorkLoadMin()+
					" max "+employeeWorkloadReport.getWorkLoadMax());
		}
		
		xAxis.setCategories(employeeNames);
		
		barChart.getData().addAll(minSeries, maxSeries);
	}
	
	private void addLabelListener(final Data<String, Number> data, final Number number) {
		data.nodeProperty().addListener(new ChangeListener<Node>() {
	        @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
	          if (node != null) {
	            //setNodeStyle(data);
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
	
	  /** Change color of bar if value of i is <5 then red, if >5 then green if i>8 then blue */
	  private void setNodeStyle(XYChart.Data<String, Number> data) {
	    Node node = data.getNode();
	    if (data.getYValue().intValue() > 8) {
	      node.setStyle("-fx-bar-fill: -fx-exceeded;");
	    } else if (data.getYValue().intValue() > 5) {
	      node.setStyle("-fx-bar-fill: -fx-achieved;");
	    } else {
	      node.setStyle("-fx-bar-fill: -fx-not-achieved;");
	    }
	  }

	/** places a text label with a bar's value above a bar node for a given XYChart.Data */
	  private void displayLabelForData(XYChart.Data<String, Number> data, final Number number) {
	    final Node node = data.getNode();
	    final Text dataText = new Text(number + "");
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
