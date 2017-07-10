package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PrepaidItem extends MISMODataAccessObject {
	public final PrepaidItemDetail prepaidItemDetail;

	public PrepaidItem(Element element) {
		super(element);
		prepaidItemDetail = new PrepaidItemDetail((Element)getElementAddNS("PREPAID_ITEM_DETAIL"));
	}
}
