package com.actualize.loanestimate.leform;

import java.util.List;

import com.actualize.loanestimate.mismodao.Address;
import com.actualize.loanestimate.mismodao.Addresses;
import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.mismodao.Party;
import com.actualize.loanestimate.pdferector.Border;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;
import com.actualize.loanestimate.pdferector.Drawable.Alignment.Vertical;

public class AddendumSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 11, Typeface.CALIBRI_BOLD);
	private static final Text LIGHT_TEXT   = new Text(Color.LIGHT_GRAY, 9, Typeface.CALIBRI);
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	private static final Border light_border = new Border(Color.LIGHT_GRAY, 0.5f/72f);

	private static final float leftIndent  = 5f/72f;
	
	private Grid title, grid;
	
	public boolean display = false; // TODO
	
	/**
	 * Constructor of AddendumSection which initialize the title grid named as Transaction Information Addendum
	 * @param page
	 * @param object
	 */
	public AddendumSection(Page page, Object object) {
		// Create title
		float[] heights = { 16f/72f };
		float[] widths = { 7.5f };
		title = new Grid(heights.length, heights, widths.length, widths);
		title.setCell(0, 0, new FormattedText("Transaction Information Addendum", TAB_TEXT))
			.setMargin(Alignment.Vertical.BOTTOM, 3f/72f).setMargin(Alignment.Horizontal.LEFT, leftIndent).setBackground(new Tab(3.0f));
		title.setBorder(Vertical.BOTTOM, 0, border);
	}

	public void draw(Page page, Object object) {
		draw(page, (Deal)object);
	}

	/**
	 * This method design the comlelte Addendum Section
	 * @param page
	 * @param deal
	 */
	public void draw(Page page, Deal deal) {
		List<Party> additionalApplicants = LoanEstimateSection.additionalApplicants(deal);
		int count = additionalApplicants.size();

		// Create grid
		float[] heights = { 16f/72f, 15f/72f };
		float[] widths = { 20f/72f, 230f/72f };
		grid = new Grid(3*count + 1, heights, widths.length, widths);
		grid.setCell(0, 0, new FormattedText("Additional Applicants", TEXT));
		
		// Render borrowers / addresses
		for (int i = 0; i < count; i++) {
			Party borrower = additionalApplicants.get(i);
			if (borrower == null)
				continue;
			grid.setCell(i*3 + 1, 0, new FormattedText(String.format("%02d", i+1), LIGHT_TEXT));
			if (borrower.individual != null && borrower.individual.name != null)
				grid.setCell(i*3 + 1, 1, new FormattedText(LoanEstimateSection.fullName(borrower.individual.name), TEXT));
			grid.setBorder(Alignment.Vertical.BOTTOM, i*3 + 1, light_border);
			Address address = getMailing(borrower.addresses);
			grid.setCell(i*3 + 2, 1, new FormattedText(LoanEstimateSection.streetAddress(address), TEXT));
			grid.setBorder(Alignment.Vertical.BOTTOM, i*3 + 2, light_border);
			grid.setCell(i*3 + 3, 1, new FormattedText(LoanEstimateSection.cityStateZip(address), TEXT));
			grid.setBorder(Alignment.Vertical.BOTTOM, i*3 + 3, light_border);
		}

		try {
			float y = page.height - page.topMargin - title.height(page, 7.5f);
			title.draw(page, page.leftMargin, y, 7.5f);
			y = y - grid.height(page, 7.5f) - 24f/72f;
			grid.draw(page, page.leftMargin, y, 7.5f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param page
	 */
	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
	
	/**
	 * Checks from the Addresses if it is mailing address or not
	 * @param addresses
	 * @return Mailing Address
	 */
	private Address getMailing(Addresses addresses) {
		if (addresses == null)
			return null;
		for (Address address : addresses.addresses)
			if (address != null && address.AddressType.equalsIgnoreCase("Mailing"))
				return address;
		return null;
	}
}
