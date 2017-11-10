package com.tvd12.ezyfoxserver.nio.entity;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public interface EzyChannel {

	void close();
	
	void disconnect();
	
	boolean isConnected();
	
	int write(Object data) throws Exception;
	
	<T> T getConnection();
	
	EzyConnectionType getConnectionType();
	
	SocketAddress getServerAddress();
	
	SocketAddress getClientAddress();
	
}