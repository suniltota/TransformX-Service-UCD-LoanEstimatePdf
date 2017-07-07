package com.actualize.mortgage.leform;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import com.actualize.mortgage.mismodao.Deal;
import com.actualize.mortgage.mismodao.EstimatedPropertyCost;
import com.actualize.mortgage.mismodao.EstimatedPropertyCostComponent;
import com.actualize.mortgage.mismodao.EstimatedPropertyCostDetail;
import com.actualize.mortgage.mismodao.InterestOnly;
import com.actualize.mortgage.mismodao.LoanDetail;
import com.actualize.mortgage.mismodao.ProjectedPayment;
import com.actualize.mortgage.mismodao.ProjectedPayments;
import com.actualize.mortgage.pdferector.Border;
import com.actualize.mortgage.pdferector.BoxedCharacter;
import com.actualize.mortgage.pdferector.Color;
import com.actualize.mortgage.pdferector.Drawable;
import com.actualize.mortgage.pdferector.FormattedText;
import com.actualize.mortgage.pdferector.Grid;
import com.actualize.mortgage.pdferector.LineFeed;
import com.actualize.mortgage.pdferector.Page;
import com.actualize.mortgage.pdferector.Paragraph;
import com.actualize.mortgage.pdferector.Region;
import com.actualize.mortgage.pdferector.Section;
import com.actualize.mortgage.pdferector.Spacer;
import com.actualize.mortgage.pdferector.Text;
import com.actualize.mortgage.pdferector.Typeface;
import com.actualize.mortgage.pdferector.Drawable.Alignment;
import com.actualize.mortgage.pdferector.Drawable.Alignment.Horizontal;
import com.actualize.mortgage.pdferector.Grid.Direction;

public class ProjectedPaymentsSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text ESCROW_TEXT  = new Text(Color.BLACK, 11, Typeface.CALIBRI_BOLD);
	private static final Text ROW_HEADER   = new Text(Color.BLACK, 11.5f, Typeface.CALIBRI_BOLD);
	private static final Text ESCROW_ROW_HEADER   = new Text(Color.BLACK, 10f, Typeface.CALIBRI_BOLD);
	private static final Text TEXT_REGULAR = new Text(Color.BLACK, 11.5f, Typeface.CALIBRI);
	private static final Text ESCROW_TEXT_REGULAR = new Text(Color.BLACK, 10f, Typeface.CALIBRI);
	private static final Text TEXT_TOTAL   = new Text(Color.BLACK, 14, Typeface.CALIBRI);
	private static final Text TEXT_OBLIQUE = new Text(Color.BLACK, 9, Typeface.CALIBRI_OBLIQUE);

	private static final Border border = new Border(Color.BLACK, 1f/72f);
	private static final Border ESCROW_BORDER = new Border(Color.BLACK, 0.7f/72f);
	private static final Border thickborder = new Border(Color.BLACK, 2f/72f);
	
	private static final float spacing = -2f/72f;

	private static final Drawable yes = new FormattedText("YES", ESCROW_ROW_HEADER);
	private static final Drawable no = new FormattedText("NO", ESCROW_ROW_HEADER);
	private static final Drawable some = new FormattedText("SOME", ESCROW_ROW_HEADER);

	private Grid paymentsGrid = null;
	private Grid taxesInsGrid = null;
	private Grid escrowGrid = null;

	public void createGrids(Page page, Deal deal) {
		Region region;
		
		// Create payments grid
		ProjectedPayments payments = new ProjectedPayments((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/PROJECTED_PAYMENTS"));
		int numPayments = payments.projectedPayments.length==0 ? 1 : payments.projectedPayments.length;
		float[] pmtheights = { 20f/72f, 20f/72f, 31f/72f, 22f/72f, 30f/72f, 36f/72f };
		float[] pmtwidths = { 2.1f, 5.4f / numPayments };
		paymentsGrid = new Grid(pmtheights.length, pmtheights, numPayments + 1, pmtwidths);
		paymentsGrid.setBorder(Alignment.Horizontal.RIGHT, 0, border);
		paymentsGrid.setBorder(Alignment.Horizontal.RIGHT, 0, 0, null);
		paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 0, 0, border);
		paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 1, 0, border);
		paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 4, 0, ESCROW_BORDER);
		paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 5, thickborder);

		// Tab header
		paymentsGrid.setCell(0, 0, new FormattedText("  Projected Payments", TAB_TEXT))
			.setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setBackground(new Tab(pmtwidths[0]));

		// Payment calculation (MISMO spec 5.1)
		paymentsGrid.setCell(1, 0, new FormattedText("  Payment Calculation", ROW_HEADER)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setShade(Color.LIGHT_GRAY);
		
		// Principal & interest (MISMO spec 5.2)
		paymentsGrid.setCell(2, 0, new FormattedText("   Principal & Interest", TEXT_REGULAR)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setMargin(Alignment.Horizontal.LEFT, 6f/72f);

		// Mortgage insurance (MISMO spec 5.3)
		paymentsGrid.setCell(3, 0, new FormattedText("   Mortgage Insurance", TEXT_REGULAR)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setMargin(Alignment.Horizontal.LEFT, 6f/72f);

		// Estimated escrow (MISMO spec 5.4)
		region = new Region().setSpacing(-2f/72f).append(new FormattedText("   Estimated Escrow", ESCROW_TEXT)).append(new FormattedText("    Amount can increase over time", TEXT_OBLIQUE));
		paymentsGrid.setCell(4, 0, region).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setMargin(Alignment.Horizontal.LEFT, 6f/72f);
		
		// Create taxes and insurance grid
		float[] escheights = { 11.5f/72f, 11.5f/72f, 11.5f/72f, 11.5f/72f, 21f/72f };
		float[] escwidths = { 2.7f, 1.35f };
		escrowGrid = new Grid(escheights.length, escheights, escwidths.length, escwidths);
		escrowGrid.setCell(0, 0, new FormattedText("This estimate includes", ESCROW_ROW_HEADER));
		escrowGrid.getCell(0, 0).setMargin(Horizontal.LEFT, 9f/72f);
		escrowGrid.setCell(0, 1, new FormattedText("In escrow?", ESCROW_ROW_HEADER));
		region = new Region()
			.append(new FormattedText("See Section G on page 2 for escrowed property costs. You must pay for other", TEXT_OBLIQUE))
			.append(new FormattedText("property costs separately.", TEXT_OBLIQUE));
		escrowGrid.setCell(4, 0, region);
		escrowGrid.getCell(4, 0).setMargin(Horizontal.LEFT, 9f/72f);
		
		// Estimated Taxes, insurance & Assessments (MISMO spec 5.6)
		float[] tiheights = { 75f/72f };
		float[] tiwidths = { 2.1f, 1.35f, 4.05f };
		taxesInsGrid = new Grid(tiheights.length, tiheights, tiwidths.length, tiwidths);
		region = new Region().setSpacing(-2f/72f)
			.append(new FormattedText("Estimated Taxes, Insurance", ROW_HEADER)).append(new FormattedText("& Assessments", ROW_HEADER)).append(new LineFeed(5f/72f))
			.append(new FormattedText("Amount can increase over time", TEXT_OBLIQUE));
		taxesInsGrid.setCell(0, 0, region).setAlignment(Alignment.Vertical.MIDDLE).setMargin(Alignment.Horizontal.LEFT, 6f/72f);
		taxesInsGrid.setCell(0, 2, escrowGrid);
		taxesInsGrid.setBorder(Alignment.Horizontal.RIGHT, 0, border);
		taxesInsGrid.setBorder(Alignment.Vertical.BOTTOM, 0, border);
	}
	
	public void draw(Page page, Object object) {
		try {
			createGrids(page, (Deal)object);
			draw(page, (Deal)object);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws XPathExpressionException {
		
		Boolean valueRounded = false;
		
		EstimatedPropertyCost propertyCost = new EstimatedPropertyCost((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/ESTIMATED_PROPERTY_COST"));
		LoanDetail loanDetail = new LoanDetail((Element)deal.getElementAddNS("LOANS/LOAN/LOAN_DETAIL"));
		ProjectedPayments payments = new ProjectedPayments((Element)deal.getElementAddNS("LOANS/LOAN/DOCUMENT_SPECIFIC_DATA_SETS/DOCUMENT_SPECIFIC_DATA_SET/INTEGRATED_DISCLOSURE/PROJECTED_PAYMENTS"));
		
		// Estimate Total Period Payments (MISMO spec 5.5)
		String str = "Period";
		if (payments.projectedPayments.length > 0)
			str = payments.projectedPayments[0].PaymentFrequencyType;
		Region region = new Region().setSpacing(spacing).append(new FormattedText("   Estimated Total", ROW_HEADER)).append(new FormattedText("   Monthly Payment", ROW_HEADER));
		paymentsGrid.setCell(5, 0, region).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f).setMargin(Alignment.Horizontal.LEFT, 6f/72f);

		// Estimated Taxes, insurance & Assessments (MISMO spec 5.6.1)
		EstimatedPropertyCostDetail propertyCostDetail = propertyCost.estimatedPropertyCostDetail;
		region = new Region().setSpacing(spacing)
			.append(new LineFeed(16.5f/72f))
			.append(new FormattedText(Formatter.TRUNCDOLLARS.format(propertyCostDetail.ProjectedPaymentEstimatedTaxesInsuranceAssessmentTotalAmount), TEXT_TOTAL)).setSpacing(2f/72f)
			.append(new FormattedText("a month",Text.SECTION_TEXT));
		taxesInsGrid.setCell(0, 1, region).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Horizontal.LEFT, 12f/72f);
		
		// Estimated property taxes (MISMO spec 5.6.3)
		Paragraph paragraph = new Paragraph();
		EstimatedPropertyCostComponent component = findComponent(propertyCost, "PropertyTaxes");
		if (component != null && !component.ProjectedPaymentEscrowedType.equalsIgnoreCase("")) {
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
			//escrowGrid.setCell(1, 1, component.ProjectedPaymentEscrowedType.equalsIgnoreCase("Escrowed") ? yes : no);
			Drawable escrow = findEscow(component);
			escrowGrid.setCell(1, 1, escrow);
			//escrowGrid.getCell(1, 1).setMargin(Horizontal.LEFT, 9f/72f);
		} else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		escrowGrid.setCell(1, 0, paragraph.append(new FormattedText("  Property Taxes", ESCROW_TEXT_REGULAR)));
		escrowGrid.getCell(1, 0).setMargin(Horizontal.LEFT, 9.5f/72f);
		
		// Estimated homeowner's insurance (MISMO spec 5.6.4)
		paragraph = new Paragraph();
		component = findComponent(propertyCost, "HomeownersInsurance");
		if (component != null && !component.ProjectedPaymentEscrowedType.equalsIgnoreCase("")) {
			paragraph.append(BoxedCharacter.CHECK_BOX_NO);
			Drawable escrow = findEscow(component);
			//escrowGrid.setCell(2, 1, component.ProjectedPaymentEscrowedType.equalsIgnoreCase("Escrowed") ? yes : no);
			escrowGrid.setCell(2, 1, escrow);
		} else
			paragraph.append(BoxedCharacter.CHECK_BOX_EMPTY);
		escrowGrid.setCell(2, 0, paragraph.append(new FormattedText("  Homeowner\'s Insurance", ESCROW_TEXT_REGULAR)));
		escrowGrid.getCell(2, 0).setMargin(Horizontal.LEFT, 9.5f/72f);
		// Estimated other (MISMO spec 5.6.3)
		Drawable escrow = findOtherEscow(propertyCost);
		paragraph = new Paragraph().append(escrow == null ? BoxedCharacter.CHECK_BOX_EMPTY : BoxedCharacter.CHECK_BOX_NO);
		escrowGrid.setCell(3, 0, paragraph.append(findOtherComponents(propertyCost)));
		escrowGrid.getCell(3, 0).setMargin(Horizontal.LEFT, 9.5f/72f);
		escrowGrid.setCell(3, 1, escrow);
		
		// Draw each set of projected payments
		boolean hasMI = false;
		for (int i = 0; i < payments.projectedPayments.length && i < 4; i++) {
			ProjectedPayment payment = payments.projectedPayments[i];

			int col = i + 1;
			
			// Paint borders
			if (col < paymentsGrid.columns()-1) {
				paymentsGrid.setBorder(Alignment.Horizontal.RIGHT, col, border);
				paymentsGrid.setBorder(Alignment.Horizontal.RIGHT, 0, col, null);
			}
			paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 0, col, border);
			paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 1, col, border);
			paymentsGrid.setBorder(Alignment.Vertical.BOTTOM, 4, col, ESCROW_BORDER);
			

			// Ensure max and min P&I payments are in order
			String maxPI = payment.ProjectedPaymentPrincipalAndInterestMaximumPaymentAmount;
			String minPI = payment.ProjectedPaymentPrincipalAndInterestMinimumPaymentAmount;
			if (Formatter.doubleValue(minPI) == 0) {
				minPI = maxPI;
				maxPI = "";
			} else if (Formatter.doubleValue(maxPI) == 0) {
				maxPI = "";
			}

			// Ensure max and min total payments are in order
			String maxTL = payment.ProjectedPaymentEstimatedTotalMaximumPaymentAmount;
			String minTL = payment.ProjectedPaymentEstimatedTotalMinimumPaymentAmount;
			if (Formatter.doubleValue(minTL) == 0) {
				minTL = maxTL;
				maxTL = "";
			} else if (Formatter.doubleValue(maxTL) == 0) {
				maxTL = "";
			}
			
			// Payment calculation years header (MISMO spec 5.1.1)
			if (payment.ProjectedPaymentCalculationPeriodStartNumber.equals(payment.ProjectedPaymentCalculationPeriodEndNumber))
				str = "Year " + payment.ProjectedPaymentCalculationPeriodStartNumber;
			else
				str = "Years " + payment.ProjectedPaymentCalculationPeriodStartNumber + "-" + payment.ProjectedPaymentCalculationPeriodEndNumber;
			paymentsGrid.setCell(1, col, new FormattedText(str, ROW_HEADER)).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
				.setAlignment(Alignment.Horizontal.CENTER).setShade(Color.LIGHT_GRAY);
			
			// Principal & interest (MISMO spec 5.2.1)
			Formatter formatter = null;
			Paragraph mPI = new Paragraph();
			Spacer pISpacer = new Spacer(.5f, 0);
			FormattedText minPIAmount = null;
			FormattedText maxPIAmount = null;
			FormattedText dPIAmount = null;
			if (maxPI.equals("")) { 
				formatter = Formatter.DOLLARS; 
			} else { 
				//round p&I when it is not a range then also round the total
					valueRounded = true;
					formatter = Formatter.TRUNCDOLLARS;
			}
			
			region = new Region().setSpacing(spacing);
			minPIAmount = new FormattedText(formatter.format(minPI), TEXT_REGULAR);
			maxPIAmount = new FormattedText(formatter.format(maxPI), TEXT_REGULAR);
			try {
				pISpacer = new Spacer(.6f - minPIAmount.width(page), 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(maxPI.equals(""))
			{
				try {
					minPIAmount = getAmount(formatter.format(minPI), TEXT_REGULAR);
					dPIAmount = getAmountDecimal(formatter.format(minPI), TEXT_REGULAR);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mPI.append(pISpacer).append(minPIAmount).append(dPIAmount);
			}
			else
			{
				try {
					pISpacer = new Spacer(.5f - minPIAmount.width(page), 0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mPI.append(pISpacer).append(minPIAmount).append(new FormattedText(" min", TEXT_REGULAR));
			}
			region.append(mPI);
			if (!maxPI.equals(""))
			{
				try {
					pISpacer = new Spacer(.5f - maxPIAmount.width(page), 0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				region.append(new Paragraph().append(pISpacer).append(maxPIAmount).append(new FormattedText(" max", TEXT_REGULAR)));
			}
			//boolean interestOnly = false;
			InterestOnly interestOnly = new InterestOnly((Element)deal.getElementAddNS("LOANS/LOAN/INTEREST_ONLY"));
			String monthscount  = interestOnly.InterestOnlyTermMonthsCount;
			int interestOnlyTermMonthsCount = 0;
			if(null != monthscount && !"".equalsIgnoreCase(monthscount)){
				interestOnlyTermMonthsCount = Integer.parseInt(monthscount);
			}
			//int interestOnlyTermMonthsCount = Integer.parseInt(monthscount);
			int startYear = 0;
			if(null != payment.ProjectedPaymentCalculationPeriodStartNumber && !payment.ProjectedPaymentCalculationPeriodStartNumber.isEmpty())
				startYear = Integer.parseInt(payment.ProjectedPaymentCalculationPeriodStartNumber);
            	
			if ((startYear-1)*12 < interestOnlyTermMonthsCount && loanDetail.InterestOnlyIndicator.equalsIgnoreCase("true"))
				region.append(new FormattedText("only interest", TEXT_OBLIQUE));
			paymentsGrid.setCell(2, col, region).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
				.setAlignment(Alignment.Horizontal.CENTER).setMargin(Alignment.Horizontal.RIGHT, 6f/72f);
			
			// Mortgage insurance (MISMO spec 5.3.1)
			String add = "+";
			String amount = "0";
			if (col == 1 && Formatter.doubleValue(payment.ProjectedPaymentMIPaymentAmount) > 0.01){
				hasMI = true;
				//always round when loan has MI
				valueRounded = true;
			}
			if (Formatter.doubleValue(payment.ProjectedPaymentMIPaymentAmount) == 0) {
				if (hasMI) {
					amount = "-----";
					add = "";
				}
			} else {
				amount = Formatter.TRUNCNUMBER.format(payment.ProjectedPaymentMIPaymentAmount);
			}	
			FormattedText ftAdd = new FormattedText(add, TEXT_REGULAR);
			FormattedText ftAmount = new FormattedText(amount, TEXT_REGULAR);
			Spacer spacer = new Spacer(.5f, 0);
			try {
				spacer = new Spacer(.6f - ftAmount.width(page) - ftAdd.width(page), 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Paragraph p = new Paragraph();
			p.append(ftAdd).append(spacer).append(ftAmount).append(new Spacer(.4f,0));
			paymentsGrid.setCell(3, col, p).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
				.setAlignment(Alignment.Horizontal.CENTER).setMargin(Alignment.Horizontal.RIGHT, 6f/72f);
			
			// Estimated escrow (MISMO spec 5.4.1)
			ftAdd = new FormattedText("+", TEXT_REGULAR);
			ftAmount = new FormattedText(Formatter.TRUNCNUMBER.format(payment.ProjectedPaymentEstimatedEscrowPaymentAmount), TEXT_REGULAR);
			if (Formatter.doubleValue(payment.ProjectedPaymentEstimatedEscrowPaymentAmount) > 0.01){
				valueRounded = true;
			} 
			spacer = new Spacer(.5f, 0);
			try {
				spacer = new Spacer(.6f - ftAmount.width(page) - ftAdd.width(page), 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p = new Paragraph();
			p.append(ftAdd).append(spacer).append(ftAmount).append(new Spacer(.4f,0));
			paymentsGrid.setCell(4, col, p).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
				.setAlignment(Alignment.Horizontal.CENTER).setMargin(Alignment.Horizontal.RIGHT, 6f/72f);
			
			// Estimated total period payment (MISMO spec 5.5.1)
			Paragraph para = null;
			if (valueRounded)
				para = new Paragraph().append(new FormattedText(Formatter.TRUNCDOLLARS.format(minTL), TEXT_TOTAL));
			else
				para = new Paragraph().append(new FormattedText(Formatter.DOLLARS.format(minTL), TEXT_TOTAL));
			if (!maxTL.equals(""))
				para.append(new FormattedText(" - " + Formatter.TRUNCDOLLARS.format(maxTL), TEXT_TOTAL));
			paymentsGrid.setCell(5, col, para).setAlignment(Alignment.Vertical.TOP).setMargin(Alignment.Vertical.TOP, 2f/72f)
				.setAlignment(Alignment.Horizontal.CENTER).setMargin(Alignment.Horizontal.RIGHT, 6f/72f);
		}
		
		// Draw
		try {
			taxesInsGrid.draw(page, page.leftMargin, 2.03f, page.width - page.leftMargin - page.rightMargin);
			float yCord = 2.03f + taxesInsGrid.height(page, page.width - page.leftMargin - page.rightMargin);
			paymentsGrid.draw(page, page.leftMargin, yCord, page.width - page.leftMargin - page.rightMargin);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private FormattedText getAmount(String amountStr, Text textType) throws Exception {
		int idx = amountStr.indexOf('.');
		if (idx != -1)
			amountStr = amountStr.substring(0, idx);
	return new FormattedText(amountStr, TEXT_REGULAR);
	}
	private FormattedText getAmountDecimal(String amountStr, Text textType) throws Exception {
		int idx = amountStr.indexOf('.');
		if (idx != -1)
			amountStr = amountStr.substring(idx);
		return new FormattedText(amountStr, TEXT_REGULAR);
	}
	EstimatedPropertyCostComponent findComponent(EstimatedPropertyCost propertyCost, String componentType) {
		for (EstimatedPropertyCostComponent component : propertyCost.estimatedPropertyCostComponents.estimatedPropertyCostComponent)
			if (component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase(componentType))
				return component;
		return null;
	}
	
	Drawable findOtherComponents(EstimatedPropertyCost propertyCost) {
		boolean first = true;
		Paragraph paragraph = new Paragraph().append(new FormattedText("  Other: ", ESCROW_TEXT_REGULAR));
		for (EstimatedPropertyCostComponent component : propertyCost.estimatedPropertyCostComponents.estimatedPropertyCostComponent)
			if (!component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("")
					&& !component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("PropertyTaxes")
					&& !component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("HomeownersInsurance"))
				if (first) {
					first = false;
					if (component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("Other") && !component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentTypeOtherDescription.equals(""))
						paragraph.append(new FormattedText(component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentTypeOtherDescription, ESCROW_TEXT_REGULAR));
					else
						paragraph.append(new FormattedText(component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType, ESCROW_TEXT_REGULAR));
				}
				else
					return paragraph.append(new FormattedText(" and additional costs", ESCROW_TEXT_REGULAR));
		return paragraph;
	}
	
	Drawable findOtherEscow(EstimatedPropertyCost propertyCost) {
		Drawable escrow = null;
		for (EstimatedPropertyCostComponent component : propertyCost.estimatedPropertyCostComponents.estimatedPropertyCostComponent)
			if (!component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("")
					&& !component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("PropertyTaxes")
					&& !component.ProjectedPaymentEstimatedTaxesInsuranceAssessmentComponentType.equalsIgnoreCase("HomeownersInsurance"))
				if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("SomeEscrowed"))
					return some;
				else if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("Escrowed"))
					escrow = yes;
				else if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("NotEscrowed"))
					escrow = no;
		return escrow;
	}

	
	Drawable findEscow(EstimatedPropertyCostComponent component) {
		Drawable escrow = null;
		
				if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("SomeEscrowed"))
					return some;
				else if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("Escrowed"))
					return escrow = yes;
				else if (component.ProjectedPaymentEscrowedType.equalsIgnoreCase("NotEscrowed"))
					return escrow = no;
		return escrow;
	}
	
	@Override
	public float height(Page page) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
