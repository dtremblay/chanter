package com.datsystems.chanter.parsers;

import com.datsystems.chanter.model.RObject;

/**
 * All file parsers that generate requirements must implement this interface.
 * @author daniel
 *
 */
public interface ChanterParser {
	/**
	 * Initiate reading a file of the appropriate type.
	 * @param filename
	 * @return
	 */
	void parse(String filename) throws ChanterParserException;

	void registerListener(ChanterParseEventListener listener);
	
	void pushEvent(String event, RObject r);
}
