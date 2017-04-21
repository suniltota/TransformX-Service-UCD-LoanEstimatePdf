package com.actualize.loanestimate.pdferector;

import java.util.ArrayList;

public class Region extends Drawable {
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private float spacing = 0;

	public Region append(Drawable drawable) {
		drawables.add(drawable);
		return this;
	}

	public float width(Page page) throws Exception {
		float max = 0;
		for (Drawable drawable : drawables) {
			float w = drawable.width(page);
			if (w > max)
				max = w;
		}
		return max;
	}

	public float height(Page page, float width) throws Exception {
		float height = 0;
		for (Drawable drawable : drawables) {
			boolean wrap = drawable.isWrappable();
			if (isWrappable())
				drawable.setWrappable(true);
			height += drawable.height(page, width) + spacing;
			drawable.setWrappable(wrap);
		}
		return height;
	}

	public void draw(Page page, float x, float y, float width) throws Exception {
		y += height(page, width);
		for (Drawable drawable : drawables) {
			boolean wrap = drawable.isWrappable();
			if (isWrappable())
				drawable.setWrappable(true);
			y -= drawable.height(page, width);
			drawable.draw(page, x, y, width);
			y -= spacing;
			drawable.setWrappable(wrap);
		}
	}
	
	public Region setSpacing(float spacing) {
		this.spacing = spacing;
		return this;
	}
}
