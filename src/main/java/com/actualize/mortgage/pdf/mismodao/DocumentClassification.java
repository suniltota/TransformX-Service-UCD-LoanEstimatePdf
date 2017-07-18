package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class DocumentClassification extends MISMODataAccessObject {
	public DocumentClasses documentClasses;
	public DocumentClassificationDetail documentClassificationDetail;
    public DocumentClassification(String NS, Element element) {
		super(element);
		Element elem;
        elem = getElement(element, NS + "DOCUMENT_CLASSES");
        documentClasses = elem == null ? null : new DocumentClasses(elem);
        elem = getElement(element, NS + "DOCUMENT_CLASSIFICATION_DETAIL");
        documentClassificationDetail = elem == null ? null : new DocumentClassificationDetail(elem);
	}
}
