package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EscrowItemDetail extends MISMODataAccessObject {
	public  String DisplayLabelText = "";
	public final String EscrowCollectedNumberOfMonthsCount;
	public final String EscrowItemCategoryType;
	public final String EscrowItemEstimatedTotalAmount;
	public final String EscrowItemType;
	public final String EscrowItemTypeOtherDescription;
	public final String EscrowMonthlyPaymentAmount;
	public final String FeePaidToType;
	public final String FeePaidToTypeOtherDescription;
	public final String IntegratedDisclosureSectionType;
	public final String RegulationZPointsAndFeesIndicator;
	  
	public EscrowItemDetail(Element element) {
		super(element);
		EscrowCollectedNumberOfMonthsCount = getValueAddNS("EscrowCollectedNumberOfMonthsCount");
		EscrowItemCategoryType = getValueAddNS("EscrowItemCategoryType");
		EscrowItemEstimatedTotalAmount = getValueAddNS("EscrowItemEstimatedTotalAmount");
		EscrowItemType = getValueAddNS("EscrowItemType");
		NodeList node = getElementsAddNS("EscrowItemType");
		if(null != node)
		{
			Element ele =(Element)node.item(0);
			if(null != ele)
			{	
				DisplayLabelText = ele.getAttribute("DisplayLabelText");
				if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
					DisplayLabelText = ele.getAttribute("gse:DisplayLabelText");
			}
		}
		if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
			DisplayLabelText = getAttributeValue("gse:DisplayLabelText");
		if("".equals(DisplayLabelText) || DisplayLabelText.isEmpty())
			DisplayLabelText = getAttributeValue("DisplayLabelText");
		EscrowItemTypeOtherDescription = getValueAddNS("EscrowItemTypeOtherDescription");
		EscrowMonthlyPaymentAmount = getValueAddNS("EscrowMonthlyPaymentAmount");
		FeePaidToType = getValueAddNS("FeePaidToType");
		FeePaidToTypeOtherDescription = getValueAddNS("FeePaidToTypeOtherDescription");
		IntegratedDisclosureSectionType = getValueAddNS("IntegratedDisclosureSectionType");
		RegulationZPointsAndFeesIndicator = getValueAddNS("RegulationZPointsAndFeesIndicator");
	}
	
	public String displayName() {
		if (!DisplayLabelText.equals(""))
			return DisplayLabelText;
		if (EscrowItemType.equalsIgnoreCase("Other"))
			return EscrowItemTypeOtherDescription;
		return EscrowItemType;
	}
}
