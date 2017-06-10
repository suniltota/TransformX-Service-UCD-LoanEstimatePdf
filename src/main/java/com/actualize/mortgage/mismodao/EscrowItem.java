package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class EscrowItem extends MISMODataAccessObject {
	public final EscrowItemDetail escrowItemDetail;

	public EscrowItem(Element element) {
		super(element);
		escrowItemDetail = new EscrowItemDetail((Element)getElementAddNS("ESCROW_ITEM_DETAIL"));
	}
}
