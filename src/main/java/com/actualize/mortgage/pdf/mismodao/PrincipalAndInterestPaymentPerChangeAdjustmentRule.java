package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PrincipalAndInterestPaymentPerChangeAdjustmentRule extends MISMODataAccessObject {
	public final String AdjustmentRuleType;
	public final String PerChangeMaximumPrincipalAndInterestPaymentAmount;
	public final String PerChangeMinimumPrincipalAndInterestPaymentAmount;
	public final String PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount;
	
	public PrincipalAndInterestPaymentPerChangeAdjustmentRule(Element element) {
		super(element);
		AdjustmentRuleType = getValueAddNS("AdjustmentRuleType");
		PerChangeMaximumPrincipalAndInterestPaymentAmount = getValueAddNS("PerChangeMaximumPrincipalAndInterestPaymentAmount");
		PerChangeMinimumPrincipalAndInterestPaymentAmount = getValueAddNS("PerChangeMinimumPrincipalAndInterestPaymentAmount");
		PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount = getValueAddNS("PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount");
	}
}
