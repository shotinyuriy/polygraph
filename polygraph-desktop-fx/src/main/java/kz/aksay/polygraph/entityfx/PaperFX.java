package kz.aksay.polygraph.entityfx;

import java.math.BigDecimal;

import kz.aksay.polygraph.entity.Paper;

public class PaperFX extends EntityFX<Paper> {

	public PaperFX(Paper entity) {
		super(entity);
	}
	
	public String getFormatName() {
		if(entity != null && entity.getFormat() != null) {
			return entity.getFormat().name();
		}
		return null;
	}
	
	public String getDensityName() {
		if(entity != null && entity.getDensity() != null) {
			return entity.getDensity().toString();
		}
		return null;
	}
	
	public String getTypeName() {
		if(entity.getType() != null) {
			return entity.getType().getName();
		}
		return null;
	}
	
	public String getDescription() {
		return entity.getDescription();
	}
	
	public BigDecimal getPrice() {
		return entity.getPrice();
	}
	
	public String getPriceString() {
		if(entity.getPrice() != null) {
			return entity.getPrice().toString();
		}
		return null;
	}

}
