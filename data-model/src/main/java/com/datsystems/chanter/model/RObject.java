package com.datsystems.chanter.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * RObject have a guid, version, text. RObject are immutable. So we shouldn't be
 * able to modify them.
 * 
 * @author daniel
 *
 */
public class RObject {

	// The GUID and version are not visible to users
	private String guid;
	private int version;
	private Boolean deleted = false;
	private Date created;
	private Date updated;

	private String type;
	private String text;
	private String name;
	private Map<String, String> attributes;

	// JPA requires a default constructor.
	public RObject() {
	}

	/**
	 * Simple constructor with only text supplied
	 */
	public RObject(String name) {
		this.version = 1;
		this.created = new Date();
		this.updated = new Date();
		this.name = name;
		this.attributes = new HashMap<>();
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean value) {
		this.deleted = value;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getType() {
		return type;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> map) {
		attributes = map;
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Update an existing requirement
	 * 
	 * @return
	 */
	public RObject uprev() {
		RObject r = new RObject();
		r.setCreated(this.getCreated());
		r.setUpdated(new Date());
		r.setVersion(this.getVersion() + 1);
		r.setText(this.getText());
		r.setName(this.getName());

		if (this.getAttributes() != null) {
			r.setAttributes(this.getAttributes());
		} else {
			r.attributes = new HashMap<>();
		}
		r.type = this.getType();
		return r;
	}

}
