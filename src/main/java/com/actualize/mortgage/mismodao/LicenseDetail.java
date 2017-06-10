package com.actualize.mortgage.mismodao;

import org.w3c.dom.Element;

public class LicenseDetail extends MISMODataAccessObject {
	public final String LicenseAuthorityLevelType;
	public final String LicenseIdentifier;
	public final String IdentifierOwnerURI;
	public final String LicenseIssueDate;
	public final String LicenseIssuingAuthorityName;
	public final String LicenseIssuingAuthorityStateCode;
	
	public LicenseDetail(Element element) {
		super(element);
		LicenseAuthorityLevelType = getValueAddNS("LicenseAuthorityLevelType");
		LicenseIdentifier = getValueAddNS("LicenseIdentifier");
		IdentifierOwnerURI = getValueAddNS("IdentifierOwnerURI"); // TODO an attribute
		LicenseIssueDate = getValueAddNS("LicenseIssueDate");
		LicenseIssuingAuthorityName = getValueAddNS("LicenseIssuingAuthorityName");
		LicenseIssuingAuthorityStateCode = getValueAddNS("LicenseIssuingAuthorityStateCode");
	}
}
