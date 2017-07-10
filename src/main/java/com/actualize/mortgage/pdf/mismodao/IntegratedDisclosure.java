package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class IntegratedDisclosure {
	public final CashToCloseItems cashToCloseItems;
	public final EstimatedPropertyCost estimatedPropertyCost;
	public final IntegratedDisclosureDetail integratedDisclosureDetail;
	public final IntegratedDisclosureSectionSummaries integratedDisclosureSectionSummaries;
	public final ProjectedPayments projectedPayments;

	public IntegratedDisclosure(String NS, Element element) {
		cashToCloseItems = new CashToCloseItems(element);
		estimatedPropertyCost = new EstimatedPropertyCost(element);
		integratedDisclosureDetail = new IntegratedDisclosureDetail(element);
		integratedDisclosureSectionSummaries = new IntegratedDisclosureSectionSummaries(NS, element);
		projectedPayments = new ProjectedPayments(element);
	}
}
