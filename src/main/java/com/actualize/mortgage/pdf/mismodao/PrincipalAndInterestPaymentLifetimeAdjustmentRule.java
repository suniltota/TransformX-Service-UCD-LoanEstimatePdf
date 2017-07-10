package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PrincipalAndInterestPaymentLifetimeAdjustmentRule extends MISMODataAccessObject {
	public final String FirstPrincipalAndInterestPaymentChangeMonthsCount;
	public final String PrincipalAndInterestPaymentMaximumAmount;
	public final String PrincipalAndInterestPaymentMaximumAmountEarliestEffectiveMonthsCount;
	
	public PrincipalAndInterestPaymentLifetimeAdjustmentRule(Element element) {
		super(element);
		FirstPrincipalAndInterestPaymentChangeMonthsCount = getValueAddNS("FirstPrincipalAndInterestPaymentChangeMonthsCount");
		PrincipalAndInterestPaymentMaximumAmount = getValueAddNS("PrincipalAndInterestPaymentMaximumAmount");
		PrincipalAndInterestPaymentMaximumAmountEarliestEffectiveMonthsCount = getValueAddNS("PrincipalAndInterestPaymentMaximumAmountEarliestEffectiveMonthsCount");
	}
}
