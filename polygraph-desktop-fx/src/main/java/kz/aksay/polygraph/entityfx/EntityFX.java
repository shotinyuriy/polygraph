package kz.aksay.polygraph.entityfx;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.EntitySupport;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.util.FormatUtil;

public class EntityFX<T extends EntitySupport> {
	
	protected T entity;
	
	public static <T extends EntityFX, V> List<T> convertListEntityToFX(List<V> entities, Class<? extends EntityFX> clazz) {
		List<T> materialsFx = new LinkedList<>();
		for(V material : entities) {
			T entityFX;
			try {
				entityFX = (T) clazz.getDeclaredConstructor(material.getClass()).newInstance(material);
				materialsFx.add(entityFX);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return materialsFx;
	}
	
	public EntityFX(T entity) {
		this.entity = entity;
	}
	
	public T getEntity() {
		return entity;
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
