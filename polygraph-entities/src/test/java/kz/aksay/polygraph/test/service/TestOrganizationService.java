package kz.aksay.polygraph.test.service;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.test.ContextUtils;
import kz.aksay.polygraph.util.AddressGenerator;

public class TestOrganizationService extends Assert {
	private ApplicationContext context;
	
	private IOrganizationService organizationService;
	
	@BeforeClass
	public void setUp() {
		context = ContextUtils.getApplicationContext();
		organizationService = context.getBean(IOrganizationService.class);
	}
	
	private Organization createOrganization() throws Exception {
		Organization organization = new Organization();
		
		Date now = new Date();
		
		organization.setCreatedAt(now);
		organization.setCreatedBy(User.TECH_USER);
		organization.setFullname("Тестовая Организация");
		organization.setShortname("Тестовая Организация");
		organization.setInn("0123456789");
		organization.setKpp("0123456789");
		organization.setAddress(AddressGenerator.generateAddress());
		
		organizationService.save(organization);
		
		return organization;
	}
	
	@Test
	public void testSave() throws Exception {
		Organization organization  = createOrganization();
		
		organizationService.save(organization);
		
		organizationService.delete(organization);
	}
}
