package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Licenses extends MISMODataAccessObject {
	public License[] licenses;

	public Licenses(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("LICENSE");
		licenses = new License[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < licenses.length; i++)
			licenses[i] = new License((Element)nodes.item(i));
	}
}
