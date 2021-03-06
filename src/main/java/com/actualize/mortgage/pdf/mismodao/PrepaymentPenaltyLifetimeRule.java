package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PrepaymentPenaltyLifetimeRule extends MISMODataAccessObject {
	public final String PrepaymentPenaltyExpirationDate;
	public final String PrepaymentPenaltyExpirationMonthsCount;
	public final String PrepaymentPenaltyMaximumLifeOfLoanAmount;
	
	public PrepaymentPenaltyLifetimeRule(Element element) {
		super(element);
		PrepaymentPenaltyExpirationDate = getValueAddNS("PrepaymentPenaltyExpirationDate");
		PrepaymentPenaltyExpirationMonthsCount = getValueAddNS("PrepaymentPenaltyExpirationMonthsCount");
		PrepaymentPenaltyMaximumLifeOfLoanAmount = getValueAddNS("PrepaymentPenaltyMaximumLifeOfLoanAmount");
	}
}
