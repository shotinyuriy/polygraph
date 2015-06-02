package kz.aksay.polygraph.util;

import java.util.Random;

public class PersonNameGenerator {
	public static class FullName {
		private String lastName;
		private String firstName;
		private String middleName;
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getMiddleName() {
			return middleName;
		}
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}
		
		@Override
		public String toString() {
			StringBuffer fullName = new StringBuffer();
			fullName.append(lastName);
			if(firstName != null) {
				fullName.append(" ").append(firstName);
			}
			if(middleName != null) {
				fullName.append(" ").append(middleName);
			}
			return fullName.toString();
		}
	}
	
	public static abstract class AbstractFullnameGenerator {
		protected static String[] nameBeginings;
		protected static String[] firstNameEndings;
		protected static String[] lastNameEndings;
		protected static String[] middleNameEndings;
		
		public FullName generate() {
			Random r = new Random();
			FullName fullName = new FullName();
			int firstNameBeginIndex = r.nextInt(nameBeginings.length);
			int lastNameBeginIndex = r.nextInt(nameBeginings.length);
			int middleNameBeginIndex = r.nextInt(nameBeginings.length);
			
			int firstNameEndIndex = r.nextInt(firstNameEndings.length);
			int lastNameEndIndex = r.nextInt(lastNameEndings.length);
			int middleNameEndIndex = r.nextInt(middleNameEndings.length);
			
			fullName.setFirstName(nameBeginings[firstNameBeginIndex]+firstNameEndings[firstNameEndIndex]);
			fullName.setLastName(nameBeginings[lastNameBeginIndex]+lastNameEndings[lastNameEndIndex]);
			fullName.setMiddleName(nameBeginings[middleNameBeginIndex]+middleNameEndings[middleNameEndIndex]);
			return fullName; 
		}

	}
	
	public static class MaleNameGenerator extends AbstractFullnameGenerator {
		static {
			nameBeginings = new String[]{"Анд","Бор","Влад","Григ","Дмит","Ег","Зах","Ив","Кон","Лев","Мак","Ник","Ол","Пет"};
			firstNameEndings = new String[]{"рей","ис","имир","орий","рий","ор","ар","ан","драт","сим","олай","ег","р"};
			lastNameEndings = new String[]{"ов","ев","ин","оев","аев","еин"};
			middleNameEndings = new String[]{"евич","ович"};
		}
	}
}
