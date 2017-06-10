package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class License extends MISMODataAccessObject {
	public final LicenseDetail licenseDetail;

	public License(Element element) {
		super(element);
		licenseDetail = new LicenseDetail((Element)getElementAddNS("LICENSE_DETAIL"));
	}
}
