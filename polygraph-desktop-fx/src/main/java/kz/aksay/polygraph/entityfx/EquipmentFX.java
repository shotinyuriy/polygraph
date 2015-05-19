package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.util.FormatUtil;

public class EquipmentFX extends EntityFX<Equipment> {

	public EquipmentFX(Equipment entity) {
		super(entity);
	}
	
	public static Collection<EquipmentFX> convertListEntityToFX(Collection<Equipment> Equipments) {
		
		Collection<EquipmentFX> equipmentsFX = new LinkedList<>();
		
		for(Equipment Equipment : Equipments) {
			equipmentsFX.add(new EquipmentFX(Equipment));
		}
		
		return equipmentsFX;
	}
	
	public Equipment getEquipmentType() {
		return entity;
	}
	
	public String getName() {
		if(entity != null)
			return entity.getName();
		return null;
	}

	public String getCreatedAtString() {
		if(entity != null)
			return FormatUtil.dateFormatter.format(entity.getCreatedAt());
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
	
	public String getWorkTypeName() {
		if(entity != null && entity.getWorkType() != null)
			return entity.getWorkType().getName();
		return null;
	}
	
	public Integer getMonochromeUsageCount() {
		if(entity != null)
			return entity.getMonochromeUsageCount();
		return Integer.valueOf(0);
	}
	
	public Integer getColoredUsageCount() {
		if(entity != null)
			return entity.getColoredUsageCount();
		return Integer.valueOf(0);
	}
	
	@Override
	public String toString() {
		
		return entity.getName();
	}
	
	public Equipment getEquipment() {
		return entity;
	}
}
