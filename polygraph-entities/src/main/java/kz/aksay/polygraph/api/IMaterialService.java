package kz.aksay.polygraph.api;

import java.util.List;

import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.WorkType;

import org.springframework.stereotype.Service;

public interface IMaterialService extends IGenericService<Material, Long> {

	List<Material> findMaterialsByWorkType(WorkType workType);
}
