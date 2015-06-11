package kz.aksay.polygraph.service;

import java.util.Map;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.ProducedWork;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentService extends AbstractGenericService<Equipment, Long> implements IEquipmentService {

	private GenericDao<Equipment, Long> equipmentDao;
	
	private IProducedWorkService producedWorkService;
	
	@Override
	protected GenericDao<Equipment, Long> getDao() {
		 
		return equipmentDao;
	}

	@Autowired
	public void setEquipmentDao(GenericDao<Equipment, Long> equipmentDao) {
		
		this.equipmentDao = equipmentDao;
	}


	@Override
	@Transactional(readOnly=true)
	public Equipment findByName(String name) {
		
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
		criteria.add(Restrictions.eq("name", name));
		
		return getDao().readUniqueByCriteria(criteria);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer countOfUsagesByExample(ProducedWork producedWork) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT pw FROM ProducedWork pw ")
		.append(" WHERE pw.equipment = :equipment ")
		.append(" AND pw.workType = :workType")
		.append(" AND pw.format = :format");
		
		Equipment equipment = producedWork.getEquipment();
		if(equipment.getId() == null) {
			Equipment tmp = findByName(equipment.getName());
			if(tmp != null) {
				equipment.setId(tmp.getId());
			}
		}
		
		if(equipment.getId() != null) {
			Query query = getDao().getSession().createQuery(sb.toString())
					.setParameter("equipment", producedWork.getEquipment())
					.setParameter("workType", producedWork.getWorkType())
					.setParameter("format", producedWork.getFormat());
			
			Integer size = query.list().size();
			return size;
		}
		return Integer.valueOf(0);
		 
	}

}
