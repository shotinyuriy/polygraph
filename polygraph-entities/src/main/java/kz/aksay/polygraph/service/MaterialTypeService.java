package kz.aksay.polygraph.service;

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

}
