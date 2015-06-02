package kz.aksay.xml1c.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

public class XMLOrder {
	
	private String number;
	
	private XMLOrganization customer;
	
	private XMLOrganization executor;
	
	private String contractNumber;
	
	private String vicariousPowerNumber;
	
	private XMLProducedWorkList producedWorkList;

	@XmlElement(name="Номер")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@XmlElement(name="Заказчик")
	public XMLOrganization getCustomer() {
		return customer;
	}

	public void setCustomer(XMLOrganization customer) {
		this.customer = customer;
	}

	@XmlElement(name="Исполнитель")
	public XMLOrganization getExecutor() {
		return executor;
	}

	public void setExecutor(XMLOrganization executor) {
		this.executor = executor;
	}

	@XmlElement(name="НомерДоговора")
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	@XmlElement(name="НомерДоверенности")
	public String getVicariousPowerNumber() {
		return vicariousPowerNumber;
	}

	public void setVicariousPowerNumber(String vicariousPowerNumber) {
		this.vicariousPowerNumber = vicariousPowerNumber;
	}

	@XmlElement(name="ВыполненныеРаботы")
	public XMLProducedWorkList getProducedWorkList() {
		return producedWorkList;
	}

	public void setProducedWorkList(XMLProducedWorkList producedWorkList) {
		this.producedWorkList = producedWorkList;
	}

	

}
