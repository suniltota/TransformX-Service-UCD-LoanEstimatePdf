package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class RoleDetail extends MISMODataAccessObject {
	public final String PartyRoleType;
	
	public RoleDetail(Element element) {
		super(element);
		PartyRoleType = getValueAddNS("PartyRoleType");
	}
}
