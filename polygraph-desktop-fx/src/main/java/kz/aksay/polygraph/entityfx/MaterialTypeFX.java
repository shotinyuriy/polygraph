package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.MaterialType;

public class MaterialTypeFX {
	
	public static List<MaterialTypeFX> convertListEntityToFX(List<MaterialType> materialTypes) {
		List<MaterialTypeFX> materialTypesFx = new LinkedList<>();
		for(MaterialType materialType : materialTypes) {
			MaterialTypeFX materialTypeFx = new MaterialTypeFX(materialType);
			materialTypesFx.add(materialTypeFx);
		}
		return materialTypesFx;
	}
	
	private MaterialType materialType;

	public MaterialTypeFX(MaterialType materialType) {
		this.materialType = materialType;
	}
	

	public MaterialType getMaterialType() {
		return this.materialType;
	}
	
	public String getName() {
		return materialType.getName();
	}
	
	public String getCreatedByLogin() {
		return materialType.getCreatedBy().getLogin();
	}
	
	public String getUpdatedByLogin() {
		return materialType.getUpdatedBy().getLogin();
	}
	
	@Override
	public String toString() {
		return materialType.getName();
	}
}
