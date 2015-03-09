package kz.aksay.polygraph.test.service;
import java.util.Date;

import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.UserService;
import kz.aksay.polygraph.test.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestUserService extends Assert {
	
	private ApplicationContext context;
	
	private IUserService userService; 
	
	@BeforeClass
	public void setUp() {
		context = ContextUtils.getApplicationContext();
		userService = context.getBean(IUserService.class);
	}
	
	private User createUser() throws Exception {
		User user = new User();
		user.setCreatedAt(new Date());
		user.setCreatedBy(User.TECH_USER);
		user.setLogin("test");
		user.setPassword("test");
		user.setRole(User.Role.DESIGNER);
		userService.save(user);
		return user;
	}
	
	@Test
	public void testFindByLogin() throws Exception {
		User user = createUser();
		
		User foundedUser = userService.findByLogin(user.getLogin());
		
		if(foundedUser != null) {
			System.out.println("foundedUser "+foundedUser.getLogin());
		}
		
		assertNotNull(foundedUser);
		
		userService.delete(foundedUser);
	}
	
	@Test
	public void testFindByLoginAndPassword() throws Exception {
		User user = createUser();
		
		User foundedUser = userService.findByLoginAndPassword(
				user.getLogin(), user.getPassword());
		
		if(foundedUser != null) {
			System.out.println("foundedUser "+foundedUser.getLogin());
		}
		
		assertNotNull(foundedUser);
		
		userService.delete(foundedUser);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
