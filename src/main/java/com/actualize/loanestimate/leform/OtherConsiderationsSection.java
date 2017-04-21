package com.actualize.loanestimate.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.LateChargeRule;
import com.actualize.loanestimate.mismodao.LoanDetail;
import com.actualize.loanestimate.mismodao.TermsOfLoan;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.BoxedCharacter;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Paragraph;
import com.actualize.loanestimate.pdferector.Region;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;
import com.actualize.loanestimate.pdferector.Drawable.Alignment.Vertical;

public class OtherConsiderationsSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 10.5f, Typeface.CALIBRI);
	private static final Text TEXT_BOLD    = new Text(Color.BLACK, 10.5f, Typeface.CALIBRI_BOLD);
	private static final Text TEXT_ITALIC    = new Text(Color.BLACK, 10.5f, Typeface.CALIBRI_OBLIQUE);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	private static final float leftIndent  = 5f/72f;
	
	private Grid grid;
	
	private enum DisclosureType { Construction, Other, Refinance }
	
	public OtherConsiderationsSection(Page page, Object object) {
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
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS("LOANS/LOAN/LOAN_DETAIL"));
		LateChargeRule lateRule = new LateChargeRule((Element)deal.getElementAddNS("LOANS/LOAN/LATE_CHARGE/EXTENSION/OTHER/gse:LATE_CHARGE_RULES/LATE_CHARGE_RULE"));
		TermsOfLoan loanTerms = new TermsOfLoan((Element)deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN"));

		// Set disclosure type
		DisclosureType disclosureType = DisclosureType.Other;
		if (loanDetail.ConstructionLoanIndicator.equalsIgnoreCase("true"))
			disclosureType = DisclosureType.Construction;
		else if (loanTerms.LoanPurposeType.equalsIgnoreCase("Refinance"))
			disclosureType = DisclosureType.Refinance;

		// Create grid
		float[] heights1 = { 16f/72f, 42f/72f, 42f/72f, 30f/72f, 30f/72f, 42f/72f, 18f/72f, 30f/72f, 42f/72f };
		float[] heights2 = { 16f/72f, 42f/72f, 42f/72f, 30f/72f, 30f/72f, 18f/72f, 30f/72f, 42f/72f };
		float[] heights3 = { 16f/72f, 42f/72f, 42f/72f, 30f/72f, 30f/72f, 42f/72f, 18f/72f, 30f/72f, 42f/72f };
		float[] widths = { 1.6f, 5.9f };
		switch (disclosureType) {
		case Construction:
			grid = new Grid(heights3.length, heights3, widths.length, widths);
			break;
		case Other:
			grid = new Grid(heights2.length, heights2, widths.length, widths);
			break;
		case Refinance:
			grid = new Grid(heights1.length, heights1, widths.length, widths);
			break;
		}
		int line = 0;
				
		// Tab header (MISMO spec 17.0/26.0)
		grid.setCell(0, 0, new FormattedText("Other Considerations", TAB_TEXT)).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(1.90f));
		grid.setBorder(Vertical.BOTTOM, 0, border);

		// Appraisal (MISMO spec 26.1)
		grid.setCell(++line, 0, new FormattedText("Appraisal", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new Region()
			.append(new FormattedText("We may order an appraisal to determine the property\'s value and charge you for this", TEXT))
			.append(new FormattedText("appraisal. We will promptly give you a copy of any appraisal, even if your loan does not close.", TEXT))
			.append(new FormattedText("You can pay for an additional appraisal for your own use at your own cost.", TEXT))).setAlignment(Alignment.Vertical.TOP);

		// Assumption (MISMO spec 17.1)
		grid.setCell(++line, 0, new FormattedText("Assumption", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new Region()
			.append(new FormattedText("If you sell or transfer this property to another person, we", TEXT))
			.append(new Paragraph().append(loanDetail.AssumabilityIndicator.equalsIgnoreCase("true") ? BoxedCharacter.CHECK_BOX_NO : BoxedCharacter.CHECK_BOX_EMPTY)
					.append(new FormattedText("  will allow, under certain conditions, this person to assume this loan on the original terms.", TEXT)))
			.append(new Paragraph().append(loanDetail.AssumabilityIndicator.equalsIgnoreCase("true") ? BoxedCharacter.CHECK_BOX_EMPTY : BoxedCharacter.CHECK_BOX_NO)
					.append(new FormattedText("  will not allow assumption of this loan on the original terms.", TEXT)))).setAlignment(Alignment.Vertical.TOP);

		// Construction
		if (disclosureType == DisclosureType.Construction) {
			grid.setCell(++line, 0, new Region().append(new FormattedText("Construction", TEXT_BOLD))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(line, 1, new Region()
				.append(new FormattedText("You may receive a revised Loan Estimate at any time prior to 60 days before consummation.", TEXT))).setAlignment(Alignment.Vertical.TOP);
		}

		// Homeowner's Insurance
		grid.setCell(++line, 0, new Region().append(new FormattedText("Homeowner\'s", TEXT_BOLD)).append(new FormattedText("Insurance", TEXT_BOLD))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new Region()
			.append(new FormattedText("This loan requires homeowner\'s insurance on the property, which you may obtain from a", TEXT))
			.append(new FormattedText("company of your choice that we find acceptable.", TEXT))).setAlignment(Alignment.Vertical.TOP);

		// Late Payment (MISMO spec 17.3)
		String maximum = lateRule.LateChargeMaximumAmount;
		String minimum = lateRule.LateChargeMinimumAmount;
		Region region = new Region().append(
			new Paragraph()
				.append(new FormattedText("If your payment is more than ", TEXT))
				.append(new FormattedText(lateRule.LateChargeGracePeriodDaysCount, TEXT_ITALIC))
				.append(new FormattedText(" days late, we will charge a late fee of ", TEXT))
				.append(new FormattedText(Formatter.PERCENT.format(lateRule.LateChargeRatePercent) + " of the", TEXT_ITALIC)));
		Paragraph para = new Paragraph();
		para.append(new FormattedText(lateChargeText(lateRule.LateChargeType), TEXT_ITALIC));
		if ("".equals(maximum)) {
			if (!"".equals(minimum))
				para.append(new FormattedText(" but no less than ", TEXT_ITALIC)).append(new FormattedText(Formatter.DOLLARS.format(minimum), TEXT_ITALIC));
			para.append(new FormattedText(".", TEXT_ITALIC));
		} else {
			if ("".equals(minimum))
				para.append(new FormattedText(" not to exceed ", TEXT_ITALIC));
			else
				para.append(new FormattedText(". Late fee to be no less than ", TEXT_ITALIC)).append(new FormattedText(Formatter.DOLLARS.format(minimum), TEXT_ITALIC)).append(new FormattedText(" and will not exceed ", TEXT_ITALIC));
			para.append(new FormattedText(Formatter.DOLLARS.format(maximum), TEXT_ITALIC)).append(new FormattedText(".", TEXT_ITALIC));
		}
		grid.setCell(++line, 0, new Region().append(new FormattedText("Late Payment", TEXT_BOLD))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);;
		region.append(para);
		grid.setCell(line, 1, region).setAlignment(Alignment.Vertical.TOP);

		// Liability after Foreclosure
		if (disclosureType == DisclosureType.Refinance) {
			grid.setCell(++line, 0, new Region().append(new FormattedText("Liability after", TEXT_BOLD)).append(new FormattedText("Foreclosure", TEXT_BOLD))).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
			grid.setCell(line, 1, new Region()
				.append(new FormattedText("Taking this loan could end any state law protection you may currently have against liability for", TEXT))
				.append(new FormattedText("unpaid debt if your lender forecloses on your home. If you lose this protection, you may have to pay", TEXT))
				.append(new FormattedText("any debt remaining even after foreclosure. You may want to consult a lawyer for more information.", TEXT))).setAlignment(Alignment.Vertical.TOP);
		}

		// Loan Acceptance (MISMO spec 26.2)
		grid.setCell(++line, 0, new FormattedText("Loan Acceptance", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new FormattedText("You do not have to accept this loan because you have received this form or signed a loan application.", TEXT)).setAlignment(Alignment.Vertical.TOP);

		// Refinance
		grid.setCell(++line, 0, new FormattedText("Refinance", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new Region()
			.append(new FormattedText("Refinancing this loan will depend on your future financial situation, the property value, and", TEXT))
			.append(new FormattedText("market conditions. You may not be able to refinance this loan.", TEXT))).setAlignment(Alignment.Vertical.TOP);

		// Servicing (MISMO spec 26.3)
		grid.setCell(++line, 0, new FormattedText("Servicing", TEXT_BOLD)).setAlignment(Alignment.Vertical.TOP).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, leftIndent);
		grid.setCell(line, 1, new Region()
			.append(new FormattedText("We intend", TEXT))
			.append(new Paragraph().append(loanDetail.CreditorServicingOfLoanStatementType.equalsIgnoreCase("CreditorIntendsToServiceLoan") ? BoxedCharacter.CHECK_BOX_NO : BoxedCharacter.CHECK_BOX_EMPTY)
					.append(new FormattedText("  to service your loan. If so, you will make your payments to us.", TEXT)))
			.append(new Paragraph().append(loanDetail.CreditorServicingOfLoanStatementType.equalsIgnoreCase("CreditorIntendsToTransferServicingOfLoan") ? BoxedCharacter.CHECK_BOX_NO : BoxedCharacter.CHECK_BOX_EMPTY)
					.append(new FormattedText("  to transfer servicing of your loan.", TEXT)))).setAlignment(Alignment.Vertical.TOP);
		
		try {
			grid.draw(page, page.leftMargin, 2.0f, 7.5f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
	
	private String lateChargeText(String lateChargeType) {
		switch (lateChargeType) {
		case "FlatDollarAmount":
			return "";
		case "NoLateCharges":
			return "";
		case "PercentageOfPrincipalAndInterest":
		case "PercentOfPrincipalAndInterest":
			return "monthly principal and interest payment";
		case "PercentageOfDelinquentInterest":
			return "delinquent interest";
		case "PercentageOfNetPayment":
			return "net payment";
		case "PercentageOfPrincipalBalance":
			return "principal balance";
		case "PercentageOfTotalPayment":
			return "total payment";
		}
		return "";
	}
}
