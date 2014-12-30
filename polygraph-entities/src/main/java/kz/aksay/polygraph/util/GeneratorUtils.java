package kz.aksay.polygraph.util;

import java.util.Random;

import kz.aksay.polygraph.util.OrgNameGenerator.OrgName;
import kz.aksay.polygraph.util.PersonNameGenerator.FullName;
import kz.aksay.polygraph.util.PersonNameGenerator.MaleNameGenerator;

public class GeneratorUtils {
	
	private static MaleNameGenerator maleNameGenerator = new MaleNameGenerator();
	private static OrgNameGenerator orgNameGenerator = new OrgNameGenerator();
	
	public static FullName generateFullName() {
		return maleNameGenerator.generate();
	}
	
	public static OrgName generateOrgName() {
		return orgNameGenerator.generate();
	}
	
	
}