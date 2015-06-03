package kz.aksay.polygraph.util;

import java.util.Random;

public class OrgNameGenerator {

	public static class OrgName {
		protected String fullName;
		protected String shortName;
		protected String number;
		protected String kpp;

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
		public String getKpp() {
			return kpp;
		}
		public void setKpp(String kpp) {
			this.kpp = kpp;
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
		StringBuffer kpp = new StringBuffer();
		
		orgName.setFullName(orgTypes[orgTypeIndex][1]+" "+orgSphere[orgSphereIndex]+" "+nouns[nounIndex]);
		orgName.setShortName(orgTypes[orgTypeIndex][0]+" "+orgSphere[orgSphereIndex]+" "+nouns[nounIndex]);
		for(int i = 0; i < 11; i ++) {
			inn.append(r.nextInt(10));
		}
		orgName.setNumber(inn.toString());
		for(int i = 0; i < 9; i ++) {
			kpp.append(r.nextInt(10));
		}
		orgName.setKpp(kpp.toString());
		
		return orgName;
	}
	
}
