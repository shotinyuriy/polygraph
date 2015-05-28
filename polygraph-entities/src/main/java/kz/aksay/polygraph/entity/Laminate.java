package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="cover")
public class Laminate extends Material {

	@Enumerated(EnumType.ORDINAL)
	private Format format;
	
	@Column
	@Min(value=1)
	private Integer density;

	@Override
	public MaterialClass getMaterialClass() {
		return MaterialClass.LAMINATE;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Integer getDensity() {
		return density;
	}

	public void setDensity(Integer density) {
		this.density = density;
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
		if(density != null) {
			sb.append(density);
		}
		
		return sb.toString();
	}
}
