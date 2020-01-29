package com.datsystems.chanter.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.datsystems.chanter.implementation.ChanterException;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;
import com.datsystems.chanter.parsers.ChanterParserException;

public interface IChanterServer {

    // Return the summary for all modules
    //List<ModuleSummary> getModules();
	Response getModules();

	Response getModuleByName(@PathParam("name") String name);

	List<RObject> getRequirementsForBaseline(
			@PathParam("moduleName") String moduleName, 
			@PathParam("baselineName") String baselineName);

	RObject getRequirementByIdForModule(@PathParam("name") String name, @PathParam("rid") String rid);

	Module createModule(Module module) throws ChanterException;

	Module deleteModule(@PathParam("name") String name) throws ChanterException;

	RObject createRequirementInModule(@PathParam("name") String name, RObject r);

	Baseline createBaseline(@PathParam("name") String modName, String blName, String description);
	
	RObject updateRequirementInModule(@PathParam("name") String name, RObject r) throws ChanterException;

	void saveAttribute(@PathParam("name") String moduleName, @FormParam("attName") String attName,
			@FormParam("attType") String attType, @FormParam("attDefaultValue") String attDefaultValue);

	void deleteAttribute(@PathParam("name") String moduleName, @PathParam("attName") String attName);

	Module importFromHtml(@PathParam("name") String moduleName, @PathParam("filename") String filename);

	Module importFromPdf(@PathParam("name") String moduleName, @PathParam("filename") String filename);


}