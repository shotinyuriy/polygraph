package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialConsumption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialConsumptionService extends
		GenericService<MaterialConsumption, Long> {

	private GenericDao<MaterialConsumption, Long> materialConsumptionDao;

	@Override
	protected GenericDao<MaterialConsumption, Long> getDao() {
		return materialConsumptionDao;
	}

	@Autowired
	public void setMaterialConsumptionDao(
			GenericDao<MaterialConsumption, Long> materialConsumptionDao) {
		this.materialConsumptionDao = materialConsumptionDao;
	}

}
