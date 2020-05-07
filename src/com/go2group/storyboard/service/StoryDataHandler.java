package com.go2group.storyboard.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.persistence.PersistenceFactory;
import com.go2group.storyboard.persistence.StoryDAO;

public class StoryDataHandler {
	
	private static StoryDataHandler instance;
	
	private List<Story> stories;

	private StoryDataHandler() {
		reload();
	}
	
	public static StoryDataHandler getInstance() {
		if(instance == null) {
			instance = new StoryDataHandler();
		}
		return instance;
	}
	
	public void reload() {
		Connection conn = null;
		try {
			System.out.println("Loading stories from database ...");
			conn = PersistenceFactory.getPersistenceManager().getConnection();
	        this.stories = new StoryDAO().selectAll(conn);			
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
	}
	
	public List<Story> getStories() {
		System.out.println("Invoked getStories()");
		return this.stories;
	}
	
	public void addStory(Story story) {
		this.stories.add(story);
		
		Connection conn = null;
		try {
			System.out.println("Adding story into the database ...");
			conn = PersistenceFactory.getPersistenceManager().getConnection();
	        new StoryDAO().create(conn, story);			
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

	}
	
}
