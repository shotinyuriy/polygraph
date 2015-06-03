package kz.aksay.polygraph.util;

import java.util.Map;

public interface MainMenu {
	public void loadFxmlAndOpenInTab(String url, String tabName);
	public void loadFxmlAndOpenInTab(String url, String tabName, Map<String, Object> parameters);
	public void setLeftStatus(String status);
	public void setRightStatus(String status);
	public void setProgressBarVisible(boolean visible);
}
