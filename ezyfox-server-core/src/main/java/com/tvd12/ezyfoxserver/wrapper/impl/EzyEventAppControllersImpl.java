package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;

import com.tvd12.ezyfoxserver.appcontroller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.appcontroller.EzyUserDisconnectController;
import com.tvd12.ezyfoxserver.appcontroller.EzyUserReconnectController;
import com.tvd12.ezyfoxserver.appcontroller.EzyUserRequestAppController;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventAppControllersImpl extends EzyAbstractEventControllers {

	protected EzyEventAppControllersImpl(Builder builder) {
		super(builder);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEventControllers.Builder {
		
		@SuppressWarnings("rawtypes")
		@Override
		protected void addControllers(Map<EzyConstant, EzyEventController> answer) {
			super.addControllers(answer);
			answer.put(EzyEventType.SERVER_READY, new EzyServerReadyController());
			answer.put(EzyEventType.USER_REQUEST, new EzyUserRequestAppController());
			answer.put(EzyEventType.USER_RECONNECT, new EzyUserReconnectController());
			answer.put(EzyEventType.USER_DISCONNECT, new EzyUserDisconnectController());
		}
		
		@Override
		public EzyEventControllers build() {
			return new EzyEventAppControllersImpl(this);
		}
	}

}
