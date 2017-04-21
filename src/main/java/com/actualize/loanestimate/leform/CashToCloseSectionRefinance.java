package com.actualize.loanestimate.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.loanestimate.mismodao.CashToCloseItem;
import com.actualize.loanestimate.mismodao.CashToCloseItems;
import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.TermsOfLoan;
import com.actualize.loanestimate.mismodao.UrlaDetail;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.BoxedCharacter;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.Drawable;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Paragraph;
import com.actualize.loanestimate.pdferector.Region;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;

public class CashToCloseSectionRefinance implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	private static final Border borderlight = new Border(Color.MEDIUM_GRAY, 0.5f/72f);
	private static final Border borderheavy = new Border(Color.BLACK, 1f/72f);
	
	private static final float leftIndent  = 5f/72f;

	private final Grid grid;
	
	private boolean purchase = true;  // TODO purchase (or refi) transaction

	public CashToCloseSectionRefinance(Page page, Deal deal) {
		// Create grid
		float[] heights = { 16f/72f, 12f/72f, 12f/72f, 12f/72f, 12f/72f, 32f/72f };
		float[] widths = { 204f/72f, 58f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);
		
		CashToCloseItems items = new CashToCloseItems((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/CASH_TO_CLOSE_ITEMS"));
		

		// Tab header
		grid.setCell(0, 0, new FormattedText("Calculating Cash to Close", TAB_TEXT))
			.setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(2f));

		// Items (MISMO spec 10.x)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
        grid.setCell(1, 0, new FormattedText("Loan Amount", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
        grid.setBorder(Alignment.Vertical.TOP, 2, borderlight);
        grid.setCell(2, 0, new FormattedText("Total Closing Costs (J)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
        grid.setBorder(Alignment.Vertical.TOP, 3, borderlight);
        grid.setCell(3, 0, new FormattedText("Estimated Total Payoffs and Payments", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
        grid.setBorder(Alignment.Vertical.TOP, 4, borderheavy);
        //Logic to identify from/to borrower
        boolean isFromBorrowerElement = isFromBorrower(items, "FromBorrower");
		grid.setCell(4, 0, estimatedCashToClose(isFromBorrowerElement).setMargin(Alignment.Horizontal.LEFT, leftIndent));
		grid.setBorder(Alignment.Vertical.TOP, 5, border);
		
		
		Region estimatedClosingCostsFinancedRegion = new Region();
		estimatedClosingCostsFinancedRegion.append(new Paragraph().append(new FormattedText("Estimated Closing Costs Financed", TEXT)));
		estimatedClosingCostsFinancedRegion.append(new Paragraph().append(new FormattedText("(Paid from your Loan Amount)", TEXT)));
		
		grid.setCell(5, 0, estimatedClosingCostsFinancedRegion).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.BOTTOM, 5, border);
	}
	
	
	public static Drawable estimatedCashToClose(boolean isFromBorrowerElement) {

        Paragraph paragraph = new Paragraph();
        paragraph.append(new FormattedText("Estimated Cash to Close ", TEXT_BOLD));
        if(isFromBorrowerElement)
            paragraph.append(BoxedCharacter.CHECK_BOX_NO);
        else
            paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
        
        paragraph.append(new FormattedText(" From  ", TEXT_BOLD));
        
        if(!isFromBorrowerElement)
            paragraph.append(BoxedCharacter.CHECK_BOX_NO);
        else
            paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
        
        paragraph.append(new FormattedText(" To Borrower", TEXT_BOLD));
        
        return paragraph;
    }
	
	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws XPathExpressionException {
		
		// MISMO Data
		CashToCloseItems items = new CashToCloseItems((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/CASH_TO_CLOSE_ITEMS"));
		// Items (MISMO spec 10.x)
		grid.setCell(1, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "LoanAmount")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(2, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "TotalClosingCosts")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(3, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "TotalPayoffsAndPayments")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(4, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "CashToCloseTotal")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(5, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "ClosingCostsFinanced")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		System.out.println(cashToCloseItemAmount(items, "TotalPayoffsAndPayments"));
		try {
			grid.draw(page, page.width - page.rightMargin - 262f/72f, 4f - height(page), 3.8f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String cashToCloseItemAmount(CashToCloseItems items, String type) {
		for (CashToCloseItem item : items.cashToCloseItems)
			if (item.IntegratedDisclosureCashToCloseItemType.equalsIgnoreCase(type))
				return item.IntegratedDisclosureCashToCloseItemEstimatedAmount;
		return "";
	}
	
	private static String loanAmount(TermsOfLoan loanTerms, UrlaDetail urlaDetail) {
		if ("".equals(loanTerms.NoteAmount))
			return urlaDetail.BorrowerRequestedLoanAmount;
		return loanTerms.NoteAmount;
	}
	
	boolean isFromBorrower(CashToCloseItems items, String type) {
        for (CashToCloseItem item : items.cashToCloseItems)
            if (item.IntegratedDisclosureCashToCloseItemPaymentType.equalsIgnoreCase(type))
                return true;
        return false;
    }

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
