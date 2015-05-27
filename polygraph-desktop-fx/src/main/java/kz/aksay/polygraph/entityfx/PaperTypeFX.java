package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.util.FormatUtil;

public class PaperTypeFX extends EntityFX<PaperType> {
	
	public PaperTypeFX(PaperType paperType) {
		super(paperType);
	}

	public PaperType getPaperType() {
		return this.entity;
	}
	
	public String getName() {
		return entity.getName();
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(entity.getCreatedAt());
	}
	
	public String getCreatedByLogin() {
		return entity.getCreatedBy().getLogin();
	}
	
	public String getUpdatedByLogin() {
		return entity.getUpdatedBy().getLogin();
	}
	
	@Override
	public String toString() {
		return entity.getName();
	}
}
