package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialConsumptionService extends
		AbstractGenericService<MaterialConsumption, Long> implements IMaterialConsumptionService {

	private GenericDao<MaterialConsumption, Long> materialConsumptionDao;
	
	@Override
	@Transactional
	public MaterialConsumption save(MaterialConsumption materialConsumption) 
			throws Exception {
		super.save(materialConsumption);
		materialConsumption.setDirty(false);
		return materialConsumption;
	}

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
	
	@Transactional(readOnly=true)
	public List<MaterialConsumption> findByExample(
			MaterialConsumption materialConsumptionExample) { 
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
		
		if(materialConsumptionExample.getMaterial() != null) {
			criteria.add(Restrictions.eq("material", materialConsumptionExample.getMaterial()));
		}
		
		if(materialConsumptionExample.getId() != null) {
			criteria.add(Restrictions.eq("id", materialConsumptionExample.getId()));
		}
		
		Order order = materialConsumptionExample.getOrder();
		if(order != null) {
			Criteria orderCriteria = criteria.createCriteria("order", JoinType.INNER_JOIN);
			if(order.getId() != null) {
				orderCriteria.add(Restrictions.eq("id",order.getId()));
			}
			
			if(order.getCreatedAt() != null) {
				orderCriteria.add(Restrictions.ge("createdAt", order.getCreatedAt()));
			}
			
			if(order.getUpdatedAt() != null) {
				orderCriteria.add(Restrictions.le("createdAt", order.getUpdatedAt()));
			}
			
			if(order.getState() != null) {
				orderCriteria.add(Restrictions.eq("state", order.getState()));
			}
			
			criteria = orderCriteria;
		}
		
		return criteria.list();
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
