package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.util.FormatUtil;

public class EquipmentTypeFX {
	
	private Equipment equipment;
	
	public EquipmentTypeFX(Equipment equipment) {
		
		this.equipment = equipment;
	}
	
	public static Collection<EquipmentTypeFX> convertListEntityToFX(Collection<Equipment> equipments) {
		
		Collection<EquipmentTypeFX> employeesFX = new LinkedList<>();
		
		for(Equipment equipment : equipments) {
			employeesFX.add(new EquipmentTypeFX(equipment));
		}
		
		return employeesFX;
	}

	public Equipment getEquipmentType() {
		return equipment;
	}
	
	public String getName() {
		return equipment.getName();
	}

	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(equipment.getCreatedAt());
	}
	
	public String getCreatedByLogin() {
		return equipment.getCreatedBy().getLogin();
	}
	
	public String getUpdatedByLogin() {
		return equipment.getUpdatedBy().getLogin();
	}
	
	public String getWorkTypeName() {
		if(equipment != null) {
			if(equipment.getWorkType() != null) {
				return equipment.getWorkType().getName();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return equipment.getName();
	}
}
