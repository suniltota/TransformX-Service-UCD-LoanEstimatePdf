package com.actualize.mortgage.pdf.pdferector;

import java.io.IOException;

public class Spacer extends Drawable {
	private float height, width;
	
	public Spacer(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public float width(Page page) throws IOException {
		return this.width;
	}

	public float height(Page page, float height) throws IOException {
		return this.height;
	}

	public void draw(Page page, float x, float y, float width) throws IOException {
		// nothing to draw
		
		
	}
}
