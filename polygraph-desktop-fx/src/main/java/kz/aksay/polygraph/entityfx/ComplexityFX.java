package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Complexity;

public class ComplexityFX extends EntityFX<Complexity> {

	public ComplexityFX(Complexity entity) {
		super(entity);
	}
	
	public String getMinString() {
		if(entity.getMin() != null) {
			return entity.getMin().toString();
		}
		return null;
	}
	
	public String getMaxString() {
		if(entity.getMax() != null) {
			return entity.getMax().toString();
		}
		return null;
	}
	
	public String getName() {
		return entity.getName();
	}

}
