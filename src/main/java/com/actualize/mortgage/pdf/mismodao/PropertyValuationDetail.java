package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PropertyValuationDetail extends MISMODataAccessObject {
	// public final String IdentifierOwnerURI;
	public final String PropertyValuationAmount;
	public final String PropertyValuationMethodType;
	public final String PropertyValuationMethodTypeOtherDescription;
	
	public PropertyValuationDetail(Element element) {
		super(element);
		// IdentifierOwnerURI = getValueAddNS("IdentifierOwnerURI");
		PropertyValuationAmount = getValueAddNS("PropertyValuationAmount");
		PropertyValuationMethodType = getValueAddNS("PropertyValuationMethodType");
		PropertyValuationMethodTypeOtherDescription = getValueAddNS("PropertyValuationMethodTypeOtherDescription");
	}
}
