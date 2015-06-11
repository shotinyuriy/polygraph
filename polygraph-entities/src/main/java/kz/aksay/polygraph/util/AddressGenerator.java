package kz.aksay.polygraph.util;

import java.util.Random;

import kz.aksay.polygraph.entity.Address;

public class AddressGenerator {
	private static final String[] cities = {
		"Аксай", "Уральск", "Алматы"
	};

	private static final String[] streets = {
		"ул. Абайская", "пр. Казахов", "ул. Казахская", "ул. Уральская", 
		"ул. Аксайская", "Конный пер."
	};
	
	private static final String[] houses = {
		"1", "2а", "13/17", "22"
	};
	
	private static final String[] apartments = {
		null, "1", "345" 
	};
	
	private static Random random = new Random(System.currentTimeMillis());
	
	public static Address generateAddress() {
		Address address = new Address();
		address.setCity(cities[random.nextInt(cities.length)]);
		address.setStreet(streets[random.nextInt(streets.length)]);
		address.setHouse(houses[random.nextInt(houses.length)]);
		address.setApartment(apartments[random.nextInt(apartments.length)]);
		return address;
	}
}
