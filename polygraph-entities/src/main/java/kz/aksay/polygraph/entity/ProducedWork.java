package kz.aksay.polygraph.entity;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kz.aksay.polygraph.entity.EntitySupport;

@Entity
@Table(name="produced_work")
public class ProducedWork extends EntitySupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9184662638673352545L;

	@OneToMany(mappedBy="producedWork")
	private Set<MaterialConsumption> materialConsumption;
	
	@ManyToOne
	@JoinColumn(name="work_type_id")
	private WorkType workType;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;

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
}
