package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class ContactPointEmail extends MISMODataAccessObject {
	public final String ContactPointEmailValue;
	
	public ContactPointEmail(Element element) {
		super(element);
		ContactPointEmailValue = getValueAddNS("ContactPointEmailValue");
	}
}
