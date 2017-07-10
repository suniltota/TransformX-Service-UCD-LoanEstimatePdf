package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class EstimatedPropertyCostDetail extends MISMODataAccessObject {
	public final String ProjectedPaymentEstimatedTaxesInsuranceAssessmentTotalAmount;
	
	public EstimatedPropertyCostDetail(Element element) {
		super(element);
		ProjectedPaymentEstimatedTaxesInsuranceAssessmentTotalAmount = getValueAddNS("ProjectedPaymentEstimatedTaxesInsuranceAssessmentTotalAmount");
	}
}
