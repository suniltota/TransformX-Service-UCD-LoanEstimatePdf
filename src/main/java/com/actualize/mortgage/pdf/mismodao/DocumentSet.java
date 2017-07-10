package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class DocumentSet extends MISMODataAccessObject {
    Documents documents;

	public DocumentSet(String NS, Element element) {
		super(element);
		Element elem;
		elem = getElement(element, NS + "DOCUMENTS");
		documents = elem == null ? null : new Documents(NS, elem);
	}
}
