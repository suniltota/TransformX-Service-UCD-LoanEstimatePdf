package com.actualize.mortgage.pdf.pdferector;

public enum Color{
	LIGHT_GREEN (205, 240, 205),
	BLACK       (0, 0, 0),
	LIGHT_GRAY  (220, 220, 220),
	MEDIUM_GRAY (196, 196, 196),
	DARK_GRAY	(164, 164, 164),
	WHITE	    (255, 255, 255);

	private final int red, green, blue;
	
	Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int red()   { return red; }
	public int green() { return green; }
	public int blue()  { return blue; }
}
