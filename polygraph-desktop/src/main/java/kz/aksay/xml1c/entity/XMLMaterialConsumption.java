package kz.aksay.xml1c.entity;

import javax.xml.bind.annotation.XmlElement;

public class XMLMaterialConsumption {

	
	private String materialCode1c;
	
	
	private String materialName;
	
	
	private Integer quantity;
	
	
	private Integer wasted;

	@XmlElement
	public String getMaterialCode1c() {
		return materialCode1c;
	}

	public void setMaterialCode1c(String materialCode1c) {
		this.materialCode1c = materialCode1c;
	}

	@XmlElement
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	@XmlElement
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@XmlElement
	public Integer getWasted() {
		return wasted;
	}

	public void setWasted(Integer wasted) {
		this.wasted = wasted;
	}
}
