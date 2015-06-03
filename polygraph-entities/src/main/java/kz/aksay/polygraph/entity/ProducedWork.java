package kz.aksay.polygraph.entity;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

@Entity
@Table(name="produced_work")
public class ProducedWork extends EntitySupport implements MaterialConsumer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9184662638673352545L;
	
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
	
	@ManyToOne
	@JoinColumn(name="equipment_id")
	private Equipment equipment;
	
	@Column
	private Format format;
	
	@Column
	private BigDecimal price = BigDecimal.ZERO;
	
	@Min(value = 1, message="Количество должно быть больше нуля")
	@Column
	private Integer quantity = 0;
	
	@Min(value = 0, message="Количество должно быть больше или равно нулю")
	@Column
	private Integer wasted = 0;
	
	@Column(name="equipment_order_number")
	private Integer equipmentOrderNumber;
	
	@Transient
	private BigDecimal cost;
	
	@Transient
	private boolean dirty;

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
		if(wasted == null) wasted = Integer.valueOf(0); 
		cost = price.multiply(BigDecimal.valueOf(quantity));
		BigDecimal wastedCost = price.multiply(BigDecimal.valueOf(wasted));
		cost = cost.subtract(wastedCost);
	}
	
	@Override
	public int hashCode() {
		if(id != null) {
			return id.hashCode();
		} else if(workType != null && workType.getName() != null){
			return workType.getName().hashCode();
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof ProducedWork)) return false;
		ProducedWork other = (ProducedWork) obj;
		if(this.id == null) {
			if(other.getId() != null) return false;
			return true;
		} else {
			if(other.getId() == null) return false;
			return this.id.equals(other.getId());
		}
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

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Integer getWasted() {
		return wasted;
	}

	public void setWasted(Integer wasted) {
		this.wasted = wasted;
	}

	public Integer getEquipmentOrderNumber() {
		return equipmentOrderNumber;
	}

	public void setEquipmentOrderNumber(Integer equipmentOrderNumber) {
		this.equipmentOrderNumber = equipmentOrderNumber;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}
}