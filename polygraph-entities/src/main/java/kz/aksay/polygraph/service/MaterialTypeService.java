package kz.aksay.polygraph.service;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialType;

@Service
public class MaterialTypeService extends GenericService<MaterialType, Long> {

	private GenericDao<MaterialType, Long> materialTypeDao;

	@Override
	protected GenericDao<MaterialType, Long> getDao() {
		return materialTypeDao;
	}

	@Autowired
	public void setMaterialTypeDao(
			GenericDao<MaterialType, Long> materialTypeDao) {
		this.materialTypeDao = materialTypeDao;
	}

	public MaterialType findByName(String materialTypeName) {
		MaterialType materialType = null;
		
		if(materialTypeName != null && !materialTypeName.isEmpty()) {
			Criteria findbyName = getDao().getSession().createCriteria(getDao().clazz());
			findbyName.add(Restrictions.ilike("name", materialTypeName));
			materialType = getDao().readUniqueByCriteria(findbyName);
		}
		
		return materialType;
	}

}
