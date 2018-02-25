package com.datsystems.chanter.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.Module.AttributeType;
import com.datsystems.chanter.model.RObject;

public class DataModelTest {

  @Test
  public void testCreateRequirement() {
    RObject r1 = new RObject("test");
    assertEquals(1, r1.getVersion());
    
    // Copy the requirement, check version is updated
    RObject r2 = new RObject(r1);
    assertEquals(2, r2.getVersion());
    assertEquals("test", r2.getText());
  }
  
  @Test
  public void testCreateModule() {
    Module m1 = new Module("my module", "module description");
    m1.addAttribute("category", AttributeType.STRING);
    RObject r = new RObject("test requirement");
    r = m1.addRequirement(r);
    r.setAttribute("category", "my category");
    m1.addBaseline(new Baseline("baseline 1"));
  }
}
