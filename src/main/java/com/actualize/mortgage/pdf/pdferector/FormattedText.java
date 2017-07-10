package com.actualize.mortgage.pdf.pdferector;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattedText extends Drawable {
	private final String str;
	private final Text style;
	private final boolean underline;
	static private final Pattern whitespace = Pattern.compile("\\s+");
	
	public FormattedText(String str, Text style) {
		this.str = str == null ? "" : str;
		this.style = style;
		this.underline = false;
	}
	
	public FormattedText(String str, Text style, boolean underline) {
		this.str = str;
		this.style = style;
		this.underline = underline;
	}
	
	public float width(Page page) throws Exception {
		return width(page, str);
	}
	
	public float height(Page page, float width) throws Exception {
		if (!canWrap(page, width))
			return height(page);
		Drawable[] split = wrap(page, width);
		return ((FormattedText)split[0]).height(page) + split[1].height(page, width);
	}
	
	public void draw(Page page, float x, float y, float width) throws Exception {
		if (!canWrap(page, width))
			draw(page, x, y);
		else {
			Drawable[] split = wrap(page, width);
			float offset = split[1].height(page, width);
			((FormattedText)split[0]).draw(page, x, y + offset);
			split[1].draw(page, x, y, width);
		}
	}
	
	public boolean canWrap(Page page, float width) throws IOException {
		if (!isWrappable()) // Drawable not wrappable
			return false;
		if (width(page, str) < width) // String already less than width
			return false;
		Matcher matcher = whitespace.matcher(str);
        if (!matcher.find()) // no spaces found
        	return false;
        if (matcher.start() == 0) // str starts with space
        	return matcher.end() != str.length() && width(page, " ") < width;
        return width(page, str.substring(0, matcher.start())) < width;
	}

	public Drawable[] wrap(Page page, float width) throws Exception {
		if (!canWrap(page, width))
			return super.wrap(page, width);
		String tmpStr;
		float subwidth;
		int endIndex, tmpEnd = 0;
		Matcher matcher = whitespace.matcher(str);
		do {
			endIndex = tmpEnd;
			if (!matcher.find())
				break;
			tmpEnd = matcher.start() == 0 ? matcher.end() : matcher.start();
			tmpStr = str.substring(0, tmpEnd);
			subwidth = width(page, tmpStr);
		} while (subwidth < width);
		Drawable[] drawable = new Drawable[2];
        drawable[0] = new FormattedText(str.substring(0, endIndex), style);
        drawable[1] = new FormattedText(str.substring(endIndex+1, str.length()), style).setWrappable(true);
        return drawable;
	}

	private void draw(Page page, float x, float y) throws IOException {
		try {
			page.stream().beginText();
			style.apply(page.doc(), page.stream());
			page.stream().moveTextPositionByAmount(x*72, y*72);
			page.stream().drawString(str);
			if (underline) {
				page.stream().setLineWidth(0.5f);
				page.stream().drawLine(x*72, y*72 - 1, (x+width(page, str))*72, y*72 - 1);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			page.stream().endText();
		}
	}
	
	private float height(Page page) throws IOException {
		return style.size * style.typeface.font(page.doc(), page.stream()).getFontBoundingBox().getHeight() / 72000;
	}
	
	private float width(Page page, String str) throws IOException {
		return style.size * style.typeface.font(page.doc(), page.stream()).getStringWidth(str) / 72000.0f;
	}
}
