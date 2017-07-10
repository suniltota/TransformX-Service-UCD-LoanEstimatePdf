package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class SalesContractDetail extends MISMODataAccessObject {
	public final String PersonalPropertyAmount;
	public final String PersonalPropertyIncludedIndicator;
	public final String RealPropertyAmount;
	public final String SalesContractAmount;
	
	public SalesContractDetail(Element element) {
		super(element);
		PersonalPropertyAmount = getValueAddNS("PersonalPropertyAmount");
		PersonalPropertyIncludedIndicator = getValueAddNS("PersonalPropertyIncludedIndicator");
		RealPropertyAmount = getValueAddNS("RealPropertyAmount");
		SalesContractAmount = getValueAddNS("SalesContractAmount");
	}
}
