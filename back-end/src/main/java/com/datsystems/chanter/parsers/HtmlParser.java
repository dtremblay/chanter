package com.datsystems.chanter.parsers;

/**
 * Read an HTML file and find requirements.
 * Eventually, it would be awesome if we can read images and store them in the database too, such that the document can be rendered back with a different template.
 * @author daniel
 *
 */
public class HtmlParser extends ChanterParserImpl {
	
	@Override
	public void parse(String filename) throws ChanterParserException {
		// open the file with a Document reader
		
		// as you read items, add attributes and assign text to descriptions.
		
		// As you read requirements, call push event

	}

}
