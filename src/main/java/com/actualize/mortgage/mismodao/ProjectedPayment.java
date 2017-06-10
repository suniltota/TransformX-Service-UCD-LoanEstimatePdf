package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class ProjectedPayment extends MISMODataAccessObject {
	public final String PaymentFrequencyType;
	public final String ProjectedPaymentCalculationPeriodEndNumber;
	public final String ProjectedPaymentCalculationPeriodStartNumber;
	public final String ProjectedPaymentCalculationPeriodTermType;
	public final String ProjectedPaymentEstimatedEscrowPaymentAmount;
	public final String ProjectedPaymentEstimatedTotalMaximumPaymentAmount;
	public final String ProjectedPaymentEstimatedTotalMinimumPaymentAmount;
	public final String ProjectedPaymentMIPaymentAmount;
	public final String ProjectedPaymentPrincipalAndInterestMaximumPaymentAmount;
	public final String ProjectedPaymentPrincipalAndInterestMinimumPaymentAmount;
	
	public ProjectedPayment(Element element) {
		super(element);
		PaymentFrequencyType = getValueAddNS("PaymentFrequencyType");
		ProjectedPaymentCalculationPeriodEndNumber = getValueAddNS("ProjectedPaymentCalculationPeriodEndNumber");
		ProjectedPaymentCalculationPeriodStartNumber = getValueAddNS("ProjectedPaymentCalculationPeriodStartNumber");
		ProjectedPaymentCalculationPeriodTermType = getValueAddNS("ProjectedPaymentCalculationPeriodTermType");
		ProjectedPaymentEstimatedEscrowPaymentAmount = getValueAddNS("ProjectedPaymentEstimatedEscrowPaymentAmount");
		ProjectedPaymentEstimatedTotalMaximumPaymentAmount = getValueAddNS("ProjectedPaymentEstimatedTotalMaximumPaymentAmount");
		ProjectedPaymentEstimatedTotalMinimumPaymentAmount = getValueAddNS("ProjectedPaymentEstimatedTotalMinimumPaymentAmount");
		ProjectedPaymentMIPaymentAmount = getValueAddNS("ProjectedPaymentMIPaymentAmount");
		ProjectedPaymentPrincipalAndInterestMaximumPaymentAmount = getValueAddNS("ProjectedPaymentPrincipalAndInterestMaximumPaymentAmount");
		ProjectedPaymentPrincipalAndInterestMinimumPaymentAmount = getValueAddNS("ProjectedPaymentPrincipalAndInterestMinimumPaymentAmount");
	}
}
