package kz.aksay.polygraph.service;

import static kz.aksay.polygraph.entity.DefaultData.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import kz.aksay.polygraph.api.IBindingSpringService;
import kz.aksay.polygraph.api.IComplexityService;
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
import kz.aksay.polygraph.entity.Complexity;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Laminate;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;
import kz.aksay.polygraph.entity.Sticker;
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
	private IComplexityService complexityService;

	@Autowired
	private PlatformTransactionManager  txManager;
	
	@PostConstruct
	public void createDefaultData() {
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
					 
					 createDefaultComplexity();
					 createDefaultPaperTypes();
					 createDefaultBindingSprings();
					 createDefaultLaminates();
					 createStickers();
					 createDefaultEquipmentTypes();
					 createOrganizations();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

			

			
		});
	}
	
	private void createDefaultComplexity() throws Exception {
		for(Complexity complexity : COMPLEXITY_DEFAULTS) {
			Complexity example = new Complexity();
			example.setName(complexity.getName());
			List<Complexity> foundExamples = complexityService.findByExample(example);
			if(!foundExamples.isEmpty()) {
				example = foundExamples.iterator().next();
				complexity.setId(example.getId());
				complexity.setCreatedAt(example.getCreatedAt());
				complexity.setCreatedBy(example.getCreatedBy());
			} else {
				complexity.setCreatedAt(new Date());
				complexity.setCreatedBy(User.TECH_USER);
				complexityService.save(complexity);
			}
		}
		
	}
	
	private void createDefaultBindingSprings() throws Exception { 
		createBindingSpring(10, BindingSpring.Type.METAL, "Обычная 1", 10.0);
		createBindingSpring(10, BindingSpring.Type.PLASTIC, "Обычная 2", 11.0);
		createBindingSpring(15, BindingSpring.Type.PLASTIC, "Большая", 15.0);
	}
	
	private void createDefaultLaminates() throws Exception { 
		createLaminate(120, Format.A4, "Обычная пленка", 5.0);
		createLaminate(200, Format.A4, "Толстая пленка", 4.0);
		createLaminate(100, Format.A3, "Большая тонкая", 3.0);
	}

	private void createDefaultPaperTypes() throws Exception {
		for(PaperType paperType : paperTypeService.findAll()) {
			 Paper example = new Paper();
			 example.setType(paperType);
			 example.setDensity(250);
			 example.setFormat(Format.A4);
			 
			 if(paperService.findByExampleAndPaperType(example, paperType).size() == 0) {
				 paperService.save(createPaper(example, 20.0));
			 }
			 
			 example = new Paper();
			 example.setType(paperType);
			 example.setDensity(250);
			 example.setFormat(Format.A3);
			 
			 if(paperService.findByExampleAndPaperType(example, paperType).size() == 0) {
				 paperService.save(createPaper(example, 30.0));
			 }
		 }
	}
	
	private void createStickers() throws Exception {
		Sticker sticker = new Sticker();
		sticker.setFormat(Format.A4);
		sticker.setDensity(280);
		sticker.setStickerType(Sticker.Type.ADASTER);
		List<Sticker> stickers = stickerService.findByExample(sticker);
		
		if(stickers.isEmpty()) {
			sticker.setCreatedAt(new Date());
			sticker.setCreatedBy(User.TECH_USER);
			sticker.setDescription("Обычная");
			stickerService.save(sticker);
		}
		
		sticker = new Sticker();
		sticker.setFormat(Format.A4);
		sticker.setDensity(280);
		sticker.setStickerType(Sticker.Type.PLASTIC);
		stickers = stickerService.findByExample(sticker);
		
		if(stickers.isEmpty()) {
			sticker.setCreatedAt(new Date());
			sticker.setCreatedBy(User.TECH_USER);
			sticker.setDescription("Пластиковая");
			stickerService.save(sticker);
		}
	}
	
	private PaperType createMaterialType(String name) {
		PaperType paperType = new PaperType();
		paperType.setCreatedAt(new Date());
		paperType.setCreatedBy(User.TECH_USER);
		paperType.setName(name);
		return paperType;
	}
	
	private Paper createPaper(Paper material, double price) {
		material.setCreatedAt(new Date());
		material.setCreatedBy(User.TECH_USER);
		material.setPrice(BigDecimal.valueOf(price));
		return material;
	}
	
	private BindingSpring createBindingSpring(Integer diameter, BindingSpring.Type type, String description, double price) throws Exception {
		BindingSpring bindingSpring = new BindingSpring();
		bindingSpring.setDiameter(diameter);
		bindingSpring.setType(type);
		
		List<BindingSpring> foundBindingSprings = 
				bindingSpringService.findByExample(bindingSpring);
		
		if(foundBindingSprings.size() == 0) {
			
			bindingSpring.setCreatedAt(new Date());
			bindingSpring.setCreatedBy(User.TECH_USER);
			bindingSpring.setDescription(description);
			bindingSpring.setPrice(BigDecimal.valueOf(price));
			bindingSpringService.save(bindingSpring);
		}
		
		return bindingSpring;
	}
	
	
	private Laminate createLaminate(Integer density, Format format, String description, double price) throws Exception {
		Laminate laminate = new Laminate();
		laminate.setDensity(density);
		laminate.setFormat(format);
		
		List<Laminate> foundBindingSprings = 
				laminateService.findByExample(laminate);
		
		if(foundBindingSprings.size() == 0) {
			
			laminate.setCreatedAt(new Date());
			laminate.setCreatedBy(User.TECH_USER);
			laminate.setDescription(description);
			laminate.setPrice(BigDecimal.valueOf(price));
			laminateService.save(laminate);
		}
		
		return laminate;
	}
	
	private void createDefaultEquipmentTypes() throws Exception {
		
		WorkType printing = WorkType.PRINTING_BLACK_AND_WHITE;
		 
		if(printing != null) {
			
			for(String printerName : PRINTER_NAMES) {
				
				Equipment equipment = equipmentService.findByName(printerName);
				
				if(equipment == null) {
					equipment = new Equipment();
					equipment.setCreatedAt(new Date());
					equipment.setCreatedBy(User.TECH_USER);
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
	
	@Autowired
	public void setComplexityService(IComplexityService complexityService) {
		this.complexityService = complexityService;
	}
}
