package kz.aksay.polygraph.util;


public class TrowableUtils {
	
	public static <T extends Throwable> void findAndThrowCause(Throwable t,
			Class<T> targetClass ) throws T {
		while(t != null) {
			if(t.getClass().equals(targetClass)) {
				throw (T)t;
			}
			t = t.getCause();
		}
	}
}
