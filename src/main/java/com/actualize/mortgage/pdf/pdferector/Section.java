package com.actualize.mortgage.pdf.pdferector;

import java.io.IOException;

public interface Section {
	public void draw(Page page, Object data) throws IOException;
	
	public float height(Page page) throws Exception;
}
