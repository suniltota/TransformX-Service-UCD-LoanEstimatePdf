package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class PaymentRule extends MISMODataAccessObject {
	public final String FullyIndexedInitialPrincipalAndInterestPaymentAmount;
	public final String InitialPrincipalAndInterestPaymentAmount;
	public final String PartialPaymentAllowedIndicator;
	public final String PaymentFrequencyType;
	public final String PaymentOptionIndicator;
	public final String SeasonalPaymentPeriodEndMonth;
	public final String SeasonalPaymentPeriodStartMonth;
	
	public PaymentRule(Element element) {
		super(element);
		FullyIndexedInitialPrincipalAndInterestPaymentAmount = getValueAddNS("FullyIndexedInitialPrincipalAndInterestPaymentAmount");
		InitialPrincipalAndInterestPaymentAmount = getValueAddNS("InitialPrincipalAndInterestPaymentAmount");
		PartialPaymentAllowedIndicator = getValueAddNS("PartialPaymentAllowedIndicator");
		PaymentFrequencyType = getValueAddNS("PaymentFrequencyType");
		PaymentOptionIndicator = getValueAddNS("PaymentOptionIndicator");
		SeasonalPaymentPeriodEndMonth = getValueAddNS("SeasonalPaymentPeriodEndMonth");
		SeasonalPaymentPeriodStartMonth = getValueAddNS("SeasonalPaymentPeriodStartMonth");
	}
}
