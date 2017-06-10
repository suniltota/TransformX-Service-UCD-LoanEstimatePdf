package com.actualize.mortgage.pdferector;

import java.util.ArrayList;

public class Paragraph extends Drawable {
	ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	
	public Paragraph append(Drawable drawable) {
		this.drawables.add(drawable);
		return this;
	}

	public float width(Page page) throws Exception {
		float width = 0;
		for (Drawable drawable : drawables)
			width += drawable.width(page);
		return width;
	}
	
	public float height(Page page, float width) throws Exception {
		if (!canWrap(page, width))
			return height(page);
		Paragraph[] split = (Paragraph[])wrap(page, width);
		return split[0].height(page) + split[1].height(page, width);
	}
	
	public void draw(Page page, float x, float y, float width) throws Exception {
		if (!canWrap(page, width))
			draw(page, x, y);
		else {
			Paragraph[] split = (Paragraph[])wrap(page, width);
			float offset = split[1].height(page, width);
			split[0].draw(page, x, y + offset);
			split[1].draw(page, x, y, width);
		}
	}
	
	public boolean canWrap(Page page, float width) throws Exception {
		if (!isWrappable())
			return false;
		if (drawables.size() == 0)
			return false;
		if (drawables.size() == 1)
			return drawables.get(0).canWrap(page, width);
		return width < width(page);
	}

	public Drawable[] wrap(Page page, float width) throws Exception {
		if (!canWrap(page, width))
			return super.wrap(page, width);

		// Create parts
		Paragraph parts[] = new Paragraph[2];
		parts[0] = new Paragraph();
		parts[1] = (Paragraph)new Paragraph().setWrappable(true);
		
		// Build up part 1
		float w = width;
		int i = 0;
		while (i < drawables.size() && drawables.get(i).width(page) < w) {
			parts[0].append(drawables.get(i));
			w -= drawables.get(i).width(page);
			i++;
		}
		
		// Split (if able)
		if (drawables.get(i).canWrap(page, w)) {
			Drawable[] split = drawables.get(i).wrap(page, w);
			parts[0].append(split[0]);
			parts[1].append(split[1]);
		}
		
		// Fill up remainder of part 2
		while (i < drawables.size()) {
			parts[1].append(drawables.get(i));
			i++;
		}
		return parts;
	}
	
	public void draw(Page page, float x, float y) throws Exception {
		for (Drawable drawable : drawables) {
			float w = drawable.width(page);
			drawable.draw(page, x, y, w);
			x += w;
		}
	}
	
	private float height(Page page) throws Exception {
		float height = 0;
		for (Drawable drawable : drawables) {
			float h = drawable.height(page, drawable.width(page));
			if (h > height) height = h;
		}
		return height;
	}
}