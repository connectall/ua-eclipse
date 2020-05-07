package com.go2group.storyboard.connector;

import java.util.List;

import org.apache.http.conn.HttpHostConnectException;

import com.go2group.storyboard.model.Story;

public interface Connector {
	
	public List<Story> getModifiedStories() throws HttpHostConnectException;
	
	public void createStory(Story story) throws HttpHostConnectException;
	
	public void updateStory(Story story) throws HttpHostConnectException;
	
	public String getLinkedDestinationRecordId(String sourceRecordId) throws HttpHostConnectException;
	
}
