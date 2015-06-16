package kz.aksay.polygraph.validation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.springframework.beans.BeanUtils;

public class SimpleDateEquationValidator {
	
	private Comparison comparison;
	private String date1Name;
	private String date2Name;

	public SimpleDateEquationValidator(DateEquation constraintAnnotation) {
		comparison = constraintAnnotation.comparison();
		date1Name = constraintAnnotation.date1Name();
		date2Name = constraintAnnotation.date2Name();
	}
	
	public boolean isSimpleValid(Object value) {
			if(value == null) return false;
			Class<?> clazz = value.getClass();
			try {
				PropertyDescriptor date1Descr = BeanUtils.getPropertyDescriptor(clazz, date1Name);
				PropertyDescriptor date2Descr = BeanUtils.getPropertyDescriptor(clazz, date2Name);
				Method method1 = date1Descr.getReadMethod();
				Method method2 = date2Descr.getReadMethod();
				
				Date date1 = (Date)method1.invoke(value);
				Date date2 = (Date)method2.invoke(value);		
				
				if(date1 == null || date2 == null) return true;
				
				switch(comparison) {
					case GREATER_THAN:
						return date1.after(date2);
					case GREATER_THAN_OR_EQUALS:
						return date1.after(date2) || date1.equals(date2);
					case EQUALS:
						return date1.equals(date2);
					case LESS_THAN_OR_EQUAL:
						return date1.before(date2) || date1.equals(date2);
					case LESS_THAN:
						return date1.before(date2);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return false;
	}
}
