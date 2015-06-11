package kz.aksay.polygraph.fxapi;

import java.util.Set;

import javafx.collections.ObservableList;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;

public interface MaterialConsumptionHolderFX {

	public ObservableList<MaterialConsumptionFX> getMaterialConsumptionFX();
	
	public void setMaterialConsumptionFX(
			ObservableList<MaterialConsumptionFX> materialConsumption);
	
	public Set<MaterialConsumption> getMaterialConsumption();
	
	public void setMaterialConsumption(
			Set<MaterialConsumption> materialConsumptions);

	public boolean isAllowedToEdit();

}
