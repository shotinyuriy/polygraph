package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="\"order\"")
public class Order extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315648934763901488L;
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="current_executor_id")
	private Employee currentExecutor;
	

	@Column
	private String description;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Employee getCurrentExecutor() {
		return currentExecutor;
	}

	public void setCurrentExecutor(Employee currentExecutor) {
		this.currentExecutor = currentExecutor;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
