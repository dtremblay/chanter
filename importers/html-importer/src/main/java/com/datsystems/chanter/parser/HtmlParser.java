package com.datsystems.chanter.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.chanter.api.ChanterParseEventListener;
import com.datsystems.chanter.api.ChanterParser;
import com.datsystems.chanter.api.ChanterParserException;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;


/**
 * Read an HTML file and find requirements.
 * Eventually, it would be awesome if we can read images and store them in the database too, such that the document can be rendered back with a different template.
 * @author daniel
 *
 */
public class HtmlParser implements ChanterParser {
	private static final Logger logger = LoggerFactory.getLogger(HtmlParser.class.getName());
	final String type = "HTML";
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public ModuleSummary parse(byte[] filename) throws ChanterParserException {
		logger.info("Starting the import of a HTML file");
		// open the file with a Document reader
		
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
