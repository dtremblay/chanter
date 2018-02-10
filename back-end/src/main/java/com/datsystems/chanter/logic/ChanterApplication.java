package com.datsystems.chanter.logic;

import java.util.ArrayList;
import java.util.List;

import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

/**
 * Current concept is that the data is held in memory.
 * @author daniel
 *
 */
public class ChanterApplication {
  // The data is held in memory for now.
  List<Module> modules = new ArrayList<>();
  
  public List<Module> getModules() {
    return modules;
  }
  
  public Module getModuleById(String id) {
    for (Module m : modules) {
      if (m.getGuid().equals(id)) {
        return m;
      }
    }
    return null;
  }
  public void createModule(String name, String description) {
    Module m = new Module(name, description);
    modules.add(m);
  }
  
  public List<RObject> getRequirementForModule(String id) {
    Module m = getModuleById(id);
    if (m != null) {
      return m.getrObjects();
    }
    return null;
  }
  
  public void createRequirementInModule(String id, String text) {
    Module m = getModuleById(id);
    if (m != null) {
      RObject r = new RObject(text);
      m.getrObjects().add(r);
    }
  }
}
