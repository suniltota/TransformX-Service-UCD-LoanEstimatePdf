package com.actualize.loanestimate.mismodao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EscrowItems extends MISMODataAccessObject {
	public final EscrowItem[] escrowItems;
	private static Pattern ptrnNumbers = Pattern.compile(" [2-9]+");
	private static Matcher matches = null;
	private static String strPrepend = "zzz";

	public EscrowItems(Element element) {
		super(element);
		NodeList nodes = getElementsAddNS("ESCROW_ITEM");
		escrowItems = new EscrowItem[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < escrowItems.length; i++)
			escrowItems[i] = new EscrowItem((Element)nodes.item(i));
		
		// Sort prepaid items
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < escrowItems.length; i++)
			for (int j = i+1; j < escrowItems.length; j++){
				str1 = fixSorts(escrowItems[i].escrowItemDetail.displayName());
				str2 = fixSorts(escrowItems[j].escrowItemDetail.displayName());
				if (escrowItems[i].escrowItemDetail != null && escrowItems[j].escrowItemDetail != null 
					&& str1.compareToIgnoreCase(str2) > 0) {
					EscrowItem tmp = escrowItems[i];
					escrowItems[i] = escrowItems[j];
					escrowItems[j] = tmp;
				}
		}
	}
	//this replaces the string (space)(number) with (space)"zzz"(number to make fees with 2's etc. sort nicer
	private static String fixSorts(String strIn){
	        // get a matcher object
	        matches = ptrnNumbers.matcher(strIn);
	        while (matches.find()){
	        	strIn = strIn.substring(0,matches.start())+" "+strPrepend+strIn.substring(matches.start()+1);
	        }
	        //System.out.println(">>>"+strIn);
		return strIn;
	}
}
