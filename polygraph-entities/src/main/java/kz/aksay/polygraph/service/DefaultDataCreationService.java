package kz.aksay.polygraph.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IMaterialTypeService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Material;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.WorkType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DefaultDataCreationService {
	
	private IUserService userService;
	
	private IMaterialTypeService materialTypeService;
	
	private IWorkTypeService workTypeService;
	
	private IMaterialService materialService;
	
	private IEquipmentService equipmentService;
	
	@Autowired
	private PlatformTransactionManager  txManager;
	
	@PostConstruct
	public void createTechUser() {
		TransactionTemplate tmpl = new TransactionTemplate(txManager);
		tmpl.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					
					User techUser = userService.findByLogin(User.TECH_USER.getLogin());
					if(techUser == null) {
						techUser = userService.save(User.TECH_USER);
					}
					
					User.TECH_USER = techUser;
					 
					 for(String materialTypeName : 
						 MaterialType.DefaultNames.all()) {
						 if(materialTypeService.
								 findByName(materialTypeName) == null) {
							 materialTypeService.save(
									 createMaterialType(materialTypeName));
						 }
					 }
					 
					 for(String workTypeName : 
						 WorkType.DefaultNames.all()) {
						 if(workTypeService.
								 findByName(workTypeName) == null) {
							 workTypeService.save(
									 createWorkType(workTypeName));
						 }
					 }
					 
					 for(MaterialType materialType : materialTypeService.findAll()) {
						 String names[] = Material.DefaultNames.materialNames.get(materialType.getName());
						 if(names != null) {
							 for(String name: names) {
								 if(materialService.findByMaterialTypeAndName(materialType, name) == null) {
									 materialService.save(createMaterial(materialType, name));
								 }
							 }
						 }
					 }
					 
					 createDefaultEquipmentTypes();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private MaterialType createMaterialType(String name) {
		MaterialType materialType = new MaterialType();
		materialType.setCreatedAt(new Date());
		materialType.setCreatedBy(User.TECH_USER);
		materialType.setName(name);
		return materialType;
	}
	
	private WorkType createWorkType(String name) {
		WorkType workType = new WorkType();
		workType.setCreatedAt(new Date());
		workType.setCreatedBy(User.TECH_USER);
		workType.setName(name);
		return workType;
	}
	
	private Material createMaterial(MaterialType materialType, String name) {
		Material material = new Material();
		material.setCreatedAt(new Date());
		material.setCreatedBy(User.TECH_USER);
		material.setName(name);
		material.setMaterialType(materialType);
		return material;
	}
	
	private void createDefaultEquipmentTypes() throws Exception {
		
		WorkType printing = 
				workTypeService.findByName(WorkType.DefaultNames.PRINTING);
		 
		if(printing != null) {
			
			String[] printerNames = new String[] {"DC-242", "WC-24", "DC-12", "WC-55"};
			
			for(String printerName : printerNames) {
				
				Equipment equipment = equipmentService.findByName(printerName);
				
				if(equipment == null) {
					equipment = new Equipment();
					equipment.setCreatedAt(new Date());
					equipment.setCreatedBy(User.TECH_USER);
					equipment.setWorkType(printing);
					equipment.setName(printerName);
					equipmentService.save(equipment);
				}
			}
		}
	}
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setMaterialtypeService(IMaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	@Autowired
	public void setWorkTypeService(IWorkTypeService workTypeService) {
		this.workTypeService = workTypeService;
	}

	@Autowired
	public void setMaterialService(IMaterialService materialService) {
		this.materialService = materialService;
	}

	@Autowired
	public void setEquipmentTypeService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Autowired
	public void setEquipmentService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}
}
