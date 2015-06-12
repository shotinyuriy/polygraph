package kz.aksay.polygraph.desktop.controls;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import kz.aksay.polygraph.desktop.StartingPane;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Address;

public class AddressPane extends AnchorPane {

	@FXML private TextField cityField;
	@FXML private TextField streetField;
	@FXML private TextField houseField;
	@FXML private TextField apartmentField; 
	@FXML private Label titleLabel; 
	
	private Address address; 
	
	public AddressPane() {
		super();
		FXMLLoader loader = new FXMLLoader(packageInfo.class.getResource(StartingPane.FXML_ROOT+"custom/AddressPane.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Address getAddress() {
		
		address.setCity(cityField.getText());
		address.setStreet(streetField.getText());
		address.setHouse(houseField.getText());
		address.setApartment(apartmentField.getText());
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
		
		if(address == null) {
			cityField.clear();
			streetField.clear();
			houseField.clear();
			apartmentField.clear();
		} else {
			cityField.setText(address.getCity());
			streetField.setText(address.getStreet());
			houseField.setText(address.getHouse());
			apartmentField.setText(address.getApartment());
		}
	}
	
	public StringProperty titleProperty() {
		return titleLabel.textProperty();
	}
	
	
	
}
