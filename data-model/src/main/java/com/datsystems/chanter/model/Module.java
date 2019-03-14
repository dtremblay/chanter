package com.datsystems.chanter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A system may contain multiple modules. One module would be the Requirements,
 * another the Tests. Each module will contain multiple RObjects. RObjects may
 * be linked to one another.
 * 
 * @author daniel
 *
 */
public class Module {

	private String guid;
	private String name;
	private String description;
	private Date createdDate;
	private String createdBy;
	
	private List<RObject> rObjects;
	private List<Baseline> baselines;
	private Map<String, Attribute> attributes;
	private Baseline currentBaseline;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 * @param description
	 */
	public Module(String name, String description) {
		this.name = name;
		this.description = description;
		this.rObjects = new ArrayList<>();
		this.baselines = new ArrayList<>();
		currentBaseline = new Baseline("current");
		this.baselines.add(0, currentBaseline);
		this.attributes = new HashMap<>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Add a baseline to the module. Assign all current non-deleted requirements to
	 * the baseline.
	 * 
	 * @param b
	 */
	public void addBaseline(Baseline b) {
		for (String rid : currentBaseline.getReqIds()) {
			b.addReqId(rid);
		}
		b.setLocked(true);
		baselines.add(b);
	}

	public List<RObject> getrObjects() {
		return rObjects;
	}

	public List<Baseline> getBaselines() {
		return baselines;
	}

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * Add an attribute to the module. Duplicates are not handled.
	 * 
	 * @param name
	 * @param type
	 */
	public void addAttribute(String name, Attribute.AttributeType type, String defaultValue) {
		attributes.put(name, new Attribute(name, type, defaultValue));
	}

	/**
	 * Delete an attribute from the module
	 * 
	 * @param name
	 */
	public void deleteAttribute(String name) {
		if (attributes.containsKey(name)) {
			attributes.remove(name);
		}
	}

	public RObject addRequirement(RObject r) {
		RObject newR = new RObject(r);
		// Whenever a new requirement is added, copy its attributes
		for (String key : attributes.keySet()) {
			if (newR.getAttributes() != null && newR.getAttributes().containsKey(key)) {
				newR.setAttribute(key, newR.getAttributes().get(key));
			} else {
				newR.setAttribute(key, "");
			}
		}
		rObjects.add(newR);
		currentBaseline.reqIds.add(newR.getGuid());
		return newR;
	}

	public String getGuid() {
		return guid;
	}
	public void setGuid(String value) {
		guid = value;
	}

	public RObject getRObjectById(String reqId) {
		for (RObject r: rObjects) {
			if (r.getGuid().equals(reqId)) {
				return r;
			}
		}
		return null;
	}
}
