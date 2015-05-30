package kz.aksay.polygraph.entityfx;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.util.FormatUtil;

public class MaterialConsumptionFX extends EntityFX<MaterialConsumption> {
	
	private MaterialFX materialFX;
	
	public MaterialConsumptionFX(MaterialConsumption materialConsumption) {
		super(materialConsumption);
		Material material = materialConsumption.getMaterial();
		if(material != null) {
			materialFX = new  MaterialFX(material);
		}
	}
	
	public String getMaterialName() {
		if(materialFX != null) {
			return materialFX.getName();
		}
		return null;
	}
	
	public BigDecimal getQuantity() {
		return entity.getQuantity();
	}
	
	public boolean isDirty() {
		return entity.isDirty();
	}
	
	public void setDirty(boolean dirty) {
		entity.setDirty(dirty);
	}
	
	public void setProducedWork(ProducedWorkFX producedWorkFX) {
		this.entity.setProducedWork(
				producedWorkFX.getEntity());
	}
	
	public void setMaterialFX(MaterialFX materialFX) {
		if(materialFX != null) {
			this.materialFX = materialFX;
			this.entity.setMaterial(materialFX.getMaterial());
		}
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.entity.setQuantity(quantity);
	}
	
	public String getOrderCreatedAtString() {
		Order order = getEntity().getOrder();
		if(order  != null ) {
			if( order.getCreatedAt() != null ) {
				return FormatUtil.dateFormatter.format(order.getCreatedAt());
			}
		}
		return null;
	}
}
