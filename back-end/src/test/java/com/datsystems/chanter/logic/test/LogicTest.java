package com.datsystems.chanter.logic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
		byte[] data = null;
		ChanterApplication ca = new ChanterApplication();
		ca.importFile(TEST_MODNAME, "html", data);
	}
	
	/** Parsers are registered, but none for the right type **/
	@Test(expected = ChanterParserException.class)
	public void testParserNotAvailable() throws ChanterParserException {
		byte[] data = null;
		HtmlParser htmlParser = new HtmlParser();
		ChanterApplication ca = new ChanterApplication();
		ca.addParser(htmlParser);
		ca.importFile(TEST_MODNAME, "pdf", data);
	}
	
	/** if the data is null, an exception is thrown. **/
	@Test(expected = ChanterParserException.class)
	public void testImportNull() throws ChanterParserException {
		byte[] data = null;
		HtmlParser htmlParser = new HtmlParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "html", data);
		assertNull(newModule);
	}
	
	@Test()
	public void testImportHtml() throws ChanterParserException {
		byte[] data = "<h1>Title</h1>".getBytes();
		HtmlParser htmlParser = new HtmlParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "html", data);
		assertNotNull(newModule);
		assertNotNull(newModule.getDescription());
	}
	
	@Test
	public void testImportPdf() throws IOException, FileNotFoundException, ChanterParserException {
		File file = new File("./src/test/resources/data/Requirements Document Example.pdf");
		InputStream os = new FileInputStream(file);
		byte[] data = os.readAllBytes();
		os.close();
		PdfParser htmlParser = new PdfParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "pdf", data);
		assertNotNull(newModule);
		assertNotNull(newModule.getDescription());
		
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
	
	@Test
	public void testImportCsv() throws ChanterParserException {
		byte[] data = "name,description,attribute1\ntest,this is a test,value".getBytes();
		CsvParser htmlParser = new CsvParser();
		app.addParser(htmlParser);
		ModuleSummary newModule = app.importFile(TEST_MODNAME, "csv", data);
		assertNotNull(newModule);
		assertNotNull(newModule.getDescription());
		
		// How do we discover the attributes?  Is this done manually?
		//assertEquals(5, newModule.getAttributes().size());
		
		// We'll need to check how many requirements should have been imported
		
	}
	
}
