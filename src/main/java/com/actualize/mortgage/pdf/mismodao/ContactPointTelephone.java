package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class ContactPointTelephone extends MISMODataAccessObject {
	public final String ContactPointTelephoneValue;
	
	public ContactPointTelephone(Element element) {
		super(element);
		ContactPointTelephoneValue = getValueAddNS("ContactPointTelephoneValue");
	}
}
