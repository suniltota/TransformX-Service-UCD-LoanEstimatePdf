package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AboutVersions extends MISMODataAccessObject {
	public final AboutVersion[] aboutVersions;
	
	public AboutVersions(String NS, Element element) {
		super(element);
		NodeList nodes = element == null ? null : getElements(element, NS + "ABOUT_VERSION");
		if (nodes != null && nodes.getLength() > 0) {
			aboutVersions = new AboutVersion[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++)
				aboutVersions[i] = new AboutVersion(NS, (Element)nodes.item(i));
		}
		else
			aboutVersions = null;
	}
}
