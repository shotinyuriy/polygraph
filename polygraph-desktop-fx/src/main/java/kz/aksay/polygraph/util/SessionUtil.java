package kz.aksay.polygraph.util;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import kz.aksay.polygraph.entity.User;

public abstract class SessionUtil {
	
	public static final String USER_KEY = "user";
	public static final String MAIN_MENU_KEY = "mainMenu";
	
	public static MainMenu retrieveMainMenu(Map<String, Object> session) {
		if(session != null) {
			Object obj = session.get(MAIN_MENU_KEY);
			if(obj instanceof MainMenu) {
				return (MainMenu) obj;
			}
		}
		return null;
	}

	public static User retrieveUser(Map<String, Object> session) {
		if(session != null) {
			Object obj = session.get(USER_KEY);
			if(obj instanceof User) {
				return (User)obj;
			}
		}
		return null;
	}
	
	public static Node loadFxmlNodeWithSession(Class<?> clazz, String urlString, Map<String, Object> session, 
			Map<String, Object> parameters) {
		Node node = null;
		try {
			URL url = clazz.getResource(urlString);
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			node = (Node)fxmlLoader.load();
			Object controller = fxmlLoader.getController();
			
			if(controller instanceof SessionAware) {
				SessionAware sessionAwareController = (SessionAware)controller;
				sessionAwareController.setSession(session);
			}
			if(controller instanceof ParametersAware) {
				ParametersAware parametersAwareController = (ParametersAware)controller;
				parametersAwareController.setParameters(parameters);
			}
			if(controller instanceof InitializingBean) {
				InitializingBean initializableBean = (InitializingBean)controller;
				initializableBean.afterPropertiesSet();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}
}
