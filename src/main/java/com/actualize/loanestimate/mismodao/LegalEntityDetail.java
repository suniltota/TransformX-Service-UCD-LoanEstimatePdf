package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class LegalEntityDetail extends MISMODataAccessObject {
	public final String FullName;
	
	public LegalEntityDetail(Element element) {
		super(element);
		FullName = getValueAddNS("FullName");
	}
}
