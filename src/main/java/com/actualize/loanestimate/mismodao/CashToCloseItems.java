package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CashToCloseItems extends MISMODataAccessObject {
	public final CashToCloseItem[] cashToCloseItems;

	public CashToCloseItems(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("CASH_TO_CLOSE_ITEM");
		cashToCloseItems = new CashToCloseItem[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < cashToCloseItems.length; i++)
			cashToCloseItems[i] = new CashToCloseItem((Element)nodes.item(i));
	}
}
