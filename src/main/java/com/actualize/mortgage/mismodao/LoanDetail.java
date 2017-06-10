package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class LoanDetail extends MISMODataAccessObject {
	public final String AssumabilityIndicator;
	public final String BalloonIndicator;
	public final String BalloonPaymentAmount;
	public final String BuydownTemporarySubsidyFundingIndicator;
	public final String ConstructionLoanIndicator;
	public final String CreditorServicingOfLoanStatementType;
	public final String DemandFeatureIndicator;
	public final String EscrowAbsenceReasonType;
	public final String EscrowIndicator;
	public final String InterestOnlyIndicator;
	public final String InterestRateIncreaseIndicator;
	public final String LoanAmountIncreaseIndicator;
	public final String MIRequiredIndicator;
	public final String NegativeAmortizationIndicator;
	public final String PaymentIncreaseIndicator;
	public final String PrepaymentPenaltyIndicator;
	public final String SeasonalPaymentFeatureIndicator;
	public final String StepPaymentsFeatureDescription;
	public final String TotalSubordinateFinancingAmount;

	public LoanDetail(Element element) {
		super(element);
		AssumabilityIndicator = getValueAddNS("AssumabilityIndicator");
		BalloonIndicator = getValueAddNS("BalloonIndicator");
		BalloonPaymentAmount = getValueAddNS("BalloonPaymentAmount");
		BuydownTemporarySubsidyFundingIndicator = getValueAddNS("BuydownTemporarySubsidyFundingIndicator");
		ConstructionLoanIndicator = getValueAddNS("ConstructionLoanIndicator");
		CreditorServicingOfLoanStatementType = getValueAddNS("CreditorServicingOfLoanStatementType");
		DemandFeatureIndicator = getValueAddNS("DemandFeatureIndicator");
		EscrowAbsenceReasonType = getValueAddNS("EscrowAbsenceReasonType");
		EscrowIndicator = getValueAddNS("EscrowIndicator");
		InterestOnlyIndicator = getValueAddNS("InterestOnlyIndicator");
		InterestRateIncreaseIndicator = getValueAddNS("InterestRateIncreaseIndicator");
		LoanAmountIncreaseIndicator = getValueAddNS("LoanAmountIncreaseIndicator");
		MIRequiredIndicator = getValueAddNS("MIRequiredIndicator");
		NegativeAmortizationIndicator = getValueAddNS("NegativeAmortizationIndicator");
		PaymentIncreaseIndicator = getValueAddNS("PaymentIncreaseIndicator");
		PrepaymentPenaltyIndicator = getValueAddNS("PrepaymentPenaltyIndicator");
		SeasonalPaymentFeatureIndicator = getValueAddNS("SeasonalPaymentFeatureIndicator");
		StepPaymentsFeatureDescription = getValueAddNS("StepPaymentsFeatureDescription");
		TotalSubordinateFinancingAmount = getValueAddNS("TotalSubordinateFinancingAmount");
	}
}
