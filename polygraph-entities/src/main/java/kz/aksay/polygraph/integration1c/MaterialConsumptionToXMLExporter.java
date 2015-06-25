package kz.aksay.polygraph.integration1c;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.xml1c.MaterialConsumptionToXMLDriver;
import kz.aksay.xml1c.entity.XMLAddress;
import kz.aksay.xml1c.entity.XMLMaterialConsumption;
import kz.aksay.xml1c.entity.XMLMaterialConsumptionContainer;
import kz.aksay.xml1c.entity.XMLMaterialConsumptionList;
import kz.aksay.xml1c.entity.XMLOrder;
import kz.aksay.xml1c.entity.XMLOrderContainer;
import kz.aksay.xml1c.entity.XMLOrderList;
import kz.aksay.xml1c.entity.XMLOrganization;
import kz.aksay.xml1c.entity.XMLProducedWork;
import kz.aksay.xml1c.entity.XMLProducedWorkList;

@Scope(value="prototype")
@Component
public class MaterialConsumptionToXMLExporter {

	private MaterialConsumptionToXMLDriver ordersToXMLDriver = new MaterialConsumptionToXMLDriver();
		
	public void export(List<MaterialConsumption> orders, File file) throws InternalLogicException {
		
		Map<Material, int[]> materialConsumptionMap 
			= new HashMap<Material, int[]>();
		
		for(MaterialConsumption materialConsumption : orders) {
			
			int[] stats = materialConsumptionMap.get(materialConsumption.getMaterial());
			if(stats == null) {
				stats = new int[] {0, 0};
				materialConsumptionMap.put(materialConsumption.getMaterial(), stats);
			}
			stats[0] += materialConsumption.getQuantity();
			stats[1] += materialConsumption.getWasted();
		}
			
		XMLMaterialConsumptionContainer xmlMaterialConsumtionContainer 
			= new XMLMaterialConsumptionContainer();
		XMLMaterialConsumptionList xmlMaterialConsumptionList 
			= new XMLMaterialConsumptionList();
		
		for(Map.Entry<Material, int[]> entry : materialConsumptionMap.entrySet()) {
			
			Material material = entry.getKey();
			int[] stats = entry.getValue();
			XMLMaterialConsumption xmlMaterialConsumption = new XMLMaterialConsumption();
			xmlMaterialConsumption.setMaterialCode1c(material.getCode1c());
			xmlMaterialConsumption.setMaterialName(material.getName());
			xmlMaterialConsumption.setQuantity(stats[0]);
			xmlMaterialConsumption.setWasted(stats[1]);
			xmlMaterialConsumptionList.getXmlMaterialConsumptions().add(xmlMaterialConsumption);
		}
		xmlMaterialConsumtionContainer.setXmlMaterialConsumptionList(xmlMaterialConsumptionList);
		
		try {
			ordersToXMLDriver.exportToXML(xmlMaterialConsumtionContainer, file);
		} catch (JAXBException je) {
			je.printStackTrace();
			throw new InternalLogicException("Ошибка при генерации XML файла "+file.getAbsolutePath());
		}
	}
	
}
