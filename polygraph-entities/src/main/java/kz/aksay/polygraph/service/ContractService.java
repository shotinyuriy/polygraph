package kz.aksay.polygraph.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly=true)
	public List<Contract> findByParty2(Organization organization) {
		return getDao().getSession().createCriteria(getDao().clazz())
				.add(Restrictions.eq("party2", organization))
				.list();
	}

	@Override
	@Transactional(readOnly=true)
	public Contract findActiveByParty2(Organization organization) {
		return (Contract) getDao().getSession().createCriteria(getDao().clazz())
				.add(Restrictions.eq("party2", organization))
				.add(Restrictions.ge("endDate", new Date()))
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	@Transactional(readOnly=true)
	public String getNewNumber() {
		List<Contract> contracts = getDao().readAll();
		int count = contracts.size() + 1;
		int minLength = 4+3;
		StringBuffer number = new StringBuffer().append(count).append("/юл");
		StringBuffer nulls = new StringBuffer();
		for(int i = number.length(); i<minLength; i++ ) {
			nulls.append("0");
		}
		
		return nulls.append(number).toString();
	}
	
}
