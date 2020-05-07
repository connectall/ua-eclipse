package com.go2group.storyboard.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.persistence.ConnectorConfigDAO;
import com.go2group.storyboard.persistence.PersistenceFactory;

public class ConnectAllConfigService {
	
	private static final String CONNECTALL_CONNECTOR_ID = "CONNECTALL_CONNECTOR_1";

	private static ConnectAllConfigService instance;
	
	private ConnectAllConfig config;
	
	private ConnectorConfigDAO configDao = new ConnectorConfigDAO();
	
	private ConnectAllConfigService() {}
	
	public static ConnectAllConfigService getInstance() {
		if(instance == null) {
			instance = new ConnectAllConfigService();
		}
		return instance;
	}
	
	public static String getDefaultId() {
		return CONNECTALL_CONNECTOR_ID;
	}
	
	public void saveConfig(ConnectAllConfig config, boolean isNew) {
		Connection conn = null;
		
		try {
			conn = PersistenceFactory.getPersistenceManager().getConnection();
			if(isNew) {
				System.out.println("Creating new config in DB : "+config);
				configDao.create(conn, config);
			} else {
				configDao.update(conn, config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		this.config = config;
	}
	
	public ConnectAllConfig getConfig(boolean reload) {
		if(reload || this.config == null) {
			System.out.println("Reloading ConnectAll config ...");
			Connection conn = null;
			try {
				conn = PersistenceFactory.getPersistenceManager().getConnection();
				this.config = new ConnectorConfigDAO().get(conn, CONNECTALL_CONNECTOR_ID);
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
		
		return this.config;
	}
	
	public boolean isConfigured() {
		if(this.config == null) {
			this.config = getConfig(true);
		}
		return this.config!=null;
	}

}
