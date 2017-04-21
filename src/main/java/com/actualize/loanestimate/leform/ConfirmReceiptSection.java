package com.actualize.loanestimate.leform;

import com.actualize.loanestimate.mismodao.Deal;
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
import com.actualize.loanestimate.pdferector.Drawable.Alignment.Vertical;

public class ConfirmReceiptSection implements Section {
	private static final Text TAB_TEXT     = new Text(Color.WHITE, 12, Typeface.CALIBRI_BOLD);
	private static final Text TEXT         = new Text(Color.BLACK, 10, Typeface.CALIBRI);

	private static final Border border = new Border(Color.BLACK, 0.5f/72f);
	
	private final Grid grid;
	
	public boolean display = false; // TODO
	
	public ConfirmReceiptSection(Page page, Object object) {
		// Create grid
		float[] heights = { 16f/72f, 54f/72f, 15f/72f };
		float[] widths = { 165f/72f, 96f/72f, 18f/72f, 165f/72f, 96f/72f };
		grid = new Grid(heights.length, heights, widths.length, widths);

		// Tab header
		grid.setCell(0, 0, new FormattedText("  Confirm Receipts", TAB_TEXT)).setAlignment(Alignment.Vertical.BOTTOM).setMargin(Alignment.Vertical.BOTTOM, 6f/72f).setBackground(new Tab(1.9f));
		grid.setBorder(Vertical.BOTTOM, 0, border);

		// Confirm receipt (MISMO spec 24.0)
		grid.setBorder(Alignment.Vertical.TOP, 1, border);
		Region region = new Region()
			.append(new FormattedText("By signing, you are confirming that you have received this form. You do not have to accept this loan because you have signed or", TEXT))
			.append(new FormattedText("received this form", TEXT));
		grid.setCell(1, 0, region).setAlignment(Alignment.Vertical.TOP);
		grid.setBorder(Vertical.BOTTOM, 1, border);
		grid.setBorder(Vertical.BOTTOM, 1, 2, null);
		
		// Applicant signature
		grid.setCell(2, 0, new FormattedText("Applicant Signature", TEXT));
		grid.setCell(2, 1, new FormattedText("Date", TEXT));
		grid.setCell(2, 3, new FormattedText("Co-Applicant Signature", TEXT));
		grid.setCell(2, 4, new FormattedText("Date", TEXT));
	}

	public void draw(Page page, Object object) {
		draw(page, (Deal)object);
	}

	public void draw(Page page, Deal deal) {
		if (!display)
			return;
		
		try {
			grid.draw(page, page.leftMargin, page.bottomMargin, 7.5f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float height(Page page) throws Exception {
		return grid.height(page, page.width);
	}
}
