package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class TermsOfLoan extends MISMODataAccessObject {
	public final String AssumedLoanAmount;
	public final String DisclosedFullyIndexedRatePercent;
	public final String LienPriorityType;
	public final String LoanPurposeType;
	public final String MortgageType;
	public final String MortgageTypeOtherDescription;
	public final String NoteAmount;
	public final String NoteRatePercent;
	public final String WeightedAverageInterestRatePercent;
	
	public TermsOfLoan(Element element) {
		super(element);
		AssumedLoanAmount = getValueAddNS("AssumedLoanAmount");
		DisclosedFullyIndexedRatePercent = getValueAddNS("DisclosedFullyIndexedRatePercent");
		LienPriorityType = getValueAddNS("LienPriorityType");
		LoanPurposeType = getValueAddNS("LoanPurposeType");
		MortgageType = getValueAddNS("MortgageType");
		MortgageTypeOtherDescription = getValueAddNS("MortgageTypeOtherDescription");
		NoteAmount = getValueAddNS("NoteAmount");
		NoteRatePercent = getValueAddNS("NoteRatePercent");
		WeightedAverageInterestRatePercent = getValueAddNS("WeightedAverageInterestRatePercent");
	}
}
