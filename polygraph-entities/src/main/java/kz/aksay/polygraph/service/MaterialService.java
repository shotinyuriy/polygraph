package kz.aksay.polygraph.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.aksay.polygraph.api.IBindingSpringService;
import kz.aksay.polygraph.api.IGenericService;
import kz.aksay.polygraph.api.ILaminateService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IStickerService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.WorkType;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService extends AbstractGenericService<Material, Long> 
 implements IMaterialService, InitializingBean {

	private IBindingSpringService bindingSpringService;
	private IPaperService paperService;
	private ILaminateService laminateService;
	private IStickerService stickerService;
	
	public static final Map<WorkType, Set<IGenericService<?,?>>> WORK_TYPE_MAPPING 
		= new HashMap<WorkType, Set<IGenericService<?,?>>>();
	
	private GenericDao<Material, Long> materialDao;
	
	
	@Override
	protected GenericDao<Material, Long> getDao() {
		return materialDao;
	}

	@Autowired
	public void setMaterialDao(GenericDao<Material, Long> materialDao) {
		this.materialDao = materialDao;
	}

	@Override
	public List<Material> findMaterialsByWorkType(WorkType workType) {
		List<Material> materials = new ArrayList<Material>();
		
		Set<IGenericService<?,?>> services = WORK_TYPE_MAPPING.get(workType);
		if(services != null) {
			for(IGenericService<?,?> genericService : services) {
				for(Object object : genericService.findAll()) {
					Material material = (Material) object;
					materials.add(material);
				}
			}
		}
		
		return materials;
	}

	@Autowired
	public void setBindingSpringService(IBindingSpringService bindingSpringService) {
		this.bindingSpringService = bindingSpringService;
	}

	@Autowired
	public void setPaperService(IPaperService paperService) {
		this.paperService = paperService;
	}

	@Autowired
	public void setLaminateService(ILaminateService laminateService) {
		this.laminateService = laminateService;
	}

	@Autowired
	public void setStickerService(IStickerService stickerService) {
		this.stickerService = stickerService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Set<IGenericService<?,?>> bindingSet 
			= new HashSet<>();
		Set<IGenericService<?,?>> printingSet 
			= new HashSet<>();
		Set<IGenericService<?,?>> laminationSet 
			= new HashSet<>();
		
		bindingSet.add(bindingSpringService);
		printingSet.add(paperService);
		printingSet.add(stickerService);
		laminationSet.add(laminateService);
		
		WORK_TYPE_MAPPING.put(WorkType.BINDING, bindingSet);
		WORK_TYPE_MAPPING.put(WorkType.PRINTING_BLACK_AND_WHITE, printingSet);
		WORK_TYPE_MAPPING.put(WorkType.PRINTING_COLORED, printingSet);
		WORK_TYPE_MAPPING.put(WorkType.LAMINATION, laminationSet);
	}
}
