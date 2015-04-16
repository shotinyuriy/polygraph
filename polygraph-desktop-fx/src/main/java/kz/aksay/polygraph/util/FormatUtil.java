package kz.aksay.polygraph.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public final class FormatUtil {
	
	public static final String DATE_FORMAT_DESCRIPTION = "ДД.ММ.ГГГГ";
	public static final String DATETIME_FORMAT_DESCRIPTION = "ДД.ММ.ГГГГ ЧЧ:ММ:СС";
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public static Date convertLocalDate(LocalDate localDate) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.set( localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth() );
		return calendar.getTime();
	}
	
	public static LocalDate convertFromLocalDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		LocalDate localDate = LocalDate.of(
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH)+1, 
				calendar.get(Calendar.DAY_OF_MONTH));
		return localDate;
	}
}
