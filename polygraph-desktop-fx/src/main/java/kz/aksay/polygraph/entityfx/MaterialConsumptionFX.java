package kz.aksay.polygraph.entityfx;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;

import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.util.FormatUtil;

public class MaterialConsumptionFX {

	private MaterialConsumption materialConsumption;
	private MaterialFX materialFX;
	
	public MaterialConsumptionFX(MaterialConsumption materialConsumption) {
		this.materialConsumption = materialConsumption;
		Material material = materialConsumption.getMaterial();
		if(material != null) {
			materialFX = new  MaterialFX(material);
		}
	}
	
	public static Collection<MaterialConsumptionFX> convertListEntityToFX(
			Collection<MaterialConsumption> materialConsumtions) {
		Collection<MaterialConsumptionFX> materialConsumptionsFX 
			= new LinkedList<MaterialConsumptionFX>();
		if(materialConsumtions != null) {
			for(MaterialConsumption materialConsumption : materialConsumtions) {
				materialConsumptionsFX.add(new MaterialConsumptionFX(materialConsumption));
			}
		}
		return materialConsumptionsFX;
	}

	public MaterialConsumption getMaterialConsumption() {
		return materialConsumption;
	}
	
	public String getMaterialName() {
		if(materialFX != null) {
			return materialFX.getName();
		}
		return null;
	}
	
	public BigDecimal getQuantity() {
		return materialConsumption.getQuantity();
	}
	
	public boolean isDirty() {
		return materialConsumption.isDirty();
	}
	
	public void setDirty(boolean dirty) {
		materialConsumption.setDirty(dirty);
	}
	
	public void setProducedWork(ProducedWorkFX producedWorkFX) {
		this.materialConsumption.setProducedWork(
				producedWorkFX.getProducedWork());
	}
	
	public void setMaterialFX(MaterialFX materialFX) {
		this.materialFX = materialFX;
		this.materialConsumption.setMaterial(materialFX.getMaterial());
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.materialConsumption.setQuantity(quantity);
	}
	
	public String getOrderCreatedAtString() {
		Order order = getMaterialConsumption().getOrder();
		if(order  != null ) {
			if( order.getCreatedAt() != null ) {
				return FormatUtil.dateFormatter.format(order.getCreatedAt());
			}
		}
		return null;
	}
}
