package com.datsystems.chanter.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.chanter.api.IChanterServer;
import com.datsystems.chanter.model.Attribute;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.BaselineSummary;
import com.datsystems.chanter.model.summary.ModuleSummary;
import com.datsystems.chanter.parsers.ChanterParser;
import com.datsystems.chanter.parsers.ChanterParserException;
import com.datsystems.chanter.parsers.HtmlParser;
import com.datsystems.chanter.parsers.PdfParser;
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

@Path("/chanter")
@Produces("application/json")
@Component(configurationPid= "chanter-backend", 
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate=true,
	property = { "osgi.jaxrs.resource=true", "mongoUri=mongoUri", "databaseName=databaseName" })
public class ChanterApplication implements IChanterServer {
	// New logger
	private static final Logger logger = LoggerFactory.getLogger(ChanterApplication.class.getName());
	private MongoClient mongoClient  = null;
	// We have a single database for Chanter
	MongoDatabase db = null;
	String dbName = "chanter";
	
	private final String CREATED_ON = "created-on";
	private final String UPDATED_ON = "updated-on";

	// The data is held in memory for now.
	// Each module is a separate collection
	List<Module> modules = new ArrayList<>();

	public ChanterApplication() {
		// Load modules from database
		try {
			String mongoURI = System.getProperty("mongo");
			String alternateDatabaseName = System.getProperty("dbName");

			if (mongoURI != null && !mongoURI.isEmpty()) {
				
				if (alternateDatabaseName != null && !alternateDatabaseName.isEmpty()) {
					dbName = alternateDatabaseName;
				}
				setMongoUri(mongoURI);
				setDatabaseName(dbName);
			} else {
				logger.error("Mongo URI not provided.  Data will not be persisted");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Activate
	@Modified
    void activate(Map<String, Object> properties) {
		logger.info("Activation Properties Called");
		String mongoUri = (String) properties.get("mongoUri");
		if (mongoUri != null && mongoUri.length() > 0 ) {
			setMongoUri(mongoUri);
		}
		String dbName = (String) properties.get("databaseName");
		if (dbName != null && dbName.length()>0) {
			setDatabaseName(dbName);
		}
	}
	
	/**
	 * Force a connection to the Mongo database
	 * 
	 * @param mongoURI
	 * @param dbName
	 */
	public void setMongoUri(String mongoURI) {
		logger.info("Setting Mongo URI to {}", mongoURI);
		mongoClient = MongoClients.create(mongoURI);
		
		if (dbName != null) {
			db = mongoClient.getDatabase(dbName);
			// Load the MongoDB modules
			loadModules();
		}
	}

	public void setDatabaseName(String dbName) {
		logger.info("Setting database name to {}", dbName);
		
		if (mongoClient != null && !this.dbName.equals(dbName)) {
			this.dbName = dbName;
			db = mongoClient.getDatabase(dbName);
			// Load the MongoDB modules
			loadModules();
		}
	}
	
	@GET
	public Response getModules() {
		List<ModuleSummary> summaries = new ArrayList<>();
		modules.forEach(m -> {
			ModuleSummary summary = new ModuleSummary();
			summary.setGuid(m.getGuid());
			summary.setName(m.getName());
			summaries.add(summary);
			summary.setAttributes(m.getAttributes());
			
			List<BaselineSummary> baselines = new ArrayList<>();
			m.getBaselines().forEach(b -> {
				BaselineSummary baseline = new BaselineSummary();
				baseline.setGuid(b.getGuid());
				baseline.setName(b.getName());
				baseline.setReqCount(b.getReqIds().size());
				
				baselines.add(baseline);
			});
			summary.setBaselines(baselines);
		});
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(summaries).build();
	}

	@GET
	@Path("{moduleName}")
	public Response getModuleByName(@PathParam("moduleName") String name) {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(findModuleByName(name)).build();
	}
	public Module findModuleByName(String name) {
		for (Module m : modules) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

    @POST
	@Consumes("application/json")
    public Module createModule(Module module) throws ChanterException {
		// Check for duplicate names
		for (Module m : modules) {
			if (m.getName().equalsIgnoreCase(module.getName())) {
				throw new ChanterException("Module name '" + module.getName() + "' already exists!");
			}
		}
		
		if (db != null) {
			logger.info("Creating Mongo Collection {}", module.getName());
			db.createCollection(module.getName());
			persistModuleProperties(module);
		} else {
			module.setGuid(UUID.randomUUID().toString());
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

	class ModuleClosure {
		Module module;

		public ModuleClosure(Module value) {
			this.module = value;
		}
	}

	// Reading a module may take a long time, so we may have to use threads here.
	private Module readModuleFromDb(String name) {
		Module m = null;
		MongoCollection<Document> coll = db.getCollection(name);
		if (coll != null) {
			Document props = coll.find(new Document("type", "Properties")).first();
			if (props != null) {
				m = new Module(name, props.getString("description"));
				m.setGuid(props.get("_id").toString());

				// Now read the attributes
				Document attrDoc = (Document) props.get("attributes");
				if (attrDoc != null) {
					for (Entry<String, Object> entry : attrDoc.entrySet()) {
						Document doc = (Document) entry.getValue();
						m.addAttribute(entry.getKey(), Attribute.AttributeType.valueOf(doc.getString("type")),
								doc.getString("default"));
					}
				}
			}
			// Create a closure for the module - otherwise we cannot addRequirements to it.
			final ModuleClosure modClosure = new ModuleClosure(m);

			// Find all requirements stored in the database
			coll.find(new Document("type", "Requirement")).forEach((Consumer<Document>) reqDoc -> {
				RObject r = new RObject();
				r.setCreated(reqDoc.getDate(CREATED_ON));
				r.setUpdated(reqDoc.getDate(UPDATED_ON));
				r.setLastModifiedBy(reqDoc.getString("last-modified-by"));
				r.setGuid(reqDoc.get("_id").toString());
				r.setName(reqDoc.getString("name"));
				r.setVersion(reqDoc.getInteger("version", 1));
				r.setDeleted(reqDoc.getBoolean("deleted"));
				r.setText(reqDoc.getString("description"));
				for (Entry<String, Attribute> entry : modClosure.module.getAttributes().entrySet()) {
					String value = reqDoc.getString(entry.getKey());
					if (value != null) {
						r.getAttributes().put(entry.getKey(), value);
					} else {
						r.getAttributes().put(entry.getKey(), entry.getValue().getDefaultValue());
					}
				}
				modClosure.module.addRequirement(r);

			});
			// Now match the requirements for the baselines
			coll.find(new Document("type", "Baseline")).forEach((Consumer<Document>) blDoc -> {
				Baseline b = new Baseline(blDoc.getString("name"));
				b.setGuid(blDoc.get("_id").toString());
				String reqIdsStr = blDoc.getString("ids");

				if (reqIdsStr != null && !reqIdsStr.isEmpty()) {
					// Split the requirement ids
					String[] reqIds = reqIdsStr.split(",");
					for (String reqId : reqIds) {
						b.addReqId(reqId);
					}

					modClosure.module.addBaseline(b);
				}
			});
		}
		return m;
	}

	// Convert a Module object to Document
	private Document convertModuleToDocument(Module m) {
		Document props = new Document("type", "Properties")
				.append("description", m.getDescription());
		if (m.getGuid() != null) {
			props.append("_id", new ObjectId(m.getGuid()));
		}
		// Save the attributes
		Document attrDoc = new Document();
		for (Entry<String, Attribute> entry : m.getAttributes().entrySet()) {
			Attribute attr = entry.getValue();
			Document subDoc = new Document()
					.append("name", entry.getKey())
					.append("type", attr.getType().toString())
					.append("default", attr.getDefaultValue());
			attrDoc.append(entry.getKey(), subDoc);
		}
		props.append("attributes", attrDoc);
		return props;
	}

	// Persist the properties of a module
	private void persistModuleProperties(Module m) {
		if (db != null) {
			MongoCollection<Document> collection = db.getCollection(m.getName());
			if (collection != null) {
				Document props = convertModuleToDocument(m);
				if (props.get("_id") != null) {
					collection.replaceOne(new Document("_id", new ObjectId(m.getGuid())), props);
				} else {
					collection.insertOne(props);
					m.setGuid(props.get("_id").toString());
				}
				// Save the baselines
				for (Baseline bl : m.getBaselines()) {
					persistBaseline(collection, bl);

				}
			}
		}
	}

	// Add a requirement to the current baseline of a module
	private void persistModuleRequirement(Module m, RObject r) {
		if (db != null) {
			MongoCollection<Document> collection = db.getCollection(m.getName());
			if (collection != null) {
				Document requirement = new Document("type", "Requirement")
						.append("name", r.getName())
						.append(CREATED_ON, r.getCreated())
						.append("version", r.getVersion())
						.append("deleted", r.getDeleted())
						.append("description", r.getText());
				
				if (r.getUpdated() != null) {
					requirement.append(UPDATED_ON, r.getUpdated());
				}
				for (Entry<String, Attribute> entry : m.getAttributes().entrySet()) {
					String attValue = r.getAttributes().get(entry.getKey());
					requirement.append(entry.getKey(),
							attValue != null ? attValue : entry.getValue().getDefaultValue());
				}
				// Update the old requirement
				if (r.getVersion() > 1) {
					Document filter = new Document("_id", new ObjectId(r.getGuid()));
					Document  doc = new Document("deleted", true)
							.append(UPDATED_ON, r.getUpdated());
					Document setDoc = new Document("$set", doc);
					
					collection.updateOne(filter, setDoc);
				}
				collection.insertOne(requirement);
				
				r.setGuid(requirement.get("_id").toString());
			}
		} else {
			r.setGuid(UUID.randomUUID().toString());
		}
	}

	private void persistBaseline(MongoCollection<Document> collection, Baseline bl) {
		if (collection != null) {
			Document blDoc = new Document().append("type", "Baseline").append("name", bl.getName());
			StringBuilder builder = new StringBuilder();
			for (String id: bl.getReqIds()) {
				builder.append(id).append(",");
			}
			
			blDoc.append("ids", builder.toString());
			if (bl.getGuid() != null) {
				collection.replaceOne(new Document("_id", new ObjectId(bl.getGuid())), blDoc);
			} else {
				collection.insertOne(blDoc);
				bl.setGuid(blDoc.get("_id").toString());
			}
		} else {
			bl.setGuid(UUID.randomUUID().toString());
		}
	}

    @DELETE
	@Path("{moduleName}")
	public Module deleteModule(@PathParam("moduleName") String name) throws ChanterException {
		Module m = findModuleByName(name);
		if (m != null) {
			if (db != null) {
				MongoCollection<Document> col = db.getCollection(m.getName());
				col.drop();
			}
			modules.remove(m);
		} else {
			throw new ChanterException("Module name '" + name + "' not found. Delete Module operation will not proceed.");
		}
		
		return m;
	}

    @GET
	@Path("{moduleName}/baselines/{baselineName}")
    public List<RObject> getRequirementsForBaseline(
			@PathParam("moduleName") String moduleName, 
			@PathParam("baselineName") String baselineName) {
		Module m = findModuleByName(moduleName);
		if (m != null) {
			Baseline b = m.getBaselineByName(baselineName);
			List<RObject> objects = new ArrayList<>();
			b.getReqIds().forEach(rid -> {
				RObject object = m.getRObjectById(rid);
				objects.add(object);
			});
			return objects;
		}
		return null;
	}

    @GET
	@Path("{name}/requirements/{rid}")
	public RObject getRequirementByIdForModule(
			@PathParam("name") String name, 
			@PathParam("rid") String rid) {
		Module m = findModuleByName(name);
		if (m != null) {
			for (RObject r : m.getrObjects()) {
				if (r.getGuid().equals(rid) && !r.getDeleted()) {
					return r;
				}
			}
		}
		return null;
	}

    @POST
	@Consumes("application/json")
	@Path("{name}")
	public RObject createRequirementInModule(@PathParam("name") String name, RObject r) {
		Module m = findModuleByName(name);
		if (m != null) {
			logger.info("Adding Requirement {} to module {}", r.getName(), m.getName());
			m.addRequirement(r);
			persistModuleRequirement(m, r);

			// Update the baseline
			Baseline bl = m.getCurrentBaseline();
			bl.addReqId(r.getGuid());
			MongoCollection<Document> collection = null;
			if (db != null) {
				collection = db.getCollection(m.getName());
			}
			persistBaseline(collection, bl);

			return r;
		}
		return null;
	}

    @POST
	@Consumes("application/json")
	@Path("{name}/baselines")
	public Baseline createBaseline(@PathParam("name") String modName, String blName, String description) {
		Module m = findModuleByName(modName);
		if (m != null) {
			Baseline bl = m.createBaseline(blName, description);
			if (db != null) {
				MongoCollection<Document> collection = db.getCollection(modName);
				persistBaseline(collection, bl);
			}
			return bl;
		}
		return null;
	}

    @PATCH
	@Consumes("application/json")
	@Path("{name}/requirements")
	public RObject updateRequirementInModule(@PathParam("name") String name, RObject r) throws ChanterException {
		Module m = findModuleByName(name);
		if (m != null) {
			RObject oldR = getRequirementByIdForModule(name, r.getGuid());
			// Ensure the old requirement exist and is the current version
			if (oldR != null && !oldR.getDeleted()) {
				// Create a new requirement from the old one
				RObject newR = oldR.uprev();
				newR.setGuid(oldR.getGuid());
				oldR.setDeleted(true);
				oldR.setUpdated(new Date());
				m.getCurrentBaseline().getReqIds().remove(oldR.getGuid());
				// Copy all attributes for the new object into the new instance
				if (r.getName() != null) {
					newR.setName(r.getName());
				}
				if (r.getText() != null) {
					newR.setText(r.getText());
				}
				m.getrObjects().add(newR);
				persistModuleRequirement(m, newR);
				m.getCurrentBaseline().getReqIds().add(newR.getGuid());
				MongoCollection<Document> collection = null;
				if (db != null) {
					collection = db.getCollection(m.getName());
				}
				persistBaseline(collection, m.getCurrentBaseline());
				return newR;
			} else {
				throw new ChanterException("Requirement with id '" + r.getGuid() + "' not found. Update Requirement operation will not proceed.");
			}
		} else {
			throw new ChanterException("Module name '" + name + "' not found. Delete Module operation will not proceed.");
		}
	}

	@GET
	@Path("{name}/attributes")
	public Map<String, Attribute> getAttributes(@PathParam("name") String name) {
		Module m = findModuleByName(name);
		return m.getAttributes();
	}

    @PUT
	@Consumes("application/json")
	@Path("{name}/attributes")
	public void saveAttribute(@PathParam("name") String moduleName, @FormParam("attName") String attName,
			@FormParam("attType") String attType, @FormParam("attDefaultValue") String attDefaultValue) {
		Module m = findModuleByName(moduleName);
		if (m != null) {
			logger.info("Adding attribute {}", attName);
			Attribute.AttributeType attrType = Attribute.AttributeType.valueOf(attType);
			m.addAttribute(attName, attrType, attDefaultValue);
			persistModuleProperties(m);
		}
	}

    @DELETE
	@Path("{name}/attributes/{attName}")
	public void deleteAttribute(@PathParam("name") String moduleName, @PathParam("attName") String attName) {
		Module m = findModuleByName(moduleName);
		if (m != null) {
			m.deleteAttribute(attName);
			persistModuleProperties(m);
		}
	}

	public void closeMongoDatabase() {
		mongoClient.close();
		db = null;
	}

	@POST
	@Path("{name}/import/html")
	public Module importFromHtml(@PathParam("name") String moduleName, @PathParam("filename") String filename) {
		HtmlParser parser = new HtmlParser();
		return wireParser(parser, moduleName, filename);
	}

    @POST
	@Path("{name}/import/pdf")
	public Module importFromPdf(@PathParam("name") String moduleName, @PathParam("filename") String filename) {
		PdfParser parser = new PdfParser();
		return wireParser(parser, moduleName, filename);
	}
	
	private Module wireParser(ChanterParser parser, String moduleName, String filename) {
		// parse the document into requirements
		Module imported = new Module(moduleName, "Imported module from: " + filename);
		imported.setCreatedDate(new Date());
		imported.setCreatedBy("current-user");

		try {
			parser.parse(filename);
			parser.registerListener((event, r) -> {
				logger.info("Parsing event: {}", event);
				if (r != null) {
					imported.addRequirement(r);
				}
			});
		} catch (ChanterParserException cpe) {
			logger.error(cpe.getMessage());
		}

		return imported;
	}
}
