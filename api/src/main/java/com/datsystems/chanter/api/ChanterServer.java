package com.datsystems.chanter.api;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;

public interface ChanterServer {

    /**
     * Retrieves all modules from the database.
     * Modules are returned in summary form.
     * @return
     */
	List<ModuleSummary> getModules();

	/**
	 * Retrieve a given module by name.
	 * @param name
	 * @return
	 */
	Module findModuleByName(String name);

	/**
	 * Retrieve all requirements that are not deleted for a given baseline, in a module.
	 * @param moduleName
	 * @param baselineName
	 * @return
	 */
	List<RObject> getRequirementsForBaseline(
			String moduleName, 
			String baselineName);

	/**
	 * Retrieve a requirement by ID for a given module.
	 * @param name
	 * @param rid
	 * @return
	 */
	RObject getRequirementByIdForModule(String name, String rid);

	/**
	 * Create a new module.
	 * @param module
	 * @return
	 * @throws ChanterException
	 */
	Module createModule(Module module) throws ChanterException;

	/** 
	 * Delete a module by name.
	 * @param name
	 * @return
	 * @throws ChanterException
	 */
	Module deleteModule(String name) throws ChanterException;

	/**
	 * Add a new requirement in a module.
	 * @param name
	 * @param r
	 * @return
	 */
	RObject createRequirementInModule(String name, RObject r);

	/**
	 * Create a new baseline in a module.
	 * @param modName
	 * @param blName
	 * @param description
	 * @return
	 */
	Baseline createBaseline(String modName, String blName, String description);
	
	/**
	 * Update a requirement in a given module.
	 * @param name
	 * @param r
	 * @return
	 * @throws ChanterException
	 */
	RObject updateRequirementInModule(String name, RObject r) throws ChanterException;

	/**
	 * Save attributes for a module.
	 * @param moduleName
	 * @param attName
	 * @param attType
	 * @param attDefaultValue
	 */
	void saveAttribute(String moduleName, String attName,
			String attType, String attDefaultValue);

	/**
	 * Delete an attribute from a module.
	 * @param moduleName
	 * @param attName
	 */
	void deleteAttribute(String moduleName, String attName);

	/**
	 * Import a file of a given type, and create a new module.
	 * @param fileType
	 * @param moduleName
	 * @param filename
	 * @return
	 */
	ModuleSummary importFile(String fileType, String moduleName, byte[] filename) 
			throws ParserConfigurationException, ChanterParserException;	

}