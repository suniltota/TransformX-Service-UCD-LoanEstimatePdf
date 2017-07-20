package com.actualize.mortgage.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.pdf.mismodao.AmortizationRule;
import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.mismodao.IndexRule;
import com.actualize.mortgage.pdf.mismodao.InterestRateLifetimeAdjustmentRule;
import com.actualize.mortgage.pdf.mismodao.InterestRatePerChangeAdjustmentRule;
import com.actualize.mortgage.pdf.mismodao.InterestRatePerChangeAdjustmentRules;
import com.actualize.mortgage.pdf.mismodao.LoanDetail;
import com.actualize.mortgage.pdf.mismodao.TermsOfLoan;
import com.actualize.mortgage.pdf.pdferector.Border;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;



/**
 *  AIRTableSection class handle to generate the Adjustable Interest Rate
 *  (AIR) Table by implementing section interface in pdf page. * 
 *
 */
public class AIRTableSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	
	private static final float rowheight = 12f/72f;
	private static final float leftIndent  = 5f/72f;

	private Grid grid;
	
	boolean toggle = true; // TODO replace with MISMO data access logic
	
	/**
	 * AIRTableSection constructor use to initialise  grid with required row 
	 * which needs to be shown in pdf for Adjustable Interest Rate  (AIR) Table
	 * 
	 * @param page
	 * @param object
	 */
	
	public AIRTableSection(Page page, Object object) {

		// Create grid
		float[] heights = { 16f/72f, rowheight, rowheight, rowheight, rowheight, rowheight, rowheight, rowheight, rowheight, rowheight };
		float[] widths = { 126f/72f, 140f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header
		grid.setCell(0, 0, new FormattedText("Adjustable Interest Rate (AIR) Table", TAB_TEXT))
			.setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(2.75f));

		// Index + Margin (MISMO spec 20.1)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
		
		// Initial Interest Rate (MISMO spec 20.2)
		grid.setBorder(Alignment.Vertical.TOP, 2, border);
		grid.setCell(2, 0, new FormattedText("Initial Interest Rate", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Minimum/Maximum Interest Rate (MISMO spec 20.3)
		grid.setBorder(Alignment.Vertical.TOP, 3, border);
		grid.setCell(3, 0, new FormattedText("Minimum/Maximum Interest Rate", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Change Frequency
		grid.setBorder(Alignment.Vertical.TOP, 4, border);
		grid.setCell(4, 0, new FormattedText("Change Frequency", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// First Change (MISMO spec 20.4)
		grid.setBorder(Alignment.Vertical.TOP, 5, border);
		grid.setCell(5, 0, new FormattedText("   First Change", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Subsequent Changes (MISMO spec 20.5)
		grid.setBorder(Alignment.Vertical.TOP, 6, border);
		grid.setCell(6, 0, new FormattedText("   Subsequent Changes", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Limits on Interest Rate Changes
		grid.setBorder(Alignment.Vertical.TOP, 7, border);
		grid.setCell(7, 0, new FormattedText("Limits on Interest Rate Changes", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// First Change (MISMO spec 20.6)
		grid.setBorder(Alignment.Vertical.TOP, 8, border);
		grid.setCell(8, 0, new FormattedText("   First Change", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Subsequent Changes (MISMO spec 20.6)
		grid.setBorder(Alignment.Vertical.TOP, 9, border);
		grid.setCell(9, 0, new FormattedText("   Subsequent Changes", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setBorder(Alignment.Vertical.BOTTOM, 9, border);
	}
	
	
	/**
	 *  draw method handle to draw the pdf page for deal. * 
	 *@param page
	 *@param object
	 */
	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws XPathExpressionException {
		
		// MISMO UCD data
		AmortizationRule amortizationRule = new AmortizationRule((Element)deal.getElementAddNS("LOANS/LOAN/AMORTIZATION/AMORTIZATION_RULE"));
		IndexRule indexRule = new IndexRule((Element)deal.getElementAddNS("LOANS/LOAN/ADJUSTMENT/INTEREST_RATE_ADJUSTMENT/INDEX_RULES/INDEX_RULE"));
		InterestRateLifetimeAdjustmentRule irLifetimeRule = new InterestRateLifetimeAdjustmentRule((Element)deal.getElementAddNS("LOANS/LOAN/ADJUSTMENT/INTEREST_RATE_ADJUSTMENT/INTEREST_RATE_LIFETIME_ADJUSTMENT_RULE"));
		InterestRatePerChangeAdjustmentRules irChangeRules = new InterestRatePerChangeAdjustmentRules((Element)deal.getElementAddNS("LOANS/LOAN/ADJUSTMENT/INTEREST_RATE_ADJUSTMENT/INTEREST_RATE_PER_CHANGE_ADJUSTMENT_RULES"));
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS("LOANS/LOAN/LOAN_DETAIL"));
		TermsOfLoan loanTerm = new TermsOfLoan((Element)deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN"));

		// Is this table displayed?
		if (!loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true") && !amortizationRule.AmortizationType.equalsIgnoreCase("Step") )
			return;

		// Index + Margin (MISMO spec 20.1)
		// or (else) Interest Rate Adjustments (MISMO spec 20.1 continued, for step rate only)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true")) {
			grid.setCell(1, 0, new FormattedText("Index + Margin", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(1, 1,  new FormattedText(indexRuleType(indexRule) + " + " + Formatter.PERCENT.format(irLifetimeRule.MarginRatePercent), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		} else {
			grid.setCell(1, 0, new FormattedText("Interest Rate Adjustments", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(1, 1,  new FormattedText("# of Adjustments", TEXT)).setAlignment(Alignment.Horizontal.RIGHT); // TODO
		}

		// Initial Interest Rate (MISMO spec 20.2)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true"))
			grid.setCell(2, 1,  new FormattedText(Formatter.PERCENT.format(loanTerm.NoteRatePercent), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);

		// Minimum/Maximum Interest Rate (MISMO spec 20.3)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true"))
			grid.setCell(3, 1,  new FormattedText(Formatter.PERCENT.format(irLifetimeRule.FloorRatePercent) + " / " + Formatter.PERCENT.format(irLifetimeRule.CeilingRatePercent), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);

		// First Change (MISMO spec 20.4)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true") && !irLifetimeRule.FirstRateChangeMonthsCount.equals(""))
			grid.setCell(5, 1,  new FormattedText("Beginning of " + Formatter.INTEGERSUFFIX.format(Integer.toString(Integer.parseInt(irLifetimeRule.FirstRateChangeMonthsCount) +1 )) + " month", TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		
		// Subsequent Changes (MISMO spec 20.5)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true") && findIRrule(irChangeRules, "Subsequent") != null)
			grid.setCell(6, 1,  new FormattedText("Every " + Formatter.INTEGERSUFFIX.format(findIRrule(irChangeRules, "Subsequent").PerChangeRateAdjustmentFrequencyMonthsCount) + " month after first change", TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		
		// First Change (MISMO spec 20.6)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true") && findIRrule(irChangeRules, "First") != null)
			grid.setCell(8, 1,  new FormattedText(Formatter.PERCENT.format(findIRrule(irChangeRules, "First").PerChangeMaximumIncreaseRatePercent), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		
		// Subsequent Changes (MISMO spec 20.6)
		if (loanDetail.InterestRateIncreaseIndicator.equalsIgnoreCase("true") && findIRrule(irChangeRules, "Subsequent") != null)
			grid.setCell(9, 1,  new FormattedText(Formatter.PERCENT.format(findIRrule(irChangeRules, "Subsequent").PerChangeMaximumIncreaseRatePercent), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);

		// Draw it!
		try {
			grid.draw(page, page.width - page.rightMargin - 266f/72f, .5f, 3.75f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 *  height method return to page height for the pdf page. * 
	 *@param page
	 *
	 */
	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}

	
	private InterestRatePerChangeAdjustmentRule findIRrule(InterestRatePerChangeAdjustmentRules rules, String type) {
		for (InterestRatePerChangeAdjustmentRule rule : rules.interestRatePerChangeAdjustmentRules)
			if (rule.AdjustmentRuleType.equalsIgnoreCase(type))
				return rule;
		return null;
	}
	
	private String indexRuleType(IndexRule indexRule) {
		if (indexRule.IndexType.equalsIgnoreCase("Other"))
			return indexRule.IndexTypeOtherDescription;
		return indexRule.IndexType;
	}
}