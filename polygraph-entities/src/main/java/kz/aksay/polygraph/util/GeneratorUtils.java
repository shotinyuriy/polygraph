package kz.aksay.polygraph.util;

import java.util.Random;

import kz.aksay.polygraph.util.OrgNameGenerator.OrgName;
import kz.aksay.polygraph.util.PersonNameGenerator.FemaleNameGenerator;
import kz.aksay.polygraph.util.PersonNameGenerator.FullName;
import kz.aksay.polygraph.util.PersonNameGenerator.MaleNameGenerator;

public class GeneratorUtils {
	
	public static int counter = 1;
	public static Random random = new Random();
	
	private static MaleNameGenerator maleNameGenerator = new MaleNameGenerator();
	private static FemaleNameGenerator femaleNameGenerator = new FemaleNameGenerator();
	private static OrgNameGenerator orgNameGenerator = new OrgNameGenerator();
	
	public static FullName generateFullName() {
		int choice = random.nextInt(10); 
		if(choice > 5) {
			return maleNameGenerator.generate();
		} else {
			return femaleNameGenerator.generate();
		}
	}
	
	public static OrgName generateOrgName() {
		return orgNameGenerator.generate();
	}
	
	
	public static String generateCode(int length) {
		
		String code = counter+"";
		StringBuffer sb = new StringBuffer();
		int n = length - code.length();
		for(int i = 0; i<n; i++) {
			sb.append("0");
		}
		sb.append(code);
		
		counter++;
		return sb.toString();
		
	}
}