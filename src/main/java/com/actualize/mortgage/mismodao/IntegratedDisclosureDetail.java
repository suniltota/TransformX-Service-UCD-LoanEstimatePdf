package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class IntegratedDisclosureDetail extends MISMODataAccessObject{
	public final String IntegratedDisclosureIssuedDate;
	public final String IntegratedDisclosureHomeEquityLoanIndicator;
	public final String IntegratedDisclosureLoanProductDescription;
	public final String IntegratedDisclosureEstimatedClosingCostsExpirationDatetime;
	public final String FiveYearTotalOfPaymentsComparisonAmount;
	public final String FiveYearPrincipalReductionComparisonAmount;
	public final Extension extension;
	
	public IntegratedDisclosureDetail(Element element) {
		super(element);
		IntegratedDisclosureIssuedDate = getValueAddNS("IntegratedDisclosureIssuedDate");
		IntegratedDisclosureHomeEquityLoanIndicator = getValueAddNS("IntegratedDisclosureIssuedDate");
		IntegratedDisclosureLoanProductDescription = getValueAddNS("IntegratedDisclosureLoanProductDescription");
		IntegratedDisclosureEstimatedClosingCostsExpirationDatetime = getValueAddNS("IntegratedDisclosureEstimatedClosingCostsExpirationDatetime");
		FiveYearTotalOfPaymentsComparisonAmount = getValueAddNS("FiveYearTotalOfPaymentsComparisonAmount");
		FiveYearPrincipalReductionComparisonAmount = getValueAddNS("FiveYearPrincipalReductionComparisonAmount");
		extension = new Extension((Element)getElementAddNS("EXTENSION"));
	}
}
