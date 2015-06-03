package kz.aksay.polygraph.desktop;
import java.io.File;
import java.util.Stack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.test.service.TestDataCreator;
import kz.aksay.polygraph.util.ContextUtils;

import org.springframework.context.ApplicationContext;


public class StartingPane extends Application {
	
//	public static String FXML_ROOT = ""; // for eclipse
	public static String FXML_ROOT = "/"; // for maven
	private static ApplicationContext applicationContext;
	private static Stage primaryStage;
	private static Stack<Scene> sceneStack;
	private static Pane contentPane;
	private static Pane pane;
	private static User currentUser;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			File file = new File("tmp");
			System.out.println("file "+file.getAbsolutePath());
			
			StartingPane.primaryStage = primaryStage;
			primaryStage.setTitle("Управление задачами");
			TabPane pane = (TabPane)FXMLLoader.load(packageInfo.class.getResource(
					FXML_ROOT+"fxml_login.fxml"));
			Scene myScene = new Scene(pane);
			
			primaryStage.setScene(myScene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Pane pane = new Pane();
			VBox vbox = new VBox();
			Label label = new Label();
			
			vbox.getChildren().add(label);
			
			pane.getChildren().add(vbox);
			label.setText(e.getMessage());
			for(StackTraceElement ste : e.getStackTrace()) {
				Label stackTrace = new Label();
				stackTrace.setText(ste.toString());
				vbox.getChildren().add(stackTrace);
			}
			Scene myScene = new Scene(pane);
			primaryStage.setScene(myScene);
			primaryStage.show();
		}
	}
	
	public static void loadContext() {
		ContextUtils.resetContext();
		applicationContext = ContextUtils.getApplicationContext();
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

	public static void generateAllEntities() {
		TestDataCreator testDataCreator = new TestDataCreator(applicationContext);
		testDataCreator.createAllEntities();
	}
}
