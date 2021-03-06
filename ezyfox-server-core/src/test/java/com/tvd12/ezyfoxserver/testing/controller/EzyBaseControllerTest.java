package com.tvd12.ezyfoxserver.testing.controller;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyRequestMappers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyRequestMappersImpl;

public abstract class EzyBaseControllerTest extends BaseCoreTest {

    protected EzyRequestMappers requestMappers = 
            EzyRequestMappersImpl.builder().build();
    
    protected static final String RECONNECT_TOKEN_SALT = "$1$reconnectToken";
    
    protected <T> T mapDataToRequestParams(EzyData data) {
        return requestMappers.toObject(getCommand(), data);
    }
    
    protected EzyServer getServer(EzyServerContext ctx) {
        return ctx.getServer();
    }
    
    protected EzyManagers getManagers(EzyServerContext ctx) {
        return getServer(ctx).getManagers();
    }
    
    protected EzyServerUserManager getUserManager(EzyServerContext ctx) {
        return getManagers(ctx).getManager(EzyServerUserManager.class);
    }
    
    protected MyTestSessionManager getSessionManager(EzyServerContext ctx) {
        return (MyTestSessionManager) getManagers(ctx).getManager(EzySessionManager.class);
    }
    
    protected EzyAppContext getAppContext(EzyServerContext ctx, int appId) {
        return ctx.getAppContext(appId);
    }
    
    protected EzyAppContext getAppContext(EzyServerContext ctx, String appName) {
        return ctx.getAppContext(appName);
    }
    
    protected EzyServerControllers getControllers(EzyServerContext ctx) {
        return getServer(ctx).getControllers();
    }
    
    @SuppressWarnings("rawtypes")
    protected EzyController getController(EzyServerContext ctx, EzyConstant cmd) {
        return getServer(ctx).getControllers().getController(cmd);
    }
    
    protected abstract EzyConstant getCommand();
    
}
