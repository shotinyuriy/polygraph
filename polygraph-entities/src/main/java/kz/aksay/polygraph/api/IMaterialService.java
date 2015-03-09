package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialType;

import org.springframework.stereotype.Service;

public interface IMaterialService extends IGenericService<Material, Long> {

	public Material findByMaterialTypeAndName(MaterialType materialType, String name);
	
	public int deleteAllByMaterialType(MaterialType materialType);

}
