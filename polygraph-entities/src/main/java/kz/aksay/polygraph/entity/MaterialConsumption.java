package kz.aksay.polygraph.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "material_consumption")
public class MaterialConsumption extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8066545437632459224L;
	
	@ManyToOne
	@ForeignKey(name="material_consumption_fk_material")
	@JoinColumn(name="material_id", nullable=false)
	private Material material;

	@Column
	private BigDecimal quantity;
	
	@ManyToOne
	@ForeignKey(name="material_consumption_fk_produced_work")
	@JoinColumn(name="produced_work_id")
	private ProducedWork producedWork;
	
	@ManyToOne
	@ForeignKey(name="material_consumption_fk_order")
	@JoinColumn(name="order_id")
	private Order order;
	
	@Transient
	private boolean dirty = false;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MaterialConsumption: [");
		sb.append("id: ").append(id).append(", ");
		sb.append("material: ").append(material).append(", ");
		sb.append("order: ").append(order).append(", ");
		sb.append("dirty: ").append(dirty);
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null) return false;
		if(!(object instanceof MaterialConsumption)) return false;
		MaterialConsumption other = (MaterialConsumption) object;
		if(id != null) {
			if(other.getId() == null) return false;
			if(!id.equals(other.getId())) return false;
		}
		return true;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ProducedWork getProducedWork() {
		return producedWork;
	}

	public void setProducedWork(ProducedWork producedWork) {
		this.producedWork = producedWork;
	}

}
