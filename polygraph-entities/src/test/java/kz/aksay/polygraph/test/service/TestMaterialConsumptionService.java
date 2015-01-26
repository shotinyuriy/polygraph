package kz.aksay.polygraph.test.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.test.ContextUtils;

import static org.testng.Assert.*;

public class TestMaterialConsumptionService {
	
	private ApplicationContext context = ContextUtils.getApplicationContext();
	private MaterialConsumptionService materialConsumptionService;
	
	
	public void setUp() {
		materialConsumptionService = context.getBean(
				MaterialConsumptionService.class);
	}
	
	public void testFindByExample(MaterialConsumption materialConsumptionExample, List<MaterialConsumption> expected) {
		String text = null;
		List<MaterialConsumption> actual = 
				materialConsumptionService.findByExample(materialConsumptionExample);
		assertEquals(actual, expected);
	}
}
