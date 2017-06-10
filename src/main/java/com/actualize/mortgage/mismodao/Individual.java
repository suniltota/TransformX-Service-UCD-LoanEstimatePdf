package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class Individual extends MISMODataAccessObject {
	public final ContactPoints contactPoints;
	public final Name name;
	
	public Individual(Element element) {
		super(element);
		contactPoints = new ContactPoints((Element)getElementAddNS("CONTACT_POINTS"));
		name = new Name((Element)getElementAddNS("NAME"));
	}
}
