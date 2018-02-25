package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.datsystems.chanter.logic.ChanterApplication;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

public class LogicTest {
  
  public static ChanterApplication app = new ChanterApplication();
  
  @Before
  public void setup() {
    
  }
  @Test
  public void testCreateModules() {
    Module newModule = app.createModule(new Module("mod-name1", "mod-description"));
    assertNotNull(newModule.getName());
    assertNotNull(newModule.getDescription());
    assertNotNull(newModule.getGuid());
    
    RObject req = app.createRequirementInModule(newModule.getGuid(), new RObject("requirement1"));
    assertNotNull(req.getGuid());
    assertNotNull(req.getText());
  }
  
  @Test
  public void testCreateMultiversionModule() {
    Module newModule =  app.createModule(new Module("mod-name1", "mod-description"));
    RObject req1 = app.createRequirementInModule(newModule.getGuid(), new RObject("requirement1"));
    RObject req2 = app.createRequirementInModule(newModule.getGuid(), new RObject("requirement2"));
    
    Baseline b = app.createBaseline(newModule.getGuid(), "SRR");
    assertEquals(2, b.getRObjects().size());
    
    // Now, update one of the requirements
    RObject updatedReq = new RObject(req1);
    updatedReq.setText("updated requirement");
    app.updateRequirementInModule(newModule.getGuid(), updatedReq);
    // the module now should contain 3 objects
    assertEquals(3, newModule.getrObjects().size());
    
    // The baseline still contains only two requirements
    Baseline b2 = app.createBaseline(newModule.getGuid(), "PDR");
    assertEquals(2, b.getRObjects().size());
  }
}
