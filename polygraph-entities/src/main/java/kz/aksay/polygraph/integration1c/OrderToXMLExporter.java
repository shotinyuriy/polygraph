package kz.aksay.polygraph.integration1c;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.xml1c.OrdersToXMLDriver;
import kz.aksay.xml1c.entity.XMLAddress;
import kz.aksay.xml1c.entity.XMLOrder;
import kz.aksay.xml1c.entity.XMLOrderContainer;
import kz.aksay.xml1c.entity.XMLOrderList;
import kz.aksay.xml1c.entity.XMLOrganization;
import kz.aksay.xml1c.entity.XMLProducedWork;
import kz.aksay.xml1c.entity.XMLProducedWorkList;

@Scope(value="prototype")
@Component
public class OrderToXMLExporter {

	private OrdersToXMLDriver ordersToXMLDriver = new OrdersToXMLDriver();
	
	private IContractService contractService;
	private IProducedWorkService producedWorkService;
	
	public void export(List<Order> orders, File file) throws InternalLogicException {
		
		XMLOrderContainer xmlOrderContainer = new XMLOrderContainer();
		XMLOrderList xmlOrderList = new XMLOrderList();
		List<XMLOrder> xmlOrders = new ArrayList<>(orders.size());
		
		for(Order order : orders) {
			
			Subject customer = order.getCustomer();
			if(customer instanceof Organization == false) continue;
			Organization organization = (Organization) customer;
			Contract contract = contractService.findActiveByParty2(organization);
			VicariousPower vicariousPower = order.getVicariousPower();
			
			XMLOrder xmlOrder = new XMLOrder();
			
			if(contract != null) {
				xmlOrder.setContractNumber(contract.getNumber());
			}
			
			if(vicariousPower != null) {
				xmlOrder.setVicariousPowerNumber(vicariousPower.getNumber());
			}
			
			XMLOrganization xmlCustomer = convertToXMLOrganization(organization);
			XMLOrganization xmlExecutor = convertToXMLOrganization(Organization.FIRMA_SERVER_PLUS);
			
			xmlOrder.setCustomer(xmlCustomer);
			xmlOrder.setExecutor(xmlExecutor);
			
			List<ProducedWork> producedWorks = producedWorkService.findAllByOrderId(order.getId());
			XMLProducedWorkList xmlProducedWorkList = new XMLProducedWorkList();
			List<XMLProducedWork> xmlProducedWorks 
				= new ArrayList<XMLProducedWork>(producedWorks.size());
			
			for(ProducedWork producedWork : producedWorks) {
				xmlProducedWorks.add(convertToXMLProducedWork(producedWork));
			}
			
			xmlProducedWorkList.setProducedWorks(xmlProducedWorks);
			xmlOrder.setProducedWorkList(xmlProducedWorkList);
			
			xmlOrders.add(xmlOrder);
		}
		xmlOrderList.setOrders(xmlOrders);
		xmlOrderContainer.setOrderList(xmlOrderList);
		
		try {
			ordersToXMLDriver.exportToXML(xmlOrderContainer, file);
		} catch (JAXBException je) {
			throw new InternalLogicException("Ошибка при генерации XML файла "+file.getAbsolutePath());
		}
	}
	
	private XMLOrganization convertToXMLOrganization(Organization organization) {
		XMLOrganization xmlOrganization = new XMLOrganization();
		xmlOrganization.setCode(organization.getCode1c());
		xmlOrganization.setName(organization.getFullname());
		xmlOrganization.setInn(organization.getInn());
		
		XMLAddress xmlAddress = new XMLAddress();
		xmlAddress.setCity(organization.getLegalAddress().getCity());
		xmlAddress.setStreet(organization.getLegalAddress().getStreet());
		xmlAddress.setHouse(organization.getLegalAddress().getHouse());
		xmlAddress.setStreet(organization.getLegalAddress().getStreet());
		xmlOrganization.setAddress(xmlAddress);
		
		return xmlOrganization;
	}
	
	private XMLProducedWork convertToXMLProducedWork(ProducedWork producedWork) {
		XMLProducedWork xmlProducedWork = new XMLProducedWork();
		
		xmlProducedWork.setCode(producedWork.getWorkType().getCode1c());
		xmlProducedWork.setName(producedWork.getWorkType().getName());
		xmlProducedWork.setQuantity(producedWork.getQuantity());
		xmlProducedWork.setUnit("шт.");
		xmlProducedWork.setPrice(producedWork.getPrice().doubleValue());
		xmlProducedWork.setCost(producedWork.getCost().doubleValue());
		
		return xmlProducedWork;
	}

	@Autowired
	public void setContractService(IContractService contractService) {
		this.contractService = contractService;
	}

	@Autowired
 	public void setProducedWorkService(IProducedWorkService producedWorkService) {
		this.producedWorkService = producedWorkService;
	}
}
