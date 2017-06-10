package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class FeeSummaryDetail extends MISMODataAccessObject {
	public final String APRPercent;
	public final String FeeSummaryTotalAmountFinancedAmount;
	public final String FeeSummaryTotalFinanceChargeAmount;
	public final String FeeSummaryTotalInterestPercent;
	public final String FeeSummaryTotalOfAllPaymentsAmount;
	
	public FeeSummaryDetail(Element element) {
		super(element);
		APRPercent = getValueAddNS("APRPercent");
		FeeSummaryTotalAmountFinancedAmount = getValueAddNS("FeeSummaryTotalAmountFinancedAmount");
		FeeSummaryTotalFinanceChargeAmount = getValueAddNS("FeeSummaryTotalFinanceChargeAmount");
		FeeSummaryTotalInterestPercent = getValueAddNS("FeeSummaryTotalInterestPercent");
		FeeSummaryTotalOfAllPaymentsAmount = getValueAddNS("FeeSummaryTotalOfAllPaymentsAmount");
	}
}
