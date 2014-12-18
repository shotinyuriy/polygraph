package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.Set;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialConsumption;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialConsumptionService extends
		GenericService<MaterialConsumption, Long> {

	private GenericDao<MaterialConsumption, Long> materialConsumptionDao;

	@Transactional(readOnly=true)
	public Set<MaterialConsumption> findAllByProducedWorkId(Long producedWorkId) {
		Criteria criteria = getDao().getSession().createCriteria(MaterialConsumption.class);
		criteria.add(Restrictions.eqOrIsNull("producedWork.id", producedWorkId));
		Set<MaterialConsumption> materialConsumptions = new HashSet<>();
		materialConsumptions.addAll(getDao().readByCriteria(criteria));
		return materialConsumptions;
	}
	
	@Transactional(readOnly=true)
	public Set<MaterialConsumption> findAllByOrderId(Long orderId) {
		Criteria criteria = getDao().getSession().createCriteria(MaterialConsumption.class);
		criteria.add(Restrictions.eqOrIsNull("order.id", orderId));
		Set<MaterialConsumption> materialConsumptions = new HashSet<>();
		materialConsumptions.addAll(getDao().readByCriteria(criteria));
		return materialConsumptions;
	}
	
	@Override
	protected GenericDao<MaterialConsumption, Long> getDao() {
		return materialConsumptionDao;
	}

	@Autowired
	public void setMaterialConsumptionDao(
			GenericDao<MaterialConsumption, Long> materialConsumptionDao) {
		this.materialConsumptionDao = materialConsumptionDao;
	}

}
