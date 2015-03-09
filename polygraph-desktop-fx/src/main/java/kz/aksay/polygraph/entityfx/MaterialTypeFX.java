package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.util.FormatUtil;

public class MaterialTypeFX {
	
	private final MaterialType materialType;
	
	public static List<MaterialTypeFX> convertListEntityToFX(List<MaterialType> materialTypes) {
		List<MaterialTypeFX> materialTypesFx = new LinkedList<>();
		for(MaterialType materialType : materialTypes) {
			MaterialTypeFX materialTypeFx = new MaterialTypeFX(materialType);
			materialTypesFx.add(materialTypeFx);
		}
		return materialTypesFx;
	}

	public MaterialTypeFX(MaterialType materialType) {
		this.materialType = materialType;
	}

	public MaterialType getMaterialType() {
		return this.materialType;
	}
	
	public String getName() {
		return materialType.getName();
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(materialType.getCreatedAt());
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
