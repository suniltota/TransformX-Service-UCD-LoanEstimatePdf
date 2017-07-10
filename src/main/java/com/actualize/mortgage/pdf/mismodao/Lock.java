package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Lock extends MISMODataAccessObject {
	public final String LockExpirationDatetime;
	public final String LockStatusType;
	public final Extension extension;
	
	public Lock(Element element) {
		super(element);
		LockExpirationDatetime = getValueAddNS("LockExpirationDatetime");
		LockStatusType = getValueAddNS("LockStatusType");
		extension = new Extension((Element)getElementAddNS("EXTENSION"));
	}
}
