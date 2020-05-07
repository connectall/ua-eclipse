package com.go2group.storyboard.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectAllConfig {
	
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private String id;
	
	private String url;
	
	private String apiKey;
	
	private String appLinkName;
	
	private String appOrigin;
	
	private long lastQueryTime = new Date().getTime();
	
	public ConnectAllConfig(String id, String url, String apiKey, String appLinkName, String appOrigin) {
		this.id = id;
		this.url = url;
		this.apiKey = apiKey;
		this.appLinkName = appLinkName;
		this.appOrigin = appOrigin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAppLinkName() {
		return appLinkName;
	}

	public void setAppLinkName(String appLinkName) {
		this.appLinkName = appLinkName;
	}

	public String getAppOrigin() {
		return appOrigin;
	}

	public void setAppOrigin(String appOrigin) {
		this.appOrigin = appOrigin;
	}

	public long getLastQueryTime() {
		return lastQueryTime;
	}

	public void setLastQueryTime(long lastQueryTime) {
		this.lastQueryTime = lastQueryTime;
	}
	
	public String getFormattedLastQueryTime() {
		return DATE_TIME_FORMAT.format(new Date(lastQueryTime));
	}
	
	public String getFormattedLastQueryTime(long timeDiff) {
		return DATE_TIME_FORMAT.format(new Date(lastQueryTime-timeDiff));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ConnectAllConfig [Id=").append(id).append(", URL=").append(url)
			.append(",AppLinkName=").append(appLinkName)
			.append(",Application Origin=").append(appOrigin).append("]");
		return sb.toString();
	}

}
