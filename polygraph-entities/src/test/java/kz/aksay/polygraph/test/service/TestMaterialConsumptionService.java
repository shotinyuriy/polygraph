package kz.aksay.polygraph.test.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.test.ContextUtils;
import static org.testng.Assert.*;

public class TestMaterialConsumptionService {
	
	private ApplicationContext context = ContextUtils.getApplicationContext();
	private IMaterialConsumptionService materialConsumptionService;
	
	
	public void setUp() {
		materialConsumptionService = context.getBean(
				IMaterialConsumptionService.class);
	}
	
	public void testFindByExample(MaterialConsumption materialConsumptionExample, List<MaterialConsumption> expected) {
		List<MaterialConsumption> actual = 
				materialConsumptionService.findByExample(materialConsumptionExample);
		assertEquals(actual, expected);
	}
}
