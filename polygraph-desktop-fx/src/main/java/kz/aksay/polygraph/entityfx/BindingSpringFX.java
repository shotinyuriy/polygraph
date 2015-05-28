package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.BindingSpring;

public class BindingSpringFX extends EntityFX<BindingSpring> {

	public BindingSpringFX(BindingSpring entity) {
		super(entity);
	}
	
	public String getTypeName() {
		if(entity.getType() != null) {
			return entity.getType().getName();
		}
		return null;
	}
	
	public String getDiameterString() {
		if(entity.getDiameter() != null) {
			return entity.getDiameter().toString();
		}
		return null;
	}
	
	public String getDescription() {
		return entity.getDescription();
	}

}
