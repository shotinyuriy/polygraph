package kz.aksay.polygraph.util;

import java.util.Map;

public interface MainMenu {
	public void loadFxmlAndOpenInTab(String url, String tabName);
	public void loadFxmlAndOpenInTab(String url, String tabName, Map<String, Object> parameters);
}
