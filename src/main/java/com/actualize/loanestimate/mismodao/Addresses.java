package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Addresses extends MISMODataAccessObject {
	public final Address[] addresses;

	public Addresses(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("ADDRESS");
		addresses = new Address[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < addresses.length; i++)
			addresses[i] = new Address((Element)nodes.item(i));
	}
}
