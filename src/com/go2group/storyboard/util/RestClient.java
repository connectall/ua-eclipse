package com.go2group.storyboard.util;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RestClient {
	
	public RestResponse post(String uri, String json) throws HttpHostConnectException, Exception {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(uri);
	    
	    StringEntity entity = new StringEntity(json);
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    String httpResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
	    return new RestResponse(response.getStatusLine().getStatusCode(), httpResponse);
	}
	
	public RestResponse get(String uri) throws HttpHostConnectException, Exception {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpGet httpGet = new HttpGet(uri);		    
	    httpGet.setHeader("Accept", "application/json");
	 
	    CloseableHttpResponse response = client.execute(httpGet);
	    String httpResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
	    return new RestResponse(response.getStatusLine().getStatusCode(), httpResponse);
	}
		
}
