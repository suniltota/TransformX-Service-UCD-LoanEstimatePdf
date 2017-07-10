package com.actualize.mortgage.pdf.mismodao;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class MISMODataAccessObject {

	// Public visibility: Document query helpers
	public static String NS = "mismo:";

	public NodeList getElements(String expression) {
		if (expression != null)
			try {
				return (NodeList)xpath.compile(expression).evaluate(element, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				// TODO standard error
			}
		return null;
	}

	public NodeList getElementsAddNS(String expression) {
		return getElements(addNS(expression));
	}

	public Node getElement(String expression) {
		NodeList nodelist = getElements(expression);
		if (nodelist == null || nodelist.getLength() == 0)
			return null;
		return nodelist.item(0);
	}

	public Node getElementAddNS(String expression) {
		return getElement(addNS(expression));
	}

	public String getAttributeValue(String name) {
		if (name != null && element != null)
			return element.getAttribute(name);
		return "";
	}

	public String getAttributeValueAddNS(String name) {
		return getAttributeValue(addNS(name));
	}

	public String getValue(String expression) {
		if (expression != null)
			try {
				return (String)xpath.compile(expression).evaluate(element, XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				// TODO standard error
			}
		return "";
	}

	public String getValueAddNS(String expression) {
		return getValue(addNS(expression));
	}

	// Package visibility
	public final Element element;
	
	MISMODataAccessObject(Element e) {
		element = e;
		setDocumentInfo(e);
	}

	String getElementValue(Element element, String tagname) {
		Element elem = getElement(element, tagname);
		if (elem == null)
			return null;
		return elem.getTextContent();
	}
	
	Element getElement(Element element, String tagname) {
		NodeList nodes = getElements(element, tagname);
		if (nodes.getLength() > 0)
			return (Element)nodes.item(0);
		return null;
	}
	
	NodeList getElements(Element element, String tagname) {
		return element.getElementsByTagName(tagname);
	}

	// Package visibility: Data lookup helpers
//	static MISMODataAccessObject lookup(Element element) {
//		try {
//			return dataObjectMap.get(element);
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	// Private visibility: Document query helpers
	private static XPath xpath = null;
	
	void setDocumentInfo(Element e) {
		if (xpath == null) {
//			Element docElem = e.getOwnerDocument().getDocumentElement();
//			TODO: grab namespaces from above document element
			xpath = XPathFactory.newInstance().newXPath();
			NamespaceContext ctx = new NamespaceContext() {
				public String getNamespaceURI(String prefix) {
			        return prefix.equals("mismo") ? "http://www.mismo.org/residential/2009/schemas" : (prefix.equals("gse") ? "http://www.datamodelextension.org" : null); 
			    }
			    @SuppressWarnings("rawtypes")
				public Iterator getPrefixes(String val) {
			        return null;
			    }
			    public String getPrefix(String uri) {
			        return null;
			    }
			};
			xpath.setNamespaceContext(ctx);
		}
//		register(e, this);
	}
	
	private String addNS(String str) {
		String[] parts = str.split("/");
		for (int i = 0; i < parts.length; i++)
			if (!parts[i].equals("")) {
				String[] subparts = parts[i].split("\\x5B");
				for (int j = 0; j < subparts.length; j++)
					if (!subparts[j].equals("") && !subparts[j].startsWith(".")) // && subparts[j].matches("[A-Z][a-z]*"))
						subparts[j] = addNSEnd(subparts[j]);
				parts[i] = String.join("[", subparts);
			}
		return String.join("/", parts);
	}

	private String addNSEnd(String str) {
		if (str.contains(":"))
			return str;
		return NS + str;
	}

	boolean nameSortsBefore(String a, String b) {
		if (a == null || a.equals("Additional Charges"))
			return false;
		if (b == null || b.equals("Additional Charges"))
			return true;
		return a.compareToIgnoreCase(b) < 0;
	}
	
	String canonicalLabel(String str) {
		String outStr = "";
		for (String s : str.replaceAll("\\s*-+\\s*", " - ").split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
			outStr = outStr.equals("") ? s.trim() : outStr + " " + s.trim();
		if (outStr.startsWith("Homeowners Association "))
			outStr = "HOA" + outStr.substring("Homeowners Association".length());
		return outStr;
	}
}
