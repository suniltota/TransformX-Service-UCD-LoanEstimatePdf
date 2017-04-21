package com.actualize.loanestimate.leform;

import org.w3c.dom.Element;

import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.FeeDetail;
import com.actualize.loanestimate.mismodao.Fees;
import com.actualize.loanestimate.mismodao.IntegratedDisclosureSectionSummaryDetail;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.Drawable;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;

public class LoanCostsSection implements Section {
	private static final Text TITLE        = new Text(Color.BLACK, 16, Typeface.CALIBRI_BOLD);
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text ROW_HEADER   = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);

	private static final float tabheight   = 16f/72f;
	private static final float headheight  = 15f/72f;
	private static final float dataheight  = 12f/72f;
	private static final float leftIndent  = 5f/72f;
	private static final float rightIndent = 4f/72f;

	private Border border = new Border(Color.BLACK, 0.5f/72f);
	
	private final Grid title, grid;

	private final int startOfA = 1;
	private final int startOfB = 15;
	private final int startOfC = 29;
	private final int startOfD = 43;
	
	public LoanCostsSection(Page page, Object data) {
		// Create title
		float[] titleheights = { 20f };
		float[] titlewidths = { 7.5f };
		title = new Grid(titleheights.length, titleheights, titlewidths.length, titlewidths);
		title.setCell(0,  0, new FormattedText(" Closing Cost Details", TITLE));
		title.setBorder(Alignment.Vertical.BOTTOM, 0, new Border(Color.BLACK, 1.25f/72f));
		
		// Create grid
		float[] heights = { tabheight,
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,
				headheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight, dataheight,
				headheight, dataheight, dataheight, dataheight, dataheight  };
		float[] widths = { 204f/72f, 58f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);
//		for (int row = 2; row < grid.rows(); row++)
//			grid.setCell(row, 1, new Drawable().setShade(Color.LIGHT_GREEN));			

		// Tab header
		grid.setCell(0, 0, new FormattedText("Loan Costs", TAB_TEXT))
			.setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(2f));
	

		// Origination charges (MISMO spec 7.1)
		grid.setBorder(Alignment.Vertical.TOP, startOfA, border);
		grid.setCell(startOfA, 0, new FormattedText("A. Origination Charges", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);

		// Services you cannot shop for (MISMO spec 7.4)
		grid.setBorder(Alignment.Vertical.TOP, startOfB, border);
		grid.setCell(startOfB, 0, new FormattedText("B. Services You Cannot Shop For", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(startOfB, 1, new Drawable().setShade(Color.LIGHT_GRAY));

		// Services you can shop for (MISMO spec 7.6)
		grid.setBorder(Alignment.Vertical.TOP, startOfC, border);
		grid.setCell(startOfC, 0, new FormattedText("C. Services You Can Shop For", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(startOfC, 1, new Drawable().setShade(Color.LIGHT_GRAY));

		// Total loan costs (MISMO spec 7.8)
		grid.setBorder(Alignment.Vertical.TOP, startOfD, border);
		grid.setCell(startOfD, 0, new FormattedText("D. TOTAL LOAN COSTS (A + B + C)", ROW_HEADER).setShade(Color.LIGHT_GRAY)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(startOfD, 1, new Drawable().setShade(Color.LIGHT_GRAY));
	}

	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws Exception {
		
		// Data query helper strings
		String idSummary = "LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_SECTION_SUMMARIES/INTEGRATED_DISCLOSURE_SECTION_SUMMARY/INTEGRATED_DISCLOSURE_SECTION_SUMMARY_DETAIL";
		String fee = "LOANS/LOAN/FEE_INFORMATION/FEES";

		// Origination charges (MISMO spec 7.1)
		IntegratedDisclosureSectionSummaryDetail originationCharges = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='OriginationCharges']"));		
		grid.setCell(startOfA, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(originationCharges.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);

		// Points (MISMO spec 7.2.*)
		FeeDetail feeDetail = new FeeDetail((Element)deal.getElementAddNS(fee + "/FEE/FEE_DETAIL[FeePercentBasisType='OriginalLoanAmount'][FeeType='LoanDiscountPoints'][IntegratedDisclosureSectionType='OriginationCharges']"));
		if (!feeDetail.FeeTotalPercent.equals("") || !feeDetail.FeeTotalPercent.equals("")) {
			grid.setCell(startOfA + 1, 0, new FormattedText(Formatter.PERCENTWITHOUTPRECEEDINGZERO.format(feeDetail.FeeTotalPercent) + " of Loan Amount (Points)", TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(startOfA + 1, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		} else
			grid.setCell(startOfA + 1, 0, new FormattedText("% of Loan Amount (Points)", TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Other origination charge (MISMO spec 7.3)
		Fees fees = new Fees((Element)deal.getElementAddNS(fee), "[FEE_DETAIL/IntegratedDisclosureSectionType='OriginationCharges'][FEE_DETAIL/FeeType!='LoanDiscountPoints']");
		for (int i = 0; i < fees.fees.length; i++) {
			if (fees.fees[i] != null) {
				if(fees.fees[i].feeDetail.FeeEstimatedTotalAmount.equals("0")) {
					grid.setCell(startOfA + 2 + i, 0, new FormattedText(Formatter.CAMEL.format(fees.fees[i].feeDetail.displayName()), TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
					grid.setCell(startOfA + 2 + i, 1, new FormattedText("", TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
				} else {
					grid.setCell(startOfA + 2 + i, 0, new FormattedText(Formatter.CAMEL.format(fees.fees[i].feeDetail.displayName()), TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
					grid.setCell(startOfA + 2 + i, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(fees.fees[i].feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
				}
			}
		}

		// Services you cannot shop for (MISMO spec 7.4)
		IntegratedDisclosureSectionSummaryDetail cannotShopCharges = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='ServicesYouCannotShopFor']"));		
		grid.setCell(startOfB, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(cannotShopCharges.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		fees = new Fees((Element)deal.getElementAddNS(fee), "[FEE_DETAIL/IntegratedDisclosureSectionType='ServicesYouCannotShopFor']");
		for (int i = 0; i < fees.fees.length; i++) {
			if (fees.fees[i] != null) {
				grid.setCell(startOfB + 1 + i, 0, new FormattedText(Formatter.CAMEL.format(fees.fees[i].feeDetail.displayName()), TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
				grid.setCell(startOfB + 1 + i, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(fees.fees[i].feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, 4.5f/72f);
			}
		}

		// Services you can shop for (MISMO spec 7.6)
		IntegratedDisclosureSectionSummaryDetail canShopCharges = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='ServicesYouCanShopFor']"));		
		grid.setCell(startOfC, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(canShopCharges.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);
		fees = new Fees((Element)deal.getElementAddNS(fee), "[FEE_DETAIL/IntegratedDisclosureSectionType='ServicesYouCanShopFor']");
		for (int i = 0; i < fees.fees.length; i++) {
			if (fees.fees[i] != null) {
				grid.setCell(startOfC + 1 + i, 0, new FormattedText(Formatter.CAMEL.format(fees.fees[i].feeDetail.displayName()), TEXT)).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, leftIndent);
				grid.setCell(startOfC + 1 + i, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(fees.fees[i].feeDetail.FeeEstimatedTotalAmount), TEXT)).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, 4.5f/72f);
			}
		}

		// Total loan costs (MISMO spec 7.8)
		IntegratedDisclosureSectionSummaryDetail totalLoanCosts = new IntegratedDisclosureSectionSummaryDetail((Element)deal.getElementAddNS(idSummary + "[IntegratedDisclosureSectionType='TotalLoanCosts']"));		
		grid.setCell(startOfD, 1, new FormattedText(Formatter.TRUNCDOLLARS.format(totalLoanCosts.IntegratedDisclosureSectionTotalAmount), ROW_HEADER)).setShade(Color.LIGHT_GRAY).setAlignment(Alignment.Horizontal.RIGHT).setMargin(Alignment.Horizontal.RIGHT, rightIndent);

		try {
			title.draw(page, page.leftMargin, 10.05f, 7.5f);
			grid.draw(page, page.leftMargin, 10f - height(page), 3.75f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
