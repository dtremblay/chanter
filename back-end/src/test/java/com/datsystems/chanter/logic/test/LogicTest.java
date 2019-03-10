package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datsystems.chanter.logic.ChanterApplication;
import com.datsystems.chanter.logic.ChanterException;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.Module.AttributeType;
import com.datsystems.chanter.model.RObject;

public class LogicTest {

	public static final ChanterApplication app = new ChanterApplication();
	public static final String TEST_MODNAME = "mod-name1";

	@Before
	public void setup() {

	}

	@After
	public void teardown() {

	}

	@Test
	public void testCreateModules() throws ChanterException {
		Module newModule = app.createModule(new Module(TEST_MODNAME, "mod-description"));
		assertNotNull(newModule.getName());
		assertNotNull(newModule.getDescription());

		RObject req = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		assertNotNull(req.getGuid());
		assertNotNull(req.getText());
		app.deleteModule(TEST_MODNAME);
	}

	@Test
	public void testCreateMultiversionModule() throws ChanterException {
		Module newModule = app.createModule(new Module(TEST_MODNAME, "mod-description"));
		RObject req1 = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		RObject req2 = app.createRequirementInModule(newModule.getName(), new RObject("requirement2"));

		Baseline b = app.createBaseline(newModule.getName(), "SRR");
		assertEquals(2, b.getRObjects().size());

		// Now, update one of the requirements
		RObject updatedReq = new RObject(req1);
		updatedReq.setText("updated requirement");
		app.updateRequirementInModule(newModule.getName(), updatedReq);
		// the module now should contain 3 objects
		assertEquals(3, newModule.getrObjects().size());

		// The baseline still contains only two requirements
		Baseline b2 = app.createBaseline(newModule.getName(), "PDR");
		assertEquals(2, b2.getRObjects().size());
		app.deleteModule(TEST_MODNAME);
	}

	@Test
	public void testMongoOperations() throws ChanterException {
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module m = new Module("_test", "_description");
		Module mu = app.createModule(m);
		app.saveAttribute(mu.getName(), "_att", AttributeType.STRING.toString());
		app.closeMongoDatabase();
		app.deleteModule("_test");
		
		// Verify that our module exists - if we don't close before, then the objects will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName("_test");
		Map<String, AttributeType> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));

		app.deleteModule(mu.getName());
		app.closeMongoDatabase();
	}
}
