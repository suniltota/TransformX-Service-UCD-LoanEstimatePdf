package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class FeePaidTo extends MISMODataAccessObject {
	LegalEntity legalEntity;

	public FeePaidTo(Element element) {
		super(element);
		legalEntity = new LegalEntity((Element)getElementAddNS("LEGAL_ENTITY"));
	}
}
