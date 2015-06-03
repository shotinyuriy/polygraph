package kz.aksay.polygraph.fxapi;

import java.util.List;

import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.WorkType;

public interface MaterialConsumptionForm {

	void loadMaterialsByWorkType(WorkType workType);
	
	List<MaterialConsumption> getMaterialConsumptionList();
}
