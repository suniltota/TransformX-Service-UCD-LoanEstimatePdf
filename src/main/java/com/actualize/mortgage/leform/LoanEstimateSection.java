package com.actualize.mortgage.leform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.pdf.mismodao.Address;
import com.actualize.mortgage.pdf.mismodao.Construction;
import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.mismodao.IntegratedDisclosureDetail;
import com.actualize.mortgage.pdf.mismodao.LegalEntityDetail;
import com.actualize.mortgage.pdf.mismodao.LoanDetail;
import com.actualize.mortgage.pdf.mismodao.LoanIdentifier;
import com.actualize.mortgage.pdf.mismodao.Locks;
import com.actualize.mortgage.pdf.mismodao.MaturityRule;
import com.actualize.mortgage.pdf.mismodao.Name;
import com.actualize.mortgage.pdf.mismodao.Parties;
import com.actualize.mortgage.pdf.mismodao.Party;
import com.actualize.mortgage.pdf.mismodao.PropertyDetail;
import com.actualize.mortgage.pdf.mismodao.PropertyValuationDetail;
import com.actualize.mortgage.pdf.mismodao.SalesContractDetail;
import com.actualize.mortgage.pdf.mismodao.TermsOfLoan;
import com.actualize.mortgage.pdf.pdferector.Border;
import com.actualize.mortgage.pdf.pdferector.BoxedCharacter;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.Drawable;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Paragraph;
import com.actualize.mortgage.pdf.pdferector.Region;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;

public class LoanEstimateSection implements Section {
	private static final Text COMPANY      = new Text(Color.LIGHT_GRAY, 26, Typeface.CALIBRI);
	private static final Text TITLE        = new Text(Color.BLACK, 20, Typeface.CALIBRI_BOLD);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 10, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 10, Typeface.CALIBRI);
	private static final Text TEXT_OBLIQUE = new Text(Color.BLACK, 10, Typeface.CALIBRI_OBLIQUE);
	private static final Text SMALL_OBLIQUE= new Text(Color.BLACK, 9, Typeface.CALIBRI_OBLIQUE);
	private static final Text BOLD_OBLILIQUE= new Text(Color.BLACK,9,Typeface.CALIBRI_BOLD_OBLIQUE);

	Border border = new Border(Color.BLACK, 1f/72f);
	
	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws XPathExpressionException {
		
		// Data query helper strings
		String loan = "LOANS/LOAN";
		String lender = "PARTIES/PARTY[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='NotePayTo'][LEGAL_ENTITY]";
		String subjectProperty = "COLLATERALS/COLLATERAL/SUBJECT_PROPERTY";
		String propertyValuation = subjectProperty + "/PROPERTY_VALUATIONS/PROPERTY_VALUATION";
		String salesContract = subjectProperty + "/SALES_CONTRACTS/SALES_CONTRACT";
		String unparsedLegalDescription = deal.getValueAddNS(subjectProperty + "/LEGAL_DESCRIPTIONS/LEGAL_DESCRIPTION/UNPARSED_LEGAL_DESCRIPTIONS/UNPARSED_LEGAL_DESCRIPTION/UnparsedLegalDescription"); 

		// Data containers
		Address lenderAddress = new Address((Element)deal.getElementAddNS(lender + "/ADDRESSES/ADDRESS[AddressType='Mailing']"));
		Address propertyAddress = new Address((Element)deal.getElementAddNS(subjectProperty + "/ADDRESS"));
		Parties borrowerParties = new Parties((Element)deal.getElementAddNS("PARTIES"), "[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='Borrower']");
		Construction construction = new Construction((Element)deal.getElementAddNS(loan + "/CONSTRUCTION"));
		IntegratedDisclosureDetail idDetail = new IntegratedDisclosureDetail((Element)deal.getElementAddNS(loan + "/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_DETAIL"));
		LegalEntityDetail lenderDetail = new LegalEntityDetail((Element)deal.getElementAddNS(lender + "/LEGAL_ENTITY/LEGAL_ENTITY_DETAIL"));
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS(loan + "/LOAN_DETAIL"));
		LoanIdentifier loanIdentifier = new LoanIdentifier((Element)deal.getElementAddNS(loan + "/LOAN_IDENTIFIERS/LOAN_IDENTIFIER[LoanIdentifierType='LenderLoan']"));
		Locks locks = new Locks((Element)deal.getElementAddNS(loan + "/LOAN_PRODUCT/LOCKS"));
		MaturityRule maturityRule = new MaturityRule((Element)deal.getElementAddNS(loan + "/MATURITY/MATURITY_RULE[LoanMaturityPeriodType='Month']"));
		PropertyDetail propertyDetail = new PropertyDetail((Element)deal.getElementAddNS(subjectProperty + "/PROPERTY_DETAIL"));
		PropertyValuationDetail propertyValuationDetail = new PropertyValuationDetail((Element)deal.getElementAddNS(propertyValuation + "/PROPERTY_VALUATION_DETAIL"));
		SalesContractDetail salesContractDetail = new SalesContractDetail((Element)deal.getElementAddNS(salesContract + "/SALES_CONTRACT_DETAIL"));
		TermsOfLoan loanTerms = new TermsOfLoan((Element)deal.getElementAddNS(loan + "/TERMS_OF_LOAN"));

		// Create company grid
		float[] cheights = { 36f/72f, 18/72f };
		float[] cwidths = { 3.5f, 4.0f };
		Grid company = new Grid(cheights.length, cheights, cwidths.length, cwidths);
		company.setCell(0, 0, new FormattedText(Formatter.STRINGCLEAN.format(lenderDetail.FullName), COMPANY));
		company.setCell(1, 0, new FormattedText(Formatter.STRINGCLEAN.format(fullAddress(lenderAddress)), TEXT)).setMargin(Alignment.Vertical.BOTTOM, 8f/72f);
		company.setCell(1, 1, new FormattedText("Save this Loan Estimate to compare with your Closing Disclosure", TEXT_OBLIQUE)).setMargin(Alignment.Vertical.BOTTOM, 8f/72f).setMargin(Alignment.Horizontal.LEFT, -8f/72f);
		company.setBorder(Alignment.Vertical.BOTTOM, 1, new Border(Color.BLACK, 3f/72f));

		// Create left grid
		float[] lheights = { 30f/72f, 12/72f, 32/72f, 24/72f, 16/72f };
		float[] lwidths = { 1.2f, 2.5f };
		Grid left = new Grid(lheights.length, lheights, lwidths.length, lwidths);
		left.setCell(0, 0, new FormattedText("Loan Estimate", TITLE)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -4f/72f);
		left.setCell(1, 0, new FormattedText("DATE ISSUED", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(1, 1, new FormattedText(Formatter.DATE.format(idDetail.IntegratedDisclosureIssuedDate), TEXT)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(2, 0, new FormattedText("APPLICANTS", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(2, 1, applicants(borrowerParties)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(3, 0, new FormattedText("PROPERTY", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(3, 1, propertyAddress(propertyAddress, unparsedLegalDescription )).setAlignment(Alignment.Vertical.TOP);
		left.setCell(4, 0, salePriceDisplay(loanTerms, salesContractDetail, propertyValuationDetail, propertyDetail)).setAlignment(Alignment.Vertical.TOP);
		left.setCell(4, 1, new FormattedText(Formatter.ZEROTRUNCDOLLARS.format(salePrice(loanTerms, salesContractDetail, propertyValuationDetail, propertyDetail)), TEXT)).setAlignment(Alignment.Vertical.TOP);
		
		// Create right grid
		float[] rheights = { 12/72f, 12/72f, 12/72f, 12/72f, 12/72f, 52/72f };
		float[] rwidths = { 0.8f, 3.1f };
		Grid right = new Grid(rheights.length, rheights, rwidths.length, rwidths);
		right.setCell(0, 0, new FormattedText("LOAN TERM", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(0, 1, new FormattedText(Formatter.YEARSMONTHS.format(loanTerm(loanDetail, maturityRule, construction)), TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(1, 0, new FormattedText("PURPOSE", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		
		if(loanTerms.LoanPurposeType.equalsIgnoreCase("Purchase"))
			right.setCell(1, 1, new FormattedText(loanTerms.LoanPurposeType, TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		else if("true".equalsIgnoreCase(loanDetail.ConstructionLoanIndicator))
			right.setCell(1, 1, new FormattedText("Construction", TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		else if("true".equalsIgnoreCase(idDetail.IntegratedDisclosureHomeEquityLoanIndicator))
			right.setCell(1, 1, new FormattedText("Home Equity Loan", TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		else
			right.setCell(1, 1, new FormattedText("Refinance", TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		
		right.setCell(2, 0, new FormattedText("PRODUCT", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(2, 1, new FormattedText(idDetail.IntegratedDisclosureLoanProductDescription, TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(3, 0, new FormattedText("LOAN TYPE", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(3, 1, loanType(loanTerms).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f));
		right.setCell(4, 0, new FormattedText("LOAN ID #", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(4, 1, new FormattedText(loanIdentifier.LoanIdentifier, TEXT)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(5, 0, new FormattedText("RATE LOCK", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);
		right.setCell(5, 1, rateLock(locks, idDetail)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, -2f/72f);

		// Draw
		try {
			float top = 10;
			company.draw(page, page.leftMargin, top, company.width(page));
			left.draw(page, page.leftMargin, top - left.height(page, left.width(page)), left.width(page));
			right.draw(page, 3.9f, top - right.height(page, right.width(page)) - 0.15f, right.width(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float height(Page page) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public static Drawable applicants(Parties borrowers) {
		Region region = new Region().setSpacing(-2f/72f);
		String applicant = "";
		String applicantStreetAddress = "";
		String applicantCityStateZip = "";
		if (borrowers.parties.length > 0) {
			if (!borrowers.parties[0].legalEntity.legalEntityDetail.FullName.equals(""))
				applicant = borrowers.parties[0].legalEntity.legalEntityDetail.FullName;
			else
				applicant = fullName(borrowers.parties[0].individual.name);
			Address borrowerAddress = new Address((Element)borrowers.parties[0].getElementAddNS("ADDRESSES/ADDRESS[AddressType='Mailing']"));
			applicantStreetAddress = streetAddress(borrowerAddress);
			applicantCityStateZip = cityStateZip(borrowerAddress);
		}
		return region.append(new FormattedText(applicant, TEXT)).append(new FormattedText(applicantStreetAddress, TEXT)).append(new FormattedText(applicantCityStateZip, TEXT));
	}

	public static List<Party> additionalApplicants(Deal deal) {
		Parties borrowers = new Parties((Element)deal.getElementAddNS("PARTIES"), "[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='Borrower']");
		ArrayList<Party> addendum = new ArrayList<Party>();
		for (Party borrower : borrowers.parties) {
			if (borrower != borrowers.parties[0])
				addendum.add(borrower);
			
		}
	
		return addendum;
	}
	
	
	
	public static String cityStateZip(Address address) {
		if (address == null)
			return "";
		String str = "";
		if (!address.CityName.equals("")) {
			if (!str.equals("")) str += ", ";
			str += address.CityName;
		}
		if (!address.StateCode.equals("")) {
			if (!str.equals("")) str += ", ";
			str += address.StateCode;
		}
		if (!address.PostalCode.equals("")) {
			if (!str.equals("")) str += " ";
			str += address.PostalCode;
		}
		return str;
	}
	
	public static String fullAddress(Address address) {
		if (address == null)
			return "";
		String str = streetAddress(address);
		if (!cityStateZip(address).equals("")) {
			if (!str.equals("")) str += ", ";
			str += cityStateZip(address);
		}
		return str;
	}
	
	public static String fullName(Name name) {
		if (name == null)
			return null;
		if (!name.FullName.equals(""))
			return name.FullName;
		String str = name.FirstName;
		if (!name.MiddleName.equals("")) {
			if (!str.equals("")) str += " ";
			str += name.MiddleName;
		}
		if (!name.LastName.equals("")) {
			if (!str.equals("")) str += " ";
			str += name.LastName;
		}
		if (!name.SuffixName.equals("")) {
			if (!str.equals("")) str += ", ";
			str += name.SuffixName;
		}
		return str;
	}

	public static String loanTerm(LoanDetail loanDetail, MaturityRule maturityRule, Construction construction) {
		if (loanDetail.ConstructionLoanIndicator.equalsIgnoreCase("true")) {
			if (construction.ConstructionLoanType.equalsIgnoreCase("ConstructionOnly"))
				return construction.ConstructionPeriodNumberOfMonthsCount;
			return construction.ConstructionLoanTotalTermMonthsCount;
		}
		return maturityRule.LoanMaturityPeriodCount;
	}

	public static Drawable loanType(TermsOfLoan loanTerms) {
		Paragraph paragraph = new Paragraph();
		
		if (loanTerms.MortgageType.equalsIgnoreCase("Conventional"))
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
		else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		paragraph.append(new FormattedText(" Conventional  ", TEXT));
	
		if (loanTerms.MortgageType.equalsIgnoreCase("FHA"))
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
		else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		paragraph.append(new FormattedText(" FHA  ", TEXT));
	
		if (loanTerms.MortgageType.equalsIgnoreCase("VA"))
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
		else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		paragraph.append(new FormattedText(" VA  ", TEXT));
		
		if (loanTerms.MortgageType.equalsIgnoreCase("Other")) {
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
			paragraph.append(new FormattedText(" " + loanTerms.MortgageTypeOtherDescription, TEXT));
		} else {
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
			paragraph.append(new FormattedText(" ________________", TEXT));
		}
	
		return paragraph;
	}
	
	public static Region propertyAddress(Address address, String unparsedLegalDescription) {
		Region region = new Region().setSpacing(-2/72f);
		if (unparsedLegalDescription.isEmpty())
			region.append(new FormattedText(streetAddress(address), TEXT)).append(new FormattedText(cityStateZip(address), TEXT));
		else
			region.append(new FormattedText(unparsedLegalDescription, TEXT)).append(new FormattedText(cityStateZip(address), TEXT));
		return region;
	}

	public static Drawable rateLock(Locks locks, IntegratedDisclosureDetail idDetail) {
		Region region = new Region();
	
		// Get lock date/time
		String time = "";
		String timezone = "";
		if (locks.locks.length > 0)
			if (locks.locks[locks.locks.length - 1].LockStatusType.equalsIgnoreCase("Locked")) {
				time = Formatter.DATETIME.format(locks.locks[locks.locks.length - 1].LockExpirationDatetime);
				timezone = locks.locks[locks.locks.length - 1].extension.other.LockExpirationTimezoneType;
			}
	
		// Build first line
		Paragraph paragraph = new Paragraph();
		if (time.equals(""))
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
		else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		paragraph.append(new FormattedText(" NO  ", TEXT));
		if (time.equals(""))
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		else
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
		paragraph.append(new FormattedText(" YES", TEXT));
		if (!time.equals(""))
			paragraph.append(new FormattedText(", until " + time + (timezone.equals("") ? "" : (" " + timezone)), TEXT));
		region.append(paragraph);
		
		// Append subsequent lines
		timezone = idDetail.extension.other.IntegratedDisclosureEstimatedClosingCostsExpirationTimezoneType;
		region.append(new FormattedText("Before closing, your interest rate, points, and lender credits can", SMALL_OBLIQUE));
		region.append(new FormattedText("change unless you lock the interest rate. All other estimated", SMALL_OBLIQUE));
		String formattedTimezone = timezone.equals("") ? "" : (" " + timezone);
		region.append(new Paragraph()
			.append(new FormattedText("closing costs expire on ", SMALL_OBLIQUE))
			.append(new FormattedText(Formatter.DATE.format(idDetail.IntegratedDisclosureEstimatedClosingCostsExpirationDatetime), BOLD_OBLILIQUE))
			.append(new FormattedText(" at " + Formatter.TIME.format(idDetail.IntegratedDisclosureEstimatedClosingCostsExpirationDatetime)+formattedTimezone, SMALL_OBLIQUE)));
	
		return region;
	}

	public static String salePrice(TermsOfLoan loanTerms, SalesContractDetail salesContractDetail, PropertyValuationDetail propertyValuationDetail, PropertyDetail propertyDetail) {
		if (!loanTerms.LoanPurposeType.equalsIgnoreCase("Purchase"))
			if (propertyValuationDetail.PropertyValuationAmount.equals(""))
				return propertyDetail.PropertyEstimatedValueAmount;
			else
				return propertyValuationDetail.PropertyValuationAmount;		
		if (salesContractDetail.PersonalPropertyIncludedIndicator.equalsIgnoreCase("true"))
			return salesContractDetail.RealPropertyAmount;
		return salesContractDetail.SalesContractAmount;
	}

	private Drawable salePriceDisplay(TermsOfLoan loanTerms, SalesContractDetail salesContractDetail, PropertyValuationDetail propertyValuationDetail, PropertyDetail propertyDetail) {
			// propertyValuationDetail.PropertyValuationAmount.equals("") ? "Est. Prop. Value" : "Appraised", TEXT_BOLD)).append(new FormattedText("Prop. Value", TEXT_BOLD)).setSpacing(-2f/72f);
		
		if (!propertyValuationDetail.PropertyValuationAmount.isEmpty())
			return new Region().append(new FormattedText("Appr. Prop. Value", TEXT_BOLD));
		else if (!propertyDetail.PropertyEstimatedValueAmount.isEmpty())
			return new Region().append(new FormattedText("Est. Prop. Value", TEXT_BOLD));
		
		return new FormattedText("SALE PRICE", TEXT_BOLD);
	}

	public static String streetAddress(Address address) {
		if (address == null)
			return "";
		String str = "";
		if (!address.AddressLineText.equals(""))
			str += address.AddressLineText;
		return str;
	}
}
