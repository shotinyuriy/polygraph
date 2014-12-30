package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material")
public class Material extends EntitySupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7597841704963862971L;

	@ManyToOne
	@JoinColumn(name="material_type_id")
	private MaterialType materialType;

	@Column
	private String name;

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
