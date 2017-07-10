package com.actualize.mortgage.pdf.pdferector;

import java.io.IOException;

public class Border {
	private Color color = null;
	private float width = 1f/72f;

	public Border(Color color, float width) {
		this.color = color;
		this.width = width;
	}

	void draw(Page page, float x1, float y1, float x2, float y2) throws IOException {
		if (color != null && width > 0) {
			page.stream().setLineCapStyle(0);
			page.stream().setLineWidth(width*72);
			page.stream().setStrokingColor(color.red(), color.green(), color.blue());
			page.stream().drawLine(page.toPdfX(x1), page.toPdfY(y1), page.toPdfX(x2), page.toPdfY(y2));
		}
	}

}
