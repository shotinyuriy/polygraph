package kz.aksay.polygraph.desktop.controls;


import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DateStringConverter;
import kz.aksay.polygraph.desktop.controls.fxml.packageInfo;

public class DateField extends AnchorPane {
	
	@FXML private TextField dateField;
	private ObjectProperty<Date> dateProperty;
	
	public DateField() {
		FXMLLoader loader = new FXMLLoader(packageInfo.class.getResource("DateField.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
			
			dateProperty = new SimpleObjectProperty<Date>(new Date());
			dateField.textProperty().bindBidirectional(dateProperty, new DateStringConverter("dd.MM.yyyy"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setDate(Date date) {
		dateProperty().set(date);
	}
	
	public Date getDate() {
		return dateProperty().get();
	}
	
	public void setDate(String date) {
		if(date == null) {
			dateProperty().set(null);
		}
		try {
			dateField.textProperty().set(date);
		} catch(Exception pe) {
			System.out.println("UNPARSABLE DATE");
		}
	}
	
	public ObjectProperty<Date> dateProperty() {
		return dateProperty;
	}
}
