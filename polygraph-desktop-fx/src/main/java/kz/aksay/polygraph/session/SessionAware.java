package kz.aksay.polygraph.session;

import java.util.Map;

public interface SessionAware {

	public void setSession(Map<String, Object> session);
}
