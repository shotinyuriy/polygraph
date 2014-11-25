package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.util.FormatUtil;

public class MaterialFX {
	
	private Material material;
	
	public static List<MaterialFX> convertListEntityToFX(List<Material> materials) {
		List<MaterialFX> materialsFx = new LinkedList<>();
		for(Material material : materials) {
			MaterialFX materialFx = new MaterialFX(material);
			materialsFx.add(materialFx);
		}
		return materialsFx;
	}
	
	
	public MaterialFX(Material material) {
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
	public String getMaterialTypeName() {
		if(material.getMaterialType() != null) {
			return material.getMaterialType().getName();
		}
		else {
			return null;
		}
	}
	
	public String getName() {
		return material.getName();
	}
	
	public void setName(String name) {
		material.setName(name);
	}
	
	public String getCreatedByLogin() {
		return material.getCreatedBy().getLogin();
	}
	
	public String getCreatedAt() {
		if(material.getCreatedAt() != null) {
			return FormatUtil.dateFormatter.format(material.getCreatedAt());
		}
		else {
			return null;
		}
	}
	
	public String getUpdatedByLogin() {
		return material.getCreatedBy().getLogin();
	}
	
	public String getUpdatedAt() {
		if(material.getUpdatedAt() != null) {
			return FormatUtil.dateFormatter.format(material.getUpdatedAt());
		}
		else {
			return null;
		}
	}
	
	public String getFullName() {
		String fullName = material.getName();
		MaterialType materialType = material.getMaterialType();
		if(materialType != null) {
			fullName = materialType.getName()+" "+fullName;
		}
		return fullName;
	}
}
