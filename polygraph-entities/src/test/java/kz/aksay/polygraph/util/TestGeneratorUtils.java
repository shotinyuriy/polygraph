package kz.aksay.polygraph.util;

import kz.aksay.polygraph.util.GeneratorUtils;
import kz.aksay.polygraph.util.PersonNameGenerator.FullName;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGeneratorUtils extends Assert {
	
	@Test
	public void testGenerateFullName() {
		FullName fullName = GeneratorUtils.generateFullName();
		
		System.out.println(fullName);
		
		assertNotNull(fullName);
	}
}
