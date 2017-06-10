package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DealSets extends MISMODataAccessObject {
	public final DealSet[] dealSet;

	public DealSets(String NS, Element element) {
		super(element);
		NodeList nodes = getElements(element, NS + "DEAL_SET");
		if (nodes.getLength() > 0) {
			dealSet = new DealSet[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++)
				dealSet[i] = new DealSet(NS, (Element)nodes.item(i));
		}
		else
			dealSet = null;
	}
}
