package com.go2group.storyboard.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.model.StoryStatus;

public class StoryDAO {
	
	public List<Story> selectAll(Connection conn) throws Exception {
		List<Story> stories = new ArrayList<>();
		
		Statement stmt = null;
		try {
		      stmt = conn.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM STORY;" );
		      
		      while(rs.next()) {
		    	  Story story = new Story();
		    	  story.setId(rs.getString("ID"));
		    	  story.setTitle(rs.getString("TITLE"));
		    	  story.setDescription(rs.getString("DESCRIPTION"));
		    	  story.setPriority(rs.getString("PRIORITY"));
		    	  story.setStatus(StoryStatus.valueOf(rs.getString("STATUS")));
		    	  story.setAssignedTo(rs.getString("ASSIGNED_TO"));
		    	  story.setCreatedBy(rs.getString("CREATED_BY"));
		    	  story.setLinkedStoryId(rs.getString("LINKED_STORY_ID"));
		    	  story.setCreatedTime(rs.getLong("CREATED_TIME"));
		    	  story.setModifiedTime(rs.getLong("MODIFIED_TIME"));
		    	  System.out.println(rs.getString("LINKED_STORY_ID")+" : "+story);
		    	  stories.add(story);
		      }
		} catch (Exception e) {
			throw e;
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return stories;
	}
	
	public void create(Connection conn, List<Story> stories) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO STORY(ID,TITLE,DESCRIPTION,PRIORITY,STATUS,LINKED_STORY_ID,CREATED_BY,ASSIGNED_TO,CREATED_TIME,MODIFIED_TIME) VALUES(?,?,?,?,?,?,?,?,?,?);";
		
		if(stories == null || stories.size() == 0) {
			return;
		}

		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			
			for (Story story : stories) {
				stmt.setString(1, story.getId()); //UUID should be generated in service layer
				stmt.setString(2, story.getTitle());
				stmt.setString(3, story.getDescription());
				stmt.setString(4, story.getPriority());
				stmt.setString(5, story.getStatus().name());
				stmt.setString(6, story.getLinkedStoryId()); 
				stmt.setString(7, story.getCreatedBy());
				stmt.setString(8, story.getAssignedTo());
				stmt.setLong(9, story.getCreatedTime());
				stmt.setLong(10, story.getModifiedTime());

				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				if(conn!=null) {
					conn.setAutoCommit(true);
					stmt.close();
				}
			} catch (SQLException e) {
			}			
		}
		
	}
	
	public void create(Connection conn, Story story) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO STORY(ID,TITLE,DESCRIPTION,PRIORITY,STATUS,LINKED_STORY_ID,CREATED_BY,ASSIGNED_TO,CREATED_TIME,MODIFIED_TIME) VALUES(?,?,?,?,?,?,?,?,?,?);";

		if(story == null) {
			return;
		}
		
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, story.getId()); //UUID should be generated in service layer
			stmt.setString(2, story.getTitle());
			stmt.setString(3, story.getDescription());
			stmt.setString(4, story.getPriority());
			stmt.setString(5, story.getStatus().name());
			stmt.setString(6, story.getLinkedStoryId()); 
			stmt.setString(7, story.getCreatedBy());
			stmt.setString(8, story.getAssignedTo());
			stmt.setLong(9, story.getCreatedTime());
			stmt.setLong(10, story.getModifiedTime());
			stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				if(conn!=null) {
					conn.setAutoCommit(true);
					stmt.close();
				}
			} catch (SQLException e) {
			}			
		}
	}
	
	public void update(Connection conn, List<Story> stories) {
		PreparedStatement stmt = null;
		String sql = "UPDATE STORY SET TITLE=?,DESCRIPTION=?,PRIORITY=?,STATUS=?,LINKED_STORY_ID=?,CREATED_BY=?,ASSIGNED_TO=?,MODIFIED_TIME=? WHERE ID=?;";

		try {
			if(stories == null || stories.size() == 0) {
				return;
			}
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			
			for (Story story : stories) {
				stmt.setString(1, story.getTitle());
				stmt.setString(2, story.getDescription());
				stmt.setString(3, story.getPriority());
				stmt.setString(4, story.getStatus().name());
				stmt.setString(5, story.getLinkedStoryId());
				stmt.setString(6, story.getCreatedBy());
				stmt.setString(7, story.getAssignedTo());
				stmt.setLong(8, story.getModifiedTime());
				stmt.setString(9, story.getId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if(conn!=null) {
					conn.setAutoCommit(true);
					stmt.close();
				}
			} catch (SQLException e) {
			}			
		}
		
	}

	public void update(Connection conn, Story story) {
		
	}
	
	public Story get(Connection conn, String storyId) {
		return null;
	}

	public String getStoryId(Connection conn, String linkedStoryId) throws Exception {
		String storyId = null;
		PreparedStatement stmt = null;
		
		if(linkedStoryId == null) {
			return storyId;
		}
		
		try {
		      stmt = conn.prepareStatement("SELECT ID FROM STORY WHERE LINKED_STORY_ID = ?;");
		      stmt.setString(1, linkedStoryId);
		      ResultSet rs = stmt.executeQuery();
		      
		      if(rs.next()) {
		    	  storyId = rs.getString("ID");
		      }
		} catch (Exception e) {
			throw e;
		} finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return storyId;
	}

}
