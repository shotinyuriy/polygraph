package kz.aksay.polygraph.service;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Organization;

@Service
public class ContractService extends AbstractGenericService<Contract, Long> implements
		IContractService {

	private GenericDao<Contract, Long> contractDao;
	
	@Override
	protected GenericDao<Contract, Long> getDao() {
		
		return contractDao;
	}
	
	@Autowired
	public void setContractDao(GenericDao<Contract, Long> contractDao) {
		
		this.contractDao = contractDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findByParty2(Organization organization) {
		return getDao().getSession().createCriteria(getDao().clazz())
				.add(Restrictions.eq("party2", organization))
				.list();
	}
	
}
