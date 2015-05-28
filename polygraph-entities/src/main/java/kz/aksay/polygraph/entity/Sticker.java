package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;

public class Sticker extends Material {
	
	@Enumerated(EnumType.ORDINAL)
	private Format format;
	
	@Enumerated(EnumType.ORDINAL)
	private Type type;
	
	@Min(value=1)
	@Column
	private Integer density;

	public static enum Type {
		
		ADASTER("Adaster"),
		PLASTIC("Plastic");
		
		private String name;
		
		public String getName() {
			return name;
		}
		
		private Type(String name) {
			this.name = name;
		}
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getDensity() {
		return density;
	}

	public void setDensity(Integer density) {
		this.density = density;
	}

	@Override
	public MaterialClass getMaterialClass() {
		return MaterialClass.STICKER;
	}

	@Override
	public String getName() {
		StringBuffer sb = new StringBuffer();
		
		if(format != null) {
			sb.append(format.name());
		}
		if(sb.length() > 0) {
			sb.append(" ");
		}
		if(type != null) {
			sb.append(type.getName());
		}
		if(sb.length() > 0) {
			sb.append(" ");
		}
		if(density != null) {
			sb.append(density);
		}
		
		return sb.toString();
	}
}
