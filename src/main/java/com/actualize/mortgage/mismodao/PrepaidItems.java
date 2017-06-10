package com.actualize.mortgage.mismodao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PrepaidItems extends MISMODataAccessObject {
	public final PrepaidItem[] prepaidItems;
	private static Pattern ptrnNumbers = Pattern.compile(" [2-9]+");
	private static Matcher matches = null;
	private static String strPrepend = "zzz";

	public PrepaidItems(Element element) {
		super(element);
		
		
		NodeList nodes = getElementsAddNS("PREPAID_ITEM");
		prepaidItems = new PrepaidItem[nodes==null ? 0 : nodes.getLength()];
		for (int i = 0; i < prepaidItems.length; i++)
			prepaidItems[i] = new PrepaidItem((Element)nodes.item(i));
		
		// Sort prepaid items
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < prepaidItems.length; i++)
			for (int j = i+1; j < prepaidItems.length; j++){
				str1 = fixSorts(prepaidItems[i].prepaidItemDetail.displayName());
				str1 = fixSorts(prepaidItems[j].prepaidItemDetail.displayName());
				if (prepaidItems[i].prepaidItemDetail != null && prepaidItems[j].prepaidItemDetail != null 
					&& str1.compareToIgnoreCase(str2) > 0) {
					PrepaidItem tmp = prepaidItems[i];
					prepaidItems[i] = prepaidItems[j];
					prepaidItems[j] = tmp;
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
