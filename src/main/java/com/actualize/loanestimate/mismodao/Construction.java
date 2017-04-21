package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;

public class Construction extends MISMODataAccessObject {
	public final String ConstructionLoanTotalTermMonthsCount;
	public final String ConstructionLoanType;
	public final String ConstructionPeriodNumberOfMonthsCount;

	public Construction(Element element) {
		super(element);
		ConstructionLoanTotalTermMonthsCount = getValueAddNS("ConstructionLoanTotalTermMonthsCount");
		ConstructionLoanType = getValueAddNS("ConstructionLoanType");
		ConstructionPeriodNumberOfMonthsCount = getValueAddNS("ConstructionPeriodNumberOfMonthsCount");
	}
}
