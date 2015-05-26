package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.util.FormatUtil;

public class MaterialTypeFX {
	
	private final PaperType paperType;
	
	public static List<MaterialTypeFX> convertListEntityToFX(List<PaperType> paperTypes) {
		List<MaterialTypeFX> materialTypesFx = new LinkedList<>();
		for(PaperType paperType : paperTypes) {
			MaterialTypeFX materialTypeFx = new MaterialTypeFX(paperType);
			materialTypesFx.add(materialTypeFx);
		}
		return materialTypesFx;
	}

	public MaterialTypeFX(PaperType paperType) {
		this.paperType = paperType;
	}

	public PaperType getMaterialType() {
		return this.paperType;
	}
	
	public String getName() {
		return paperType.getName();
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(paperType.getCreatedAt());
	}
	
	public String getCreatedByLogin() {
		return paperType.getCreatedBy().getLogin();
	}
	
	public String getUpdatedByLogin() {
		return paperType.getUpdatedBy().getLogin();
	}
	
	@Override
	public String toString() {
		return paperType.getName();
	}
}
