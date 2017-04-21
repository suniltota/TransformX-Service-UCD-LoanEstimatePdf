package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class AmortizationRule extends MISMODataAccessObject {
	public final String AmortizationType;
	
	public AmortizationRule(Element element) {
		super(element);
		AmortizationType = getValueAddNS("AmortizationType");
	}
}
