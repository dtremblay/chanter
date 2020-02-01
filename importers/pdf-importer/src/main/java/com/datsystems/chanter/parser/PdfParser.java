package com.datsystems.chanter.parser;

import com.datsystems.chanter.api.ChanterParseEventListener;
import com.datsystems.chanter.api.ChanterParser;
import com.datsystems.chanter.api.ChanterParserException;
import com.datsystems.chanter.api.RObject;
import com.datsystems.chanter.parsers.ChanterParserImpl;

public class CsvParser implements ChanterParser {

	@Override
	public void parse(String filename) throws ChanterParserException {
		// open the file with PdfReader
		
		// as you read items, add attributes and assign text to descriptions.
		
		// As you read requirements, call push event

	}

	@Override
	public void registerListener(ChanterParseEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushEvent(String event, RObject r) {
		// TODO Auto-generated method stub
		
	}

}
