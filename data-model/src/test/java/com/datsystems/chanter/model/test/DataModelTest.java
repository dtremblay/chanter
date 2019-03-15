package com.datsystems.chanter.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.datsystems.chanter.model.Attribute;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

public class DataModelTest {

  @Test
  public void testCreateRequirement() {
    RObject r1 = new RObject("test");
    assertEquals(1, r1.getVersion());
    
    // Copy the requirement, check version is updated
    RObject r2 = r1.uprev();
    assertEquals(2, r2.getVersion());
    assertEquals("test", r2.getName());
  }
  
  @Test
  public void testCreateModule() {
    Module m1 = new Module("my module", "module description");
    m1.addAttribute("category", Attribute.AttributeType.STRING, null);
    RObject r = new RObject("test requirement");
    r.setGuid(UUID.randomUUID().toString());
    r.setAttribute("category", "my category");
    
    r = m1.addRequirement(r);
    m1.addBaseline(new Baseline("baseline 1"));
    
    // Check that all our properties and attributes are present
    List<RObject> rChecks = m1.getrObjects();
    // Find our object
    boolean found = false;
    for (RObject rc : rChecks) {
      if (rc.getGuid().equals(r.getGuid())) {
        found = true;
        Map<String, String> props = rc.getAttributes();
        String category = props.get("category");
        assertEquals("my category", category);
      }
    }
    assertTrue(found);
  }
}
