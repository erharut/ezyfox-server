package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.util.EzyNameAware;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleUser 
        extends EzyEntity 
        implements EzyUser, EzyNameAware, EzyHasUserRemoveDelegate, Serializable {
	private static final long serialVersionUID = -7846882289922504595L;
	
	protected long id = COUNTER.incrementAndGet();
	
	protected String name = "";
	
	protected String password = "";
	
	protected int maxSessions = 30;
	
	protected long maxIdleTime = 3 * 60 * 1000;
	
	protected long startIdleTime = System.currentTimeMillis();
	
	protected transient EzyUserRemoveDelegate removeDelegate;
	
	@Setter(AccessLevel.NONE)
    protected Map<String, Lock> locks = new ConcurrentHashMap<>();
	
	@Setter(AccessLevel.NONE)
	protected Map<String, EzySession> sessionMap = new ConcurrentHashMap<>();
	
	@Setter(AccessLevel.NONE)
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private transient static final AtomicLong COUNTER = new AtomicLong(0);
	
	@Override
	public void addSession(EzySession session) {
	    sessionMap.put(session.getReconnectToken(), session);
	}
	
	@Override
	public void removeSession(EzySession session) {
	    setStartIdleTime(System.currentTimeMillis());
	    sessionMap.remove(session.getReconnectToken());
	    getLogger().debug("remove session {} from user {}, remain {} sessions", 
	            session.getClientAddress(), getName(), sessionMap.size());
	}
	
	@Override
	public Collection<EzySession> getSessions() {
	    return sessionMap.values();
	}
	
	@Override
	public int getSessionCount() {
	    return sessionMap.size();
	}
	
	@Override
    public Lock getLock(String name) {
        return locks.computeIfAbsent(name, k -> new ReentrantLock());
    }
	
	@Override
	public void send(EzyData data, EzyTransportType type) {
	    for(EzySession session : getSessions())
	        session.send(data, type);
	}
	
	@Override
	public void destroy() {
	    this.removeDelegate = null;
	    this.properties.clear();
	}
	
	@Override
    public boolean equals(Object obj) {
	    return new EzyEquals<EzySimpleUser>()
	            .function(c -> c.id)
	            .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(id).toHashCode();
    }
    
    protected Logger getLogger() {
        return logger;
    }
	
}
