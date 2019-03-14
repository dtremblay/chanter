package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datsystems.chanter.logic.ChanterApplication;
import com.datsystems.chanter.logic.ChanterException;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

public class LogicTest {

	public static final ChanterApplication app = new ChanterApplication();
	public static final String TEST_MODNAME = "mod-name1";
	Module newModule;

	@Before
	public void setup() throws ChanterException {
		newModule = app.createModule(new Module(TEST_MODNAME, "mod-description"));
	}

	@After
	public void teardown() {
		app.deleteModule(TEST_MODNAME);
	}

	@Test
	public void testCreateModules() {
		assertNotNull(newModule.getName());
		assertNotNull(newModule.getDescription());

		RObject req = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		assertNotNull(req.getGuid());
		assertNotNull(req.getText());
	}

	@Test
	public void testCreateMultiversionModule() throws ChanterException {
		// Requirements are always added to the "current" baseline
		RObject req1 = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		app.createRequirementInModule(newModule.getName(), new RObject("requirement2"));

		// When a new baseline is created, all requirements are "copied"
		Baseline blSRR = app.createBaseline(newModule.getName(), "SRR");
		assertEquals(2, blSRR.getReqIds().size());

		// Now, update one of the requirements
		RObject updatedReq = new RObject(req1);
		updatedReq.setText("updated requirement");
		app.updateRequirementInModule(newModule.getName(), updatedReq);
		// the module now should contain 3 objects
		assertEquals(3, newModule.getrObjects().size());

		// The baseline still contains only two requirements
		Baseline b2 = app.createBaseline(newModule.getName(), "PDR");
		assertEquals(2, b2.getReqIds().size());
	}

	@Test
	public void testLoadHtml() throws ChanterException {
		Module newModule = app.importFromHtml(TEST_MODNAME, "./data/Requirements Document Example.html");
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
	
	@Test
	public void testLoadPdf() throws ChanterException {
		Module newModule = app.importFromPdf(TEST_MODNAME, "./data/Requirements Document Example.pdf");
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
}
