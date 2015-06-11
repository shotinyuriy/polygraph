package kz.aksay.polygraph.api;

import java.util.List;

import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Organization;

public interface IContractService extends IGenericService<Contract, Long> {

	List<Contract> findByParty2(Organization organization);

	Contract findActiveByParty2(Organization organization);

}
