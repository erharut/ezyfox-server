package com.tvd12.ezyfoxserver.nio.socket;

import static com.tvd12.ezyfoxserver.util.EzyReturner.returnWithException;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyChannel;
import static com.tvd12.ezyfoxserver.util.EzyProcessor.*;

import lombok.Getter;

@Getter
public class EzySocketChannel implements EzyChannel {

	private final SocketChannel channel;
	private final SocketAddress serverAddress;
	private final SocketAddress clientAddress;
	
	public EzySocketChannel(SocketChannel channel) {
		this.channel = channel;
		this.serverAddress = returnWithException(channel::getLocalAddress);
		this.clientAddress = returnWithException(channel::getRemoteAddress);
	}
	
	@Override
	public int write(Object data) throws Exception {
		return channel.write((ByteBuffer)data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SocketChannel getConnection() {
		return channel;
	}
	
	@Override
	public EzyConnectionType getConnectionType() {
		return EzyConnectionType.SOCKET;
	}
	
	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}
	
	@Override
	public void disconnect() {
		processWithLogException(channel::finishConnect);
	}
	
	@Override
	public void close() {
		processWithLogException(channel::close);
	}
	
}
