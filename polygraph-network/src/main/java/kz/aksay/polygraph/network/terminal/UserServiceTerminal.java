package kz.aksay.polygraph.network.terminal;

import java.io.BufferedReader;
import java.io.IOException;

import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.network.NetworkConstants;
import kz.aksay.polygraph.network.NetworkRequest;

public class UserServiceTerminal {
	
	
	
	private BufferedReader reader;

	public UserServiceTerminal(BufferedReader reader) {
		this.reader = reader;
	}
	
	public NetworkRequest findByLoginAndPassword() throws IOException {
		System.out.println("Enter login: ");
		String login = reader.readLine();
		System.out.println("Enter password: ");
		String password = reader.readLine();
		NetworkRequest request = new NetworkRequest();
		request.setServiceClazz(IUserService.class);
		request.setMethodName(NetworkConstants.UserServiceMethods.FIND_BY_LOGIN_AND_PASSWORD);
		request.addParameter("login", login);
		request.addParameter("password", password);
		return request;
	}
}
