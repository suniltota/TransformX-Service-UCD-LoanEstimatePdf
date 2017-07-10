package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class AboutVersion extends MISMODataAccessObject {
	public final String CreatedDatetime;
	public final String DataVersionIdentifier;
	
	public AboutVersion(String NS, Element element) {
		super(element);
		CreatedDatetime = getElementValue(element, NS + "CreatedDatetime");
		DataVersionIdentifier = getElementValue(element, NS + "DataVersionIdentifier");
	}
}
