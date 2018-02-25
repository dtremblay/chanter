package com.datsystems.chanter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * Baselines have a guid, name and description.
 * A Baseline will contain is a one to many relationship to requirements
 * Keeping this very plain, no getter setter, who cares.
 * 
 * @author daniel
 *
 */
public class Baseline {
  String guid;
  String name;
  String description;
  Date created;
  Boolean locked = false;
  List<RObject> rObjects;
  Properties attributes;
  
  public Baseline(String description) {
    this.description = description;
    rObjects = new ArrayList<>();
  }
  
  public List<RObject> getRObjects() {
    return rObjects;
  }
}
