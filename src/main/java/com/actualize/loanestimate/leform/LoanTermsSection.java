package com.actualize.loanestimate.leform;

import org.w3c.dom.Element;

import com.actualize.loanestimate.mismodao.AmortizationRule;
import com.actualize.loanestimate.mismodao.BuydownOccurences;
import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.InterestOnly;
import com.actualize.loanestimate.mismodao.InterestRateLifetimeAdjustmentRule;
import com.actualize.loanestimate.mismodao.InterestRatePerChangeAdjustmentRule;
import com.actualize.loanestimate.mismodao.LoanDetail;
import com.actualize.loanestimate.mismodao.MaturityRule;
import com.actualize.loanestimate.mismodao.NegativeAmortizationRule;
import com.actualize.loanestimate.mismodao.PaymentRule;
import com.actualize.loanestimate.mismodao.PrepaymentPenaltyLifetimeRule;
import com.actualize.loanestimate.mismodao.PrincipalAndInterestPaymentLifetimeAdjustmentRule;
import com.actualize.loanestimate.mismodao.PrincipalAndInterestPaymentPerChangeAdjustmentRule;
import com.actualize.loanestimate.mismodao.TermsOfLoan;
import com.actualize.loanestimate.mismodao.UrlaDetail;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.Bullet;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.Drawable;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.LineFeed;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Paragraph;
import com.actualize.loanestimate.pdferector.Region;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;

public class LoanTermsSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text ROW_HEADER   = new Text(Color.BLACK, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 10, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 10, Typeface.CALIBRI);
	private static final Text TEXT_AMOUNT  = new Text(Color.BLACK, 14, Typeface.CALIBRI);
	private static final Text TEXT_OBLIQUE = new Text(Color.BLACK, 9, Typeface.CALIBRI_OBLIQUE);
	private static final Text TEXT_BOLD_NEW    = new Text(Color.BLACK, 11, Typeface.CALIBRI_BOLD);

	private Drawable yes = new FormattedText("YES", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f).setMargin(Alignment.Horizontal.LEFT, 0.1f);
	private Drawable no = new FormattedText("NO", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f).setMargin(Alignment.Horizontal.LEFT, 0.1f);

	private Border border = new Border(Color.BLACK, 1f/72f);
	
	private float spacing = -2f/72f;
	
	public void draw(Page page, Object object) {
		draw(page, (Deal)object);
	}

	public void draw(Page page, Deal deal) {
		
		// Data query helper strings
		String loan = "LOANS/LOAN";

		// Data containers
		AmortizationRule amortizationRule = new AmortizationRule((Element)deal.getElementAddNS(loan + "/AMORTIZATION/AMORTIZATION_RULE"));
		BuydownOccurences buydownOccurences = new BuydownOccurences((Element)deal.getElementAddNS(loan + "/BUYDOWN/BUYDOWN_OCCURRENCES"), "[BuydownTemporarySubsidyFundingIndicator='false']");
		InterestRateLifetimeAdjustmentRule interestRateLifetimeAdjustmentRule = new InterestRateLifetimeAdjustmentRule((Element)deal.getElementAddNS(loan + "/ADJUSTMENT/INTEREST_RATE_ADJUSTMENT/INTEREST_RATE_LIFETIME_ADJUSTMENT_RULE"));
		InterestRatePerChangeAdjustmentRule interestRatePerChangeAdjustmentRule = new InterestRatePerChangeAdjustmentRule((Element)deal.getElementAddNS(loan + "/ADJUSTMENT/INTEREST_RATE_ADJUSTMENT/INTEREST_RATE_PER_CHANGE_ADJUSTMENT_RULES/INTEREST_RATE_PER_CHANGE_ADJUSTMENT_RULE[AdjustmentRuleType='First']"));
		InterestOnly interestOnly = new InterestOnly((Element)deal.getElementAddNS(loan + "/INTEREST_ONLY"));
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS(loan + "/LOAN_DETAIL"));
		MaturityRule maturityRule = new MaturityRule((Element)deal.getElementAddNS(loan + "/MATURITY/MATURITY_RULE"));
		NegativeAmortizationRule negativeAmortizationRule = new NegativeAmortizationRule((Element)deal.getElementAddNS(loan + "/NEGATIVE_AMORTIZATION/NEGATIVE_AMORTIZATION_RULE"));
		PaymentRule paymentRule = new PaymentRule((Element)deal.getElementAddNS(loan + "/PAYMENT/PAYMENT_RULE"));
		PrepaymentPenaltyLifetimeRule prepaymentPenaltyLifetimeRule = new PrepaymentPenaltyLifetimeRule((Element)deal.getElementAddNS(loan + "/PREPAYMENT_PENALTY/PREPAYMENT_PENALTY_LIFETIME_RULE"));
		PrincipalAndInterestPaymentLifetimeAdjustmentRule principalAndInterestPaymentLifetimeAdjustmentRule = new PrincipalAndInterestPaymentLifetimeAdjustmentRule((Element)deal.getElementAddNS(loan + "/ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_LIFETIME_ADJUSTMENT_RULE"));
		PrincipalAndInterestPaymentPerChangeAdjustmentRule principalAndInterestPaymentPerChangeAdjustmentRule = new PrincipalAndInterestPaymentPerChangeAdjustmentRule((Element)deal.getElementAddNS(loan + "/ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_ADJUSTMENT/PRINCIPAL_AND_INTEREST_PAYMENT_PER_CHANGE_ADJUSTMENT_RULES/PRINCIPAL_AND_INTEREST_PAYMENT_PER_CHANGE_ADJUSTMENT_RULE[AdjustmentRuleType='First']"));
		TermsOfLoan loanTerms = new TermsOfLoan((Element)deal.getElementAddNS(loan + "/TERMS_OF_LOAN"));
		UrlaDetail urlaDetail = new UrlaDetail((Element)deal.getElementAddNS(loan + "/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/URLA/URLA_DETAIL"));

		float[] heights = { 20f/72f, 28f/72f, 42f/72f, 54f/72f, 20f/72f, 22f/72f, 22f/72f };
		float[] widths = { 2.1f, 1.5f, 0.4f, 3.5f };
		
		// Create grid
		Grid grid = new Grid(7, heights, widths.length, widths);
		for (int row = 0; row < grid.rows(); row++) {
			if (row != 4) grid.setBorder(Alignment.Vertical.BOTTOM, row, border);
			if (row != 0) grid.setBorder(Alignment.Horizontal.RIGHT, row, 0, border);
		}
		
		// Tab header
		grid.setCell(0, 0, new FormattedText("  Loan Terms", TAB_TEXT))
			.setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setBackground(new Tab(widths[0]));
		grid.setCell(0, 2, new FormattedText("  Can this amount increase after closing?", ROW_HEADER))
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setShade(Color.LIGHT_GRAY);
		grid.setCell(0, 3, new Drawable().setShade(Color.LIGHT_GRAY));		

		// Loan amount (MISMO spec 4.1)
		grid.setCell(1, 0, new FormattedText("  Loan Amount", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		grid.setCell(1, 1, new FormattedText(Formatter.ZEROTRUNCDOLLARS.format(loanAmount(loanTerms, urlaDetail)), TEXT_AMOUNT)
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP,2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		if ("true".equalsIgnoreCase(loanDetail.LoanAmountIncreaseIndicator)) {
			grid.setCell(1, 2, new FormattedText("YES", TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
			Region explain = new Region().setSpacing(spacing);
			String prefix = negativeAmortizationRule.NegativeAmortizationType.equalsIgnoreCase("ScheduledNegativeAmortization") ? "Will" : "Can";
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText(prefix + " go ", TEXT))
				.append(new FormattedText("as high as " + Formatter.DOLLARS.format(negativeAmortizationRule.NegativeAmortizationMaximumLoanBalanceAmount), TEXT_BOLD)));
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText(prefix + " increase until year " + Formatter.YEARS.format(negativeAmortizationRule.NegativeAmortizationLimitMonthsCount), TEXT)));
			grid.setCell(1, 3,  explain.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		} else {
			grid.setCell(1, 2, new FormattedText("NO", TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		}
		
		// Interest rate (MISMO spec 4.2)
		grid.setCell(2, 0, new FormattedText("  Interest Rate", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 3f/72f));
		grid.setCell(2, 1, new FormattedText(Formatter.PERCENT.format(interestRate(loanDetail, loanTerms, buydownOccurences, amortizationRule)), TEXT_AMOUNT)
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		if ("true".equalsIgnoreCase(loanDetail.InterestRateIncreaseIndicator)) {
			grid.setCell(2, 2, new FormattedText("YES", TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 3f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
			Region explain = new Region().setSpacing(spacing);
			String years = Formatter.YEARS.format(interestRatePerChangeAdjustmentRule.PerChangeRateAdjustmentFrequencyMonthsCount);
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText("Adjusts ", TEXT))
				.append(new FormattedText("every " + ("1".equals(years) ? "year " : (years + " years ")), TEXT_BOLD))
				.append(new FormattedText("starting in year " + Formatter.YEARSPLUSONE.format(interestRateLifetimeAdjustmentRule.FirstRateChangeMonthsCount), TEXT)));
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText("Can go ", TEXT))
				.append(new FormattedText("as high as " + Formatter.PERCENT.format(interestRateLifetimeAdjustmentRule.CeilingRatePercent) + " ", TEXT_BOLD))
				.append(new FormattedText("in year " + Formatter.YEARS.format(interestRateLifetimeAdjustmentRule.CeilingRatePercentEarliestEffectiveMonthsCount), TEXT)));
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText("See ", TEXT))
				.append(new FormattedText("AIR Table on page 2 ", TEXT_BOLD))
				.append(new FormattedText("for details", TEXT)));
			grid.setCell(2, 3, explain).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 3f/72f);
		} else {
			grid.setCell(2, 2, new FormattedText("NO", TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 3f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));			
		}
		
		// [Period] principal and interest (MISMO spec 4.3)
		Region region = new Region().setSpacing(4f/72f);
		region.append(new FormattedText("  Monthly Principal & Interest", ROW_HEADER)).append(new LineFeed(0f))
			.append(new FormattedText("  See Projected Payments below for your", TEXT_OBLIQUE)).append(new LineFeed(-10f/72f))
			.append(new FormattedText("  Estimated Total Monthly Payment", TEXT_OBLIQUE));
		grid.setCell(3, 0, region.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f).setWrappable(true));
		grid.setCell(3, 1, new FormattedText(Formatter.DOLLARS.format(principalAndInterest(paymentRule)), TEXT_AMOUNT)
			.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		if ("true".equalsIgnoreCase(loanDetail.PaymentIncreaseIndicator)) {
			grid.setCell(3, 2, yes).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f);
			Region explain = new Region().setSpacing(spacing);
			String years = Formatter.YEARS.format(principalAndInterestPaymentPerChangeAdjustmentRule.PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount);
			if (!"".equalsIgnoreCase(principalAndInterestPaymentPerChangeAdjustmentRule.PerChangePrincipalAndInterestPaymentAdjustmentFrequencyMonthsCount))
				explain.append(new Paragraph()
					.append(Bullet.BULLET)
					.append(new FormattedText("Adjusts ", TEXT))
					.append(new FormattedText("every " + ("1".equals(years) ? "year " : (years + " years ")), TEXT_BOLD))
					.append(new FormattedText("starting in year " + Formatter.YEARSPLUSONE.format(principalAndInterestPaymentLifetimeAdjustmentRule.FirstPrincipalAndInterestPaymentChangeMonthsCount), TEXT)));
			if (!"".equals(principalAndInterestPaymentLifetimeAdjustmentRule.PrincipalAndInterestPaymentMaximumAmount))
				explain.append(new Paragraph()
					.append(Bullet.BULLET)
					.append(new FormattedText("Can go ", TEXT))
					.append(new FormattedText("as high as " + Formatter.TRUNCDOLLARS.format(principalAndInterestPaymentLifetimeAdjustmentRule.PrincipalAndInterestPaymentMaximumAmount) + " ", TEXT_BOLD))
					.append(new FormattedText("in year " + Formatter.ROUNDUPYEARS.format(principalAndInterestPaymentLifetimeAdjustmentRule.PrincipalAndInterestPaymentMaximumAmountEarliestEffectiveMonthsCount), TEXT)));
			if ("true".equals(loanDetail.InterestOnlyIndicator) && !"".equals(interestOnly.InterestOnlyTermMonthsCount)) {
				explain.append(new Paragraph()
					.append(Bullet.BULLET)
					.append(new FormattedText("Includes ", TEXT))
					.append(new FormattedText("only interest ", TEXT_BOLD))
					.append(new FormattedText("and ", TEXT))
					.append(new FormattedText("no principal ", TEXT_BOLD))
					.append(new FormattedText("until " + Formatter.MONTHSORYEARS.format(interestOnly.InterestOnlyTermMonthsCount), TEXT)));
				explain.append(new Paragraph()
					.append(Bullet.BULLET)
					.append(new FormattedText("See ", TEXT))
					.append(new FormattedText("AP Table on page 2 ", TEXT_BOLD))
					.append(new FormattedText("for details", TEXT)));
			}
			grid.setCell(3, 3, explain).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f);
		} else {
			grid.setCell(3, 2, no).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 1f/72f);		
		}
		
		// Prepayment penalty (MISMO spec 4.4)
		grid.setCell(5, 0, new FormattedText("  Prepayment Penalty", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		grid.setCell(4, 2, new FormattedText("  Does the loan have these features?", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f))
			.setShade(Color.LIGHT_GRAY);
		grid.setCell(4, 3, new Drawable().setShade(Color.LIGHT_GRAY));		
		if ("true".equalsIgnoreCase(loanDetail.PrepaymentPenaltyIndicator)) {
			grid.setCell(5, 2, new FormattedText("YES",TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
			Region explain = new Region().setSpacing(spacing);
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText("As high as " + Formatter.TRUNCDOLLARS.format(prepaymentPenaltyLifetimeRule.PrepaymentPenaltyMaximumLifeOfLoanAmount) + " ", TEXT_BOLD))
				.append(new FormattedText("if you pay off the loan within the first " + Formatter.YEARS.format(prepaymentPenaltyLifetimeRule.PrepaymentPenaltyExpirationMonthsCount) + " years", TEXT)));
			grid.setCell(5, 3, explain.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		} else {
			grid.setCell(5, 2, new FormattedText("NO",TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		}
		
		// Balloon payment (MISMO spec 4.5)
		grid.setCell(6, 0, new FormattedText("  Balloon Payment", ROW_HEADER).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		if ("true".equalsIgnoreCase(loanDetail.BalloonIndicator)) {
			grid.setCell(6, 2, new FormattedText("YES",TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
			Region explain = new Region().setSpacing(spacing);
			explain.append(new Paragraph()
				.append(Bullet.BULLET)
				.append(new FormattedText("You will have to pay " + Formatter.TRUNCDOLLARS.format(loanDetail.BalloonPaymentAmount) + " at the end of year " + Formatter.YEARS.format(maturityRule.LoanMaturityPeriodCount), TEXT)));
			grid.setCell(6, 3,  explain.setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f));
		} else {
			grid.setCell(6, 2, new FormattedText("NO",TEXT_BOLD_NEW).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setAlignment(Alignment.Horizontal.LEFT).setMargin(Alignment.Horizontal.LEFT, 8f/72f));
		}
		
		// Draw
		try {
			grid.draw(page, page.leftMargin, 5.4f, page.width - page.leftMargin - page.rightMargin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public float height(Page page) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static String interestRate(LoanDetail loanDetail, TermsOfLoan loanTerms, BuydownOccurences buydownOccurences, AmortizationRule amortizationRule) {
		if ("true".equalsIgnoreCase(amortizationRule.AmortizationType))
			return loanTerms.DisclosedFullyIndexedRatePercent;
		if (!"".equals(loanTerms.WeightedAverageInterestRatePercent))
			return loanTerms.WeightedAverageInterestRatePercent;
		if ("true".equalsIgnoreCase(loanDetail.BuydownTemporarySubsidyFundingIndicator) && buydownOccurences.buydownOccurences.length != 0)
			return buydownOccurences.buydownOccurences[buydownOccurences.buydownOccurences.length - 1].BuydownInitialEffectiveInterestRatePercent;
		return loanTerms.NoteRatePercent;
	}
	
	public static String loanAmount(TermsOfLoan loanTerms, UrlaDetail urlaDetail) {
		if ("".equals(loanTerms.NoteAmount))
			return urlaDetail.BorrowerRequestedLoanAmount;
		return loanTerms.NoteAmount;
	}
	
	public static String principalAndInterest(PaymentRule paymentRule) {
		if ("".equals(paymentRule.InitialPrincipalAndInterestPaymentAmount))
			return paymentRule.FullyIndexedInitialPrincipalAndInterestPaymentAmount;
		return paymentRule.InitialPrincipalAndInterestPaymentAmount;
	}
}
