package com.tvd12.ezyfoxserver.hazelcast.impl;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezyfoxserver.hazelcast.bean.EzyServicesImplementor;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyHazelcastMapService;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleSerivcesImplementor
		extends EzyLoggable
		implements EzyServicesImplementor {

	protected Set<Class<?>> autoImplInterfaces = new HashSet<>();
	protected Map<String, Class<?>> autoImplInterfaceMap = new HashMap<>();
	
	public EzyServicesImplementor scan(String packageName) {
		autoImplInterfaces.addAll(getAutoImplRepoInterfaces(packageName));
		return this;
	}
	
	public EzyServicesImplementor scan(String... packageNames) {
		return scan(Sets.newHashSet(packageNames));
	}
	
	public EzyServicesImplementor scan(Iterable<String> packageNames) {
		packageNames.forEach(this::scan);
		return this;	
	}
	
	@Override
	public EzyServicesImplementor serviceInterface(String mapName, Class<?> itf) {
		autoImplInterfaceMap.put(mapName, itf);
		return this;
	}
	
	@Override
	public Map<Class<?>, Object> implement(HazelcastInstance hzInstance) {
		Map<Class<?>, Object> repositories = new ConcurrentHashMap<>();
		for(Class<?> itf : autoImplInterfaces) {
			Object repo = newRepoImplementor(itf).implement(hzInstance);
			repositories.put(itf, repo);
		}
		for(String mapName : autoImplInterfaceMap.keySet()) {
			Class<?> itf = autoImplInterfaceMap.get(mapName);
			Object repo = newRepoImplementor(itf).implement(hzInstance, mapName);
			repositories.put(itf, repo);
		}
		return repositories;
	}
	
	private EzySimpleServiceImplementor newRepoImplementor(Class<?> itf) {
		return new EzySimpleServiceImplementor(new EzyClass(itf));
	}
	
	private Set<Class<?>> getRepoInterfaces(String packageName) {
		return EzyPackages.getExtendsClasses(packageName, EzyHazelcastMapService.class);
	}
	
	private Collection<Class<?>> getAutoImplRepoInterfaces(String packageName) {
		Set<Class<?>> classes = getRepoInterfaces(packageName);
		return EzyLists.filter(classes, this::isAutoImplRepoInterface);
	}
	
	private boolean isAutoImplRepoInterface(Class<?> clazz) {
		return  (clazz.isAnnotationPresent(EzyAutoImpl.class) || 
				 clazz.isAnnotationPresent(EzyMapServiceAutoImpl.class)) &&
				Modifier.isPublic(clazz.getModifiers()) &&
				Modifier.isInterface(clazz.getModifiers());
				
	}
	
}
