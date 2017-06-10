package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class LegalEntity extends MISMODataAccessObject {
	public final LegalEntityDetail legalEntityDetail;

	public LegalEntity(Element element) {
		super(element);
		legalEntityDetail = new LegalEntityDetail((Element)getElementAddNS("LEGAL_ENTITY_DETAIL"));
	}
}
