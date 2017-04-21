package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class ClosingInformationDetail extends MISMODataAccessObject {
	public final String CashFromBorrowerAtClosingAmount;
	public final String CashFromSellerAtClosingAmount;
	public final String CashToBorrowerAtClosingAmount;
	public final String CashToSellerAtClosingAmount;
	public final String ClosingAgentOrderNumberIdentifier;
	public final String ClosingDate;
	public final String ClosingRateSetDate;
	public final String CurrentRateSetDate;
	public final String DisbursementDate;
	
	public ClosingInformationDetail(Element element) {
		super(element);
		CashFromBorrowerAtClosingAmount = getValueAddNS("CashFromBorrowerAtClosingAmount");
		CashFromSellerAtClosingAmount = getValueAddNS("CashFromSellerAtClosingAmount");
		CashToBorrowerAtClosingAmount = getValueAddNS("CashToBorrowerAtClosingAmount");
		CashToSellerAtClosingAmount = getValueAddNS("CashToSellerAtClosingAmount");
		ClosingAgentOrderNumberIdentifier = getValueAddNS("ClosingAgentOrderNumberIdentifier");
		ClosingDate = getValueAddNS("ClosingDate");
		ClosingRateSetDate = getValueAddNS("ClosingRateSetDate");
		CurrentRateSetDate = getValueAddNS("CurrentRateSetDate");
		DisbursementDate = getValueAddNS("DisbursementDate");
	}
}
