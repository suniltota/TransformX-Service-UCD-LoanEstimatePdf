package com.actualize.mortgage.leform;

import com.actualize.mortgage.pdf.mismodao.Deal;
import com.actualize.mortgage.pdf.pdferector.Bullet;
import com.actualize.mortgage.pdf.pdferector.Color;
import com.actualize.mortgage.pdf.pdferector.FormattedText;
import com.actualize.mortgage.pdf.pdferector.Grid;
import com.actualize.mortgage.pdf.pdferector.Page;
import com.actualize.mortgage.pdf.pdferector.Paragraph;
import com.actualize.mortgage.pdf.pdferector.Section;
import com.actualize.mortgage.pdf.pdferector.Text;
import com.actualize.mortgage.pdf.pdferector.Typeface;
import com.actualize.mortgage.pdf.pdferector.Drawable.Alignment;

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
