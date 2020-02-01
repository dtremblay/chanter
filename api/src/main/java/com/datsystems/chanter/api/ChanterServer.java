package com.datsystems.chanter.api;

import java.util.List;

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;

public interface ChanterServer {

    // Return the summary for all modules
    //List<ModuleSummary> getModules();
	Response getModules();

	Response getModuleByName(String name);

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