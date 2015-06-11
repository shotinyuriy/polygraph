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

		
		public FullName generate() {
			Random r = new Random();
			FullName fullName = new FullName();
			int firstNameBeginIndex = r.nextInt(nameBeginings().length);
			int lastNameBeginIndex = r.nextInt(nameBeginings().length);
			int middleNameBeginIndex = r.nextInt(nameBeginings().length);
			
			int firstNameEndIndex = r.nextInt(firstNameEndings().length);
			int lastNameEndIndex = r.nextInt(lastNameEndings().length);
			int middleNameEndIndex = r.nextInt(middleNameEndings().length);
			
			fullName.setFirstName(nameBeginings()[firstNameBeginIndex]+firstNameEndings()[firstNameEndIndex]);
			fullName.setLastName(nameBeginings()[lastNameBeginIndex]+lastNameEndings()[lastNameEndIndex]);
			fullName.setMiddleName(nameBeginings()[middleNameBeginIndex]+middleNameEndings()[middleNameEndIndex]);
			return fullName; 
		}

		protected abstract String[] nameBeginings();
		protected abstract String[] firstNameEndings();
		protected abstract String[] lastNameEndings();
		protected abstract String[] middleNameEndings();

	}
	
	public static class MaleNameGenerator extends AbstractFullnameGenerator {
		
		protected static String[] nameBeginings;
		protected static String[] firstNameEndings;
		protected static String[] lastNameEndings;
		protected static String[] middleNameEndings;
		
		static {
			nameBeginings = new String[]{"Анд","Бор","Влад","Григ","Дмит","Ег","Зах","Ив","Кон","Лев","Мак","Ник","Ол","Пет"};
			firstNameEndings = new String[]{"рей","ис","имир","орий","рий","ор","ар","ан","драт","сим","олай","ег","р"};
			lastNameEndings = new String[]{"ов","ев","ин","оев","аев","еин"};
			middleNameEndings = new String[]{"евич","ович","ич"};
		}

		@Override
		protected String[] nameBeginings() {
			return nameBeginings;
		}

		@Override
		protected String[] firstNameEndings() {
			return firstNameEndings;
		}

		@Override
		protected String[] lastNameEndings() {
			return lastNameEndings;
		}

		@Override
		protected String[] middleNameEndings() {
			return middleNameEndings;
		}
	}
	
	public static class FemaleNameGenerator extends AbstractFullnameGenerator {
		
		protected static String[] nameBeginings;
		protected static String[] firstNameEndings;
		protected static String[] lastNameEndings;
		protected static String[] middleNameEndings;
		
		static {
			nameBeginings = new String[]{"Антон","Бэл","Вален","Гал","Да","Ел","Зин","Ир","Ксе","Лар","Мар","Ната","Оль","Прас"};
			firstNameEndings = new String[]{"ина","ла","тина","яна","рья","ена","аида","ния","риса","ия","лья","га","ковья"};
			lastNameEndings = new String[]{"ова","ева","ина","оева","аева","еина"};
			middleNameEndings = new String[]{"евна","овна","ична"};
		}
		
		@Override
		protected String[] nameBeginings() {
			return nameBeginings;
		}

		@Override
		protected String[] firstNameEndings() {
			return firstNameEndings;
		}

		@Override
		protected String[] lastNameEndings() {
			return lastNameEndings;
		}

		@Override
		protected String[] middleNameEndings() {
			return middleNameEndings;
		}
	}
}
