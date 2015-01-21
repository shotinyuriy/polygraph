package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.Set;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.ProducedWork;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
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
	
	@Transactional
	public int deleteAllByProducedWork(ProducedWork producedWork) {
		int rowsAffected = 0;
		Query query = getDao().getSession().createQuery(
				"DELETE FROM MaterialConsumption mc WHERE mc.producedWork = :producedWork");
		query.setParameter("producedWork", producedWork);
		rowsAffected = query.executeUpdate();
		return rowsAffected;
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
