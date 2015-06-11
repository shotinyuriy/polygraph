package kz.aksay.polygraph.desktop.controls;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import kz.aksay.polygraph.desktop.StartingPane;
import kz.aksay.polygraph.desktop.fxml.packageInfo;

public class AnchorButton extends AnchorPane {
	
	@FXML 
	private Button button;
	
	public AnchorButton() {
		super();
		FXMLLoader loader = new FXMLLoader(packageInfo.class.getResource(StartingPane.FXML_ROOT+"custom/AnchorButton.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setText(String text) {
		textProperty().set(text);
	}
	
	public String getText() {
		return textProperty().get();
	}
		
	public void setOnAction(EventHandler<ActionEvent> handler) {
		onActionProperty().set(handler);
	}
	
	public EventHandler<ActionEvent> getOnAction() {
		return onActionProperty().get();
	}
	
	public void setMnemonicParsing(boolean value) {
		mnemonicParsingProperty().set(value);
	}
	
	public boolean getMnemonicParsing() {
		return mnemonicParsingProperty().get();
	}
	
	public StringProperty textProperty() {
		return button.textProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return button.onActionProperty();
	}
	
	public BooleanProperty mnemonicParsingProperty() {
		return button.mnemonicParsingProperty();
	}

}
