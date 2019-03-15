package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static final ChanterApplication app = new ChanterApplication();

	@Test
	public void testMongoOperations() throws ChanterException {
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module m = new Module("_test", "_description");
		Module mu = app.createModule(m);
		app.saveAttribute(mu.getName(), "_att", Attribute.AttributeType.STRING.toString(), "_default");
		// The order of these commands matter.
		// Here, we close the Mongo database and flush the _test module from memory.
		// This way, the Mongo database is not deleted.
		app.closeMongoDatabase();
		app.deleteModule("_test");

		// Verify that our module exists - if we don't close before, then the objects
		// will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName("_test");
		Map<String, Attribute> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));

		// Here, we are going to actually delete the Mongo database.
		app.deleteModule(mu.getName());
		app.closeMongoDatabase();
	}

	@Test
	public void testMongoRequirementPersistence() throws ChanterException {
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module m = new Module("_test", "_description");
		Module mu = app.createModule(m);
		app.saveAttribute(mu.getName(), "_att", Attribute.AttributeType.STRING.toString(), "_default");
		createFakeRequirements(10, mu.getName(), mu.getAttributes());

		// The order of these commands matter.
		// Here, we close the Mongo database and flush the _test module from memory.
		// This way, the Mongo database is not deleted.
		app.closeMongoDatabase();
		app.deleteModule("_test");

		// Verify that our module exists - if we don't close before, then the objects
		// will not be re-read
		app.setMongoDatabase("mongodb://localhost:27017", "chanter");
		Module rm = app.getModuleByName("_test");
		Map<String, Attribute> attributes = rm.getAttributes();
		assertNotNull(attributes);
		assertTrue(attributes.containsKey("_att"));
		
		List<RObject> reqs = app.getRequirementsForModule(mu.getName());
		assertEquals(10, reqs.size());

		// Here, we are going to actually delete the Mongo database.
		app.deleteModule(mu.getName());
		app.closeMongoDatabase();
	}

	private void createFakeRequirements(int count, String moduleName, Map<String, Attribute> attributes) {
		for (int i = 0; i < count; i++) {
			RObject r = new RObject("fake_" + i);
			for (Entry<String, Attribute> entry : attributes.entrySet()) {
				r.setAttribute(entry.getKey(), entry.getValue().getDefaultValue());
			}
			app.createRequirementInModule(moduleName, r);
		}
	}
}
