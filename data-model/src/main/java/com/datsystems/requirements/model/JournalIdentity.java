package com.datsystems.requirements.model;

import java.io.Serializable;

import javax.persistence.Id;

public class JournalIdentity implements Serializable {
  /**
   * Auto-generated UID
   */
  private static final long serialVersionUID = 8220233858114154519L;
  
  private String guid;
  private int version;

  public JournalIdentity() {
    
  }
  public JournalIdentity(String guid, int version) {
    this.guid = guid;
    this.version = version;
  }
  @Id
  public int getVersion() {
    return version;
  }
  public void setVersion(int version) {
    this.version = version;
  }
  
  @Id
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public static JournalIdentity key(String guid, int version) {
    JournalIdentity ji = new JournalIdentity(guid, version);
    return ji;
  }
  public boolean equals(Object other) {
    if (other == this)
      return true;
    if (!(other instanceof JournalIdentity))
      return false;

    JournalIdentity ji = (JournalIdentity) other;
    return getGuid().equals(ji.getGuid()) && getVersion() == ji.getVersion();
  }
  
  public int hashCode() {
    return (getGuid().hashCode() ^ getVersion());
  } 
}
