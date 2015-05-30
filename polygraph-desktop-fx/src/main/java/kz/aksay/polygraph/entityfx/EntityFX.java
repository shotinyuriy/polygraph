package kz.aksay.polygraph.entityfx;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.EntitySupport;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.util.FormatUtil;

public class EntityFX<T extends EntitySupport> {
	
	protected T entity;
	
	public static <T extends EntityFX, V> List<T> convertListEntityToFX(Collection<V> entities, Class<? extends EntityFX> clazz) {
		List<T> entitiesFx = new LinkedList<>();
		if(entities != null) {
			for(V material : entities) {
				T entityFX;
				try {
					entityFX = (T) clazz.getDeclaredConstructor(material.getClass()).newInstance(material);
					entitiesFx.add(entityFX);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return entitiesFx;
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
