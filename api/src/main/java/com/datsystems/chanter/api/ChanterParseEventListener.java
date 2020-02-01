package com.datsystems.chanter.api;

import com.datsystems.chanter.model.RObject;

public interface ChanterParseEventListener {
	void pushEvent(String event, RObject r);
}
