package kz.aksay.polygraph.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IOrderFullTextIndexService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.WorkType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metamodel.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
public class OrderService extends AbstractGenericService<Order, Long> 
	implements IOrderService {

	private GenericDao<Order, Long> orderDao;
	
	private IProducedWorkService producedWorkService;
	
	private IMaterialConsumptionService materialConsumptionService;
	
	private IOrderFullTextIndexService orderFullTextIndexService;
	
	private IEquipmentService equipmentService;
	
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
			
			for(ProducedWork producedWork : producedWorks) {
				Iterator<MaterialConsumption> iterMC = materialConsumptions.iterator();
				while(iterMC.hasNext()) {
					MaterialConsumption mc = iterMC.next();
					if(mc.getProducedWork() != null) {
						if(producedWork.equals(mc.getProducedWork())) {
							producedWork.getMaterialConsumption().add(mc);
							iterMC.remove();
						}
					}
				}
			}
		}
		return order;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Order save(Order order) throws Exception {
		Order.State oldState = null;
		
		if(order.getId() != null) {
			 Order oldOrder = find(order.getId());
			 oldState = oldOrder.getState();
			 getDao().getSession().clear();
		}
		
		if(order.getCustomer() != null) {
			Subject customer = order.getCustomer();
			if(customer instanceof Organization) {
				Long organizationId = ((Organization) customer).getId();
				if(order.getVicariousPower() == null) {
					throw new Exception("Укажите доверенность!");
				}
				if( !organizationId.equals( order.getVicariousPower().getOrganization().getId()) ) {
					throw new Exception("Организация в доверенности и в заказе не совпадают!");
				}
			}
		}
		
		Set<Equipment> equipmentsToUpdate = new HashSet<Equipment>();
		
		order = super.save(order);
		if(order.getProducedWorks() != null) {
			for(ProducedWork producedWork : order.getProducedWorks()) {
				if(producedWork.isDirty()) {
					producedWork.setOrder(order);
					producedWork = producedWorkService.save(producedWork);
				}
				
				Equipment equipment = producedWork.getEquipment();
				boolean newEquipment = true;
				
				if(equipment != null &&
						order.getState().equals(Order.State.FINISHED) && 
						( oldState == null || !oldState.equals(Order.State.FINISHED)) ) {
				
					for(Equipment currentEquipment : equipmentsToUpdate) {
						if(currentEquipment.equals(equipment)) { 
							equipment = currentEquipment;
							newEquipment = false;
							break;
						}
					}
					
					if(producedWork.getQuantity() != null) {
						
						if(WorkType.PRINTING_COLORED.equals( producedWork.getWorkType() ) ) {
							equipment.setColoredUsageCount(equipment.getColoredUsageCount()+producedWork.getQuantity());
							if(newEquipment) equipmentsToUpdate.add(equipment);
						} else if(WorkType.PRINTING_BLACK_AND_WHITE.equals( producedWork.getWorkType() )) {
							equipment.setMonochromeUsageCount(equipment.getMonochromeUsageCount()+producedWork.getQuantity());
							if(newEquipment) equipmentsToUpdate.add(equipment);
						}
					}
				}
				
//				if(producedWork.getMaterialConsumption() != null) {
//					for(MaterialConsumption materialConsumption : producedWork.getMaterialConsumption() ){
//						if(materialConsumption.isDirty()) {
//							materialConsumption.setOrder(order);
//							materialConsumption.setProducedWork(producedWork);
//							materialConsumption = materialConsumptionService.save(materialConsumption);
//						}
//					}
//					for(MaterialConsumption materialConsumption : producedWork.getMaterialConsumption()) {
//						materialConsumption.setDirty(false);
//					}
//				}
			}
			for(ProducedWork producedWork : order.getProducedWorks()) {
				producedWork.setDirty(false);
			}
		}
		
		for(Equipment equipment : equipmentsToUpdate) {
			equipmentService.save(equipment);
		}
		
		orderFullTextIndexService.recreateOrderFullTextIndexes(order);
		
		return order;
	}
	
	public List<Order> findByExample(Order example) {
		Criteria criteria = getDao().getSession().createCriteria(Order.class);
		
		if( example.getState() != null )
			criteria.add(Restrictions.eq("state", example.getState()));
		
		if( example.getCurrentExecutor() != null )
			criteria.add(Restrictions.eq("currentExecutor.id", example.getCurrentExecutor().getId() ));
		
		return getDao().readByCriteria(criteria);
	}
	
	public List<Order> findByExampleAndSearchString(Order example, String searchString) {
		List<Order> orders = null;
		if(searchString != null) {
		
			searchString = "%"+searchString.toLowerCase()+"%";
			
			StringBuffer selectClause = new StringBuffer();
			selectClause.append("SELECT o FROM OrderFullTextIndex ofti ");
			selectClause.append("INNER JOIN ofti.order o ");
			selectClause.append("INNER JOIN ofti.fullTextIndex fti ");
			selectClause.append("WITH fti.text LIKE :searchString");
			
			StringBuffer whereClause = new StringBuffer();
			if(example != null) {
				if(example.getState() != null) {
					whereClause.append("o.state = :state ");
				}
				if(example.getCurrentExecutor() != null) {
					if(whereClause.length() > 0) {
						whereClause.append(" AND ");
					}
					whereClause.append("o.currentExecutor.id = :currentExecutorId ");
				}
			}
			
			if(whereClause.length() >0) {
				selectClause.append(" WHERE ").append(whereClause);
			} 
			
			System.out.println(selectClause);
			
			Query query = getDao().getSession().createQuery(selectClause.toString());
			
			query.setParameter("searchString", searchString);
			if(example.getState() != null) {
				query.setParameter("state", example.getState());
			}
			if(example.getCurrentExecutor() != null) {
				query.setParameter("currentExecutorId", example.getCurrentExecutor().getId());
			}
			
			orders = (List<Order>)query.list();	
		}
		return orders != null ? orders : new ArrayList<Order>();
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
	public void setProducedWorkService(IProducedWorkService producedWorkService) {
		this.producedWorkService = producedWorkService;
	}
	
	@Autowired
	public void setMaterialConsumptionService(IMaterialConsumptionService materialConsumptionService) {
		this.materialConsumptionService = materialConsumptionService;
	}

	@Autowired
	public void setOrderFullTextIndexService(
			IOrderFullTextIndexService orderFullTextIndexService) {
		this.orderFullTextIndexService = orderFullTextIndexService;
	}

	@Autowired
	public void setEquipmentService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
