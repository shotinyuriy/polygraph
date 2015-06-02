package kz.aksay.xml1c.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ВыгрузкаЗаказов")
public class XMLOrderContainer {
	
	
	private XMLOrderList orderList;

	@XmlElement(name="Заказы")
	public XMLOrderList getOrderList() {
		return orderList;
	}

	public void setOrderList(XMLOrderList orders) {
		this.orderList = orders;
	}
}
