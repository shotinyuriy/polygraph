package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Equipment;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentService extends AbstractGenericService<Equipment, Long> implements IEquipmentService {

	private GenericDao<Equipment, Long> equipmentDao;
	
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

}
