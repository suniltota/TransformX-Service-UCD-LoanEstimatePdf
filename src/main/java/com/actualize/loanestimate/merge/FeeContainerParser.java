package com.actualize.loanestimate.merge;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FeeContainerParser extends DefaultHandler {

	boolean originationCharges = false;
	String content;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if ("mismo:INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL".equals(qName)) {
			
			originationCharges = true;
			
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		System.out.println("localName :"+localName);
		if (localName.equals("mismo:IntegratedDisclosureSectionTotalAmount")){
			String totalAmount = content.toString();	
			System.out.println("totalAmount :"+totalAmount);
		} else if(localName.equals("mismo:IntegratedDisclosureSectionType")){
			String sectionType = content.toString();
			System.out.println("sectionType :"+sectionType);
			
		}

	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {

		if(originationCharges){
			content = new String(ch, start, length);
			System.out.println("content :"+content);
		}
		
		
	}

}
