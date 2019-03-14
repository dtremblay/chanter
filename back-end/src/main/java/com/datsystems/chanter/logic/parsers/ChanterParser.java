package com.datsystems.chanter.logic.parsers;

import java.util.ArrayList;
import java.util.List;

import com.datsystems.chanter.model.RObject;

/**
 * All file parsers that generate requirements must implement this interface.
 * @author daniel
 *
 */
public interface ChanterParser {
	// Not very likely to have multiple listeners, but just in case...
	List<ChanterParseEventListener> listeners = new ArrayList<>();
	
	/**
	 * Initiate reading a file of the appropriate type.
	 * @param filename
	 * @return
	 */
	void parse(String filename) throws ChanterParserException;

	void registerListener(ChanterParseEventListener listener);
	
	void pushEvent(String event, RObject r);
}
