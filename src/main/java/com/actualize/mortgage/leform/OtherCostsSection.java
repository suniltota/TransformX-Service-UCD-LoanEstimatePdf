package com.actualize.mortgage.leform;

import org.w3c.dom.Element;

import com.actualize.mortgage.pdf.mismodao.AmortizationRule;
import com.actualize.mortgage.pdf.mismodao.BuydownOccurences;
import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.mismodao.EscrowItem;
import com.actualize.mortgage.pdf.mismodao.EscrowItems;
import com.actualize.mortgage.pdf.mismodao.FeeDetail;
import com.actualize.mortgage.pdf.mismodao.Fees;
import com.actualize.mortgage.pdf.mismodao.IntegratedDisclosureSectionSummaryDetail;
import com.actualize.mortgage.pdf.mismodao.IntegratedDisclosureSubsectionPayment;
import com.actualize.mortgage.pdf.mismodao.LoanDetail;
import com.actualize.mortgage.pdf.mismodao.PrepaidItem;
import com.actualize.mortgage.pdf.mismodao.PrepaidItems;
import com.actualize.mortgage.pdf.mismodao.TermsOfLoan;
import com.actualize.mortgage.pdf.pdferector.Border;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.Drawable;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Paragraph;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Spacer;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;

public class OtherCostsSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text ROW_HEADER   = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);

	private static final float tabheight   = 16f/72f;
	private static final float headheight  = 15f/72f;
	private static final float dataheight  = 12f/72f;
	private static final float leftIndent  = 5f/72f;
	private static final float rightIndent = 4f/72f;

	private Border border = new Border(Color.BLACK, 0.5f/72f);
	
	private final Grid grid;
	
	public OtherCostsSection(Page page, Object object) {
		// Create grid
		float[] heights = { tabheight,                                                                                     // rows 0
				headheight, dataheight, dataheight,                                                                        // rows 1 - 3
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,            // rows 4 - 11
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,			   // rows 12 - 19
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,	                       // rows 20 - 26
				headheight, dataheight*7/4,                                                                                // rows 27 - 28
				headheight, dataheight, dataheight  };                                                                     // rows 29 - 33
		float[] widths = { 204f/72f, 58f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header
		grid.setCell(0, 0, new FormattedText("Other Costs", TAB_TEXT))
			.setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Horizontal.LEFT, leftIndent).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setBackground(new Tab(2f));

		// Taxes and other government fees (MISMO spec 8.1)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
		grid.setCell(1, 0, new FormattedText("E. Taxes and Other Government Fees", ROW_HEADER).setMargin(Alignment.Horizontal.LEFT, leftIndent).setShade(Color.LIGHT_GRAY));
		grid.setCell(2, 0, new FormattedText("Recording Fees and Other Taxes", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(3, 0, new FormattedText("Transfer Taxes", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Prepaids (MISMO spec 8.4)
		grid.setBorder(Alignment.Vertical.TOP, 4, border);
		grid.setCell(4, 0, new FormattedText("F. Prepaids", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Initial escrow payment at closing (MISMO spec 8.10)
		grid.setBorder(Alignment.Vertical.TOP, 12, border);
		grid.setCell(12, 0, new FormattedText("G. Initial Escrow Payment at Closing", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Other (MISMO spec 8.16)
		grid.setBorder(Alignment.Vertical.TOP, 20, border);
		grid.setCell(20, 0, new FormattedText("H. Other", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(20, 1, new Drawable().setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Total other costs (MISMO spec 8.18)
		grid.setBorder(Alignment.Vertical.TOP, 27, border);
		grid.setCell(27, 0, new FormattedText("I. TOTAL OTHER COSTS (E + F + G + H)", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Total closing costs (MISMO spec 9.1)
		grid.setBorder(Alignment.Vertical.TOP, 29, border);
		grid.setCell(29, 0, new FormattedText("J. TOTAL CLOSING COSTS", ROW_HEADER).setMargin(Alignment.Horizontal.LEFT, leftIndent).setShade(Color.LIGHT_GRAY));
		grid.setCell(29, 1, new Drawable().setShade(Color.LIGHT_GRAY));
		grid.setBorder(Alignment.Vertical.TOP, 30, border);
		grid.setCell(30, 0, new FormattedText("D + I", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(31, 0, new FormattedText("Lender Credits", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
	}

	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws Exception {

		String loan = "LOANS/LOAN";
		String idSummaryBase = loan + "/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_SECTION_SUMMARIES/INTEGRATED_DISCLOSURE_SECTION_SUMMARY";
		String idSummary = idSummaryBase + "/INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL";
		String fee = loan + "/FEE_INFORMATION/FEES";

		// Taxes and other government fees (MISMO spec 8.1)
		IntegratedDisclosureSectionSummaryDetail idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='TaxesAndOtherGovernmentFees']"));		
		grid.setCell(1, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		FeeDetail feeDetail = new FeeDetail((Element)deal.getElementAddNS(fee + "/FEE/FEE_DETAIL[FeeType='RecordingFeeTotal'][IntegratedDisclosureSectionType='TaxesAndOtherGovernmentFees']"));
		if (!feeDetail.FeeEstimatedTotalAmount.equals(""))
			grid.setCell(2, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		feeDetail = new FeeDetail((Element)deal.getElementAddNS(fee + "/FEE/FEE_DETAIL[FeeType='TransferTaxTotal'][IntegratedDisclosureSectionType='TaxesAndOtherGovernmentFees']"));
		if (!feeDetail.FeeEstimatedTotalAmount.equals(""))
			grid.setCell(3, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;

		// Prepaids (MISMO spec 8.4)
		idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='Prepaids']"));
		grid.setCell(4, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;

		// Prepaids - Homeowner's Insurance
		PrepaidItems prepaidItems = new PrepaidItems((Element)deal.getElementAddNS("LOANS/LOAN/CLOSING_INFORMATION/PREPAID_ITEMS"));
		PrepaidItem prepaidItem = getPrepaidItem(prepaidItems, "HomeownersInsurancePremium");
		if (prepaidItem != null) {
			grid.setCell(5, 0, new FormattedText("Homeowner\'s Insurance Premium ( " + prepaidItem.prepaidItemDetail.PrepaidItemMonthsPaidCount  + " months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(5, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(prepaidItem.prepaidItemDetail.PrepaidItemEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		} else
			grid.setCell(5, 0, new FormattedText("Homeowner\'s Insurance Premium (   months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Prepaids - Mortgage Insurance
		prepaidItem = getPrepaidItem(prepaidItems, "MortgageInsurancePremium");
		if (prepaidItem != null) {
			grid.setCell(6, 0, new FormattedText("Mortgage Insurance Premium ( " + prepaidItem.prepaidItemDetail.PrepaidItemMonthsPaidCount + " months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			if(prepaidItem.prepaidItemDetail.PrepaidItemEstimatedTotalAmount.equals("0"))
				grid.setCell(6, 1, new FormattedText("", TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
			else
			grid.setCell(6, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(prepaidItem.prepaidItemDetail.PrepaidItemEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		} else
			grid.setCell(6, 0, new FormattedText("Mortgage Insurance Premium (   months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Prepaids - Prepaid Interest
		AmortizationRule amortizationRule = new AmortizationRule((Element)deal.getElementAddNS(loan + "/AMORTIZATION/AMORTIZATION_RULE"));
		BuydownOccurences buydownOccurences = new BuydownOccurences((Element)deal.getElementAddNS(loan + "/BUYDOWN/BUYDOWN_OCCURRENCES"), "[BuydownTemporarySubsidyFundingIndicator='false']");
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS(loan + "/LOAN_DETAIL"));
		TermsOfLoan loanTerms = new TermsOfLoan((Element)deal.getElementAddNS(loan + "/TERMS_OF_LOAN"));
		prepaidItem = getPrepaidItem(prepaidItems, "PrepaidInterest");
		if (prepaidItem != null) {
			grid.setCell(7, 0, new FormattedText("Prepaid Interest ( " + Formatter.DOLLARS.format(prepaidItem.prepaidItemDetail.PrepaidItemPerDiemAmount) + " per day for " + prepaidItem.prepaidItemDetail.PrepaidItemNumberOfDaysCount + " days @" + Formatter.PERCENT.format(LoanTermsSection.interestRate(loanDetail, loanTerms, buydownOccurences, amortizationRule)) + ")", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(7, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(prepaidItem.prepaidItemDetail.PrepaidItemEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		} else
			grid.setCell(7, 0, new FormattedText("Prepaid Interest (   per day for   days )", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Prepaids - Property Taxes
		PrepaidItem prepaidTaxes = getPrepaidItem(prepaidItems, "Property Taxes");
		if (prepaidTaxes != null) {
			grid.setCell(8, 0, new FormattedText("Property Taxes ( " + prepaidTaxes.prepaidItemDetail.PrepaidItemMonthsPaidCount + " months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			if(prepaidTaxes.prepaidItemDetail.PrepaidItemEstimatedTotalAmount.equals("0"))
				grid.setCell(8, 1, new FormattedText("", TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
			else
			grid.setCell(8, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(prepaidTaxes.prepaidItemDetail.PrepaidItemEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		} else
			grid.setCell(8, 0, new FormattedText("Property Taxes (   months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Prepaids - Other
		int line = 8;
		for (PrepaidItem item : prepaidItems.prepaidItems) {
			if (item.prepaidItemDetail.PrepaidItemType.equals("") || Formatter.doubleValue(item.prepaidItemDetail.PrepaidItemEstimatedTotalAmount) == 0)
				continue;
			else if (!item.prepaidItemDetail.PrepaidItemType.equalsIgnoreCase("HomeownersInsurancePremium") && !item.prepaidItemDetail.PrepaidItemType.equalsIgnoreCase("MortgageInsurancePremium") && !item.prepaidItemDetail.PrepaidItemType.equalsIgnoreCase("PrepaidInterest") && item!=prepaidTaxes) {
				grid.setCell(++line, 0, new FormattedText(item.prepaidItemDetail.displayName() + " ( " + item.prepaidItemDetail.PrepaidItemMonthsPaidCount + " months)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
				grid.setCell(line, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(item.prepaidItemDetail.PrepaidItemEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
			}
		}

		// Initial escrow payment at closing (MISMO spec 8.10)
		EscrowItems escrowItems = new EscrowItems((Element)deal.getElementAddNS("LOANS/LOAN/ESCROW/ESCROW_ITEMS"));
		idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='InitialEscrowPaymentAtClosing']"));		
		grid.setCell(12, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;

		// Escrow - Homeowner's Insurance
		EscrowItem escrowItem = getEscrowItem(escrowItems, "HomeownersInsurance");
		printEscrowAmount(page, grid, escrowItem, "Homeowner's Insurance", 13);

		// Escrow - Mortgage Insurance
		escrowItem = getEscrowItem(escrowItems, "MortgageInsurance");
		printEscrowAmount(page, grid, escrowItem, "Mortgage Insurance", 14);

		// Escrow - Property Taxes
		escrowItem = getEscrowItem(escrowItems, "PropertyTax");
		printEscrowAmount(page, grid, escrowItem, "Property Taxes", 15);

		// Escrow - Other
		line = 15;
		for (EscrowItem item : escrowItems.escrowItems) {
			if (item.escrowItemDetail.EscrowItemType.equals("") || Formatter.doubleValue(item.escrowItemDetail.EscrowItemEstimatedTotalAmount) == 0)
				continue;
			else if (!item.escrowItemDetail.EscrowItemType.equalsIgnoreCase("HomeownersInsurance") && !item.escrowItemDetail.EscrowItemType.equalsIgnoreCase("MortgageInsurance") && !item.escrowItemDetail.EscrowItemType.equalsIgnoreCase("PropertyTax")) {
				printEscrowAmount(page, grid, item, item.escrowItemDetail.displayName().replaceAll(" Escrow(s*)$", ""), ++line);
			}
		}

		// Other (MISMO spec 8.16)
		idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='OtherCosts']"));
		if ("".equals(idSummaryDetail.IntegratedDisclosureSectionTotalAmount)) {
			grid.setCell(20, 1,new FormattedText("",ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		} else {
			grid.setCell(20, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		}
		
		
		Fees fees = new Fees((Element)deal.getElementAddNS(fee), "[FEE_DETAIL/IntegratedDisclosureSectionType='OtherCosts']");
		for (int i = 0; i < fees.fees.length; i++) {
			if (fees.fees[i] != null) {
				if(fees.fees[i].feeDetail.FeeEstimatedTotalAmount.equals("0")){
					grid.setCell(21+i, 0, new FormattedText(fees.fees[i].feeDetail.displayName(), TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
					grid.setCell(21+i, 1, new FormattedText("", TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
				} else{
					grid.setCell(21+i, 0, new FormattedText(fees.fees[i].feeDetail.displayName(), TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
					grid.setCell(21+i, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(fees.fees[i].feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);		
				}
			}
		}

		// Total other costs (MISMO spec 8.18)
		idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='TotalOtherCosts']"));		
		grid.setCell(27, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;

		// Total closing costs (MISMO spec 9.1)
		idSummaryDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='TotalClosingCosts'][IntegratedDisclosureSubsectionType='ClosingCostsSubtotal']"));
		if(idSummaryDetail.IntegratedDisclosureSectionTotalAmount.equals("")){
			grid.setCell(29, 1, new FormattedText("", ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		}else {
			grid.setCell(29, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idSummaryDetail.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		}
		
		
		IntegratedDisclosureSubsectionPayment idPayment = new IntegratedDisclosureSubsectionPayment((Element)deal.getElementAddNS(idSummaryBase + "[INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL/IntegratedDisclosureSubsectionType='ClosingCostsSubtotal']/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENTS/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENT"));
		grid.setCell(30, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idPayment.IntegratedDisclosureSubsectionPaymentAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
		idPayment = new IntegratedDisclosureSubsectionPayment((Element)deal.getElementAddNS(idSummaryBase + "[INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL/IntegratedDisclosureSubsectionType='LenderCredits']/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENTS/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENT"));
		String lenderCredits;
		if (idPayment.IntegratedDisclosureSubsectionPaymentAmount.equals("")) {
			IntegratedDisclosureSectionSummaryDetail idLenderCreditTwo = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSubsectionType='LenderCredits']"));
			lenderCredits = idLenderCreditTwo.IntegratedDisclosureSubsectionTotalAmount;
		} else
			lenderCredits = idPayment.IntegratedDisclosureSubsectionPaymentAmount;
			if(lenderCredits.equals("0") || lenderCredits.equals("")){
				grid.setCell(31, 1, new FormattedText("", TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
			}else {
				grid.setCell(31, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(lenderCredits), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);;
			}
			

		try {
			grid.draw(page, page.width - 262f/72f - page.rightMargin, 10f - height(page), 3.75f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void printEscrowAmount(Page page, Grid grid, EscrowItem escrowItem, String displayName, int row) throws Exception {
		String monthlyAmount = "";
		String numberOfMonths = " ";
		String totalAmount = "";
		Paragraph duration = null;
		if (escrowItem != null) {
			monthlyAmount = Formatter.DOLLARS.format(escrowItem.escrowItemDetail.EscrowMonthlyPaymentAmount);
			if ("$0".equals(monthlyAmount))
					monthlyAmount = "";
			numberOfMonths = escrowItem.escrowItemDetail.EscrowCollectedNumberOfMonthsCount;
			totalAmount = Formatter.TRUNCDOLLARS.format(escrowItem.escrowItemDetail.EscrowItemEstimatedTotalAmount);
			if ("$0".equals(totalAmount))
				totalAmount = "";
		}
		FormattedText type = new FormattedText(displayName, TEXT);
		FormattedText monthly = new FormattedText(monthlyAmount, TEXT);
		Spacer spacer = new Spacer(1.7f - type.width(page) - monthly.width(page), 0);
		Spacer mSpacer = new Spacer((new FormattedText("12",TEXT).width(page) - new FormattedText(numberOfMonths,TEXT).width(page))/2,0);
		duration =new Paragraph().append(new FormattedText(" per month for ",TEXT)).append(mSpacer).append(new FormattedText(numberOfMonths,TEXT)).append(mSpacer).append(new FormattedText(" mo.",TEXT));
		Paragraph p = new Paragraph().append(type).append(spacer).append(monthly).append(duration);
		grid.setCell(row, 0, p).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(row, 1, new FormattedText(totalAmount, TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
	}
	
	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
	
	PrepaidItem getPrepaidItem(PrepaidItems prepaidItems, String itemType) {
		for (PrepaidItem prepaidItem : prepaidItems.prepaidItems)
			if ((prepaidItem.prepaidItemDetail.PrepaidItemType.equals(itemType) || prepaidItem.prepaidItemDetail.displayName().startsWith(itemType)) && Formatter.doubleValue(prepaidItem.prepaidItemDetail.PrepaidItemEstimatedTotalAmount) != 0)
				return prepaidItem;
		return null;
	}
	
	EscrowItem getEscrowItem(EscrowItems escrowItems, String itemType) {
		for (EscrowItem escrowItem : escrowItems.escrowItems)
			if (itemType.contains(escrowItem.escrowItemDetail.EscrowItemType))
				return escrowItem;
		// Reverse just in case there's no property tax specifically labeled as "PropertyTax", e.g. CountyProprtyTax
		for (EscrowItem escrowItem : escrowItems.escrowItems)
			if (escrowItem.escrowItemDetail.EscrowItemType.contains(itemType))
				return escrowItem;
		return null;
	}
}
