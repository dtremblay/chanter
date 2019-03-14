package com.datsystems.chanter.model;

public class Attribute {
	
	private String name;
	private String defaultValue;
	
	public enum AttributeType {
		STRING, NUMBER, LIST, BOOLEAN, ENUM
	}
	private AttributeType type;
	
	public Attribute(String name, AttributeType type, String defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public AttributeType getType() {
		return type;
	}
	public void setType(AttributeType type) {
		this.type = type;
	}
}
