package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Roles extends MISMODataAccessObject {
	public Role[] roles;
	
	public Roles(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("ROLE");
		roles = new Role[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < roles.length; i++)
			roles[i] = new Role((Element)nodes.item(i));
	}
}
