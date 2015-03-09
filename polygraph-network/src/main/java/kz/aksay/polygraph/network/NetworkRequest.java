package kz.aksay.polygraph.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NetworkRequest implements Serializable {
	private Class<?> serviceClazz;
	private String methodName; 
	private Map<String, Object> parameters = new HashMap<>();
	
	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	public <T> T getParameter(String key, Class<T> clazz) {
		Object value = parameters.get(key);
		if(clazz.isInstance(value)) {
			return (T)value;
		}
		return null;
	}
	
	public Class<?> getServiceClazz() {
		return serviceClazz;
	}
	
	public void setServiceClazz(Class<?> serviceClazz) {
		this.serviceClazz = serviceClazz;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	} 
}
