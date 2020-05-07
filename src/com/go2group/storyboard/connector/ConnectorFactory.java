package com.go2group.storyboard.connector;

import com.go2group.storyboard.connector.connectall.ConnectALLConnector;

public class ConnectorFactory {

	public static Connector getConnector() {
		return new ConnectALLConnector();
	}
	
}
