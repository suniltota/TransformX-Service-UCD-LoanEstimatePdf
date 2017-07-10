package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class LateChargeRule extends MISMODataAccessObject {
	public final String LateChargeAmount;
	public final String LateChargeGracePeriodDaysCount;
	public final String LateChargeMaximumAmount;
	public final String LateChargeMinimumAmount;
	public final String LateChargeRatePercent;
	public final String LateChargeType;
	
	public LateChargeRule(Element element) {
		super(element);
		LateChargeAmount = getValue("gse:LateChargeAmount");
		LateChargeGracePeriodDaysCount = getValue("gse:LateChargeGracePeriodDaysCount");
		LateChargeMaximumAmount = getValue("gse:LateChargeMaximumAmount");
		LateChargeMinimumAmount = getValue("gse:LateChargeMinimumAmount");
		LateChargeRatePercent = getValue("gse:LateChargeRatePercent");
		LateChargeType = getValue("gse:LateChargeType");
	}
}
