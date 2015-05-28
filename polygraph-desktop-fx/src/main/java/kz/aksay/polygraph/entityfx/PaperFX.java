package kz.aksay.polygraph.entityfx;

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
		return entity.getType().getName();
	}
	
	public String getDescription() {
		return entity.getDescription();
	}

}
