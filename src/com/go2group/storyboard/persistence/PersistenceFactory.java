package com.go2group.storyboard.persistence;

public class PersistenceFactory {
	
	public static PersistenceManager getPersistenceManager() {
		return new SQLitePersistenceManager();
	}

}
