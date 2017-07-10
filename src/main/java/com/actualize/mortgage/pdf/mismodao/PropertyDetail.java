package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PropertyDetail extends MISMODataAccessObject {
	public final String PropertyEstimatedValueAmount;
	
	public PropertyDetail(Element element) {
		super(element);
		PropertyEstimatedValueAmount = getValueAddNS("PropertyEstimatedValueAmount");
	}
}
