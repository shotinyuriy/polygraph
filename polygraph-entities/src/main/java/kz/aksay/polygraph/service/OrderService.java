package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService extends GenericService<Order, Long> {

	private GenericDao<Order, Long> orderDao;
	
	private ProducedWorkService producedWorkService;
	
	private MaterialConsumptionService materialConsumptionService;
	
	@Override
	@Transactional
	public Order find(Long id) {
		Order order = getDao().read(id);
		if(order != null) {
			List<ProducedWork> producedWorks = producedWorkService.findAllByOrderId(id);
			order.setProducedWorks(new HashSet<ProducedWork>());
			order.getProducedWorks().addAll(producedWorks);
			
			Set<MaterialConsumption> materialConsumptions 
				= materialConsumptionService.findAllByOrderId(order.getId());
			order.setMaterialConsumption(materialConsumptions);
		}
		return order;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Order save(Order order) throws Exception {
		order = super.save(order);
		if(order.getProducedWorks() != null) {
			for(ProducedWork producedWork : order.getProducedWorks()) {
				producedWork.setOrder(order);
				producedWork = producedWorkService.save(producedWork);
			}
			for(ProducedWork producedWork : order.getProducedWorks()) {
				producedWork.setDirty(false);
			}
			
			if(order.getMaterialConsumption() != null) {
				for(MaterialConsumption materialConsumption : order.getMaterialConsumption() ){
					if(materialConsumption.isDirty()) {
						materialConsumption = materialConsumptionService.save(materialConsumption);
						materialConsumption.setDirty(false);
					}
				}
			}
		}
		return order;
	}
	
	@Override
	protected GenericDao<Order, Long> getDao() {
		return orderDao;
	}
	
	@Autowired
	public void setOrderDao(GenericDao<Order, Long> orderDao) {
		this.orderDao = orderDao;
	}

	@Autowired
	public void setProducedWorkService(ProducedWorkService producedWorkService) {
		this.producedWorkService = producedWorkService;
	}
	
	@Autowired
	public void setMaterialConsumptionService(MaterialConsumptionService materialConsumptionService) {
		this.materialConsumptionService = materialConsumptionService;
	}
}
