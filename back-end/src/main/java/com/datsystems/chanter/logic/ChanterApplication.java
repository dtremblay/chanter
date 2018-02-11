package com.datsystems.chanter.logic;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

/**
 * Current concept is that the data is held in memory.
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
        if (r.getGuid().equals(rid)) {
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
      RObject newReq = new RObject(r.getText());
      m.getrObjects().add(newReq);
      return newReq;
    }
    throw new WebApplicationException(Response.Status.NOT_FOUND);
  }
}