package kz.aksay.polygraph.service;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialType;

@Service
public class MaterialService extends AbstractGenericService<Material, Long> 
 implements IMaterialService {

	private GenericDao<Material, Long> materialDao;

	@Transactional(readOnly=true)
	public Material findByMaterialTypeAndName(MaterialType materialType, String name) {
		Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
		criteria.add(Restrictions.eq("materialType", materialType));
		criteria.add(Restrictions.eq("name", name));
		return getDao().readUniqueByCriteria(criteria);
	}
	

	@Transactional
	public int deleteAllByMaterialType(MaterialType materialType) {
		Query query = getDao().getSession().createQuery(
				"DELETE FROM Material m WHERE m.materialType = :materialType");
		query.setParameter("materialType", materialType);
		return query.executeUpdate();
	}
	
	@Override
	protected GenericDao<Material, Long> getDao() {
		return materialDao;
	}

	@Autowired
	public void setMaterialDao(GenericDao<Material, Long> materialDao) {
		this.materialDao = materialDao;
	}
	

}
