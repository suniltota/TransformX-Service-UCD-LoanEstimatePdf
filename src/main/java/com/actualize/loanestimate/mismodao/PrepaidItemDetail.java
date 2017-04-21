package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PrepaidItemDetail extends MISMODataAccessObject {
	public String DisplayLabelText = "";
	public final String FeePaidToType;
	public final String FeePaidToTypeOtherDescription;
	public final String IntegratedDisclosureSectionType;
	public final String PrepaidItemEstimatedTotalAmount;
	public final String PrepaidItemMonthsPaidCount;
	public final String PrepaidItemNumberOfDaysCount;
	public final String PrepaidItemPaidFromDate;
	public final String PrepaidItemPaidThroughDate;
	public final String PrepaidItemPerDiemAmount;
	public final String PrepaidItemPerDiemCalculationMethodType;
	public final String PrepaidItemType;
	public final String PrepaidItemTypeOtherDescription;
	public final String RegulationZPointsAndFeesIndicator;
	
	public PrepaidItemDetail(Element element) {
		super(element);
		FeePaidToType = getValueAddNS("FeePaidToType");
		FeePaidToTypeOtherDescription = getValueAddNS("FeePaidToTypeOtherDescription");
		IntegratedDisclosureSectionType = getValueAddNS("IntegratedDisclosureSectionType");
		PrepaidItemEstimatedTotalAmount = getValueAddNS("PrepaidItemEstimatedTotalAmount");
		PrepaidItemMonthsPaidCount = getValueAddNS("PrepaidItemMonthsPaidCount");
		PrepaidItemNumberOfDaysCount = getValueAddNS("PrepaidItemNumberOfDaysCount");
		PrepaidItemPaidFromDate = getValueAddNS("PrepaidItemPaidFromDate");
		PrepaidItemPaidThroughDate = getValueAddNS("PrepaidItemPaidThroughDate");
		PrepaidItemPerDiemAmount = getValueAddNS("PrepaidItemPerDiemAmount");
		PrepaidItemPerDiemCalculationMethodType = getValueAddNS("PrepaidItemPerDiemCalculationMethodType");
		PrepaidItemType = getValueAddNS("PrepaidItemType");
		NodeList node = getElementsAddNS("PrepaidItemType");
		if(null != node)
		{	
			Element ele =(Element)node.item(0);
			if(null != ele){
				DisplayLabelText = ele.getAttribute("DisplayLabelText");
				if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
					DisplayLabelText = ele.getAttribute("gse:DisplayLabelText");
			}
		}
		if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
			DisplayLabelText = getAttributeValue("gse:DisplayLabelText");
		
		if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
			DisplayLabelText = getAttributeValue("DisplayLabelText");

		PrepaidItemTypeOtherDescription = getValueAddNS("PrepaidItemTypeOtherDescription");
		RegulationZPointsAndFeesIndicator = getValueAddNS("RegulationZPointsAndFeesIndicator");
	}
	
	public String displayName() {
		String str = PrepaidItemType;
		if (!DisplayLabelText.equals(""))
			str = DisplayLabelText;
		else if (PrepaidItemType.equalsIgnoreCase("Other"))
			str = PrepaidItemTypeOtherDescription;
		return canonicalLabel(str);
	}
}
