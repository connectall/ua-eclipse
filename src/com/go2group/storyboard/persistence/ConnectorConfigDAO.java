package com.go2group.storyboard.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.service.ConnectAllConfigService;

public class ConnectorConfigDAO {
	
	public void create(Connection conn, ConnectAllConfig config) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO CONNECTOR_CONFIG(ID,URL,API_KEY,APPLINK_NAME,APP_ORIGIN,LAST_QUERY_TIME) VALUES(?,?,?,?,?,?);";

		if(config == null) {
			return;
		}
		
		try {
			System.out.println("Saving Config into DB ...");
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, config.getId()); 
			stmt.setString(2, config.getUrl());
			stmt.setString(3, config.getApiKey());
			stmt.setString(4, config.getAppLinkName());
			stmt.setString(5, config.getAppOrigin());
			stmt.setLong(6, config.getLastQueryTime());
			int result = stmt.executeUpdate();
			System.out.println("Config saved in DB : "+result);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				if(conn!=null) {
					conn.setAutoCommit(true);
					if(stmt!=null)
						stmt.close();
				}
			} catch (SQLException e) {
			}			
		}
	}
	
	public void update(Connection conn, ConnectAllConfig config) {
		PreparedStatement stmt = null;
		String sql = "UPDATE CONNECTOR_CONFIG SET URL=?,API_KEY=?,APPLINK_NAME=?,APP_ORIGIN=?,LAST_QUERY_TIME=? WHERE ID=?;";

		if(config == null) {
			return;
		}
		
		if(config.getId() == null || config.getId().trim().length()==0) {
			config.setId(ConnectAllConfigService.getDefaultId());
		}
		
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, config.getUrl());
			stmt.setString(2, config.getApiKey());
			stmt.setString(3, config.getAppLinkName());
			stmt.setString(4, config.getAppOrigin());
			stmt.setLong(5, config.getLastQueryTime());
			stmt.setString(6, config.getId()); 
			stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
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


	public ConnectAllConfig get(Connection conn, String configId) throws Exception {
		ConnectAllConfig config = null;
		PreparedStatement stmt = null;
		
		if(configId == null) {
			return config;
		}
		
		try {
	      stmt = conn.prepareStatement("SELECT URL,API_KEY,APPLINK_NAME,APP_ORIGIN,LAST_QUERY_TIME FROM CONNECTOR_CONFIG WHERE ID = ?;");
	      stmt.setString(1, configId);
	      ResultSet rs = stmt.executeQuery();
	      
	      if(rs.next()) {
	    	  config = new ConnectAllConfig(configId,
	    			  rs.getString("URL"),
	    			  rs.getString("API_KEY"),
	    			  rs.getString("APPLINK_NAME"),
	    			  rs.getString("APP_ORIGIN"));
	    	  config.setLastQueryTime(rs.getLong("LAST_QUERY_TIME"));
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
		return config;
	}

}
