package kz.aksay.polygraph.entityfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kz.aksay.polygraph.entity.Address;

public class AddressFX extends EntityFX<Address> {

	private StringProperty cityProperty;
	private StringProperty streetProperty;
	private StringProperty houseProperty;
	private StringProperty apartmentProperty;
	
	public AddressFX(Address entity) {
		super(entity);
		if(this.entity == null) {
			this.entity = new Address();
			cityProperty = new SimpleStringProperty();
			streetProperty = new SimpleStringProperty();
			houseProperty = new SimpleStringProperty();
			apartmentProperty = new SimpleStringProperty();
		} else {
			cityProperty = new SimpleStringProperty(entity.getCity());
			streetProperty = new SimpleStringProperty(entity.getStreet());
			houseProperty = new SimpleStringProperty(entity.getHouse());
			apartmentProperty = new SimpleStringProperty(entity.getApartment());
		}
	}
	
	@Override
	public Address getEntity() {
		entity.setCity(cityProperty.get());
		entity.setStreet(streetProperty.get());
		entity.setHouse(houseProperty.get());
		entity.setApartment(apartmentProperty.get());
		return entity;
	}

	public StringProperty getCityProperty() {
		return cityProperty;
	}

	public void setCityProperty(StringProperty cityProperty) {
		this.cityProperty = cityProperty;
	}

	public StringProperty getStreetProperty() {
		return streetProperty;
	}

	public void setStreetProperty(StringProperty streetProperty) {
		this.streetProperty = streetProperty;
	}

	public StringProperty getHouseProperty() {
		return houseProperty;
	}

	public void setHouseProperty(StringProperty houseProperty) {
		this.houseProperty = houseProperty;
	}

	public StringProperty getApartmentProperty() {
		return apartmentProperty;
	}

	public void setApartmentProperty(StringProperty apartmentProperty) {
		this.apartmentProperty = apartmentProperty;
	}

}
