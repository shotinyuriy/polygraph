package kz.aksay.polygraph.network.db;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.network.NetworkConstants;
import kz.aksay.polygraph.network.NetworkRequest;
import kz.aksay.polygraph.network.terminal.UserServiceTerminal;
import kz.aksay.polygraph.service.UserService;

@Service
public class DBManager {

	private IUserService userService;
	
	public Serializable handleRequest(NetworkRequest networkRequest) {
		if(networkRequest.getServiceClazz().equals(IUserService.class)) {
			if(NetworkConstants.UserServiceMethods.FIND_BY_LOGIN_AND_PASSWORD.equals(networkRequest.getMethodName())) {
				
				String login = networkRequest.getParameter("login", String.class);
				String password = networkRequest.getParameter("password", String.class);
				
				User user = userService.findByLoginAndPassword(login, password);
				
				return user;
			}
		}
		return null;
	} 
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
