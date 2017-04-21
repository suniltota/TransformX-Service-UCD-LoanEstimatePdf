package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class RealEstateAgent extends MISMODataAccessObject {
	public final String RealEstateAgentType;
	
	public RealEstateAgent(Element element) {
		super(element);
		RealEstateAgentType = getValueAddNS("RealEstateAgentType");
	}
}
