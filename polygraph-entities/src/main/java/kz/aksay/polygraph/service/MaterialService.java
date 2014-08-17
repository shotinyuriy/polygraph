package kz.aksay.polygraph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Material;

@Service
public class MaterialService extends GenericService<Material, Long> {

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
