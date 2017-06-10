package com.actualize.mortgage.pdferector;

import java.io.IOException;
//import java.net.URL;
//import java.net.URLClassLoader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public enum Typeface {
	CALIBRI                ("Calibri.ttf"),
	CALIBRI_BOLD           ("Calibri Bold.ttf"),
	CALIBRI_OBLIQUE        ("Calibri Italic.ttf"),
	CALIBRI_BOLD_OBLIQUE   ("Calibri Bold Italic.ttf"),

	HELVETICA              (PDType1Font.HELVETICA),
	HELVETICA_BOLD         (PDType1Font.HELVETICA_BOLD),
	HELVETICA_OBLIQUE      (PDType1Font.HELVETICA_OBLIQUE),
	HELVETICA_BOLD_OBLIQUE (PDType1Font.HELVETICA_BOLD_OBLIQUE),
	WINGDINGS              (PDType1Font.ZAPF_DINGBATS);
	
//	private static String fontDir = null;

	private final String fontname;
	private boolean loaded;
	private PDFont font;
	
	Typeface(String fontname) {
		this.fontname = fontname;
		this.loaded = false;
		this.font = null;
	}

	Typeface(PDType1Font font) {
		this.fontname = null;
		this.loaded = true;
		this.font = font;
	}

	public void load(PDDocument doc, PDPageContentStream content) throws IOException {
		if (!this.loaded) {
			try {
				this.font = PDTrueTypeFont.loadTTF(doc,  getClass().getResourceAsStream("/fonts/"+fontname));
			} catch (Exception e) {
				String fontFile = "fonts/" + fontname;
				try {
					this.font = PDTrueTypeFont.loadTTF(doc, fontFile);
				} catch (Exception e1) {
				}
			}
			this.loaded = true;
		}
//		if (!this.loaded) {
//			String fontpath = getFontDir() + "/" + fontname;
//			this.font = PDTrueTypeFont.loadTTF(doc, fontpath);
//			/*
//			String path = System.getenv("WINDIR");
//			File fontDirectory = new File(path, "Fonts");
//			this.font = PDTrueTypeFont.loadTTF(doc,  fontDirectory.getAbsolutePath()+"/"+fontname);
//			// original code
//			//this.font = PDTrueTypeFont.loadTTF(doc,  getClass().getResourceAsStream("/fonts/"+fontname)); */
//			this.loaded = true;
//		}
	}
	
	 
	
	public PDFont font(PDDocument doc, PDPageContentStream content) throws IOException {
		load(doc, content);
		return font;
	}
	
//	private String getFontDir() {
//		if (fontDir == null) {
//			ClassLoader cl = ClassLoader.getSystemClassLoader();
//			URL[] urls = ((URLClassLoader)cl).getURLs();
//			for (URL url : urls)
//				if (url.getFile().matches(".*lib.pdfbox-app.*")) {
//					fontDir = url.getFile().replaceAll("%20", " ").replaceFirst("lib.pdfbox-app.*", "fonts");
//					break;
//				}
//		}
//		return fontDir;
//	}
}
