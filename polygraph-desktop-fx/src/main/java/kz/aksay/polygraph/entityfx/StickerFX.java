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
	
	public String getDensityString() {
		if(entity.getDensity() != null) {
			return entity.getDensity().toString();
		}
		return null;
	}
	
	public String getTypeName() {
		if(entity.getStickerType() != null) {
			return entity.getStickerType().getName();
		}
		return null;
	}
	
	public String getDescription() {
		return entity.getDescription();
	}
	
	public String getPriceString() {
		if(entity.getPrice() != null) {
			return entity.getPrice().toString();
		}
		return null;
	}

}
