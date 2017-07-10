package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocumentSets extends MISMODataAccessObject {
	public final DocumentSet[] documentSet;

	public DocumentSets(String NS, Element element) {
		super(element);
		NodeList nodes = getElements(element, NS + "DOCUMENT_SET");
		if (nodes.getLength() > 0) {
			documentSet = new DocumentSet[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++)
				documentSet[i] = new DocumentSet(NS, (Element)nodes.item(i));
		}
		else
			documentSet = null;
	}
}
