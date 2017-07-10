package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Role extends MISMODataAccessObject {
    public final RealEstateAgent realEstateAgent;
    public final Licenses licenses;
    public final RoleDetail roleDetail;

    public Role(Element element) {
		super(element);
		realEstateAgent = new RealEstateAgent((Element)getElementAddNS("REAL_ESTATE_AGENT"));
		licenses = new Licenses((Element)getElementAddNS("LICENSES"));
		roleDetail = new RoleDetail((Element)getElementAddNS("ROLE_DETAIL"));
	}
}
