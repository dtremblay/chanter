package com.datsystems.chanter.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.Module.AttributeType;
import com.datsystems.chanter.model.RObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
		mongoClient = MongoClients.create(mongoURI);
		db = mongoClient.getDatabase(dbName);
		// Load the MongoDB modules
		loadModules();
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
	public Module createModule(Module module) throws ChanterException {
		// Check for duplicate names
		for (Module m: modules) {
			if (m.getName().equalsIgnoreCase(module.getName())) {
				throw new ChanterException("Module name '" + module.getName() + "' already exists!");
			}
		}
		if (db != null) {
			logger.info("Creating Mongo Collection {}", module.getName());
			db.createCollection(module.getName());
			persistModuleProperties(module);
		}
		modules.add(module);
		return module;
	}

	// For each collection in the Mongo database, read the modules
	private void loadModules() {
		// Find all collections and load them into modules
		if (db != null) {
			for (String name : db.listCollectionNames()) {
				// Each collection must contain a Properties document
				// Each collection contains baseline documents
				Module m = readModuleFromDb(name);
				if (m != null) {
					modules.add(modules.size(), m);
				}
			}
		}
	}
	private Module readModuleFromDb(String name) {
		Module m = null;
		MongoCollection<Document> coll = db.getCollection(name);
		if (coll != null) {
			Document props = coll.find(new Document("name", "Properties")).first();
			if (props != null) {
				m = new Module(name, props.getString("description"));
				m.setGuid(props.get("_id").toString());
				// Now read the attributes
				Document attrDoc = (Document) props.get("attributes");
				if (attrDoc != null) {
					for (Entry<String, Object> entry: attrDoc.entrySet()) {
						m.addAttribute(entry.getKey(), AttributeType.valueOf((String)entry.getValue()));
					}
				}
			}
		}
		return m;
	}
	
	// Convert a Module object to Document
	private Document convertModuleToDocument(Module m) {
		Document props = new Document("name", "Properties").append("description", m.getDescription());
		if (m.getGuid() != null) {
			props.append("_id", new ObjectId(m.getGuid()));
		}
		Document attrDoc = new Document();
		 
		for (Entry<String, AttributeType> entry: m.getAttributes().entrySet()) {
			attrDoc.append(entry.getKey(), entry.getValue().toString());
		}
		props.append("attributes", attrDoc);
		return props;
	}
	
	// Persist the properties of a module
	private void persistModuleProperties(Module m) {
		if (db != null) {
			MongoCollection<Document> collection = db.getCollection(m.getName());
			if (collection  != null) {
				Document props = convertModuleToDocument(m);
				if (props.get("_id") != null) {
					collection.replaceOne(new Document("_id", new ObjectId(m.getGuid())), props);
				} else {
					collection.insertOne(props);
					m.setGuid(props.get("_id").toString());
				}
			}
		}
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
	public void saveAttribute(@PathParam("name") String moduleName, @PathParam("attName") String attName, @PathParam("attType") String type) {
		Module m = getModuleByName(moduleName);
		if (m != null) {
			AttributeType attrType = AttributeType.valueOf(type);
			m.addAttribute(attName, attrType);
			persistModuleProperties(m);
		}
	}

	@DELETE
	@Path("{name}/attributes/{attName}")
	public void deleteAttribute(@PathParam("name") String moduleName, @PathParam("attName") String attName) {
		Module m = getModuleByName(moduleName);
		if (m != null) {
			m.deleteAttribute(attName);
			persistModuleProperties(m);
		}
	}

	public void closeMongoDatabase() {
		mongoClient.close();
		db = null;
	}
}
