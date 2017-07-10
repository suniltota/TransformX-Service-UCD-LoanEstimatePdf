package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Deal extends MISMODataAccessObject {
    Collaterals collaterals;
    Liabilities liabilities;
    Loans loans;
    Parties parties;
    Relationships relationships;

	public Deal(String NS, Element element) {
		super(element);
		Element elem;
		elem = getElement(element, NS + "COLLATERALS");
		collaterals = elem == null ? null : new Collaterals(NS, elem);
		elem = getElement(element, NS + "LIABILITIES");
		liabilities =  elem == null ? null : new Liabilities(NS, elem);
		elem = getElement(element, NS + "LOANS");
		loans =  elem == null ? null : new Loans(NS, elem);
		elem = getElement(element, NS + "PARTIES");
		parties =  elem == null ? null : new Parties(elem);
		elem = getElement(element, NS + "RELATIONSHIPS");
		relationships =  elem == null ? null : new Relationships(NS, elem);
	}
}
