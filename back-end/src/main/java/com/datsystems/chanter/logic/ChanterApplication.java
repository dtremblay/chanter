package com.datsystems.chanter.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.Module.AttributeType;
import com.datsystems.chanter.model.RObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Current concept is that the data is held in memory until we develop the
 * database connector.
 * 
 * In Mongo, a module corresponds to a collection. baselines are saved along the
 * collection. modules can have multiple baselines.
 * 
 * @author daniel
 *
 */
@Path("modules")
@ApplicationScoped
@Produces("application/json")
public class ChanterApplication {
	// Mew logger
	private static final Logger logger = LoggerFactory.getLogger(ChanterApplication.class.getName());
	MongoClient mongoClient;
	// We have a single database for Chanter
	MongoDatabase db;

	// The data is held in memory for now.
	// Each module is a separate collection
	List<Module> modules = new ArrayList<>();

	public ChanterApplication() {
		// Load modules from database
		try {
			String mongoURI = System.getProperty("mongo");
			String alternateDatabaseName = System.getProperty("dbName");

			if (mongoURI != null && !mongoURI.isEmpty()) {
				String dbName = "chanter";
				if (alternateDatabaseName != null && !alternateDatabaseName.isEmpty()) {
					dbName = alternateDatabaseName;
				}
				setMongoDatabase(mongoURI, dbName);
			} else {
				logger.error("Mongo URI not provided.  Data will not be persisted");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Force a connection to the Mongo database
	 * @param mongoURI
	 * @param dbName
	 */
	public void setMongoDatabase(String mongoURI, String dbName) {
		mongoClient = new MongoClient(mongoURI);
		db = mongoClient.getDatabase(dbName);
		loadModules();
	}
	
	private void loadModules() {
		// Find all collections and load them into modules
		for (String name : db.listCollectionNames()) {
			// Each document is a baseline
			Module m = new Module();
			m.setName(name);
			modules.add(modules.size(), m);
		}
	}
	
	@GET
	public List<Module> getModules() {
		return modules;
	}

	@GET
	@Path("{name}")
	public Module getModuleByName(@PathParam("name") String name) {
		for (Module m : modules) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@POST
	@Consumes("application/json")
	public Module createModule(Module m) {
		Module newModule = new Module(m.getName(), m.getDescription());
		modules.add(newModule);
		if (db != null) {
			logger.info("Creating Mongo Collection {}", m.getName());
			db.createCollection(m.getName());
		} else {
			newModule.setGuid(UUID.randomUUID().toString());
		}
		return newModule;
	}

	@DELETE
	@Path("{name}")
	public Module deleteModule(@PathParam("name") String name) {
		Module m = getModuleByName(name);
		if (m != null) {
			if (db != null) {
				MongoCollection<Document> col = db.getCollection(m.getName());
				col.drop();
			}
			modules.remove(m);
		}
		return m;
	}
	
	@GET
	@Path("{name}/requirements")
	public List<RObject> getRequirementsForModule(@PathParam("name") String name) {
		Module m = getModuleByName(name);
		if (m != null) {
			return m.getrObjects();
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@GET
	@Path("{name}/requirements/{rid}")
	public RObject getRequirementByIdForModule(@PathParam("name") String name, @PathParam("rid") String rid) {
		Module m = getModuleByName(name);
		if (m != null) {
			for (RObject r : m.getrObjects()) {
				if (r.getGuid().equals(rid) && !r.getDeleted()) {
					return r;
				}
			}
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@POST
	@Consumes("application/json")
	@Path("{name}")
	public RObject createRequirementInModule(@PathParam("name") String name, RObject r) {
		Module m = getModuleByName(name);
		if (m != null) {
			return m.addRequirement(r);
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@POST
	@Consumes("application/json")
	@Path("{name}/baselines")
	public Baseline createBaseline(@PathParam("name") String name, String description) {
		Module m = getModuleByName(name);
		if (m != null) {
			Baseline b = new Baseline(description);
			m.addBaseline(b);
			return b;
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@PUT
	@Consumes("application/json")
	@Path("{name}/requirements")
	public RObject updateRequirementInModule(@PathParam("name") String name, RObject r) {
		Module m = getModuleByName(name);
		if (m != null) {
			RObject oldR = getRequirementByIdForModule(name, r.getGuid());
			// Create a new requirement from the old one
			RObject newR = new RObject(oldR);
			oldR.setDeleted(true);
			oldR.setUpdated(new Date());
			// Copy all attributes for the new object into the new instance
			newR.setText(r.getText());
			m.getrObjects().add(newR);
			return newR;
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	@GET
	@Path("{name}/attributes")
	public Map<String, AttributeType> getAttributes(@PathParam("name") String name) {
		Module m = getModuleByName(name);
		return m.getAttributes();
	}

	@POST
	@Path("{name}/attributes/{attName}")
	public void saveAttribute(@PathParam("name") String name, @PathParam("attName") String attName, String type) {
		Module m = getModuleByName(name);
		if (m != null) {
			AttributeType attrType = AttributeType.valueOf(type);
			m.addAttribute(attName, attrType);
		}
	}

	@DELETE
	@Path("{name}/attributes/{attName}")
	public void deleteAttribute(@PathParam("name") String name, @PathParam("attName") String attName) {
		Module m = getModuleByName(name);
		if (m != null) {
			Map<String, AttributeType> attrs = m.getAttributes();
			if (attrs.containsKey(attName)) {
				attrs.remove(attName);
			} else {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
	}

	public void closeMongoDatabase() {
		mongoClient.close();
		db = null;
	}
}
