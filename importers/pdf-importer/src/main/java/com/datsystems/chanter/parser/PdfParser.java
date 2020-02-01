package com.datsystems.chanter.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.chanter.api.ChanterParseEventListener;
import com.datsystems.chanter.api.ChanterParser;
import com.datsystems.chanter.api.ChanterParserException;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;

public class PdfParser implements ChanterParser {
	private static final Logger logger = LoggerFactory.getLogger(PdfParser.class.getName());
	final String type = "PDF";
	@Override
	public String getType() {
		return type;
	}
	@Override
	public ModuleSummary parse(byte[] filename) throws ChanterParserException {
		logger.info("Starting the import of a PDF file");
		// open the file with PdfReader
		
		// as you read items, add attributes and assign text to descriptions.
		
		// As you read requirements, call push event
		return null;
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
