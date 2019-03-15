package com.datsystems.chanter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Baselines have a guid, name and description. A Baseline will contain is a one
 * to many relationship to requirements Keeping this very plain, no getter
 * setter, who cares.
 * 
 * @author daniel
 *
 */
public class Baseline {
	private String guid;
	private String name;
	String description;
	Date created;
	Boolean locked = false;
	List<String> reqIds;

	public Baseline(String name) {
		this.name = name;
		reqIds = new ArrayList<>();
	}

	public List<String> getReqIds() {
		return reqIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public void setLocked(boolean value) {
		locked = value;
	}

	public void addReqId(String id) {
		reqIds.add(id);
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String value) {
		guid = value;
	}
}
