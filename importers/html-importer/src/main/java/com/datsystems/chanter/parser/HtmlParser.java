package com.datsystems.chanter.parser;

import com.datsystems.chanter.api.ChanterParseEventListener;
import com.datsystems.chanter.api.ChanterParser;
import com.datsystems.chanter.api.ChanterParserException;


/**
 * Read an HTML file and find requirements.
 * Eventually, it would be awesome if we can read images and store them in the database too, such that the document can be rendered back with a different template.
 * @author daniel
 *
 */
public class HtmlParser implements ChanterParser {
	
	@Override
	public void parse(String filename) throws ChanterParserException {
		// open the file with a Document reader
		
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
