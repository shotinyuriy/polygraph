package kz.aksay.polygraph.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import kz.aksay.polygraph.entity.EntitySupport;

@Entity
@Table(name="produced_work")
public class ProducedWork extends EntitySupport implements MaterialConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9184662638673352545L;

	public static enum State {
		PROCESSED("В работе"),
		FINISHED("Завершена");
		
		private String name;
		
		private State(String name) {
			this.name = name;
		}
	}
	
	@Transient
	private Set<MaterialConsumption> materialConsumption;
	
	@ManyToOne
	@JoinColumn(name="work_type_id")
	private WorkType workType;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name="exeutor_id")
	private Employee executor;
	
	@Column(name="finished_at")
	private Date finishedAt;
	
	@Column
	private BigDecimal price = BigDecimal.ZERO;
	
	@Column
	private Integer quantity = 0;
	
	@Transient
	private BigDecimal cost;
	
	@Transient
	private boolean dirty;
	
	@Column
	@Enumerated(EnumType.STRING)
	private State state;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ProducedWork: [");
		sb.append("dirty: ").append(dirty);
		sb.append("]");
		return sb.toString();
	}
	
	private void calcCost() {
		if(price == null) price = BigDecimal.ZERO;
		if(quantity == null) quantity = Integer.valueOf(0);
		cost = price.multiply(BigDecimal.valueOf(quantity));
	}	
	
	public Employee getExecutor() {
		return executor;
	}

	public void setExecutor(Employee executor) {
		this.executor = executor;
	}

	public WorkType getWorkType() {
		return workType;
	}

	public void setWorkType(WorkType workType) {
		this.workType = workType;
	}

	public Set<MaterialConsumption> getMaterialConsumption() {
		return materialConsumption;
	}

	public void setMaterialConsumption(Set<MaterialConsumption> materialConsumption) {
		this.materialConsumption = materialConsumption;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}

	public BigDecimal getCost() {
		calcCost();
		return cost;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}