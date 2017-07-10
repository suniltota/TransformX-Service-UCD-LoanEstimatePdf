package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Amortization extends MISMODataAccessObject {
	public final AmortizationRule amortizationRule;

	public Amortization(Element element) {
		super(element);
		amortizationRule = new AmortizationRule((Element)getElementAddNS("AMORTIZATION_RULE"));
	}
}
