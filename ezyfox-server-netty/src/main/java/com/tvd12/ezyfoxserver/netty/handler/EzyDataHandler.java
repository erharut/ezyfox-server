package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyNettySessionManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;

public class EzyDataHandler extends EzySimpleDataHandler<EzyNettySession> {

	public void channelActive(ChannelHandlerContext ctx, EzyConnectionType type) throws Exception {
		getLogger().debug("channel actived, connection type = {}", type);
		borrowSession(ctx, type);
		handleSessionActive(ctx);
	}
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().debug("channel {} inactive", ctx.channel().remoteAddress());
		channelInactive();
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	dataReceived((EzyArray)msg);
    }
	
	public void bytesReceived(int count) {
		session.addReadBytes(count);
	}

	public void bytesSent(int count) {
		session.addWrittenBytes(count);
	}

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
    	Throwable cause = getCauseException(throwable);
    	exceptionCaught(cause);
    }
    
    private Throwable getCauseException(Throwable throwable) {
    	if(throwable instanceof DecoderException)
    		return throwable.getCause();
    	return throwable;
    }
    
	private void borrowSession(ChannelHandlerContext ctx, EzyConnectionType type) {
		borrowSession(() -> newSession(ctx, type));
	}
	
	private EzyNettySession newSession(ChannelHandlerContext ctx, EzyConnectionType type) {
		return getNettySessionManager().borrowSession(ctx.channel(), type);
	}
	
	private void handleSessionActive(ChannelHandlerContext ctx) {
		updateSession(ctx);
		sessionActive();
	}
	
	private void updateSession(ChannelHandlerContext ctx) {
		session.setProperty(ChannelHandlerContext.class, ctx);
	}

    private EzyNettySessionManager getNettySessionManager() {
    	return (EzyNettySessionManager)sessionManager;
    }
    
}
