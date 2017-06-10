package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FeeDetail extends MISMODataAccessObject {
	public String DisplayLabelText = "";
	public final String FeeActualTotalAmount;
	public final String FeeEstimatedTotalAmount;
	public final String FeePaidToType;
	public final String FeePaidToTypeOtherDescription;
	public final String FeePercentBasisType;
	public final String FeeTotalPercent;
	public final String FeeType;
	public final String FeeTypeOtherDescription;
	public final String IntegratedDisclosureSectionType;
	public final String OptionalCostIndicator;
	public final String RegulationZPointsAndFeesIndicator;
	
	public FeeDetail(Element element) {
		super(element);
		FeeActualTotalAmount = getValueAddNS("FeeActualTotalAmount");
		FeeEstimatedTotalAmount = getValueAddNS("FeeEstimatedTotalAmount");
		FeePaidToType = getValueAddNS("FeePaidToType");		
		FeePaidToTypeOtherDescription = getValueAddNS("FeePaidToTypeOtherDescription");
		FeePercentBasisType = getValueAddNS("FeePercentBasisType");
		FeeTotalPercent = getValueAddNS("FeeTotalPercent");
		FeeType = getValueAddNS("FeeType");
		NodeList node = getElementsAddNS("FeeType");
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
		FeeTypeOtherDescription = getValueAddNS("FeeTypeOtherDescription");
		IntegratedDisclosureSectionType = getValueAddNS("IntegratedDisclosureSectionType");
		OptionalCostIndicator = getValueAddNS("OptionalCostIndicator");
		RegulationZPointsAndFeesIndicator = getValueAddNS("RegulationZPointsAndFeesIndicator");
	}
	
	public String displayName() {
		if (!DisplayLabelText.equals(""))
			return DisplayLabelText;
		if (FeeType.equalsIgnoreCase("Other"))
			return FeeTypeOtherDescription;
		return FeeType;
	}
}
