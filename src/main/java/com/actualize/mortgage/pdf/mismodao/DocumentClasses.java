package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class DocumentClasses extends MISMODataAccessObject {
    DocumentClass documentClass;
	
    public DocumentClasses(Element element) {
		super(element);
        documentClass = new DocumentClass((Element)getElementAddNS("DOCUMENT_CLASS"));
	}
}
