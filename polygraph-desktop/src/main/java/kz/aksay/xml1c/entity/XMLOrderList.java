package kz.aksay.xml1c.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class XMLOrderList {

	private List<XMLOrder> orders;

	@XmlElement(name="Заказ")
	public List<XMLOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<XMLOrder> orders) {
		this.orders = orders;
	}	
}
