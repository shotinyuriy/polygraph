package kz.aksay.polygraph.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import kz.aksay.polygraph.api.IBindingSpringService;
import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.ILaminateService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IPaperTypeService;
import kz.aksay.polygraph.api.IStickerService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.entity.Address;
import kz.aksay.polygraph.entity.BindingSpring;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Laminate;
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
	private IBindingSpringService bindingSpringService;
	private IStickerService stickerService;
	private ILaminateService laminateService;
	
	@Autowired
	private PlatformTransactionManager  txManager;
	
	@PostConstruct
	public void createTechUser() {
		TransactionTemplate tmpl = new TransactionTemplate(txManager);
		tmpl.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(
					TransactionStatus status) {
				try {
					
					User techUser = userService.findByLogin(
							User.TECH_USER.getLogin());
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
					 
					 for(final WorkType workType : WorkType.DEFAULTS) {
						 WorkType foundWorkType = workTypeService.findByCode(
								 workType.getCode());
						 if(foundWorkType == null) {
							 workType.setCreatedAt(new Date());
							 workType.setCreatedBy(User.TECH_USER);
							 foundWorkType = workTypeService.save(workType);
						 } else {
							 workType.setCreatedAt( foundWorkType.getCreatedAt() );
							 workType.setCreatedBy( foundWorkType.getCreatedBy() );
						 }
						 workType.setId( foundWorkType.getId() );
						 
					 }
					 
					 createDefaultPaperTypes();
					 createDefaultBindingSprings();
					 createDefaultLaminates();
					 createDefaultEquipmentTypes();
					 createOrganizations();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

			
		});
	}
	
	private void createDefaultBindingSprings() throws Exception { 
		createBindingSpring(10, BindingSpring.Type.METAL, "Обычная 1");
		createBindingSpring(10, BindingSpring.Type.PLASTIC, "Обычная 2");
		createBindingSpring(15, BindingSpring.Type.PLASTIC, "Большая");
	}
	
	private void createDefaultLaminates() throws Exception { 
		createLaminate(120, Format.A4, "Обычная пленка");
		createLaminate(200, Format.A4, "Толстая пленка");
		createLaminate(100, Format.A3, "Большая тонкая");
	}

	private void createDefaultPaperTypes() throws Exception {
		for(PaperType paperType : paperTypeService.findAll()) {
			 Paper example = new Paper();
			 example.setType(paperType);
			 example.setDensity(250);
			 example.setFormat(Format.A4);
			 
			 if(paperService.findByExampleAndPaperType(example, paperType).size() == 0) {
				 paperService.save(createPaper(example));
			 }
			 
			 example = new Paper();
			 example.setType(paperType);
			 example.setDensity(250);
			 example.setFormat(Format.A3);
			 
			 if(paperService.findByExampleAndPaperType(example, paperType).size() == 0) {
				 paperService.save(createPaper(example));
			 }
		 }
	}
	
	private PaperType createMaterialType(String name) {
		PaperType paperType = new PaperType();
		paperType.setCreatedAt(new Date());
		paperType.setCreatedBy(User.TECH_USER);
		paperType.setName(name);
		return paperType;
	}
	
	private Paper createPaper(Paper material) {
		material.setCreatedAt(new Date());
		material.setCreatedBy(User.TECH_USER);
		return material;
	}
	
	private BindingSpring createBindingSpring(Integer diameter, BindingSpring.Type type, String description) throws Exception {
		BindingSpring bindingSpring = new BindingSpring();
		bindingSpring.setDiameter(diameter);
		bindingSpring.setType(type);
		
		List<BindingSpring> foundBindingSprings = 
				bindingSpringService.findByExample(bindingSpring);
		
		if(foundBindingSprings.size() == 0) {
			
			bindingSpring.setCreatedAt(new Date());
			bindingSpring.setCreatedBy(User.TECH_USER);
			bindingSpring.setDescription(description);
			bindingSpringService.save(bindingSpring);
		}
		
		return bindingSpring;
	}
	
	
	private Laminate createLaminate(Integer density, Format format, String description) throws Exception {
		Laminate laminate = new Laminate();
		laminate.setDensity(density);
		laminate.setFormat(format);
		
		List<Laminate> foundBindingSprings = 
				laminateService.findByExample(laminate);
		
		if(foundBindingSprings.size() == 0) {
			
			laminate.setCreatedAt(new Date());
			laminate.setCreatedBy(User.TECH_USER);
			laminate.setDescription(description);
			laminateService.save(laminate);
		}
		
		return laminate;
	}
	
	private void createDefaultEquipmentTypes() throws Exception {
		
		WorkType printing = WorkType.PRINTING_BLACK_AND_WHITE;
		 
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
			
			Address address = new Address();
			address.setCity("г. Уральск");
			address.setStreet("ул.Курмангазы");
			address.setHouse("162");
			organization.setAddress(address);
			
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

	@Autowired
	public void setBindingSpringService(IBindingSpringService bindingSpringService) {
		this.bindingSpringService = bindingSpringService;
	}

	@Autowired
	public void setStickerService(IStickerService stickerService) {
		this.stickerService = stickerService;
	}

	@Autowired
	public void setLaminateService(ILaminateService laminateService) {
		this.laminateService = laminateService;
	}
}
