package com.go2group.storyboard.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePersistenceManager implements PersistenceManager {
	
	private static final String SQLITE_URL = "jdbc:sqlite:storyboard.db";
	private static final String SQLITE_DRIVER = "org.sqlite.JDBC";

	@Override
	public void createSchema() {
		Connection conn = null;
		
		try {
			System.out.println("SQLite database path ="+new File(".").getAbsolutePath()+"/storyboard.db");
			conn = getConnection();
			
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT name FROM sqlite_master WHERE type='table';" );
            
            boolean tablesFound = false;
            while ( rs.next() ) {
            	if(tablesFound) {
            		System.out.println("\nSchema creation skipped. Existing tables below,");
            	} else {
            		tablesFound = true;
            	}
            	System.out.println(rs.getString("name"));
            }
            stmt.close();
            
            if(!tablesFound) {
                stmt = conn.createStatement();
                String sqlStoryTable = "CREATE TABLE STORY " +
                               "(ID INT PRIMARY KEY     NOT NULL," +
                               " TITLE          TEXT    NOT NULL, " + 
                               " DESCRIPTION    TEXT, " + 
                               " PRIORITY       VARCHAR(10), " + 
                               " STATUS         VARCHAR(10)," +
                               " CREATED_TIME   DATETIME," +
                               " MODIFIED_TIME  DATETIME," +
                               " CREATED_BY   	TEXT," +
                               " ASSIGNED_TO   	TEXT," +
                               " LINKED_STORY_ID   	TEXT)"; 
                stmt.executeUpdate(sqlStoryTable);
                
                String sqlConnectorTable = "CREATE TABLE CONNECTOR_CONFIG " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " URL            TEXT    NOT NULL, " + 
                        " API_KEY        TEXT    NOT NULL, " + 
                        " APPLINK_NAME   TEXT, " + 
                        " APP_ORIGIN   	 TEXT, " + 
                        " LAST_QUERY_TIME   DATETIME)";
                stmt.executeUpdate(sqlConnectorTable);

                stmt.close();
                System.out.println("\nA new database schema has been created!");
            }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	@Override
	public Connection getConnection() throws Exception {
		try {
            Class.forName(SQLITE_DRIVER);
            return DriverManager.getConnection(SQLITE_URL);
		} catch (ClassNotFoundException e) {
			throw new Exception("Unable to load SQLite driver!", e);
		} catch (Exception e) {
			throw new Exception("Error while getting the connection!", e);
		}
	}

}
