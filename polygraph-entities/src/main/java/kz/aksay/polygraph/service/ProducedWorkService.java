package kz.aksay.polygraph.service;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.dao.GenericDao;

@Service
public class ProducedWorkService extends AbstractGenericService<ProducedWork, Long>
	implements IProducedWorkService {
	
	private GenericDao<ProducedWork, Long> producedWorkDao;
	private IMaterialConsumptionService materialConsumptionService;

	@Transactional(readOnly=true)
	public List<ProducedWork> findAllByOrderId(Long orderId) {
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
		criteria.add(Restrictions.eq("order.id", orderId));
		List<ProducedWork> producedWorks = getDao().readByCriteria(criteria);
		for(ProducedWork producedWork : producedWorks) {
			Set<MaterialConsumption> materialConsumptions 
				= materialConsumptionService.findAllByProducedWorkId(
						producedWork.getId());
			producedWork.setMaterialConsumption(materialConsumptions);
		}
		return producedWorks;
	}
	
	@Override
	@Transactional
	public ProducedWork save(ProducedWork producedWork) throws Exception {
		producedWork = super.save(producedWork);
		
		if(producedWork.getMaterialConsumption() != null) {
			for(MaterialConsumption materialConsumption : producedWork.getMaterialConsumption() ){
				if(materialConsumption.isDirty()) {
					materialConsumption = materialConsumptionService.save(materialConsumption);
					materialConsumption.setDirty(false);
				}
			}
		}
		
		return producedWork;
	}
	
	@Transactional
	public int deleteAllByOrder(Order order) {
		Query query = getDao().getSession().createQuery(
				"DELETE FROM ProducedWork pw WHERE pw.order = :order");
		query.setParameter("order", order);
		return query.executeUpdate();
	}
	
	@Override
	protected GenericDao<ProducedWork, Long> getDao() {
		return producedWorkDao;
	}

	@Autowired
	public void setProducedWorkDao(GenericDao<ProducedWork, Long> producedWorkDao) {
		this.producedWorkDao = producedWorkDao;
	}

	@Autowired
	public void setMaterialConsumptionService(IMaterialConsumptionService materialConsumptionService) {
		this.materialConsumptionService = materialConsumptionService;
	}
}
