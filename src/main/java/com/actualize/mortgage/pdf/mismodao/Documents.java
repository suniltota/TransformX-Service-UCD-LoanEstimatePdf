package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Documents extends MISMODataAccessObject {
	public final Document[] documents;

	public Documents(String NS, Element element) {
		super(element);
		NodeList nodes = getElements(element, NS + "DOCUMENT");
		if (nodes.getLength() > 0) {
			documents = new Document[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++)
				documents[i] = new Document(NS, (Element)nodes.item(i));
		}
		else
			documents = null;
	}
}
