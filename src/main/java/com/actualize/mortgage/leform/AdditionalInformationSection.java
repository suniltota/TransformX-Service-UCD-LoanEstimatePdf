package com.actualize.mortgage.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.mismodao.ContactPoint;
import com.actualize.mortgage.mismodao.Deal;
import com.actualize.mortgage.mismodao.LicenseDetail;
import com.actualize.mortgage.mismodao.Parties;
import com.actualize.mortgage.mismodao.Party;
import com.actualize.mortgage.pdferector.Border;
import com.actualize.mortgage.pdferector.Color;
import com.actualize.mortgage.pdferector.FormattedText;
import com.actualize.mortgage.pdferector.Grid;
import com.actualize.mortgage.pdferector.Page;
import com.actualize.mortgage.pdferector.Section;
import com.actualize.mortgage.pdferector.Text;
import com.actualize.mortgage.pdferector.Typeface;
import com.actualize.mortgage.pdferector.Drawable.Alignment;

public class AdditionalInformationSection implements Section {
	private static final Text TITLE        = new Text(Color.BLACK, 16, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 8, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 8, Typeface.CALIBRI_BOLD);
	

	private final Grid title, grid;
	
	/**
	 * constructor use to initialise title grid and grid with required row 
	 * which needs to be shown in pdf for Additional Information section
	 * 
	 * @param page
	 * @param object
	 */
	public AdditionalInformationSection(Page page, Object object) {
		// Create title
		float[] titleheights = { 20f };
		float[] titlewidths = { 7.5f };
		title = new Grid(titleheights.length, titleheights, titlewidths.length, titlewidths);
		title.setCell(0,  0, new FormattedText("Additional Information About This Loan", TITLE));
		title.setBorder(Alignment.Vertical.BOTTOM, 0, new Border(Color.BLACK, 1.25f/72f));
		
		// Create grid
		float[] heights = { 16f/72f };
		float[] widths = { 1.2f, 1.7f, 1.2f, 1.2f, 1.6f };
		grid = new Grid(6, heights, widths.length, widths);

		// Draw static data
		grid.setCell(0, 0, new FormattedText("LENDER", TEXT_BOLD));
		grid.setCell(1, 0, new FormattedText("NMLS/ LICENSE ID", TEXT_BOLD));
		grid.setCell(2, 0, new FormattedText("LOAN OFFICER", TEXT_BOLD));
		grid.setCell(3, 0, new FormattedText("NMLS/ LICENSE ID", TEXT_BOLD));
		grid.setCell(4, 0, new FormattedText("EMAIL", TEXT_BOLD));
		grid.setCell(5, 0, new FormattedText("PHONE", TEXT_BOLD));
		grid.setCell(0, 3, new FormattedText("MORTGAGE BROKER", TEXT_BOLD));
		grid.setCell(1, 3, new FormattedText("NMLS/ LICENSE ID", TEXT_BOLD));
		grid.setCell(2, 3, new FormattedText("LOAN OFFICER", TEXT_BOLD));
		grid.setCell(3, 3, new FormattedText("NMLS/ LICENSE ID", TEXT_BOLD));
		grid.setCell(4, 3, new FormattedText("EMAIL", TEXT_BOLD));
		grid.setCell(5, 3, new FormattedText("PHONE", TEXT_BOLD));
	}

	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * This method draw the complete section of AdditionalInformation in PDF
 * @param page
 * @param deal
 * @throws XPathExpressionException
 */
	public void draw(Page page, Deal deal) throws XPathExpressionException {

		// Lender name
		Parties parties = new Parties((Element)deal.getElementAddNS("PARTIES"));
		Party party = getParty(parties, "NotePayTo", false);
		if (party != null)
			grid.setCell(0, 1, new FormattedText(Formatter.STRINGCLEAN.format(party.legalEntity.legalEntityDetail.FullName), TEXT));
		
		// Lender Legal Entity License ID
		LicenseDetail licenseDetail = getLicenseDetail(party);
		if (licenseDetail != null) {
			grid.setCell(1, 0, new FormattedText("NMLS/" + licenseDetail.LicenseIssuingAuthorityStateCode + " LICENSE ID", TEXT_BOLD));
			grid.setCell(1, 1, new FormattedText(licenseDetail.LicenseIdentifier, TEXT));
		}
		
		// Loan Officer
		party = getParty(parties, "NotePayTo", true);
		if (party != null)
			grid.setCell(2, 1, new FormattedText(party.individual.name.FullName, TEXT));
		
		// Loan Officer License ID
		licenseDetail = getLicenseDetail(party);
		if (licenseDetail != null)
			grid.setCell(3, 1, new FormattedText(licenseDetail.LicenseIdentifier, TEXT));
		 
		// Loan Officer Email
		ContactPoint contactPoint = getContactPointEmail(party);
		if (contactPoint != null)
			grid.setCell(4, 1, new FormattedText(contactPoint.contactPointEmail.ContactPointEmailValue, TEXT));

		// Loan Officer Phone
		contactPoint = getContactPointTelephone(party);
		if (contactPoint != null)
			grid.setCell(5, 1, new FormattedText(Formatter.PHONENUMBER.format(contactPoint.contactPointTelephone.ContactPointTelephoneValue), TEXT));

		// Mortgage Broker
		party = getParty(parties, "MortgageBroker", false);
		if (party != null){
			String str = party.legalEntity.legalEntityDetail.FullName.replaceAll("[a|A][n|N][d|D][a|A][m|M][p|P];", "&");
			str = str.replaceAll("''''", "'");
			grid.setCell(0, 4, new FormattedText(Formatter.STRINGCLEAN.format(str), TEXT));
		}
		
		// Mortgage Broker Legal Entity License ID
		licenseDetail = getLicenseDetail(party);
		if (licenseDetail != null) {
			String stateCode = licenseDetail.LicenseIssuingAuthorityStateCode;
			if ("MO".equals(stateCode))
				stateCode = "";
			grid.setCell(1, 3, new FormattedText("NMLS/" + stateCode + " LICENSE ID", TEXT_BOLD));
			grid.setCell(1, 4, new FormattedText(licenseDetail.LicenseIdentifier, TEXT));
		}
		
		// Loan Officer
		party = getParty(parties, "MortgageBroker", true);
		if (party != null)
			grid.setCell(2, 4, new FormattedText(party.individual.name.FullName, TEXT));
		
		// Loan Officer License ID
		licenseDetail = getLicenseDetail(party);
		if (licenseDetail != null) {
			String stateCode = licenseDetail.LicenseIssuingAuthorityStateCode;
			if ("MO".equals(stateCode))
				stateCode = "";
			grid.setCell(3, 3, new FormattedText("NMLS/" + stateCode + " LICENSE ID", TEXT_BOLD));
			grid.setCell(3, 4, new FormattedText(licenseDetail.LicenseIdentifier, TEXT));
		}
		
		// Loan Officer Email
		contactPoint = getContactPointEmail(party);
		if (contactPoint != null)
			grid.setCell(4, 4, new FormattedText(contactPoint.contactPointEmail.ContactPointEmailValue, TEXT));

		// Loan Officer Phone
		contactPoint = getContactPointTelephone(party);
		if (contactPoint != null)
			grid.setCell(5, 4, new FormattedText(Formatter.PHONENUMBER.format(contactPoint.contactPointTelephone.ContactPointTelephoneValue), TEXT));
		
		try {
			title.draw(page, page.leftMargin, 10.05f, 7.5f);
			grid.draw(page, page.leftMargin, 8.45f, 7.5f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
	
	/**
	 * check the party role 
	 * @param parties
	 * @param partyRoleType
	 * @param isIndividual
	 * @return Party info
	 */
	static Party getParty(Parties parties, String partyRoleType, boolean isIndividual) {
		for (Party party : parties.parties)
			if (party.roles.roles.length > 0 && party.roles.roles[0].roleDetail.PartyRoleType.equalsIgnoreCase(partyRoleType) && isIndividual == party.legalEntity.legalEntityDetail.FullName.equals(""))
				return party;
		return null;
	}

	/**
	 * Check the condition if licence details exist
	 * @param party
	 * @return licence details
	 */
	static LicenseDetail getLicenseDetail(Party party) {
		if (party != null && party.roles.roles.length > 0 &&  party.roles.roles[0].licenses.licenses.length > 0)
			return party.roles.roles[0].licenses.licenses[0].licenseDetail;
		return null;
	}

	/**
	 * Check for the contact point email 
	 * @param party
	 * @return contact point email
	 */
	static ContactPoint getContactPointEmail(Party party) {
		if (party != null)
			for (ContactPoint contactPoint : party.individual.contactPoints.contactPoints)
				if (!contactPoint.contactPointEmail.ContactPointEmailValue.equals(""))
					return contactPoint;
		return null;
	}

	/**
	 * check for the contact point phone number
	 * @param party
	 * @return contact point telephone
	 */
	static ContactPoint getContactPointTelephone(Party party) {
		if (party != null)
			for (ContactPoint contactPoint : party.individual.contactPoints.contactPoints)
				if (!contactPoint.contactPointTelephone.ContactPointTelephoneValue.equals(""))
					return contactPoint;
		return null;
	}
}
