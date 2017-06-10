package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class Name extends MISMODataAccessObject {
	public final String FirstName;
	public final String FullName;
	public final String LastName;
	public final String MiddleName;
	public final String SuffixName;
	
	public Name(Element element) {
		super(element);
		FirstName = getValueAddNS("FirstName");
		FullName = getValueAddNS("FullName");
		LastName = getValueAddNS("LastName");
		MiddleName = getValueAddNS("MiddleName");
		SuffixName = getValueAddNS("SuffixName");
	}
}
