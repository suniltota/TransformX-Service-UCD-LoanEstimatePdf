package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class InterestOnly extends MISMODataAccessObject {
	public final String InterestOnlyTermMonthsCount;
	
	public InterestOnly(Element element) {
		super(element);
		InterestOnlyTermMonthsCount = getValueAddNS("InterestOnlyTermMonthsCount");
	}
}
