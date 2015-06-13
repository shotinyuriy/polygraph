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
import kz.aksay.polygraph.entityfx.AddressFX;

public class AddressPane extends AnchorPane {

	@FXML private TextField cityField;
	@FXML private TextField streetField;
	@FXML private TextField houseField;
	@FXML private TextField apartmentField; 
	@FXML private Label titleLabel; 
	
	private AddressFX addressFX; 
	
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
	
	public AddressFX getAddressFX() {
		return addressFX;
	}
	
	public void setAddressFX(AddressFX address) {
		
		if(this.addressFX != null) {
			cityField.textProperty().unbindBidirectional(this.addressFX.getCityProperty());
			streetField.textProperty().unbindBidirectional(this.addressFX.getStreetProperty());
			houseField.textProperty().unbindBidirectional(this.addressFX.getHouseProperty());
			apartmentField.textProperty().unbindBidirectional(this.addressFX.getApartmentProperty());
		}
		
		this.addressFX = address;
		
		if(address == null || address.getEntity() == null) {
			cityField.clear();
			streetField.clear();
			houseField.clear();
			apartmentField.clear();
		} else {
			cityField.textProperty().bindBidirectional(address.getCityProperty());
			streetField.textProperty().bindBidirectional(address.getStreetProperty());
			houseField.textProperty().bindBidirectional(address.getHouseProperty());
			apartmentField.textProperty().bindBidirectional(address.getApartmentProperty());
		}
	}
	
	public StringProperty titleProperty() {
		return titleLabel.textProperty();
	}
	
	
	
}
