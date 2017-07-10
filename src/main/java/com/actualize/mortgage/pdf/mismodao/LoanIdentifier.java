package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class LoanIdentifier extends MISMODataAccessObject {
	public final String LoanIdentifier;
	public final String LoanIdentifierType;
	
	public LoanIdentifier(Element element) {
		super(element);
		LoanIdentifier = getValueAddNS("LoanIdentifier");
		LoanIdentifierType = getValueAddNS("LoanIdentifierType");
	}
}
