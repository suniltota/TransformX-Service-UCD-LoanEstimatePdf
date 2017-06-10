package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class Party extends MISMODataAccessObject {
    public final Individual individual;
    public final LegalEntity legalEntity;
    public final Addresses addresses;
    public final Roles roles;

	public Party(Element element)  {
		super(element);
		individual = new Individual((Element)getElementAddNS("INDIVIDUAL"));
		legalEntity = new LegalEntity((Element)getElementAddNS("LEGAL_ENTITY"));
		addresses = new Addresses((Element)getElementAddNS("ADDRESSES"));
		roles = new Roles((Element)getElementAddNS("ROLES"));
	}
}
