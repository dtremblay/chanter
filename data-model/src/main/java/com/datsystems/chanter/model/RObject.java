package com.datsystems.chanter.model;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * RObject have a guid, version, text.
 * RObject are immutable. So we shouldn't be able to modify them.
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
  private Properties attributes;
  
  /**
   * Disable the empty constructor
   */
  public RObject() {}
  
  /**
   * Simple constructor with only text supplied
   */
  public RObject(String text) {
    this.guid = UUID.randomUUID().toString();
    this.version = 1;
    this.created = new Date();
    this.updated = new Date();
    this.text = text;
    this.attributes = new Properties();
  }
  
  /**
   * Allow copying an existing requirement
   * @return
   */
  public RObject(RObject r) {
    this.guid = r.getGuid();
    this.version = r.getVersion() + 1;
    this.text = r.getText();
    this.created = r.getCreated();
    this.updated = new Date();
    this.attributes = r.getAttributes();
    this.type = r.getType();
  }
  public String getGuid() {
    return guid;
  }
  public int getVersion() {
    return version;
  }
  public Boolean getDeleted() {
    return deleted;
  }
  public Date getCreated() {
    return created;
  }
  public Date getUpdated() {
    return updated;
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
  public Properties getAttributes() {
    return attributes;
  }

  
}
