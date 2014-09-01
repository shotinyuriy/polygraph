package kz.aksay.polygraph.desktop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class StartingPane extends Application {

	private static Stage primaryStage;
	
	private static BorderPane contentBorderPane;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		StartingPane.primaryStage = primaryStage;
		primaryStage.setTitle("Управление задачами");
		Scene myScene = (Scene)FXMLLoader.load(getClass().getResource(
				"fxml_login.fxml"));
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {		
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void setContent(Node node) {
		contentBorderPane.setRight(node);
	}

	public static BorderPane getContentBorderPane() {
		return contentBorderPane;
	}

	public static void setContentBorderPane(BorderPane contentBorderPane) {
		StartingPane.contentBorderPane = contentBorderPane;
	}
}
