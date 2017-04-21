package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class IntegratedDisclosureSubsectionPayments extends MISMODataAccessObject {
	public final IntegratedDisclosureSubsectionPayment[] integratedDisclosureSubsectionPayments;

	public IntegratedDisclosureSubsectionPayments(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENT");
		integratedDisclosureSubsectionPayments = new IntegratedDisclosureSubsectionPayment[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < integratedDisclosureSubsectionPayments.length; i++)
			integratedDisclosureSubsectionPayments[i] = new IntegratedDisclosureSubsectionPayment((Element)nodes.item(i));
	}
}
