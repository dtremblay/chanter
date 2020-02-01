package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datsystems.chanter.api.ChanterException;
import com.datsystems.chanter.api.ChanterParserException;
import com.datsystems.chanter.implementation.ChanterApplication;
import com.datsystems.chanter.model.Baseline;
import com.datsystems.chanter.model.Module;
import com.datsystems.chanter.model.RObject;
import com.datsystems.chanter.model.summary.ModuleSummary;
import com.datsystems.chanter.parser.CsvParser;
import com.datsystems.chanter.parser.HtmlParser;
import com.datsystems.chanter.parser.PdfParser;

public class LogicTest {

	public static final ChanterApplication app = new ChanterApplication();
	public static final String TEST_MODNAME = "mod-name1";
	Module newModule;

	@Before
	public void setup() throws ChanterException {
		newModule = app.createModule(new Module(TEST_MODNAME, "mod-description"));
	}

	@After
	public void teardown() throws ChanterException {
		app.deleteModule(TEST_MODNAME);
	}

	@Test
	public void testCreateModules() {
		assertNotNull(newModule.getName());
		assertNotNull(newModule.getDescription());

		RObject req = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		assertNotNull(req.getGuid());
		assertNotNull(req.getName());
	}

	@Test
	public void testCreateMultiversionModule() throws ChanterException {
		// Requirements are always added to the "current" baseline
		RObject req1 = app.createRequirementInModule(newModule.getName(), new RObject("requirement1"));
		app.createRequirementInModule(newModule.getName(), new RObject("requirement2"));

		// When a new baseline is created, all requirements are "copied"
		Baseline blSRR = app.createBaseline(newModule.getName(), "SRR", "System Requirement Review");
		assertEquals(2, blSRR.getReqIds().size());

		// Now, update one of the requirements
		RObject updatedReq = req1;
		updatedReq.setText("updated requirement");
		app.updateRequirementInModule(newModule.getName(), updatedReq);
		// the module now should contain 3 objects
		assertEquals(3, newModule.getrObjects().size());

		// The baseline still contains only two requirements
		Baseline b2 = app.createBaseline(newModule.getName(), "PDR", "Preliminary Design Review");
		assertEquals(2, b2.getReqIds().size());
	}

	/** This method should raise an exception, because the import is not wired in. **/
	@Test(expected = ChanterParserException.class)
	public void testParserNotPresent() throws ChanterParserException {
		File file = new File("./data/Requirements Document Example.html");
		byte[] data = null;
		ChanterApplication ca = new ChanterApplication();
		ca.importFile(TEST_MODNAME, "html", data);
	}
	
	/** Parsers are registered, but none for the right type **/
	@Test(expected = ChanterParserException.class)
	public void testParserNotAvailable() throws ChanterParserException {
		File file = new File("./data/Requirements Document Example.html");
		byte[] data = null;
		HtmlParser htmlParser = new HtmlParser();
		ChanterApplication ca = new ChanterApplication();
		ca.addParser(htmlParser);
		ca.importFile(TEST_MODNAME, "pdf", data);
	}
	
	@Test()
	public void testImportHtml() throws ChanterParserException {
		File file = new File("./data/Requirements Document Example.html");
		byte[] data = null;
		HtmlParser htmlParser = new HtmlParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "html", data);
		assertNotNull(newModule);
		assertEquals("Import Module from html file.", newModule.getDescription());
	}
	
	@Test
	public void testImportPdf() throws ChanterParserException {
		File file = new File("./data/Requirements Document Example.pdf");
		byte[] data = null;
		PdfParser htmlParser = new PdfParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "pdf", data);
		assertNotNull(newModule);
		assertEquals("Import Module from pdf file.", newModule.getDescription());
		
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
	
	@Test
	public void testImportCsv() throws ChanterParserException {
		File file = new File("./data/Requirements Document Example.csv");
		byte[] data = null;
		CsvParser htmlParser = new CsvParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "csv", data);
		assertNotNull(newModule);
		assertEquals("Import Module from csv file.", newModule.getDescription());
		
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
	
}
