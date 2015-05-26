package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService extends AbstractGenericService<Material, Long> 
 implements IMaterialService {

	private GenericDao<Material, Long> materialDao;
	
	
	@Override
	protected GenericDao<Material, Long> getDao() {
		return materialDao;
	}

	@Autowired
	public void setMaterialDao(GenericDao<Material, Long> materialDao) {
		this.materialDao = materialDao;
	}
	

}
