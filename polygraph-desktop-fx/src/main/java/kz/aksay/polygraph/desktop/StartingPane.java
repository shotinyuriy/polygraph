package kz.aksay.polygraph.desktop;
import java.util.Stack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.util.ContextUtils;

import org.springframework.context.ApplicationContext;


public class StartingPane extends Application {

	private static ApplicationContext applicationContext;

	private static Stage primaryStage;
	
	private static Stack<Scene> sceneStack;
	
	private static Pane contentPane;
	
	private static Pane pane;
	
	private static User currentUser;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		applicationContext = ContextUtils.getApplicationContext();
		
		StartingPane.primaryStage = primaryStage;
		primaryStage.setTitle("Управление задачами");
		Pane pane = (Pane)FXMLLoader.load(packageInfo.class.getResource(
				"fxml_login.fxml"));
		Scene myScene = new Scene(pane);
		
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {		
		launch(args);
	}
	
	public static <T> T getBean(Class<?> clazz) {
		return (T)applicationContext.getBean(clazz);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void changeScene(Parent parent) {
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
	}
	
	public static void clearStack() {
		sceneStack.clear();
	}
	
	public static void pushScene(Parent parent) {
		if(sceneStack == null) { sceneStack = new Stack<Scene>(); }
		sceneStack.push(primaryStage.getScene());
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
	}
	
	public static void popScene() {
		if(sceneStack != null) { 
			Scene scene = sceneStack.pop();
			primaryStage.setScene(scene);
		}
	}
	
	public static void setContent(Node node) {
		if(contentPane instanceof BorderPane) {
			((BorderPane)contentPane).setRight(node);
		}
	}

	public static Pane getContentBorderPane() {
		return contentPane;
	}

	public static void setContentBorderPane(Pane contentBorderPane) {
		StartingPane.contentPane = contentBorderPane;
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		StartingPane.currentUser = currentUser;
	}
}
