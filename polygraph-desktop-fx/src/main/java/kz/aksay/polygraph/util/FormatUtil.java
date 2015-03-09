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
}
