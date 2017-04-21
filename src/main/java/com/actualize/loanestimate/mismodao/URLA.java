package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class URLA extends MISMODataAccessObject {
	public final UrlaDetail urlaDetail;

	public URLA(String NS, Element element) {
		super(element);
		urlaDetail = new UrlaDetail(element);
	}
}
