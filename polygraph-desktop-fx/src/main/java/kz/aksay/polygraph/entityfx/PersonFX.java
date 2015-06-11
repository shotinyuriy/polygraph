package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.util.FormatUtil;

public class PersonFX {
	
	private Person person;
	
	public static List<PersonFX> convertListEntityToFX(List<Person> persons) {
		List<PersonFX> personsFX = new LinkedList<>();
		for(Person person : persons) {
			personsFX.add(new PersonFX(person));
		}
		return personsFX;
	}
	
	public PersonFX(Person person) {
		this.person = person;
	}
	
	public String getFullName() {
		return person.getFullName();
	}
	
	public String getBirthDateString() {
		if(person != null && person.getBirthDate() != null) {
			return FormatUtil.dateFormatter.format(person.getBirthDate());
		}
		return "";
	}

	public Person getPerson() {
		return person;
	}
}
