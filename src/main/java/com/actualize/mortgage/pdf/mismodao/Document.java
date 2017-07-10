package com.actualize.mortgage.pdf.mismodao;

import org.w3c.dom.Element;

public class Document extends MISMODataAccessObject {
    public final AuditTrail auditTrail;
	public final DealSets dealSets;
	public final Signatories signatories;
	public final Views views;
	public final AboutVersions aboutVersions;
	public final DocumentClassification documentClassification;

	public Document(String NS, Element element) {
		super(element);
		Element elem;
		elem = getElement(element, NS + "AUDIT_TRAIL");
		auditTrail = new AuditTrail(NS, elem);
		elem = getElement(element, NS + "DEAL_SETS");
		dealSets = new DealSets(NS, elem);
		elem = getElement(element, NS + "SIGNATORIES");
		signatories = new Signatories(NS, elem);
		elem = getElement(element, NS + "VIEWS");
		views = new Views(NS, elem);
		elem = getElement(element, NS + "ABOUT_VERSIONS");
		aboutVersions = new AboutVersions(NS, elem);
		elem = getElement(element, NS + "DOCUMENT_CLASSIFICATION");
		documentClassification = new DocumentClassification(NS, elem);
	}
}
