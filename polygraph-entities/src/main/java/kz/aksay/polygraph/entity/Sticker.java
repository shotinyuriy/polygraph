package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Sticker extends Material {
	
	@NotNull(message="Не указан формат наклейки")
	@Enumerated(EnumType.ORDINAL)
	private Format format;
	
	@NotNull(message="Не указан тип наклейки")
	@Enumerated(EnumType.ORDINAL)
	private Type type;
	
	@NotNull(message="Не указана плотность наклейки")
	@Min(value=1, message="Минимальное значение плотности 1мк")
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
		
		sb.append(getMaterialClass().getName());
		sb.append(" ");
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
