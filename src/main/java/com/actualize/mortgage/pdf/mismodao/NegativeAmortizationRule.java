package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class NegativeAmortizationRule extends MISMODataAccessObject {
	public final String LoanNegativeAmortizationResolutionType;
	public final String LoanNegativeAmortizationResolutionTypeOtherDescription;
	public final String NegativeAmortizationLimitMonthsCount;
	public final String NegativeAmortizationMaximumLoanBalanceAmount;
	public final String NegativeAmortizationType;
	
	public NegativeAmortizationRule(Element element) {
		super(element);
		LoanNegativeAmortizationResolutionType = getValueAddNS("LoanNegativeAmortizationResolutionType");
		LoanNegativeAmortizationResolutionTypeOtherDescription = getValueAddNS("LoanNegativeAmortizationResolutionTypeOtherDescription");
		NegativeAmortizationLimitMonthsCount = getValueAddNS("NegativeAmortizationLimitMonthsCount");
		NegativeAmortizationMaximumLoanBalanceAmount = getValueAddNS("NegativeAmortizationMaximumLoanBalanceAmount");
		NegativeAmortizationType = getValueAddNS("NegativeAmortizationType");
	}
}
