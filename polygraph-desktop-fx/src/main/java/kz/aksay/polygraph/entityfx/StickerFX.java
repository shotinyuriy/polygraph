package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.Sticker;

public class StickerFX extends EntityFX<Sticker> {

	public StickerFX(Sticker entity) {
		super(entity);
	}
	
	public String getFormatName() {
		if(entity.getFormat() != null) {
			return entity.getFormat().name();
		}
		return null;
	}
	
	public String getDensityName() {
		if(entity.getDensity() != null) {
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

}
