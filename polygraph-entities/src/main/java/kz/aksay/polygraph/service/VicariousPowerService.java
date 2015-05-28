package kz.aksay.polygraph.service;


import java.util.List;

import kz.aksay.polygraph.api.IVicariousPowerService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.VicariousPower;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VicariousPowerService extends AbstractGenericService<VicariousPower, Long>
		implements IVicariousPowerService {

	private GenericDao<VicariousPower, Long> vicariousPowerDao;

	@Override
	protected GenericDao<VicariousPower, Long> getDao() {
		return vicariousPowerDao;
	}
	
	@Autowired
	public void setVicariousPowerDao(GenericDao<VicariousPower, Long> vicariousPowerDao) {
		this.vicariousPowerDao = vicariousPowerDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VicariousPower> findByOrganization(Organization organization) {
		return getDao().getSession().createCriteria(getDao().clazz())
				.add(Restrictions.eq("organization", organization))
				.list();
	}
}
