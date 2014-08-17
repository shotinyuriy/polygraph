package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="order")
public class Order extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315648934763901488L;
	
	@Column(nullable=false)
	private Customer customer;
	
	@Column
	private Employee executor;
	
	@Column
	private String description;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Employee getExecutor() {
		return executor;
	}

	public void setExecutor(Employee executor) {
		this.executor = executor;
	} 
}
