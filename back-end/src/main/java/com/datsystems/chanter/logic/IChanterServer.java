package com.datsystems.chanter.logic;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("modules")
@ApplicationScoped
@Produces("application/json")
public interface IChanterServer {

    @GET
    List<Module> getModules();

    @GET
	@Path("{name}")
    Module getModuleByName(@PathParam("name") String name);

    @POST
	@Consumes("application/json")
	Module createModule(Module module) throws ChanterException;

    @DELETE
	@Path("{name}")
	Module deleteModule(@PathParam("name") String name);

    @GET
	@Path("{name}/requirements")
	List<RObject> getRequirementsForModule(@PathParam("name") String name);

    @GET
	@Path("{name}/requirements/{rid}")
	RObject getRequirementByIdForModule(@PathParam("name") String name, @PathParam("rid") String rid);

    @POST
	@Consumes("application/json")
	@Path("{name}")
	RObject createRequirementInModule(@PathParam("name") String name, RObject r);

    @POST
	@Consumes("application/json")
	@Path("{name}/baselines")
	Baseline createBaseline(@PathParam("name") String modName, String blName, String description);
	
    @PUT
	@Consumes("application/json")
	@Path("{name}/requirements")
	RObject updateRequirementInModule(@PathParam("name") String name, RObject r);

	@POST
	@Path("{name}/attributes")
	void saveAttribute(@PathParam("name") String moduleName, @FormParam("attName") String attName,
			@FormParam("attType") String attType, @FormParam("attDefaultValue") String attDefaultValue);

    @DELETE
	@Path("{name}/attributes/{attName}")
	void deleteAttribute(@PathParam("name") String moduleName, @PathParam("attName") String attName);

	@POST
	@Path("{name}/import/html")
	Module importFromHtml(@PathParam("name") String moduleName, @PathParam("filename") String filename);

    @POST
	@Path("{name}/import/pdf")
	Module importFromPdf(@PathParam("name") String moduleName, @PathParam("filename") String filename);


}