package com.datsystems.chanter.model.summary;

import java.util.List;
import java.util.Map;

import com.datsystems.chanter.model.Attribute;

public class ModuleSummary {
	private String id;
	private String name;
	private String description;
	
	private List<BaselineSummary> baselines;
	private Map<String, Attribute> attributes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BaselineSummary> getBaselines() {
		return baselines;
	}
	public void setBaselines(List<BaselineSummary> baselines) {
		this.baselines = baselines;
	}
	public Map<String, Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
}
