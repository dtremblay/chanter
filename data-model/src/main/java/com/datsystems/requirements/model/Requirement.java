package com.datsystems.requirements.model;

import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.PrePersist;

@Entity
@IdClass(JournalIdentity.class)
public class Requirement {

  // The GUID and version are not visible to users
  String guid;
  int version;
  boolean deleted = false;
  Date createdOn;
  Date updatedOn;

  // The id is a user assigned value
  String id;
  String type;
  String text;
  Properties options;

  @Id
  @Column(length=36, updatable=false)
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }

  @Id
  @Column(updatable=false)
  public int getVersion() {
    return version;
  }
  public void setVersion(int version) {
    this.version = version;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  @Lob
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  @ElementCollection
  @CollectionTable(
        name="Requirement_Options",
        joinColumns=  {
          @JoinColumn(name="req-guid", referencedColumnName="guid"), 
          @JoinColumn(name="req-version",referencedColumnName="version")
        }
  )
  public Properties getOptions() {
    return options;
  }
  public void setOptions(Properties options) {
    this.options = options;
  }
  public boolean isDeleted() {
    return deleted;
  }
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
  
  public Date getCreatedOn() {
    return createdOn;
  }
  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }
  public Date getUpdatedOn() {
    return updatedOn;
  }
  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }
  @PrePersist
  public void prePersist() {
    if (version == 0) {
      setGuid(UUID.randomUUID().toString());
      setVersion(1);
      Date now = new Date();
      setCreatedOn(now);
      setUpdatedOn(now);
    }
  }
  
  public Requirement copy() {
    Requirement req = new Requirement();
    req.setGuid(this.getGuid());
    req.setId(this.getId());
    req.setVersion(this.getVersion()+1);
    req.setType(this.getType());
    req.setText(this.getText());
    req.setCreatedOn(this.getCreatedOn());
    req.setUpdatedOn(new Date());
    if (this.getOptions() != null) {
      Properties options = new Properties();
      Set<String> keys = this.getOptions().stringPropertyNames();
      for (String key: keys) {
        options.setProperty(key, this.getOptions().getProperty(key));
      }
      req.setOptions(options);
    }
    return req;
  }
}
