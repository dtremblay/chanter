package com.datsystems.chanter.logic.parsers;

import com.datsystems.chanter.model.RObject;

public interface ChanterParseEventListener {
	void pushEvent(String event, RObject r);
}
