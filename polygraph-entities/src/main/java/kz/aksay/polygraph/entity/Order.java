package kz.aksay.polygraph.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="\"order\"")
public class Order extends EntitySupport implements MaterialConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315648934763901488L;
	
	public static enum State {
		PROCESSED("В работе"),
		FINISHED("Завершен");
		
		private String name;
		
		private State(String name) {
			this.name = name;
		}
	}
	
	@Transient
	private Set<MaterialConsumption> materialConsumption;
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="current_executor_id")
	private Employee currentExecutor;
	
	@Transient
	private Set<ProducedWork> producedWorks;

	@Column
	private String description;
	
	@Column
	@Enumerated(EnumType.STRING)
	private State state;

	@Transient
	public BigDecimal getTotalCost() {
		BigDecimal totalCost = BigDecimal.ZERO;
		if(producedWorks != null) {
			for(ProducedWork producedWork : producedWorks) {
				BigDecimal cost;
				if((cost = producedWork.getCost()) != null)
					totalCost = totalCost.add(cost);
			}
		}
		return totalCost;
	}
	
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

	public Set<ProducedWork> getProducedWorks() {
		return producedWorks;
	}

	public void setProducedWorks(Set<ProducedWork> producedWorks) {
		this.producedWorks = producedWorks;
	}

	public Set<MaterialConsumption> getMaterialConsumption() {
		return materialConsumption;
	}

	public void setMaterialConsumption(Set<MaterialConsumption> materialConsumption) {
		this.materialConsumption = materialConsumption;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
