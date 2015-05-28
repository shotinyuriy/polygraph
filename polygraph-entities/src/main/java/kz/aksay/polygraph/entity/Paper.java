package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="paper")
public class Paper extends Material {
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Format format;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="paper_type_id")
	private PaperType type;
	
	@NotNull
	@Min(value=1)
	@Column
	private Integer density;

	@Override
	public MaterialClass getMaterialClass() {
		return MaterialClass.PAPER;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public PaperType getType() {
		return type;
	}

	public void setType(PaperType type) {
		this.type = type;
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
