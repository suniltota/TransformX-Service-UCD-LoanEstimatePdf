package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class RealEstateAgent extends MISMODataAccessObject {
	public final String RealEstateAgentType;
	
	public RealEstateAgent(Element element) {
		super(element);
		RealEstateAgentType = getValueAddNS("RealEstateAgentType");
	}
}
