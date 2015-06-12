package kz.aksay.polygraph.service;

import java.util.Arrays;
import java.util.List;

import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Address;
import kz.aksay.polygraph.entity.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService extends AbstractGenericService<Organization, Long> 
	implements IOrganizationService {
	private GenericDao<Organization, Long> organizationDao;
	
	@Override
	@Transactional
	public Organization save(Organization organization) throws Exception {
		
		List<Address> addresses = Arrays.asList(organization.getLegalAddress(),
				organization.getPhysicalAddress(),
				organization.getMailAddress());
		for(Address address : addresses) {
			address.setCreatedAt(organization.getCreatedAt());
			address.setCreatedBy(organization.getCreatedBy());
		}
		return super.save(organization);
	}
	
	@Autowired
	public void setOrganizationDao(GenericDao<Organization, Long> organizationDao) {
		this.organizationDao = organizationDao;
	}

	@Override
	protected GenericDao<Organization, Long> getDao() {
		return organizationDao;
	}
}
