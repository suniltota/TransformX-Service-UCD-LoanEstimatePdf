package com.actualize.loanestimate.mismodao;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EstimatedPropertyCostComponents extends MISMODataAccessObject {
	public EstimatedPropertyCostComponent[] estimatedPropertyCostComponent;

	public EstimatedPropertyCostComponents(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("ESTIMATED_PROPERTY_COST_COMPONENT");
		estimatedPropertyCostComponent = new EstimatedPropertyCostComponent[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < estimatedPropertyCostComponent.length; i++)
			estimatedPropertyCostComponent[i] = new EstimatedPropertyCostComponent((Element)nodes.item(i));
	}
}
