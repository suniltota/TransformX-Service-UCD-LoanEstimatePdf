package com.actualize.loanestimate.mismodao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MISMODocument extends MISMODataAccessObject {
	public final String MISMOReferenceModelIdentifier;
	public final AboutVersions aboutVersions;
	public final DocumentSets documentSets;
	
	public MISMODocument(String filename) throws ParserConfigurationException, SAXException, IOException {
		this(new FileInputStream(filename));
	}
	
	public MISMODocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		this(getElement(in));
	}

	private static Element getElement(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder().parse(new InputSource(in)).getDocumentElement();
	}

	private MISMODocument(String NS, Element element) {
		super(element);
		Element elem;
		MISMOReferenceModelIdentifier = getElementValue(element, NS + "MISMOReferenceModelIdentifier");
		elem = getElement(element, NS + "ABOUT_VERSIONS");
		aboutVersions = elem == null ? null : new AboutVersions(NS, elem);
		elem = getElement(element, NS + "DOCUMENT_SETS");
		documentSets =  elem == null ? null : new DocumentSets(NS, elem);
	}

	private MISMODocument(Element element) {
		this(element.lookupPrefix("http://www.mismo.org/residential/2009/schemas")==null ? "" : element.lookupPrefix("http://www.mismo.org/residential/2009/schemas")+":", element);
	}
}
