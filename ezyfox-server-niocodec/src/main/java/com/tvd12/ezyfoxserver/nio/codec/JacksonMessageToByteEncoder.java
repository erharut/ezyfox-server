package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.codec.EzyMessageByTypeSerializer;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsObjectToByteEncoder;

public class JacksonMessageToByteEncoder implements EzyWsObjectToByteEncoder {

	protected final Logger logger;
	protected final EzyMessageByTypeSerializer serializer;
	
	public JacksonMessageToByteEncoder(EzyMessageByTypeSerializer serializer) {
		this.serializer = serializer;
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	public ByteBuffer encode(Object msg) throws Exception {
		byte[] bytes = serializer.serialize(msg);
		return ByteBuffer.wrap(bytes);
	}
	
	@Override
	public <T> T encode(Object msg, Class<T> outType) throws Exception {
		return serializer.serialize(msg, outType);
	}
	
}
