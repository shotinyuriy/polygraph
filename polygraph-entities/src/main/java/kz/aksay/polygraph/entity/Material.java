package kz.aksay.polygraph.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "material")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Material extends EntitySupport {
	
	private static final long serialVersionUID = -7597841704963862971L;

	@Column
	protected String description;
	
	@Min(value=0, message="Цена должна быть больше или равна нулю")
	@Column
	protected BigDecimal price;
	
	@Size(min=1, message="Длина кода в 1С дожна составлять хотя бы 1 символ")
	protected String code1c;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Material [");
		sb.append("id: ").append(id).append(", ");
		sb.append("name: ").append(" ").append(description).append(", ");
		sb.append("]");
		
		return sb.toString();
	}
	
	
	public abstract MaterialClass getMaterialClass();
	
	public abstract String getName();
	

	public String getDescription() {
		return description;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public String getCode1c() {
		return code1c;
	}


	public void setCode1c(String code1c) {
		this.code1c = code1c;
	}
}
