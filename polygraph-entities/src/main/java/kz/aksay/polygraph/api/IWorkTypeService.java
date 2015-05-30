package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.WorkType;

public interface IWorkTypeService extends IGenericService<WorkType, Long> {
	public WorkType findByName(String workTypeName);
	
	public WorkType findByCode(String workTypeCode);
}