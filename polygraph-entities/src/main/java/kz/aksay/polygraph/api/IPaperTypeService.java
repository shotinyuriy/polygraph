package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.PaperType;

public interface IPaperTypeService extends IGenericService<PaperType, Long> {
	
	public PaperType findByName(String paperTypeName);

}