package com.tvd12.ezyfoxserver.asm;

import java.util.Map;

import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.reflect.EzyTypes;

public class EzyInstruction {
	
	protected final String end;
	protected final boolean semicolon;
	protected final StringBuilder builder = new StringBuilder();
	
	@SuppressWarnings("rawtypes")
	protected static final Map<Class, Class> PRIMITIVE_WRAPPER_TYPES = 
			EzyTypes.PRIMITIVE_WRAPPER_TYPES_MAP;
	
	public EzyInstruction() {
		this("");
	}
	
	public EzyInstruction(String begin) {
		this(begin, "");
	}
	
	public EzyInstruction(String begin, String end) {
		this(begin, end, true);
	}
	
	public EzyInstruction(String begin, String end, boolean semicolon) {
		this.end = end;
		this.builder.append(begin);
		this.semicolon = semicolon;
	}
	
	public EzyInstruction equal() {
		builder.append(" = ");
		return this;
	}
	
	public EzyInstruction finish() {
		builder.append(";");
		return this;
	} 
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction constructor(Class clazz) {
		return clazz(clazz).append("()");
	}
	
	public EzyInstruction append(Object value) {
		builder.append(value.toString());
		return this;
	}
	
	public EzyInstruction append(String str) {
		builder.append(str);
		return this;
	}
	
	public EzyInstruction string(String str) {
		builder.append("\"").append(str).append("\"");
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction clazz(Class clazz) {
		return clazz(clazz, false);
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction clazz(Class clazz, boolean extension) {
		append(clazz.getTypeName());
		return extension ? append(".class") : this;
	}
	
	public EzyInstruction dot() {
		return append(".");
	}
	
	public EzyInstruction comma() {
		return append(", ");
	}
	
	public EzyInstruction bracketopen() {
		return append("(");
	}
	
	public EzyInstruction bracketclose() {
		return append(")");
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction brackets(Class clazz) {
		builder.append("(").append(clazz.getTypeName()).append(")");
		return this;
	}
	
	public EzyInstruction brackets(String expression) {
		builder.append("(").append(expression).append(")");
		return this;
	}
	
	public EzyInstruction answer() {
		return append("return ");
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction variable(Class type) {
		return variable(type, EzyClasses.getVariableName(type));
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction variable(Class type, String name) {
		builder.append(type.getTypeName()).append(" ").append(name);
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction cast(Class type, String expression) {
		builder
			.append("(")
				.append("(").append(type.getTypeName()).append(")")
				.append("(").append(expression).append(")")
			.append(")");
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public EzyInstruction valueOf(Class type, String expression) {
		if(PRIMITIVE_WRAPPER_TYPES.containsKey(type))
			return clazz(PRIMITIVE_WRAPPER_TYPES.get(type)).append(".valueOf").brackets(expression);
		return append(expression);
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean autoFinish) {
		String string = builder.toString();
		if(autoFinish && semicolon)
			string = string.endsWith(";") ? string : string + ";";
		return string + end;
	}
}