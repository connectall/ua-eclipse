package com.go2group.storyboard.util;

public 	class RestResponse {
	private int responseCode;
	private String responseString;
	
	public RestResponse(int responseCode, String responseString) {
		this.responseCode = responseCode;
		this.responseString = responseString;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseString() {
		return responseString;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RestResponse [responseCode=").append(responseCode);
		sb.append(", responseString=").append(responseString).append("]");
		return sb.toString();
	}
}
