package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.PaperType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperTypeService extends AbstractGenericService<PaperType, Long> 
	implements IPaperTypeService {

	private GenericDao<PaperType, Long> materialTypeDao;

	@Override
	protected GenericDao<PaperType, Long> getDao() {
		return materialTypeDao;
	}

	@Autowired
	public void setMaterialTypeDao(
			GenericDao<PaperType, Long> materialTypeDao) {
		this.materialTypeDao = materialTypeDao;
	}

	public PaperType findByName(String materialTypeName) {
		PaperType paperType = null;
		
		if(materialTypeName != null && !materialTypeName.isEmpty()) {
			Criteria findbyName = getDao().getSession().createCriteria(getDao().clazz());
			findbyName.add(Restrictions.like("name", materialTypeName));
			paperType = getDao().readUniqueByCriteria(findbyName);
		}
		
		return paperType;
	}
}
