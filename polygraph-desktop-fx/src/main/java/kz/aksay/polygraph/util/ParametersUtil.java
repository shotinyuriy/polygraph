package kz.aksay.polygraph.util;

import java.util.Map;

public abstract class ParametersUtil {
	
	public static <T> T extractParameter(Map<String, Object> parameters, String key, Class<T> clazz) {
		T value = null;
		Object obj = parameters.get(key);
		if(clazz.isAssignableFrom(obj.getClass())) {
			value = (T) obj;
		}
		return value;
	}
}
