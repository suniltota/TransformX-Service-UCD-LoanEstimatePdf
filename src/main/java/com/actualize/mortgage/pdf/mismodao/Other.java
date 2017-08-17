package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Other extends MISMODataAccessObject {
	public final String IntegratedDisclosureEstimatedClosingCostsExpirationTimezoneType;
	public final String LockExpirationTimezoneType;
	public final String DocumentSignatureRequiredIndicator;
	public final String buydownReflectedInNoteIndicator;
	public final String totalOptionalPaymentCount;
	public final String totalStepPaymentCount;
	public Other(Element element) {
		super(element);
		IntegratedDisclosureEstimatedClosingCostsExpirationTimezoneType = getValue("gse:IntegratedDisclosureEstimatedClosingCostsExpirationTimezoneType");
		LockExpirationTimezoneType = getValue("gse:LockExpirationTimezoneType");
		DocumentSignatureRequiredIndicator = getValue("gse:DocumentSignatureRequiredIndicator");
		buydownReflectedInNoteIndicator = getValue("gse:BuydownReflectedInNoteIndicator");
		totalOptionalPaymentCount = getValue("gse:TotalOptionalPaymentCount");
		totalStepPaymentCount = getValue("gse:TotalStepPaymentCount");
	}
}
