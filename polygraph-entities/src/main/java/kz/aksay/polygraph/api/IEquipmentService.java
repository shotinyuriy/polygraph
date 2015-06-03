package kz.aksay.polygraph.api;

import java.util.Map;

import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.ProducedWork;

public interface IEquipmentService extends IGenericService<Equipment, Long> {

	public Equipment findByName(String name);
	
	public Integer countOfUsagesByExample(ProducedWork producedWork);
}
