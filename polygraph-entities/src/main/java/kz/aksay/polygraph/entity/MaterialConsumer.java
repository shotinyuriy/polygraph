package kz.aksay.polygraph.entity;

import java.util.Set;

public interface MaterialConsumer {

	public Set<MaterialConsumption> getMaterialConsumption();
	
	public void setMaterialConsumption(Set<MaterialConsumption> materialConsumption);
}
