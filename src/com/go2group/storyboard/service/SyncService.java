package com.go2group.storyboard.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.http.conn.HttpHostConnectException;

import com.go2group.storyboard.connector.ConnectorFactory;
import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.persistence.PersistenceFactory;
import com.go2group.storyboard.persistence.StoryDAO;

public class SyncService {
	
	private StoryDAO storyDAO = new StoryDAO();
	
	public List<Story> queryModifiedStoriesFromRemote() throws HttpHostConnectException {
		List<Story> stories = new ArrayList<Story>();
		try {
			// Fetch the stories modified since last_query_time from the remote application
			stories = ConnectorFactory.getConnector().getModifiedStories();
			System.out.println("Stories retrieved from Remote app :\n"+stories);
			// Sync modified stories into database
			stories = syncToDatabase(stories);
			
			//update last query time
			updateLastQueryTime();
		}catch(HttpHostConnectException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return stories;
	}

	private void updateLastQueryTime() {
		ConnectAllConfig config = ConnectAllConfigService.getInstance().getConfig(false);
		config.setLastQueryTime(new Date().getTime());
		ConnectAllConfigService.getInstance().saveConfig(config, false);
	}
	
	private List<Story> syncToDatabase(List<Story> stories) {
		Connection conn = null;
		
		try {
			conn = PersistenceFactory.getPersistenceManager().getConnection();
			
			// Consolidate stories
			for (Story story : stories) {
				String storyId = story.getId();//storyDAO.getStoryId(conn, story.getLinkedStoryId());
				
				if(storyId == null || storyId.trim().length() == 0) { // New story
					story.setId(UUID.randomUUID().toString()); // Generate new story id
					story.setNew(true);
					long now = new Date().getTime();
					story.setCreatedTime(now);
					story.setModifiedTime(now);
				} else { // Update
					story.setId(storyId);
					story.setNew(false);
					long now = new Date().getTime();
					story.setModifiedTime(now);
				}
			}		

			// Filter stories to be created
			List<Story> stories2bCreated = stories.stream()
					.filter(s -> s.isNew())
					.collect(Collectors.toList()); 
			storyDAO.create(conn, stories2bCreated);
			
			// Filter stories to be updated
			List<Story> stories2bUpdated = stories.stream()
					.filter(s -> s.isNew()==false)
					.collect(Collectors.toList()); 
			storyDAO.update(conn, stories2bUpdated);
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return stories;
	}
	
	public Story createStory(Story story) throws HttpHostConnectException {
		try {
			// post request
			ConnectorFactory.getConnector().createStory(story);
			// get linked story id from remote (in a sync thread - polling)
		}catch(HttpHostConnectException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return story;
	}
	
	public Story updateStory(Story story) throws HttpHostConnectException {
		try {
			// post request
			ConnectorFactory.getConnector().updateStory(story);
		}catch(HttpHostConnectException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return story;
	}

	
	public Story getLinkedStoryId(Story story) throws HttpHostConnectException {
		try {
			String linkedStoryId = ConnectorFactory.getConnector().getLinkedDestinationRecordId(story.getId());
			story.setLinkedStoryId(linkedStoryId);
		}catch(HttpHostConnectException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return story;
	}

}
