package kz.aksay.polygraph.util;

import java.util.Random;

public class OrgNameGenerator {

	public static class OrgName {
		protected String fullName;
		protected String shortName;
		protected String number;

		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		public String getShortName() {
			return shortName;
		}
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		
	}
	
	protected static String[][] orgTypes = new String[][]{{"ОАО","Открытое акционерное общество"}, {"ЗАО", "Закрытое акционерное общество"}, {"ООО","Общество с ограниченной ответственностью"}};
	protected static String[] orgSphere = new String[]{"школа", "детский сад", "компания", "банк", "магазин", "такси"};
	protected static String[] nouns = new String[]{"Азамат","Абай","Кожик","Нукер","Жураган","Алтын","Маздарын","Кыздар", "Щербет", "Орыс", "Сырбу", "Сен", "Жал", "Нугер"};
	
	
	public OrgName generate() {
		OrgName orgName = new OrgName();
		Random r = new Random();
		int orgTypeIndex = r.nextInt(orgTypes.length);
		int orgSphereIndex = r.nextInt(orgSphere.length);
		int nounIndex = r.nextInt(nouns.length);
		
		StringBuffer inn = new StringBuffer(); 
		
		orgName.setFullName(orgTypes[orgTypeIndex][1]+" "+orgSphere[orgSphereIndex]+" "+nouns[nounIndex]);
		orgName.setShortName(orgTypes[orgTypeIndex][0]+" "+orgSphere[orgSphereIndex]+" "+nouns[nounIndex]);
		for(int i = 0; i < 9; i ++) {
			inn.append(r.nextInt(10));
		}
		orgName.setNumber(inn.toString());
		
		//System.out.println("ORG FULL NAME "+orgName.getFullName());
		//System.out.println("ORG SHORT NAME "+orgName.getShortName());
		
		return orgName;
	}
	
}
