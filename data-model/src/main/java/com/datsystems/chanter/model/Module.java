package com.datsystems.chanter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  
  public enum AttributeType {
    STRING, NUMBER, LIST
  }
  private String guid;
  private String name;
  private String description;
  private List<RObject> rObjects;
  private List<Baseline> baselines;
  private Map<String, AttributeType> attributeTypes;
  
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
    this.attributeTypes = new HashMap<>();
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
  public void addBaseline(Baseline b) {
    baselines.add(b);
    for (RObject r: rObjects) {
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
  
  public void addAttribute(String name, AttributeType type) {
    attributeTypes.put(name, type);
  }
  public RObject addRequirement(RObject r) {
    RObject newR = new RObject(r);
    for (String key :attributeTypes.keySet()) {
      newR.setAttribute(key, "");
    }
    getrObjects().add(newR);
    return newR;
  }
}
