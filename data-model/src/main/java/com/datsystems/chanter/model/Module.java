package com.datsystems.chanter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A system may contain multiple modules.
 * One module would be the Requirements, another the Tests.
 * Each module will contain multiple RObjects.
 * RObjects may be linked to one another.
 * 
 * @author daniel
 *
 */
public class Module {
  private String guid;
  private String name;
  private String description;
  private List<RObject> rObjects;
  private List<Baseline> baselines;
  
  /**
   * We need a default constructor to allow serialization and deserialization to work.
   */
  public Module() {}
  
  /**
   * Default constructor.
   * @param name
   * @param description
   */
  public Module(String name, String description) {
    guid = UUID.randomUUID().toString();
    this.name = name;
    this.description = description;
    this.rObjects = new ArrayList<>();
    this.baselines = new ArrayList<>();
  }

  public String getGuid() {
    return guid;
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

  public List<RObject> getrObjects() {
    return rObjects;
  }

  public List<Baseline> getBaselines() {
    return baselines;
  }
}
