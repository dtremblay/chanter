package com.datsystems.chanter.logic.parsers;

import java.util.ArrayList;
import java.util.List;

import com.datsystems.chanter.model.RObject;

/** 
 * This class abstract the registration of listeners and publication of events
 * @author daniel
 *
 */
abstract class ChanterParserImpl implements ChanterParser {
	
	// Not very likely to have multiple listeners, but just in case...
	List<ChanterParseEventListener> listeners = new ArrayList<>();

	public void registerListener(ChanterParseEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Publish an event to all listeners registered.
	 * @param event
	 * @param r
	 */
	public void pushEvent(String event, RObject r) {
		for (ChanterParseEventListener listener : listeners) {
			listener.pushEvent(event, r);
		}
	}

}
