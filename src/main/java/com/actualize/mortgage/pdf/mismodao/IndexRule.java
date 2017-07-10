package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class IndexRule extends MISMODataAccessObject {
	public final String IndexType;
	public final String IndexTypeOtherDescription;
	
	public IndexRule(Element element) {
		super(element);
		IndexType = getValueAddNS("IndexType");
		IndexTypeOtherDescription = getValueAddNS("IndexTypeOtherDescription");
	}
}
