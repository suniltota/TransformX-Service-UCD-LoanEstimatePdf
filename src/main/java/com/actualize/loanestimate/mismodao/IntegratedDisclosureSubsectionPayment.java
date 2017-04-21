package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class IntegratedDisclosureSubsectionPayment extends MISMODataAccessObject {
	public final String IntegratedDisclosureSubsectionPaidByType;
	public final String IntegratedDisclosureSubsectionPaymentAmount;
	public final String IntegratedDisclosureSubsectionPaymentTimingType;
	
	public IntegratedDisclosureSubsectionPayment(Element element) {
		super(element);
		IntegratedDisclosureSubsectionPaidByType = getValueAddNS("IntegratedDisclosureSubsectionPaidByType");
		IntegratedDisclosureSubsectionPaymentAmount = getValueAddNS("IntegratedDisclosureSubsectionPaymentAmount");
		IntegratedDisclosureSubsectionPaymentTimingType = getValueAddNS("IntegratedDisclosureSubsectionPaymentTimingType");
	}
}
