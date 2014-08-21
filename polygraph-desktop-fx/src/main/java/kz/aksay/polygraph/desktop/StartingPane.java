package kz.aksay.polygraph.desktop;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class StartingPane extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("FXMLTableView Example");
		Scene myScene = (Scene)FXMLLoader.load(getClass().getResource(
				"fxml_login.fxml"));
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
