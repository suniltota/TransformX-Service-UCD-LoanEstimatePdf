/**
 * 
 * @license Copyright 2015 Actualize Consulting 
 *
 */
package com.actualize.mortgage.pdf.pdferector;

import java.io.IOException;

public class Drawable {
	private boolean wrappable = false;

	public boolean isWrappable() {
		return wrappable;
	}

	public Drawable setWrappable(boolean wrappable) {
		this.wrappable = wrappable;
		return this;
	}
	
	public static class Alignment {
		static public enum Vertical { TOP, MIDDLE, BOTTOM; }
		static public enum Horizontal { LEFT, CENTER, RIGHT; }
	}
	
	private Alignment.Horizontal hAlign = Alignment.Horizontal.LEFT;
	
	public Drawable setAlignment(Alignment.Horizontal align) {
		this.hAlign = align;
		return this;
	}

	private Alignment.Vertical vAlign = Alignment.Vertical.BOTTOM;

	public Drawable setAlignment(Alignment.Vertical align) {
		this.vAlign = align;
		return this;
	}

	private float marginLeft = 1f / 72f;
	private float marginRight = 1f / 72f;

	public Drawable setMargin(Alignment.Horizontal margin, float amount) {
		switch (margin) {
		case LEFT:
			marginLeft = amount;
			break;
		case RIGHT:
			marginRight = amount;
			break;
		case CENTER:
			break;
		}
		return this;
	}

	private float marginTop = 2f / 72f;
	@SuppressWarnings("unused") // TODO use and remove
	private float marginMiddle = 0;
	private float marginBottom = 3f / 72f;

	public Drawable setMargin(Alignment.Vertical margin, float amount) {
		switch (margin) {
		case TOP:
			marginTop = amount;
			break;
		case BOTTOM:
			marginBottom = amount;
			break;
		case MIDDLE:
			marginMiddle = amount;
			break;
		}
		return this;
	}

	private Drawable background = null;
	
	public Drawable setBackground(Drawable background) {
		this.background = background;
		return this;
	}

	private Color shade = null;
	
	public Drawable setShade(Color shade) {
		this.shade = shade;
		return this;
	}

	final void drawBackground(Page page, float x, float y, float width, float height) throws Exception {
		if (background != null) {
			background.draw(page, x, y, width);
		} else if (shade != null) {
			page.stream().setNonStrokingColor(shade.red(), shade.green(), shade.blue());
			page.stream().fillRect(page.toPdfX(x), page.toPdfY(y), page.scaleToPdf(width), page.scaleToPdf(height));
		}
	}
	
	private Border borderLeft = null, borderRight = null, borderTop = null, borderBottom = null;

	public Drawable setBorder(Alignment.Horizontal border, Border style) {
		switch (border) {
		case LEFT:
			borderLeft = style;
			break;
		case RIGHT:
			borderRight = style;
			break;
		case CENTER:
			break;
		}
		return this;
	}
	
	public Drawable setBorder(Alignment.Vertical border, Border style) {
		switch (border) {
		case TOP:
			borderTop = style;
			break;
		case BOTTOM:
			borderBottom = style;
			break;
		case MIDDLE:
			break;
		}
		return this;
	}

	final void drawBorder(Page page, float x, float y, float width, float height) throws IOException {
		if (borderLeft   != null) borderLeft.draw  (page, x,       y,        x,       y+height);
		if (borderRight  != null) borderRight.draw (page, x+width, y,        x+width, y+height);
		if (borderTop    != null) borderTop.draw   (page, x,       y+height, x+width, y+height);
		if (borderBottom != null) borderBottom.draw(page, x,       y,        x+width, y       );
	}

	private float adjustedX(Page page, float width, float x) throws Exception {
		float adjustment = 0;
		switch (this.hAlign) {
		case LEFT:
			adjustment = marginLeft;
			break;
		case CENTER:
			adjustment = (width - width(page)) / 2;
			break;
		case RIGHT:
			adjustment = width - width(page) - marginRight;
			break;
		}
		return x+adjustment;
	}

	private float adjustedY(Page page, float width, float height, float y) throws Exception {
		float adjustment = 0;
		switch (this.vAlign) {
		case TOP:
			adjustment = height - height(page, width) - marginTop;
			break;
		case MIDDLE:
			adjustment = (height - height(page, width)) / 2;
			break;
		case BOTTOM:
			adjustment = marginBottom;
			break;
		}
		return y+adjustment;
	}
	
	/**
	 * Returns the width of the Drawable.
	 *
	 */
	public float width(Page page) throws Exception {
		return 0;
	}

	/**
	 * Returns the height of the Drawable, wrapping at width if possible.
	 *
	 * @param  width  the width of the drawing area
	 */
	public float height(Page page, float width) throws Exception {
		return 0;
	}

	/**
	 * Renders the Drawable on a Page, anchored at a lower-left, e.g. x, y, coordinate.
	 *
	 * @param  x  the bottom-most x-coordinate location for the rendering
	 * @param  y  the left-most y-coordinate location for the rendering
	 * @param  width  the width of the drawing area
	 */
	public void draw(Page page, float x, float y, float width) throws Exception {
		// do nothing
	}

	final void draw(Page page, float x, float y, float width, float height) throws Exception {
		draw(page, adjustedX(page, width, x), adjustedY(page, width, height, y), width);
	}

	public final void drawAll(Page page, float x, float y, float width) throws Exception {
		drawAll(page, x, y, width, height(page, width));
	}

	public final void drawAll(Page page, float x, float y, float width, float height) throws Exception {
		drawBackground(page, x, y, width, height);
		drawBorder(page, x, y, width, height);
		draw(page, x, y, width, height);
	}
	
	/**
	 * Determines if a Drawable, can be wrapped at width.
	 * 
	 * @param  width  the proposed width to wrap at
	 * @throws Exception 
	 */
	public boolean canWrap(Page page, float width) throws Exception { return false; }
		
	/**
	 * Splits a Drawable into at most two parts. Part one consists of a part up-to, but
	 * not exceeding width. Part two is any remaining portion.
	 * 
	 * @param  width  the proposed width to wrap at
	 * @throws Exception 
	 */
	public Drawable[] wrap(Page page, float width) throws Exception {
		throw new Exception("Drawable can not wrap.");
	}
}
