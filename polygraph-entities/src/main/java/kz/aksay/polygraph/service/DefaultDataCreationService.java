package kz.aksay.polygraph.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;
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
	
	private IPaperTypeService paperTypeService;
	
	private IWorkTypeService workTypeService;
	
	private IPaperService paperService;
	
	private IEquipmentService equipmentService;
	
	private IOrganizationService organizationService;
	
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
						 PaperType.DefaultNames.all()) {
						 if(paperTypeService.
								 findByName(materialTypeName) == null) {
							 paperTypeService.save(
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
					 
					 for(PaperType paperType : paperTypeService.findAll()) {
						 Paper example = new Paper();
						 example.setType(paperType);
						 if(paperService.findByExampleAndPaperType(example, paperType).size() == 0) {
							 paperService.save(createPaper(paperType));
						 }
					 }
					 
					 createDefaultEquipmentTypes();
					 createOrganizations();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private PaperType createMaterialType(String name) {
		PaperType paperType = new PaperType();
		paperType.setCreatedAt(new Date());
		paperType.setCreatedBy(User.TECH_USER);
		paperType.setName(name);
		return paperType;
	}
	
	private WorkType createWorkType(String name) {
		WorkType workType = new WorkType();
		workType.setCreatedAt(new Date());
		workType.setCreatedBy(User.TECH_USER);
		workType.setName(name);
		return workType;
	}
	
	private Paper createPaper(PaperType paperType) {
		Paper material = new Paper();
		material.setCreatedAt(new Date());
		material.setCreatedBy(User.TECH_USER);
		material.setFormat(Format.A4);
		material.setDensity(250);
		material.setType(paperType);
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
	
	private void createOrganizations() throws Exception {
		Organization organization = new Organization();
		
		organization.setFullname("Товарищество с ограниченной ответственностью Фирма \"Сервер+\"");
		organization.setShortname("ТОО Фирма \"Сервер+\"");
		organization.setInn("960740001061");
		organization.setKpp("586856000000");
		organization.setDirectorName("Наумов А.В.");
		
		List<Organization> foundOrganizations = organizationService.findByExample(organization);
		
		if(foundOrganizations.isEmpty()) {
			organization.setCreatedAt(new Date());
			organization.setCreatedBy(User.TECH_USER);
			
			organizationService.save(organization);
		} else {
			organization = foundOrganizations.get(0);
		}
		
		Organization.FIRMA_SERVER_PLUS = organization;
	}
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setMaterialtypeService(IPaperTypeService paperTypeService) {
		this.paperTypeService = paperTypeService;
	}

	@Autowired
	public void setWorkTypeService(IWorkTypeService workTypeService) {
		this.workTypeService = workTypeService;
	}

	@Autowired
	public void setPaperService(IPaperService paperService) {
		this.paperService = paperService;
	}

	@Autowired
	public void setEquipmentTypeService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Autowired
	public void setEquipmentService(IEquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	@Autowired
	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
