package com.actualize.mortgage.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.mismodao.AmortizationRule;
import com.actualize.mortgage.mismodao.Deal;
import com.actualize.mortgage.mismodao.InterestOnly;
import com.actualize.mortgage.mismodao.LoanDetail;
import com.actualize.mortgage.mismodao.PaymentRule;
import com.actualize.mortgage.mismodao.PrincipalAndInterestPaymentLifetimeAdjustmentRule;
import com.actualize.mortgage.mismodao.PrincipalAndInterestPaymentPerChangeAdjustmentRules;
import com.actualize.mortgage.pdferector.Border;
import com.actualize.mortgage.pdferector.Color;
import com.actualize.mortgage.pdferector.FormattedText;
import com.actualize.mortgage.pdferector.Grid;
import com.actualize.mortgage.pdferector.Page;
import com.actualize.mortgage.pdferector.Section;
import com.actualize.mortgage.pdferector.Text;
import com.actualize.mortgage.pdferector.Typeface;
import com.actualize.mortgage.pdferector.Drawable.Alignment;


public class APTableSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 9, Typeface.CALIBRI_BOLD);

	private static final FormattedText Yes = new FormattedText("    YES", TEXT);
	private static final FormattedText No = new FormattedText("    NO", TEXT);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	
	private static final float leftIndent  = 5f/72f;

	private final Grid grid;
	
	boolean toggle = true; // TODO replace with MISMO data access logic

	public APTableSection(Page page, Object object) {
		// Create grid
		float[] heights = { 16f/72f, 14f/72f, 14f/72f, 14f/72f, 14f/72f, 14f/72f, 12.6f/72f, 12.7f/72f, 12.7f/72f };
		float[] widths = { 110f/72f, 30f/72f, 122f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header
		grid.setCell(0, 0, new FormattedText("Adjustable Payment (AP) Table", TAB_TEXT))
			.setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(2.75f));

		// Interest Only Payments (MISMO spec 19.1)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
		grid.setBorder(Alignment.Horizontal.RIGHT, 1, 0, border);
		grid.setCell(1, 0, new FormattedText("Interest Only Payments?", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Optional Payments (MISMO spec 19.2)
		grid.setBorder(Alignment.Vertical.TOP, 2, border);
		grid.setBorder(Alignment.Horizontal.RIGHT, 2, 0, border);
		grid.setCell(2, 0, new FormattedText("Optional Payments?", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Step Payments (MISMO spec 19.3)
		grid.setBorder(Alignment.Vertical.TOP, 3, border);
		grid.setBorder(Alignment.Horizontal.RIGHT, 3, 0, border);
		grid.setCell(3, 0, new FormattedText("Step Payments?", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Seasonal Payments (MISMO spec 19.4)
		grid.setBorder(Alignment.Vertical.TOP, 4, border);
		grid.setBorder(Alignment.Horizontal.RIGHT, 4, 0, border);
		grid.setCell(4, 0, new FormattedText("Seasonal Payments?", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Principal and Interest Payments (MISMO spec 19.5)
		grid.setBorder(Alignment.Vertical.TOP, 5, border);
		
		// First Change/Amount (MISMO spec 19.6)
		grid.setBorder(Alignment.Vertical.TOP, 6, border);
		grid.setBorder(Alignment.Horizontal.RIGHT, 1, 0, border);
		grid.setCell(6, 0, new FormattedText("   First Change/Amount", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Subsequent Changes (MISMO spec 19.7)
		grid.setBorder(Alignment.Vertical.TOP, 7, border);
		grid.setCell(7, 0, new FormattedText("   Subsequent Changes", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// Minimum Payment (MISMO spec 19.8)
		grid.setBorder(Alignment.Vertical.TOP, 8, border);
		grid.setCell(8, 0, new FormattedText("   Maximum Payment", TEXT)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
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
		// MISMO UCD data
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS("LOANS/LOAN/LOAN_DETAIL"));
		if (!loanDetail.PaymentIncreaseIndicator.equalsIgnoreCase("true") || !loanDetail.InterestOnlyIndicator.equalsIgnoreCase("true"))
			return;
		AmortizationRule amortizationRule = new AmortizationRule((Element)deal.getElementAddNS("LOANS/LOAN/AMORTIZATION/AMORTIZATION_RULE"));
		InterestOnly interestOnly = new InterestOnly((Element)deal.getElementAddNS("LOANS/LOAN/INTEREST_ONLY"));
		PaymentRule paymentRule = new PaymentRule((Element)deal.getElementAddNS("LOANS/LOAN/PAYMENT/PAYMENT_RULE"));
		PrincipalAndInterestPaymentLifetimeAdjustmentRule lifeAdjustmentRule =  new PrincipalAndInterestPaymentLifetimeAdjustmentRule((Element)deal.getElementAddNS("LOANS/LOAN/ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_LIFETIME_ADJUSTMENT_RULE"));;
		PrincipalAndInterestPaymentPerChangeAdjustmentRules perAdjustmentRules = new PrincipalAndInterestPaymentPerChangeAdjustmentRules((Element)deal.getElementAddNS("LOANS/LOAN/ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_PER_CHANGE_ADJUSTMENT_RULES"));

		// Interest Only Payments (MISMO spec 19.1)
		if (loanDetail.InterestOnlyIndicator.equalsIgnoreCase("true")) {
			grid.setCell(1, 1, Yes);
			grid.setCell(1, 2,  new FormattedText("for your first " + interestOnly.InterestOnlyTermMonthsCount + " payments", TEXT));
		} else
			grid.setCell(1, 1, No);			
		
		// Optional Payments (MISMO spec 19.2)
		if (paymentRule.PaymentOptionIndicator.equalsIgnoreCase("true")) {
			grid.setCell(2, 1, Yes);
			grid.setCell(2, 2,  new FormattedText("for your first " + "TBD" + " payments", TEXT));
		} else
			grid.setCell(2, 1, No);			
		
		// Step Payments (MISMO spec 19.3)
		if (amortizationRule.AmortizationType.equalsIgnoreCase("GPM")
				|| amortizationRule.AmortizationType.equalsIgnoreCase("GraduatedPaymentARM")
				|| amortizationRule.AmortizationType.equalsIgnoreCase("Step")) {
			grid.setCell(3, 1, Yes);
			grid.setCell(3, 2,  new FormattedText("for your first " + "TBD" + " payments", TEXT));
		} else
			grid.setCell(3, 1, No);			
		
		// Seasonal Payments (MISMO spec 19.4)
		if (loanDetail.SeasonalPaymentFeatureIndicator.equalsIgnoreCase("true")) {
			grid.setCell(4, 1, Yes);
			grid.setCell(4, 2,  new FormattedText("from " + paymentRule.SeasonalPaymentPeriodStartMonth + " to " + paymentRule.SeasonalPaymentPeriodEndMonth + " each year", TEXT));
		} else
			grid.setCell(4, 1, No);			
		
		// Principal and Interest Payments (MISMO spec 19.5)
		String pmtFrequency = paymentRule.PaymentFrequencyType;
		if ("".equals(pmtFrequency))
			pmtFrequency = "Monthly";
		grid.setCell(5, 0, new FormattedText(pmtFrequency + " Principal and Interest Payments", TEXT_BOLD)).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		
		// First Change/Amount (MISMO spec 19.6)
		if (perAdjustmentRules.principalAndInterestPaymentPerChangeAdjustmentRules.length > 0) {
			String min = perAdjustmentRules.principalAndInterestPaymentPerChangeAdjustmentRules[0].PerChangeMinimumPrincipalAndInterestPaymentAmount;
			String max = perAdjustmentRules.principalAndInterestPaymentPerChangeAdjustmentRules[0].PerChangeMaximumPrincipalAndInterestPaymentAmount;
			if ("".equals(min) || "0".equals(min))
				min = max;
			String str = Formatter.TRUNCDOLLARS.format(min);
			if (!max.equals(min))
				str = str + " - " + Formatter.TRUNCDOLLARS.format(max);
			grid.setCell(6, 2,  new FormattedText(str + " at " + Formatter.INTEGERSUFFIX.format(lifeAdjustmentRule.FirstPrincipalAndInterestPaymentChangeMonthsCount) + " payment", TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		}
		
		// Subsequent Changes (MISMO spec 19.7)
		if (perAdjustmentRules.principalAndInterestPaymentPerChangeAdjustmentRules.length > 1) {
			String year = Formatter.YEARS.format(perAdjustmentRules.principalAndInterestPaymentPerChangeAdjustmentRules[1].PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount);
			grid.setCell(7, 2,  new FormattedText("Every " + (year.equals("1") ? "year" : year + " years"), TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		}
		
		// Maximum Payment (MISMO spec 19.8)
		grid.setCell(8, 2,  new FormattedText(Formatter.TRUNCDOLLARS.format(lifeAdjustmentRule.PrincipalAndInterestPaymentMaximumAmount)
					+ " starting at "
					+ Formatter.INTEGERSUFFIX.format(lifeAdjustmentRule.PrincipalAndInterestPaymentMaximumAmountEarliestEffectiveMonthsCount)
					+ " payment", TEXT)).setAlignment(Alignment.Horizontal.RIGHT);
		
		try {
			grid.draw(page, page.leftMargin, .5f, 3.75f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
