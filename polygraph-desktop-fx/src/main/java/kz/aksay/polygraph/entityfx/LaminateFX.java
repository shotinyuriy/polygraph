package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Laminate;

public class LaminateFX extends EntityFX<Laminate> {

	public LaminateFX(Laminate entity) {
		super(entity);
	}
	
	public String getFormatName() {
		if(entity.getFormat() != null) {
			return entity.getFormat().name();
		}
		return null;
	}
	
	public String getDensityString() {
		if(entity.getDensity() != null) {
			return entity.getDensity().toString();
		}
		return null;
	}
	
	public String getPriceString() {
		if(entity.getPrice() != null) {
			return entity.getPrice().toString();
		}
		return null;
	}

	public String getName() {
		if(entity != null) 
			return entity.getName();
		return null;
		
	}
	
	public String getCode1c() {
		if(entity != null) {
			return entity.getCode1c();
		}
		return null;
	}
	
	public String getDescription() {
		return entity.getDescription();
	}
}
