package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Address extends MISMODataAccessObject {
	public final String AddressLineText;
	public final String AddressType;
	public final String CityName;
	public final String PostalCode;
	public final String StateCode;
	
	public Address(Element element) {
		super(element);
		AddressLineText = getValueAddNS("AddressLineText");
		AddressType = getValueAddNS("AddressType");
		CityName = getValueAddNS("CityName");
		PostalCode = getValueAddNS("PostalCode");
		StateCode = getValueAddNS("StateCode");
	}
}
