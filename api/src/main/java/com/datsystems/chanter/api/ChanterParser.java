package com.datsystems.chanter.api;

import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;

/**
 * All file parsers that generate requirements must implement this interface.
 * @author daniel
 *
 */
public interface ChanterParser {
	/**
	 * The parser must return it's type.
	 * @return
	 */
	String getType();
	
	/**
	 * Initiate reading a file of the appropriate type.
	 * @param filename
	 * @return
	 */
	ModuleSummary parse(String filename) throws ChanterParserException;

	void registerListener(ChanterParseEventListener listener);
	
	void pushEvent(String event, RObject r);
}
