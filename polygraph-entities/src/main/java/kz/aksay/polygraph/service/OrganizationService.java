package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends AbstractGenericService<Organization, Long> 
	implements IOrganizationService {
	private GenericDao<Organization, Long> organizationDao;
	
	@Autowired
	public void setOrganizationDao(GenericDao<Organization, Long> organizationDao) {
		this.organizationDao = organizationDao;
	}

	@Override
	protected GenericDao<Organization, Long> getDao() {
		return organizationDao;
	}
}
