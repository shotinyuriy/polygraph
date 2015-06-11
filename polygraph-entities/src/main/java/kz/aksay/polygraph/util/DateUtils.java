package kz.aksay.polygraph.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 
	 * @param first
	 * @param second
	 * @return first - second in days. first date minus second in days
	 */
	public static int differenceInDays(Date first, Date second) {
		
		Calendar firstCal = Calendar.getInstance();
		firstCal.setTime(first);
		
		Calendar secondCal = Calendar.getInstance();
		secondCal.setTime(second);
		
		int diffYears = firstCal.get(Calendar.YEAR) - secondCal.get(Calendar.YEAR);
		int diffDays = firstCal.get(Calendar.DAY_OF_YEAR) - secondCal.get(Calendar.DAY_OF_YEAR);
		
		diffDays+=(int)(diffYears*365.25);
		
		return diffDays;
	}
}
