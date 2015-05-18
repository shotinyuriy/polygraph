package kz.aksay.polygraph.entityfx;

import kz.aksay.polygraph.entity.EntitySupport;
import kz.aksay.polygraph.util.FormatUtil;

public class EntityFX<T extends EntitySupport> {
	
	protected T entity;
	
	public EntityFX(T entity) {
		this.entity = entity;
	}
	
	public String getCreatedAtString() {
		if(entity != null)
			return FormatUtil.dateFormatter.format(entity.getCreatedAt());
		return null;
	}
	
	public String getUpdatedAtByString() {
		if(entity != null)
			return FormatUtil.dateFormatter.format(entity.getUpdatedAt());
		return null;
	}
	
	public String getCreatedByLogin() {
		if(entity != null)
			return entity.getCreatedBy().getLogin();
		return null;
	}
	
	public String getUpdatedByLogin() {
		if(entity != null)
			return entity.getUpdatedBy().getLogin();
		return null;
	}

}
