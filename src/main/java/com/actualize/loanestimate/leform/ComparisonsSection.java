package com.actualize.loanestimate.leform;

import java.math.BigDecimal;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.FeeSummaryDetail;
import com.actualize.loanestimate.mismodao.IntegratedDisclosureDetail;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Region;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;

public class ComparisonsSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT_BIG     = new Text(Color.BLACK, 14, Typeface.CALIBRI);
	private static final Text TEXT         = new Text(Color.BLACK, 10, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 10, Typeface.CALIBRI_BOLD);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	private static final float leftIndent  = 5f/72f;
	
	private final Grid grid;
	
	public ComparisonsSection(Page page, Object object) {

		// Create grid
		float[] heights = { 16f/72f, 36f/72f, 20f/72f, 32f/72f };
		float[] widths = { 1.9f, 0.9f, 4.7f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header (MISMO spec 21.0)
		grid.setCell(0, 0, new FormattedText("Comparisons", TAB_TEXT)).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(1.9f));
		grid.setCell(0, 1, new FormattedText("   Use these measures to compare this loan with other loans.", TEXT_BOLD));
		grid.setBorder(Alignment.Vertical.BOTTOM, 0, border);

		// In 5 Years (MISMO spec 21.6, 21.7)
		grid.setCell(1, 0, new FormattedText("In 5 Years", TEXT_BOLD)).setAlignment(Alignment.Vertical.MIDDLE).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(1, 2, new Region().setSpacing(5.5f/72f)
			.append(new FormattedText("Total you will have paid in principal, interest, mortgage insurance, and loan costs.", TEXT))
			.append(new FormattedText("Principal you will have paid off.", TEXT))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 5.5f/72f);
		grid.setBorder(Alignment.Horizontal.RIGHT, 1, 0, border);
		grid.setBorder(Alignment.Vertical.BOTTOM, 1, border);

		// Annual Percentage Rate (APR) (MISMO spec 21.4)
		grid.setCell(2, 0, new FormattedText("Annual Percentage Rate (APR)", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(2, 2, new FormattedText("Your costs over the loan term expressed as a rate. This is not your interest rate.", TEXT)).setAlignment(Alignment.Vertical.MIDDLE);
		grid.setBorder(Alignment.Horizontal.RIGHT, 2, 0, border);
		grid.setBorder(Alignment.Vertical.BOTTOM, 2, border);

		// Optional Payments (MISMO spec 21.5)
		grid.setCell(3, 0, new FormattedText("Total Interest Percentage (TIP)", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent).setMargin(Alignment.Vertical.TOP, 3f/72f);
		grid.setCell(3, 2, new Region().setSpacing(-1f/72f)
			.append(new FormattedText("The total amount of interest that you will pay over the loan term as a", TEXT))
			.append(new FormattedText("percentage of your loan amount.", TEXT))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 6.5f/72f);
		grid.setBorder(Alignment.Horizontal.RIGHT, 3, 0, border);
		grid.setBorder(Alignment.Vertical.BOTTOM, 3, border);
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
		IntegratedDisclosureDetail idDetail = new IntegratedDisclosureDetail((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_DETAIL"));
		FeeSummaryDetail feeDetail = new FeeSummaryDetail((Element)deal.getElementAddNS("LOANS/LOAN/FEE_INFORMATION/FEES_SUMMARY/FEE_SUMMARY_DETAIL"));

		// In 5 Years (MISMO spec 21.6, 21.7)
		grid.setCell(1, 1, new Region().setSpacing(.5f/72f)
				.append(new FormattedText(Formatter.TRUNCDOLLARS.format(idDetail.FiveYearTotalOfPaymentsComparisonAmount), TEXT_BIG))
				.append(new FormattedText(Formatter.TRUNCDOLLARS.format(idDetail.FiveYearPrincipalReductionComparisonAmount), TEXT_BIG))).setAlignment(Alignment.Vertical.TOP).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 0.1f).setMargin(Alignment.Vertical.TOP, -0.5f/72f);

		// Annual Percentage Rate (APR) (MISMO spec 21.4)
		grid.setCell(2, 1, new FormattedText(Formatter.APRPERCENT.format(feeDetail.APRPercent), TEXT_BIG)).setAlignment(Alignment.Vertical.TOP).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 0.1f).setMargin(Alignment.Vertical.TOP, -.5f/72f);

		// Optional Payments (MISMO spec 21.5)
		//String totalInterestPercent = feeDetail.FeeSummaryTotalInterestPercent;
		grid.setCell(3, 1, new FormattedText(Formatter.APRPERCENT.format(feeDetail.FeeSummaryTotalInterestPercent), TEXT_BIG)).setAlignment(Alignment.Vertical.TOP).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 0.1f);
		
		try {
			grid.draw(page, page.leftMargin, 6.3f, 7.5f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
