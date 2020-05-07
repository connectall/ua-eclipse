package com.go2group.storyboard.persistence;

import java.sql.Connection;

public interface PersistenceManager {
	
	public Connection getConnection() throws Exception;

	public void createSchema();

}
