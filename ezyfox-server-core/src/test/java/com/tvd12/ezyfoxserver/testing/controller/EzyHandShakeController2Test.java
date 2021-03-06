package com.tvd12.ezyfoxserver.testing.controller;

import static org.testng.Assert.assertEquals;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeRequest;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;

public class EzyHandShakeController2Test extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession first = getSessionManager(ctx).borrowSession(EzyConnectionType.SOCKET);
        System.err.println("first.token:    " + first);
        System.err.println("alive sessions: " + getSessionManager(ctx).getAliveSessions());
        assertEquals(getSessionManager(ctx).containsSession(first.getReconnectToken()), true);
        EzySession session = getSessionManager(ctx).borrowSession(EzyConnectionType.SOCKET);
        System.err.println("session: " + session);
        EzyArray data = newHandShakeData(first.getReconnectToken());
        EzyHandShakeParams requestParams = mapDataToRequestParams(data);
        EzyHandShakeRequest request = EzySimpleHandShakeRequest.builder()
                .params(requestParams)
                .session(session)
                .build();
        assertEquals(first.getReconnectToken(), requestParams.getReconnectToken());
        EzyHandShakeController controller = new EzyHandShakeController();
        controller.handle(ctx, request);
    }
    
    private EzyArray newHandShakeData(String reconnectToken) {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
                .append("adroid#1")
                .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
                .append(reconnectToken)
                .append("android")
                .append("1.0.0")
                .build();
    }
    
    protected EzySession newSession(String reconnectToken) {
        KeyPair keyPair = newRSAKeys();
        EzySession session = super.newSession();
        session.setReconnectToken(reconnectToken);
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }
    
}
