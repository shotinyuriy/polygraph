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

	/**
	 * 
	 * @param first
	 * @param second
	 * @return first - second (in full years)
	 */
	public static int differenceInYears(Date first, Date second) {
		
		Calendar firstCal = Calendar.getInstance();
		firstCal.setTime(first);
		
		Calendar secondCal = Calendar.getInstance();
		secondCal.setTime(second);
		
		int year1 = firstCal.get(Calendar.YEAR);
		int year2 = secondCal.get(Calendar.YEAR);
		int month1 = firstCal.get(Calendar.MONTH);
		int month2 = secondCal.get(Calendar.MONTH);
		int day1 = firstCal.get(Calendar.DAY_OF_MONTH);
		int day2 = secondCal.get(Calendar.DAY_OF_MONTH);
		
		int diffYears = year1 - year2;
		if(month1 < month2 || 
				month1 == month2 && day1 < day2) {
			diffYears--;
		}
		return diffYears;
	}
}
