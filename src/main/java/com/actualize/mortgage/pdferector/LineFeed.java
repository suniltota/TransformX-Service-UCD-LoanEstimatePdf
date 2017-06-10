package com.actualize.mortgage.pdferector;

import java.io.IOException;

public class LineFeed extends Drawable {
	private float height;
	
	public LineFeed(float height) {
		this.height = height;
	}

	public float width(Page page) throws IOException {
		return 0;
	}

	public float height(Page page, float width) throws IOException {
		return this.height;
	}

	public void draw(Page page, float x, float y, float width) throws IOException {
		// nothing to draw
	}
}
