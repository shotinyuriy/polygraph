package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Equipment;

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
	
	@Override
	public String toString() {
		
		return entity.getEquipmentType().getName()+" "+entity.getName();
	}
	
	public Equipment getEquipment() {
		return entity;
	}
}
