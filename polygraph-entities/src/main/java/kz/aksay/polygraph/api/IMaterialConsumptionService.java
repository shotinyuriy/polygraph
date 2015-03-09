package kz.aksay.polygraph.api;

import java.util.List;
import java.util.Set;

import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.ProducedWork;

public interface IMaterialConsumptionService extends
IGenericService<MaterialConsumption, Long> {

	public MaterialConsumption save(MaterialConsumption materialConsumption) 
			throws Exception;

	public Set<MaterialConsumption> findAllByProducedWorkId(Long producedWorkId);
	
	public Set<MaterialConsumption> findAllByOrderId(Long orderId);
	
	public List<MaterialConsumption> findByExample(
			MaterialConsumption materialConsumptionExample);
	
	public int deleteAllByProducedWork(ProducedWork producedWork);

}
