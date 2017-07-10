package com.actualize.mortgage.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.pdf.mismodao.CashToCloseItem;
import com.actualize.mortgage.pdf.mismodao.CashToCloseItems;
import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.pdferector.Border;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;

public class CashToCloseSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	private static final Border borderlight = new Border(Color.MEDIUM_GRAY, 0.5f/72f);
	private static final Border borderheavy = new Border(Color.BLACK, 1f/72f);
	
	private static final float leftIndent  = 5f/72f;

	private final Grid grid;
	
	private boolean purchase = true;  // TODO purchase (or refi) transaction

	public CashToCloseSection(Page page, Object object) {
		// Create grid
		float[] heights = { 16f/72f, 12f/72f, 12f/72f, 12f/72f, 12f/72f, 12f/72f, 12f/72f, 12f/72f, 14f/72f };
		float[] widths = { 204f/72f, 58f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header
		grid.setCell(0, 0, new FormattedText("Calculating Cash to Close", TAB_TEXT))
			.setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(2f));

		// Items (MISMO spec 10.x)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
		grid.setCell(1, 0, new FormattedText("Total Closing Costs (J)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 2, borderlight);
		grid.setCell(2, 0, new FormattedText("Closing Costs Financed (Paid from your Loan Amount)", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 3, borderlight);
		grid.setCell(3, 0, new FormattedText("Down Payment/Funds from Borrower", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 4, borderlight);
		grid.setCell(4, 0, new FormattedText("Deposit", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 5, borderlight);
		grid.setCell(5, 0, new FormattedText("Funds for Borrower", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 6, borderlight);
		grid.setCell(6, 0, new FormattedText("Seller Credits", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 7, borderlight);
		grid.setCell(7, 0, new FormattedText("Adjustments and Other Credits", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.TOP, 8, borderheavy);
		grid.setCell(8, 0, new FormattedText("Estimated Cash to Close", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.BOTTOM, 8, border);
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
		grid.setCell(1, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "TotalClosingCosts")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(2, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "ClosingCostsFinanced")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		if (purchase)
			grid.setCell(3, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "DownPayment")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		else
			grid.setCell(3, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "FundsFromBorrower")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(4, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "Deposit")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(5, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "FundsForBorrower")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(6, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "SellerCredits")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(7, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "AdjustmentsAndOtherCredits")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		grid.setCell(8, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cashToCloseItemAmount(items, "CashToCloseTotal")), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);

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

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
