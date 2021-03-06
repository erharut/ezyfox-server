package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public class EzyAppsStarter extends EzyComponentsStater {

    protected Map<String, EzyAppClassLoader> appClassLoaders;
    
    protected EzyAppsStarter(Builder builder) {
        super(builder);
        this.appClassLoaders = builder.appClassLoaders;
    }
    
    @Override
    public void start() {
        startAllApps();
    }
    
    protected void startAllApps() {
        Set<String> appNames = getAppNames();
        getLogger().info("start apps: {}" + appNames);
        appNames.forEach(this::startApp);
    }
    
    protected void startApp(String appName) {
        try {
            getLogger().debug("app " + appName + " loading...");
            EzyAppContext context = serverContext.getAppContext(appName);
            EzyApplication application = context.getApp();
            EzyAppEntry entry = startApp(appName, newAppEntryLoader(appName));
            ((EzyEntryAware)application).setEntry(entry);
            getLogger().debug("app " + appName + " loaded");
        }
        catch(Exception e) {
            getLogger().error("can not start app " + appName, e);
        } 
    }
    
    protected EzyAppEntry startApp(String appName, EzyAppEntryLoader loader) throws Exception {
        EzyAppEntry entry = loader.load();
        entry.config(getAppContext(appName));
        entry.start();
        return entry;
    }
    
    @JsonIgnore
    public Set<String> getAppNames() {
        return settings.getAppNames();
    }
    
    public EzyAppSetting getAppByName(String name) {
        return settings.getAppByName(name);
    }
    
    public Class<EzyAppEntryLoader> 
            getAppEntryLoaderClass(String appName) throws Exception {
        return getAppEntryLoaderClass(getAppByName(appName));
    }
    
    @SuppressWarnings("unchecked")
    public Class<EzyAppEntryLoader> 
            getAppEntryLoaderClass(EzyAppSetting app) throws Exception {
        return (Class<EzyAppEntryLoader>) 
                Class.forName(app.getEntryLoader(), true, getClassLoader(app.getName()));
    }
    
    public EzyAppEntryLoader newAppEntryLoader(String appName) throws Exception {
         Class<EzyAppEntryLoader> entryLoaderClass = getAppEntryLoaderClass(appName);
         return entryLoaderClass.newInstance();
    }
    
    public EzyAppClassLoader getClassLoader(String appName) {
        if(appClassLoaders.containsKey(appName)) 
            return appClassLoaders.get(appName);
        throw new IllegalArgumentException("has no class loader for app " + appName);
    }
    
    protected EzyAppContext getAppContext(String appName) {
        return serverContext.getAppContext(appName);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyComponentsStater.Builder<EzyAppsStarter, Builder> {
        protected Map<String, EzyAppClassLoader> appClassLoaders;
        
        public Builder appClassLoaders(Map<String, EzyAppClassLoader> appClassLoaders) {
            this.appClassLoaders = appClassLoaders;
            return this;
        }
        
        @Override
        public EzyAppsStarter build() {
            return new EzyAppsStarter(this);
        }
    }
    
}
