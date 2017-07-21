package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class BuydownRule extends MISMODataAccessObject {
	public final String BuydownChangeFrequencyMonthsCount;
	public final String BuydownDurationMonthsCount;
	public final String BuydownIncreaseRatePercent;
	public final Extension extension;
	public BuydownRule(Element element) {
		super(element);
		BuydownChangeFrequencyMonthsCount = getValueAddNS("BuydownChangeFrequencyMonthsCount");
		BuydownDurationMonthsCount = getValueAddNS("BuydownDurationMonthsCount");
		BuydownIncreaseRatePercent = getValueAddNS("BuydownIncreaseRatePercent");
		extension = new Extension((Element)getElementAddNS("EXTENSION"));
	}
}
