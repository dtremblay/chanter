package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datsystems.chanter.logic.ChanterApplication;
import com.datsystems.chanter.logic.ChanterException;
import com.datsystems.chanter.model.Attribute;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

/**
 * These tests require a running MongoDB instance.
 * 
 * @author daniel
 *
 */
public class MongoTest {

	public static ChanterApplication app;
	public static final String MOD_NAME = "_test";

	@Before
	public void setup() {
		app = new ChanterApplication();
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
	}

	@After
	public void teardown() {
		app.closeMongoDatabase();
	}

	@Test
	public void testMongoModulesWithAttributes() throws ChanterException {
		// Ensure the module is clean
		app.deleteModule(MOD_NAME);
		Module m = new Module(MOD_NAME, "_description");
		Module mu = app.createModule(m);

		// Add attributes
		app.saveAttribute(mu.getName(), "_att", Attribute.AttributeType.STRING.toString(), "_default");
		// The order of these commands matter.
		// Here, we close the Mongo database and flush the _test module from memory.
		// This way, the Mongo database is not deleted.
		app.closeMongoDatabase();
		app.deleteModule(MOD_NAME);

		// Verify that our module exists - if we don't close before, then the objects
		// will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName(MOD_NAME);
		Map<String, Attribute> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));
	}

	@Test
	public void testMongoRequirementPersistence() throws ChanterException {
		// Ensure the module is clean
		app.deleteModule(MOD_NAME);
		Module m = new Module(MOD_NAME, "_description");
		Module mu = app.createModule(m);

		// Add attributes and requirements
		app.saveAttribute(mu.getName(), "_att", Attribute.AttributeType.STRING.toString(), "_default");
		createFakeRequirements(10, mu.getName(), mu.getAttributes());

		// The order of these commands matter.
		// Here, we close the Mongo database and flush the _test module from memory.
		// This way, the Mongo database is not deleted.
		app.closeMongoDatabase();
		app.deleteModule(MOD_NAME);

		// Verify that our module exists - if we don't close before, then the objects
		// will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName("_test");
		Map<String, Attribute> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));

		List<RObject> reqs = app.getRequirementsForModule(mu.getName());
		assertEquals(10, reqs.size());
	}

	@Test
	public void testMongoBaslines() throws ChanterException {
		// Ensure the module is clean
		app.deleteModule(MOD_NAME);
		Module m = new Module(MOD_NAME, "_description");
		Module mu = app.createModule(m);

		// Add attributes and requirements
		app.saveAttribute(mu.getName(), "_att", Attribute.AttributeType.STRING.toString(), "_default");
		createFakeRequirements(10, mu.getName(), mu.getAttributes());
		app.createBaseline(mu.getName(), "_newbaseline", "This baseline should contain 10 requirements all in version 1");
		createFakeRequirements(5, mu.getName(), mu.getAttributes());

		// The order of these commands matter.
		// Here, we close the Mongo database and flush the _test module from memory.
		// This way, the Mongo database is not deleted.
		app.closeMongoDatabase();
		app.deleteModule(MOD_NAME);

		// Verify that our module exists - if we don't close before, then the objects
		// will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName("_test");
		Map<String, Attribute> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));

		List<RObject> reqs = app.getRequirementsForModule(mu.getName());
		assertEquals(15, reqs.size());
		
		assertEquals(2, rm.getBaselines().size());
		assertEquals(10, rm.getBaselineByName("_newbaseline").getReqIds().size());
		assertEquals(15, rm.getBaselineByName("current").getReqIds().size());
	}

	private void createFakeRequirements(int count, String moduleName, Map<String, Attribute> attributes) {
		for (int i = 0; i < count; i++) {
			RObject r = new RObject("fake_" + i);
			for (Entry<String, Attribute> entry : attributes.entrySet()) {
				r.setAttribute(entry.getKey(),
						i % 2 == 0 ? entry.getValue().getDefaultValue() : UUID.randomUUID().toString());
			}
			app.createRequirementInModule(moduleName, r);
		}
	}
}
