package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="spring")
public class BindingSpring extends Material {

	public static enum Type {
		
		PLASTIC("Пластик"),
		METAL("Металл");
		
		private String name;
		
		private Type(String name) {
			this.name = name();
		}
		
		public String getName() {
			return name;
		}
	}
	
	@Column
	@Min(value=1)
	private Integer diameter;
	
	@Enumerated(EnumType.STRING)
	private Type type;

	public Integer getDiameter() {
		return diameter;
	}

	public void setDiameter(Integer diameter) {
		this.diameter = diameter;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public MaterialClass getMaterialClass() {
		return MaterialClass.BINDING_SPRING;
	}
	
	@Override
	public String getName() {
		StringBuffer sb = new StringBuffer();
		
		if(type != null) {
			sb.append(type.name());
		}
		if(sb.length() > 0) {
			sb.append(" ");
		}
		if(diameter != null) {
			sb.append(diameter);
		}
		
		return sb.toString();
	}
}
