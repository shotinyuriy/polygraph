package kz.aksay.polygraph.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DefaultDataCreationService {
	
	private UserService userService;
	
	private EmployeeTypeService employeeTypeService;
	
	private MaterialTypeService materialTypeService;
	
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
					
					 for(String employeeTypeName : 
						 EmployeeType.DefaultNames.all()) {
						 if(employeeTypeService.
								 findByName(employeeTypeName) == null) {
							 employeeTypeService.save(
									 createEmployeeType(employeeTypeName));
						 }
					 }
					 
					 for(String materialTypeName : 
						 MaterialType.DefaultNames.all()) {
						 if(materialTypeService.
								 findByName(materialTypeName) == null) {
							 materialTypeService.save(
									 createMaterialType(materialTypeName));
						 }
					 }
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private EmployeeType createEmployeeType(String name) {
		EmployeeType employeeType = new EmployeeType();
		employeeType.setCreatedAt(new Date());
		employeeType.setCreatedBy(User.TECH_USER);
		employeeType.setName(name);
		return employeeType;
	}
	
	private MaterialType createMaterialType(String name) {
		MaterialType materialType = new MaterialType();
		materialType.setCreatedAt(new Date());
		materialType.setCreatedBy(User.TECH_USER);
		materialType.setName(name);
		return materialType;
	}
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setEmployeeTypeService(EmployeeTypeService employeeTypeService) {
		this.employeeTypeService = employeeTypeService;
	}
	
	@Autowired
	public void setMaterialtypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}
}
