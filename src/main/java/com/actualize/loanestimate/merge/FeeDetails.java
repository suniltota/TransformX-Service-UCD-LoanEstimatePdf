package com.actualize.loanestimate.merge;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FeeDetails extends DefaultHandler {

	private static Logger logger = Logger.getLogger(FeeDetails.class.getName());
	public static void main(String[] args) {

		try {
			
			getIntegratedDisclosureSectionTotalAmount("C:\\file\\LE_2200428521.xml");
			System.out.println("Reading total amont and origin charges "+FeeContainerParser.class);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getIntegratedDisclosureSectionTotalAmount(String fileName)
			throws ParserConfigurationException, SAXException {
		String totalAmount = "";
		File mismoDAOFile = new File(fileName);
		FeeContainerParser feeContainerParser = new FeeContainerParser();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		try {
			saxParser.parse(mismoDAOFile, feeContainerParser);
		} catch (IOException e) {
			logger.info("Failed to read the MISMO file for Feee container.");
			e.printStackTrace();
		}
		return totalAmount;
	}
}
