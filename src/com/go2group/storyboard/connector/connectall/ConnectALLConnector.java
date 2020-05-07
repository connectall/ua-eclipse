package com.go2group.storyboard.connector.connectall;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;

import com.go2group.storyboard.connector.Connector;
import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.service.ConnectAllConfigService;
import com.go2group.storyboard.util.RestClient;
import com.go2group.storyboard.util.RestResponse;

/**
 * Connector which manages communication to ConnectALL Generic Adapter.
 * 
 * @author Vijayakumar Selvaraju
 *
 */
public class ConnectALLConnector implements Connector {
	
	private static final String APIKEY_QUERY_PARM = "?apikey=";
	private static final String BASE_URI = "/connectall/api/2/";

	public ConnectALLConnector() {
	}
	
	/**
	 * Get stories modified after the last query time from the destination application.
	 * 
	 * @return list of stories modified from the destination application
	 * @throws HttpHostConnectException 
	 */
	public List<Story> getModifiedStories() throws HttpHostConnectException {
		List<Story> stories = new ArrayList<>();
		
		try {
			Map<String, Object> keyFields = new LinkedHashMap<>();
			String origin = getConnectAllConfig().getAppOrigin();
			keyFields.put("appLinkName", getConnectAllConfig().getAppLinkName());
			keyFields.put("origin", "source".equals(origin)?"destination":"source"); //destination origin
			keyFields.put("lastModifiedTime", getConnectAllConfig().getFormattedLastQueryTime()); 
			
			String json = JsonConvertor.toJson(keyFields);
			String uri = buildApiUri(API.search);
			System.out.println("API : "+uri);
			System.out.println("Request : "+json);
			
			RestResponse response = new RestClient().post(uri, json);
			if(response.getResponseCode() >= 200) {
				stories = JsonConvertor.getStories(response.getResponseString());
			}
		}catch(HttpHostConnectException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return stories;
	}

	/**
	 * Creates a story in the destination application.
	 * 
	 * This method would just initiate the synchronization asynchronously.
	 * It is necessary to poll the newly created story id at the destination application.
	 * @throws HttpHostConnectException 
	 * 
	 * @see getLinkedDestinationRecordId
	 */
	public void createStory(Story story) throws HttpHostConnectException {
		pushStory(story);
	}
	
	/**
	 * Updates a story in the destination application if it is already sync'ed by ConnectALL.
	 * Otherwise, it will attempt to create a new story in the destination application.
	 * @throws HttpHostConnectException 
	 */	
	public void updateStory(Story story) throws HttpHostConnectException {
		pushStory(story);
	}
	
	private void pushStory(Story story) throws HttpHostConnectException {
		
		try {
			Map<String, Object> keyFields = new LinkedHashMap<>();
			keyFields.put("appLinkName", getConnectAllConfig().getAppLinkName());
			keyFields.put("origin", getConnectAllConfig().getAppOrigin());
			keyFields.put("fields", JsonConvertor.buildAsMap(story));
			
			String json = JsonConvertor.toJson(keyFields);			
			String uri = buildApiUri(API.postRecord);
			System.out.println("API : "+uri);
			System.out.println("Request : "+json);
			
			RestResponse response = new RestClient().post(uri, json);
			System.out.println(response);
			if(response.getResponseCode() == 201) {
				System.out.println("Story pushed for synchronization!");
			} else {
				System.out.println("Push request failed. Cause:"+response);
			}
		}catch(HttpHostConnectException e) {
			throw e;			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Get linked destination record id from ConnectALL record mapping persistence store.
	 * 
	 * @param sourceRecordId - source record id created in eclipse for which we need to find the link.
	 * @return linked destination record id when sync is completed, otherwise returns null
	 * @throws HttpHostConnectException 
	 */
	public String getLinkedDestinationRecordId(String sourceRecordId) throws HttpHostConnectException {
		String linkedStoryId = null;
		
		try {
			Map<String, Object> keyFields = new LinkedHashMap<>();
			keyFields.put("appLinkName", getConnectAllConfig().getAppLinkName());
			keyFields.put("origin", getConnectAllConfig().getAppOrigin());
			keyFields.put("recordId", sourceRecordId);
			
			String json = JsonConvertor.toJson(keyFields);
			String uri = buildApiUri(API.getLinkedRecordId);
			System.out.println("API : "+uri);
			System.out.println("Request : "+json);

			RestResponse response = new RestClient().post(uri, json);
			if(response.getResponseCode() >= 200) {
				linkedStoryId = JsonConvertor.getLinkedId(response.getResponseString());
			} else {
				System.out.println("Linked destination record id not found! Perhaps the synchroniztion must be in progress ...");
			}
		}catch(HttpHostConnectException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return linkedStoryId;
	}
	
	
	
	private String buildApiUri(API api) {
		StringBuilder uri = new StringBuilder();
		uri.append(getConnectAllConfig().getUrl())
			.append(BASE_URI)
			.append(api.name())
			.append(APIKEY_QUERY_PARM)
			.append(getConnectAllConfig().getApiKey());
		return uri.toString();
	}
	
	public ConnectAllConfig getConnectAllConfig() {
		return ConnectAllConfigService.getInstance().getConfig(false);
	}
	
}
