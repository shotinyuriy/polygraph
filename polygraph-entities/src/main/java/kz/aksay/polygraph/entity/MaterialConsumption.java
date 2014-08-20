package kz.aksay.polygraph.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material_consumption")
public class MaterialConsumption extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8066545437632459224L;
	
	@ManyToOne
	@JoinColumn(name="material_id", nullable=false)
	private Material material;

	@Column
	private BigDecimal quantity;
	
	@ManyToOne
	@JoinColumn(name="produced_work_id", nullable=false)
	private ProducedWork producedWork;

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

	public ProducedWork getProducedWork() {
		return producedWork;
	}

	public void setProducedWork(ProducedWork producedWork) {
		this.producedWork = producedWork;
	}

}
