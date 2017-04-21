package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class DocumentClass extends MISMODataAccessObject {
    public final String documentType;
    public final String documentTypeOtherDescription;
    
	public DocumentClass(Element element) {
		super(element);
		documentType = getValueAddNS("DocumentType");
		documentTypeOtherDescription = getValueAddNS("DocumentTypeOtherDescription");
	}
}
