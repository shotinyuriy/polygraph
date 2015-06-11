package kz.aksay.polygraph.util;

import java.util.Map;

public interface SessionAware {

	public void setSession(Map<String, Object> session);
}
