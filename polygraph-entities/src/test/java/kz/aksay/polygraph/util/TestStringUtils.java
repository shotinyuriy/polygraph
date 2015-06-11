package kz.aksay.polygraph.util;

import kz.aksay.polygraph.util.StringUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestStringUtils extends Assert {

	@Test
	public void testRusToEngTranslit() {
		String rusString = "Калиночка Красненькая 1234";
		String engStringExpected = "Kalinochka Krasnen'kaya 1234";
		
		String engStringActual = StringUtils.rusToEngTranslit(rusString);
		
		System.out.println(String.format("Source %s Expected %s actual %s", rusString, engStringExpected, engStringActual));
		
		assertEquals(engStringExpected, engStringActual);
	}
}
