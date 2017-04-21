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

public class AddendumFooterSection implements Section {
	private static final Text TEXT         = new Text(Color.BLACK, 9, Typeface.CALIBRI);

	private final Grid grid;
	
	public AddendumFooterSection(Page page, int pagenum, String loanid) {
		float[] heights = { 16f/32f };
		float[] widths = { 3.75f };
		grid = new Grid(heights.length, heights, 2, widths);
		grid.setCell(0,  0, new FormattedText("LOAN ESTIMATE", TEXT));		
		grid.setCell(0,  1, new Paragraph()
			.append(new FormattedText("ADDENDUM ", TEXT)).append(Bullet.BULLET).append(new FormattedText(" LOAN ID # " + loanid, TEXT)))
			.setAlignment(Alignment.Horizontal.RIGHT);
	}
	/**
	 * This method is use to draw grid of Addendum Footer Section
	 * @param - page 
	 * @param object
	 */
	public void draw(Page page, Object object) {
		try {
			draw(page, (Deal)object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Page page, Deal deal) throws Exception {
		grid.draw(page, page.leftMargin, 16f/72f, 7.5f);
	}

	/**
	 * this method return the height of footer grid
	 * 
	 * @param page
	 * @return float value of height
	 */
	public float height(Page page) throws Exception {
		return 32f/72f;
	}

}
