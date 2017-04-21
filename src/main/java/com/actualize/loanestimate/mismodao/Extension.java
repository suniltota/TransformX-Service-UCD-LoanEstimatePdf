package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class Extension extends MISMODataAccessObject {
	public final Other other;
	
	public Extension(Element element) {
		super(element);
		other = new Other((Element)getElementAddNS("OTHER"));
	}
}
