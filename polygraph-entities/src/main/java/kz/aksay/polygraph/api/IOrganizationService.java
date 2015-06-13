package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.Organization;

public interface IOrganizationService extends IGenericService<Organization, Long> {
	
	boolean physicalAddressSameAsLegal(Organization organization);
	
	boolean mailAddressSameAsLegalAddress(Organization organization);
}
