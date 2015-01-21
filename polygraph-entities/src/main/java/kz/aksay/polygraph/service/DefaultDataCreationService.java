package kz.aksay.polygraph.service;

import java.util.Date;

import javax.annotation.PostConstruct;

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
	
	private UserService userService;
	
	private MaterialTypeService materialTypeService;
	
	private WorkTypeService workTypeService;
	
	private MaterialService materialService;
	
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
						 for(String name: names) {
							 if(materialService.findByMaterialTypeAndName(materialType, name) == null) {
								 materialService.save(createMaterial(materialType, name));
							 }
						 }
					 }
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
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setMaterialtypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	@Autowired
	public void setWorkTypeService(WorkTypeService workTypeService) {
		this.workTypeService = workTypeService;
	}

	@Autowired
	public void setMaterialService(MaterialService materialService) {
		this.materialService = materialService;
	}
}
