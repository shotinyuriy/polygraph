package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.util.FormatUtil;

public class EquipmentFX extends EntityFX<Equipment> {

	private Integer monochromeUsagesA3 = 0;
	private Integer monochromeUsagesA4 = 0;
	private Integer coloredUsagesA3 = 0;
	private Integer coloredUsagesA4 = 0;
	
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
	
	
	
	public Integer getOrdersCount() {
		if(entity != null)
			return entity.getOrdersCount();
		return Integer.valueOf(0);
	}
	
	public String getOrdersCountString() {
		return getOrdersCount().toString();
	}
	
	@Override
	public String toString() {
		
		return entity.getName();
	}
	
	public Equipment getEquipment() {
		return entity;
	}

	public Integer getMonochromeUsagesA3() {
		return monochromeUsagesA3;
	}

	public void setMonochromeUsagesA3(Integer monochromeUsagesA3) {
		this.monochromeUsagesA3 = monochromeUsagesA3;
	}

	public Integer getMonochromeUsagesA4() {
		return monochromeUsagesA4;
	}

	public void setMonochromeUsagesA4(Integer monochromeUsagesA4) {
		this.monochromeUsagesA4 = monochromeUsagesA4;
	}

	public Integer getColoredUsagesA3() {
		return coloredUsagesA3;
	}

	public void setColoredUsagesA3(Integer coloredUsagesA3) {
		this.coloredUsagesA3 = coloredUsagesA3;
	}

	public Integer getColoredUsagesA4() {
		return coloredUsagesA4;
	}

	public void setColoredUsagesA4(Integer coloredUsagesA4) {
		this.coloredUsagesA4 = coloredUsagesA4;
	}
}
