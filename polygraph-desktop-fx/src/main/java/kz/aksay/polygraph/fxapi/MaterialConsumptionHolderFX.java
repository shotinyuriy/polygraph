package kz.aksay.polygraph.fxapi;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;

public class MaterialConsumptionHolderFX {

	private ObservableList<MaterialConsumptionFX> materialConsumptionsFX = new SimpleListProperty<>();
	
	public ObservableList<MaterialConsumptionFX> getMaterialConsumptionFX() {
		return materialConsumptionsFX;
	}
	
	public void setMaterialConsumptionFX(
			ObservableList<MaterialConsumptionFX> materialConsumption) {
		this.materialConsumptionsFX = materialConsumption;
	}
	
	public Set<MaterialConsumption> getMaterialConsumption() {
		Set<MaterialConsumption> resultSet = new HashSet<>();
		for(MaterialConsumptionFX materialConsumptionFX : materialConsumptionsFX) {
			resultSet.add(materialConsumptionFX.getMaterialConsumption());
		}
		return resultSet;
	}

	public void setMaterialConsumption(
			Set<MaterialConsumption> materialConsumptions) {
		materialConsumptionsFX.clear();
		for(MaterialConsumption materialConsumption : materialConsumptions) {
			materialConsumptionsFX.add(new MaterialConsumptionFX(materialConsumption));
		}
	}

}
