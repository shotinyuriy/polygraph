package kz.aksay.polygraph.util;

import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDateUtils extends Assert {
	

	@Test
	public void testDifferenceInDays() {
		Calendar calendar = Calendar.getInstance();
		Date first = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 3);
		Date second = calendar.getTime();
		int difference = DateUtils.differenceInDays(first, second);
		assertEquals(difference, -3);
	}
}
