package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class BuydownOccurence extends MISMODataAccessObject {
	public final String BuydownInitialEffectiveInterestRatePercent;
	
	public BuydownOccurence(Element element) {
		super(element);
		BuydownInitialEffectiveInterestRatePercent = getValueAddNS("BuydownInitialEffectiveInterestRatePercent");
	}
}
