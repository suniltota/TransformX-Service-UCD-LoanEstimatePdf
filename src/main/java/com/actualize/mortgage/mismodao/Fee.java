package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class Fee extends MISMODataAccessObject {
	public final FeeDetail feeDetail;
	public final FeePaidTo feePaidTo;
	public final FeePayments feePayments;

	public Fee(Element element) {
		super(element);
		feeDetail = new FeeDetail((Element)getElementAddNS("FEE_DETAIL"));
		feePaidTo = new FeePaidTo((Element)getElementAddNS("FEE_PAID_TO"));
		feePayments = new FeePayments((Element)getElementAddNS("FEE_PAYMENTS"));
	}
}
