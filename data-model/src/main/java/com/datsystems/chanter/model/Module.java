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

	private Module() {
		baselines = new ArrayList<>();
		attributes = new HashMap<>();
		rObjects = new ArrayList<>();
		createdDate = new Date();
	}
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
		baselines.add(b);
		if (b.getName().equalsIgnoreCase("current")) {
			currentBaseline = b;
		}
	}
	
	public Baseline createBaseline(String name, String description) {
		Baseline bl = new Baseline(name);
		bl.setDescription(description);
		bl.setLocked(true);
		for (String rid : getCurrentBaseline().getReqIds()) {
			bl.addReqId(rid);
		}
		addBaseline(bl);
		return bl;
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
		// Go through all requirements and add the attribute, with the default value
		for (RObject r: rObjects) {
			r.getAttributes().put(name, defaultValue);
		}
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
		// Whenever a new requirement is added, copy its attributes
		for (String key : attributes.keySet()) {
			if (r.getAttributes() != null && r.getAttributes().containsKey(key)) {
				r.setAttribute(key, r.getAttributes().get(key));
			} else {
				r.setAttribute(key, getAttributes().get(key).getDefaultValue());
			}
		}
		rObjects.add(r);
		return r;
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

	public Baseline getCurrentBaseline() {
		if (currentBaseline == null) {
			currentBaseline = new Baseline("current");
			this.baselines.add(0, currentBaseline);
		}
		return currentBaseline;
	}
	public Baseline getBaselineByName(String value) {
		for (Baseline bl: baselines) {
			if (bl.getName().equals(value)) {
				return bl;
			}
		}
		return null;
	}
	
}
