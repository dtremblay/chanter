package com.datsystems.chanter.model;

import java.util.ArrayList;
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

	public enum AttributeType {
		STRING, NUMBER, LIST, BOOLEAN, ENUM
	}

	private String guid;
	private String name;
	private String description;
	private List<RObject> rObjects;
	private List<Baseline> baselines;
	private Map<String, AttributeType> attributeTypes;

	/**
	 * We need a default constructor to allow serialization and deserialization to
	 * work.
	 */
	private Module() {
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
		this.attributeTypes = new HashMap<>();
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

	/**
	 * Add a baseline to the module. Assign all current non-deleted requirements to
	 * the baseline.
	 * 
	 * @param b
	 */
	public void addBaseline(Baseline b) {
		baselines.add(b);
		for (RObject r : rObjects) {
			if (!r.getDeleted()) {
				b.rObjects.add(r);
			}
		}
	}

	public List<RObject> getrObjects() {
		return rObjects;
	}

	public List<Baseline> getBaselines() {
		return baselines;
	}

	public Map<String, AttributeType> getAttributes() {
		return attributeTypes;
	}

	/**
	 * Add an attribute to the module. Duplicates are not handled.
	 * 
	 * @param name
	 * @param type
	 */
	public void addAttribute(String name, AttributeType type) {
		attributeTypes.put(name, type);
	}

	/**
	 * Delete an attribute from the module
	 * 
	 * @param name
	 */
	public void deleteAttribute(String name) {
		if (attributeTypes.containsKey(name)) {
			attributeTypes.remove(name);
		}
	}

	public RObject addRequirement(RObject r) {
		RObject newR = new RObject(r);
		// Whenever a new requirement is added, copy its attributes
		for (String key : attributeTypes.keySet()) {
			if (newR.getAttributes() != null && newR.getAttributes().containsKey(key)) {
				newR.setAttribute(key, newR.getAttributes().get(key));
			} else {
				newR.setAttribute(key, "");
			}
		}
		getrObjects().add(newR);
		return newR;
	}

	public String getGuid() {
		return guid;
	}
	public void setGuid(String value) {
		guid = value;
	}
}
