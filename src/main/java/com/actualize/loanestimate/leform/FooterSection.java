package com.actualize.loanestimate.leform;

import com.actualize.loanestimate.mismodao.Deal;
import com.actualize.loanestimate.pdferector.Bullet;
import com.actualize.loanestimate.pdferector.Color;
import com.actualize.loanestimate.pdferector.FormattedText;
import com.actualize.loanestimate.pdferector.Grid;
import com.actualize.loanestimate.pdferector.Page;
import com.actualize.loanestimate.pdferector.Paragraph;
import com.actualize.loanestimate.pdferector.Section;
import com.actualize.loanestimate.pdferector.Text;
import com.actualize.loanestimate.pdferector.Typeface;
import com.actualize.loanestimate.pdferector.Drawable.Alignment;

public class FooterSection implements Section {
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);
	private static final Text LARGE        = new Text(Color.BLACK, 11, Typeface.CALIBRI);
	private static final Text LARGE_BOLD   = new Text(Color.BLACK, 11, Typeface.CALIBRI_BOLD);

	private final Grid grid;
	private final int pagenum;
	
	public FooterSection(Page page, int pagenum, String loanid) {
		float[] heights = { 16f/32f };
		float[] widths = { 3.75f };
		grid = new Grid(heights.length, heights, 2, widths);
		grid.setCell(0,  0, new FormattedText("LOAN ESTIMATE", TEXT));		
		grid.setCell(0,  1, new Paragraph()
			.append(new FormattedText("PAGE " + String.valueOf(pagenum) + " OF 3 ", TEXT)).append(Bullet.BULLET).append(new FormattedText(" LOAN ID # " + loanid, TEXT)))
			.setAlignment(Alignment.Horizontal.RIGHT);
		this.pagenum = pagenum;
	}
	
	public void draw(Page page, Object object) {
		try {
			if (pagenum == 1)
				new Paragraph()
					.append(new FormattedText("Visit ", LARGE))
					.append(new FormattedText("www.consumerfinance.gov/mortgage-estimate", LARGE_BOLD))
					.append(new FormattedText(" for general information and tools.", LARGE))
					.draw(page, 1.5f, .5f, 6f);
			draw(page, (Deal)object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws Exception {
		grid.draw(page, page.leftMargin, 16f/72f, 7.5f);
	}

	public float height(Page page) throws Exception {
		return 32f/72f;
	}

}
