package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.BindingSpring;
import kz.aksay.polygraph.entity.Laminate;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.Sticker;

public class MaterialFX extends EntityFX<Material> {
	
	public MaterialFX(Material entity) {
		super(entity);
	}
	
	public MaterialFX(BindingSpring entity) {
		super(entity);
	}
	
	public MaterialFX(Paper entity) {
		super(entity);
	}
	
	public MaterialFX(Sticker entity) {
		super(entity);
	}
	
	public MaterialFX(Laminate entity) {
		super(entity);
	}

	@Override
	public String toString() {
		return getName();
	}
	
	public String getMaterialClassName() {
		if(entity.getMaterialClass() != null) {
			return entity.getMaterialClass().getName();
		}
		else {
			return null;
		}
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
