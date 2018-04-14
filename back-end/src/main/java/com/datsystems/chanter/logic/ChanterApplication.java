package com.datsystems.chanter.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.Module.AttributeType;
import com.datsystems.chanter.model.RObject;

/**
 * Current concept is that the data is held in memory until we develop the database connector.
 * @author daniel
 *
 */
@Path("modules")
@ApplicationScoped
@Produces("application/json")
public class ChanterApplication {
  // The data is held in memory for now.
  List<Module> modules = new ArrayList<>();
  
  @GET
  public List<Module> getModules() {
    return modules;
  }
  
  @GET
  @Path("{id}")
  public Module getModuleById(@PathParam("id") String id) {
    for (Module m : modules) {
      if (m.getGuid().equals(id)) {
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
    return newModule;
  }
  
  @GET
  @Path("{id}/requirements")
  public List<RObject> getRequirementsForModule(@PathParam("id") String id) {
    Module m = getModuleById(id);
    if (m != null) {
      return m.getrObjects();
    }
    throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
  
  @GET
  @Path("{id}/requirements/{rid}")
  public RObject getRequirementByIdForModule(@PathParam("id") String id, @PathParam("rid") String rid) {
    Module m = getModuleById(id);
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
  @Path("{id}")
  public RObject createRequirementInModule(@PathParam("id") String id, RObject r) {
    Module m = getModuleById(id);
    if (m != null) {
      return m.addRequirement(r);
    }
    throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
  
  @POST
  @Consumes("application/json")
  @Path("{id}/baselines")
  public Baseline createBaseline(@PathParam("id") String id, String description) {
    Module m = getModuleById(id);
    if (m != null) {
      Baseline b = new Baseline(description);
      m.addBaseline(b);
      return b;
    }
    throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
  
  @PUT
  @Consumes("application/json")
  @Path("{id}/requirements")
  public RObject updateRequirementInModule(@PathParam("id") String id, RObject r) {
    Module m = getModuleById(id);
    if (m != null) {
      RObject oldR = getRequirementByIdForModule(id, r.getGuid());
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
  @Path("{id}/attributes")
  public Map<String, AttributeType> getAttributes(@PathParam("id") String id) {
    Module m = getModuleById(id);
    return m.getAttributes();
  }
  @POST
  @Path("{id}/attributes/{attName}")
  public void saveAttribute(@PathParam("id") String id, @PathParam("attName") String attName, String type) {
    Module m = getModuleById(id);
    if (m != null) {
      AttributeType attrType = AttributeType.valueOf(type);
      m.addAttribute(attName, attrType);
    }
  }
  
  @DELETE
  @Path("{id}/attributes/{attName}")
  public void deleteAttribute(@PathParam("id") String id, @PathParam("attName") String attName) {
    Module m = getModuleById(id);
    if (m != null) {
      Map<String, AttributeType> attrs = m.getAttributes();
      if (attrs.containsKey(attName)) {
        attrs.remove(attName);
      } else {
        throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
    }
  }
}
