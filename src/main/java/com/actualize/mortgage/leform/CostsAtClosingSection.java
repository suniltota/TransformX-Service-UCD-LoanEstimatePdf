package com.actualize.mortgage.leform;

import org.w3c.dom.Element;

import com.actualize.mortgage.pdf.mismodao.ClosingInformationDetail;
import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.mismodao.DocumentClass;
import com.actualize.mortgage.pdf.mismodao.IntegratedDisclosureSectionSummaryDetail;
import com.actualize.mortgage.pdf.mismodao.IntegratedDisclosureSubsectionPayment;
import com.actualize.mortgage.pdf.mismodao.TermsOfLoan;
import com.actualize.mortgage.pdf.pdferector.Border;
import com.actualize.mortgage.pdf.pdferector.BoxedCharacter;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.Drawable;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Paragraph;
import com.actualize.mortgage.pdf.pdferector.Region;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;

public class CostsAtClosingSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text ROW_HEADER   = new Text(Color.BLACK, 11, Typeface.CALIBRI_BOLD);
	private static final Text LARGE_TEXT   = new Text(Color.BLACK, 14, Typeface.CALIBRI);
	private static final Text TEXT         = new Text(Color.BLACK, 11, Typeface.CALIBRI);
	private static final Text TEXT_OBLIQUE = new Text(Color.BLACK, 9, Typeface.CALIBRI_OBLIQUE);

	Border border = new Border(Color.BLACK, 1f/72f);
	
	private final Grid grid;

	public CostsAtClosingSection(Page page, Deal deal) {
	    TermsOfLoan termsOfLoan = new TermsOfLoan((Element)deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN[LoanPurposeType='Refinance']"));
	    DocumentClass documentClass = new DocumentClass((Element)deal.getElementAddNS("../../../../DOCUMENT_CLASSIFICATION/DOCUMENT_CLASSES/DOCUMENT_CLASS[DocumentType='Other']"));
		// Create grid
	    float[] heights = { 24f/72f, 36f/72f, 24f/72f };
		float[] widths = { 2.1f, 1.0f, 4.4f };
		
		//if("Refinance".equalsIgnoreCase(termsOfLoan.LoanPurposeType)){
		if("LoanEstimate:AlternateForm".equalsIgnoreCase(documentClass.documentTypeOtherDescription)){
            float[] heightsRefinance = { 24f/72f, 30f/72f, 30f/72f };
            grid = new Grid(3, heightsRefinance, widths.length, widths);    
        } else {
            grid = new Grid(3, heights, widths.length, widths);
        }
		for (int row = 0; row < grid.rows(); row++) {
			grid.setBorder(Alignment.Vertical.BOTTOM, row, border);
			if (row != 0) grid.setBorder(Alignment.Horizontal.RIGHT, row, 0, border);
		}

		// Tab header
		grid.setCell(0, 0, new FormattedText("  Costs At Closing", TAB_TEXT))
			.setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setBackground(new Tab(widths[0]));

		// Estimated closing costs (MISMO spec 6.1)
		grid.setCell(1, 0, new FormattedText("  Estimated Closing Costs", ROW_HEADER))
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f);
		
		// Estimated cash to close (MISMO spec 6.2)
		grid.setCell(2, 0, new FormattedText("  Estimated Cash to Close", ROW_HEADER))
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f);
		
	}

	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Drawable addEstimatedCashtoCloseCheckboxes(boolean isFromBorrowerElement) {

        Paragraph paragraph = new Paragraph();
        
        if(isFromBorrowerElement)
            paragraph.append(BoxedCharacter.CHECK_BOX_NO);
        else
            paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
        
        paragraph.append(new FormattedText(" From  ", TEXT));
        
        if(!isFromBorrowerElement)
            paragraph.append(BoxedCharacter.CHECK_BOX_NO);
        else
            paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
        
        paragraph.append(new FormattedText(" To Borrower", TEXT));
        
        return paragraph;
    }

	public void draw(Page page, Deal deal) throws Exception {

		// Data query helper strings
		String idSummary = "LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_SECTION_SUMMARIES/INTEGRATED_DISCLOSURE_SECTION_SUMMARY";
		String idDetail = idSummary + "/INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL";
		String ciDetail = "LOANS/LOAN/CLOSING_INFORMATION/CLOSING_INFORMATION_DETAIL";
		TermsOfLoan termsOfLoan = new TermsOfLoan((Element)deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN[LoanPurposeType='Refinance']"));
		DocumentClass documentClass = new DocumentClass((Element)deal.getElementAddNS("../../../../DOCUMENT_CLASSIFICATION/DOCUMENT_CLASSES/DOCUMENT_CLASS[DocumentType='Other']"));

		// Estimated closing costs (MISMO spec 6.1.1)
		IntegratedDisclosureSectionSummaryDetail idClosingDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idDetail + "[IntegratedDisclosureSectionType='TotalClosingCosts'][IntegratedDisclosureSubsectionType='ClosingCostsSubtotal']"));		
		grid.setCell(1, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(idClosingDetail.IntegratedDisclosureSectionTotalAmount), LARGE_TEXT)
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
			.setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));

		// Estimated closing costs (MISMO spec 6.1.2 through 6.1.4)
		IntegratedDisclosureSectionSummaryDetail idLoanDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idDetail + "[IntegratedDisclosureSectionType='TotalLoanCosts']"));
		String loanCosts = Formatter.TRUNCDOLLARS.format(idLoanDetail.IntegratedDisclosureSectionTotalAmount);
		IntegratedDisclosureSectionSummaryDetail idOtherDetail = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idDetail + "[IntegratedDisclosureSectionType='TotalOtherCosts']"));
		String otherCosts = Formatter.TRUNCDOLLARS.format(idOtherDetail.IntegratedDisclosureSectionTotalAmount);
		IntegratedDisclosureSubsectionPayment idLenderCredits = new IntegratedDisclosureSubsectionPayment((Element)deal.getElementAddNS(idSummary + "[INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL/IntegratedDisclosureSubsectionType='LenderCredits']/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENTS/INTEGRATED_DISCLOSURE_SUBSECTION_PAYMENT"));
		String lenderCredits;
		if (idLenderCredits.IntegratedDisclosureSubsectionPaymentAmount.equals("")) {
			IntegratedDisclosureSectionSummaryDetail idLenderCreditTwo = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idDetail + "[IntegratedDisclosureSubsectionType='LenderCredits']"));
			lenderCredits = Formatter.ABSTRUNCDOLLARS.format(idLenderCreditTwo.IntegratedDisclosureSubsectionTotalAmount);
		} else
			lenderCredits = Formatter.ABSTRUNCDOLLARS.format(idLenderCredits.IntegratedDisclosureSubsectionPaymentAmount);
		Region explain = new Region();
		/*if (lenderCredits.equals("$0"))
			explain.append(new FormattedText("Includes " + loanCosts + " in Loan Costs + " + otherCosts + " in Other Costs.", TEXT))
				.append(new Paragraph().append(new FormattedText("See page 2 for details.", TEXT_OBLIQUE)));
		else*/
		    explain.append(new FormattedText("Includes " + loanCosts + " in Loan Costs + " + otherCosts + " in Other Costs - " + lenderCredits, TEXT))
				.append(new Paragraph().append(new FormattedText("in Lender Credits. ", TEXT)).append(new FormattedText("See page 2 for details.", TEXT_OBLIQUE)));
		
		grid.setCell(1, 2, explain).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f);
		
		// Estimated closing costs (MISMO spec 6.2.1 through 6.2.2)
		ClosingInformationDetail closingDetail = new ClosingInformationDetail((Element)deal.getElementAddNS(ciDetail));
		grid.setCell(2, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(closingDetail.CashFromBorrowerAtClosingAmount.equals("")?closingDetail.CashToBorrowerAtClosingAmount:closingDetail.CashFromBorrowerAtClosingAmount), LARGE_TEXT)
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
			.setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));

		Region estimatedCashtoCloseRowColumn2 = new Region();
        estimatedCashtoCloseRowColumn2.append(new Paragraph().append(new FormattedText("Includes Closing Costs. ", TEXT))
                      .append(new FormattedText("See Calculating Cash to Close on page 2 for details.", TEXT_OBLIQUE)));
        //if("Refinance".equalsIgnoreCase(termsOfLoan.LoanPurposeType)) {
        if("LoanEstimate:AlternateForm".equalsIgnoreCase(documentClass.documentTypeOtherDescription)){
            boolean isFromBorrower = ("".equals(closingDetail.CashFromBorrowerAtClosingAmount.trim())?false:true);
            estimatedCashtoCloseRowColumn2.append(new Paragraph().append(addEstimatedCashtoCloseCheckboxes(isFromBorrower)));
        }
        grid.setCell(2, 2, estimatedCashtoCloseRowColumn2)
            .setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f);
		
		// Draw grid
		grid.draw(page, page.leftMargin, .80f, page.width - page.leftMargin - page.rightMargin);
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
