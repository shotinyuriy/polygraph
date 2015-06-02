package kz.aksay.xml1c.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class XMLProducedWork {
	
	private String code;
	
	private String name;
	
	private String unit;
	
	private int quantity;
	
	private double price;
	
	private double cost;

	@XmlElement(name="Наименование")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="ЕдИзм")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@XmlElement(name="Количество")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@XmlElement(name="Цена")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@XmlElement(name="Стоимость")
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@XmlElement(name="Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
