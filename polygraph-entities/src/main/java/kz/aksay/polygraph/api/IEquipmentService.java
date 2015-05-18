package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.Equipment;

public interface IEquipmentService extends IGenericService<Equipment, Long> {

	public Equipment findByName(String name);
}
