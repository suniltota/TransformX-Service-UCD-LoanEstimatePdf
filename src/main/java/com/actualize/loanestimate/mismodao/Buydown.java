package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class Buydown extends MISMODataAccessObject {
	public final BuydownOccurences buydownOccurences;
	public final BuydownRule buydownRule;

	public Buydown(String NS, Element element) {
		super(element);
		buydownOccurences = new BuydownOccurences((Element)getElementAddNS("BUYDOWN_OCCURRENCES"));
		buydownRule = new BuydownRule((Element)getElementAddNS("BUYDOWN_RULE"));
	}
}
