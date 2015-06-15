package kz.aksay.polygraph.entity.report;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kz.aksay.polygraph.entity.Complexity;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.util.DateUtils;

@Entity
public class EmployeeWorkloadReport {
	
	@Id
	private Long orderId;
	
	@Enumerated(EnumType.STRING)
	private Order.State state;

	@ManyToOne
	private Employee employee;
	
	@ManyToOne
	private Complexity complexity;
	
	private Date dateEndPlan;
	
	private Double workLoadMin;
	
	private Double workLoadMax;
	
	public EmployeeWorkloadReport(Long orderId, Employee employee, Complexity complexity, 
			Date dateEndPlan, Order.State state) {
		this.orderId = orderId;
		this.employee = employee;
		this.complexity = complexity;
		this.dateEndPlan = dateEndPlan;
		this.state = state;
	}

	public void calcWorkload(Date date) {
		int differenceInDays = DateUtils.differenceInDays(dateEndPlan, date);
		if(differenceInDays <= 0) differenceInDays = 1;
		
		workLoadMin = (double)(complexity.getMin()) / differenceInDays;
		workLoadMax = (double)(complexity.getMax()) / differenceInDays;
	} 
	
	public void add(EmployeeWorkloadReport addition) {
		if(addition != null) {
			this.workLoadMin += addition.getWorkLoadMin();
			this.workLoadMax += addition.getWorkLoadMax();
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public void setComplexity(Complexity complexity) {
		this.complexity = complexity;
	}

	public Date getDateEndPlan() {
		return dateEndPlan;
	}

	public void setDateEndPlan(Date dateEndPlan) {
		this.dateEndPlan = dateEndPlan;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getWorkLoadMin() {
		return workLoadMin;
	}

	public Double getWorkLoadMax() {
		return workLoadMax;
	}
	
	public Double getWorkLoadAvg() {
		return (workLoadMin + workLoadMax) / 2.0;
	}

	public Order.State getState() {
		return state;
	}

	public void setState(Order.State state) {
		this.state = state;
	}

}
