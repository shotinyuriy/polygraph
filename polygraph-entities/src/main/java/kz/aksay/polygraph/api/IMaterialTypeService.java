package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.MaterialType;

public interface IMaterialTypeService extends IGenericService<MaterialType, Long> {
	
	public MaterialType findByName(String materialTypeName);

}
