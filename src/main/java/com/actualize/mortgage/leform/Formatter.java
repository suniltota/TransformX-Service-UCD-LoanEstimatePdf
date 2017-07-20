package com.actualize.mortgage.leform;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public enum Formatter {
	DATE,                 // Applies the date format: "mm/dd/yyyy"
	DATETIME,             // Applies the date format: "mm/dd/yyyy at HH:MM"
	TIME,                 // Applies the date format: "HH:MM"
	ABSDOLLARS,           // Dollar format of absolute value
	ABSTRUNCDOLLARS,
	DOLLARS,              // Adds a preceding dollar sign and two digits after decimal.
	TRUNCDOLLARS,         // Removes decimal and cents from the dollar format.
	TRUNCNUMBER,          // Removes decimal and cents from the dollar format.
	ZEROTRUNCDOLLARS,     // Dollar format, except zero's are '$0'
	PERCENT,              // Adds a percent sign as a suffix and truncated zeros
	PERCENTWITHOUTPRECEEDINGZERO,// Adds a percent sign as a suffix, removes preceeding zero(0.0250 =>.025% )
	APRPERCENT,           // Adds a percent sign but does not truncate zeros
	INTERESTRATE,         // Whole number if no significant digits after decimal. Otherwise, minimum of two digits after decimal, maximum three digits.
	YEARS,
	MONTHSORYEARS,
	YEARSMONTHS,
	YEARSPLUSONE,
	ROUNDUPYEARS,
	ROUNDUPPLUSONEYEAR,
	INTEGERSUFFIX,		  // add the appropriate suffix e.g.st, nd, th
	INTEGERPLUSONESUFFIX, // add the appropriate suffix e.g.st, nd, th
	MONTH,			      // Translate MISMO month (--nn) to month name
	PHONENUMBER,          // Puts dashes into a number string
	CAMEL,                // Reformats a Camel-case string to have spaces
	CAMELLOWER,           // Reformats a Camel-case string to have spaces and lower case
	NEGATE,               // Negates a number string, e.g. turns negatives into positives and vice versa
	STRINGCLEAN;          // Takes bad characters out of strings

	private static NumberFormat     currencyFormatter   = NumberFormat.getCurrencyInstance();
	private static NumberFormat     numberFormatter     = NumberFormat.getInstance();
	private static DateFormat       inputDate           = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
	private static DateFormat       inputDateTime       = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH);
	private static SimpleDateFormat outputDate          = new SimpleDateFormat("m/dd/yyyy");
	private static SimpleDateFormat outputDateTime      = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
	private static SimpleDateFormat outputTime          = new SimpleDateFormat("h:mm a");
	
	public String format(String inStr) {
		String outStr = inStr; // Default to input string
		switch (this) {
		case DATE:
			try {
				outStr = outputDate.format(inputDate.parse(inStr));
			} catch (Exception e) {
			}
			break;
		case DATETIME:
			try {
				Date d = inputDateTime.parse(inStr);
				outStr = outputDateTime.format(d).replace("AM", "a.m.").replace("PM", "p.m.");
			} catch (Exception e) {
			}
			break;
		case TIME:
			try {
				Date d = inputDateTime.parse(inStr);
				outStr = outputTime.format(d).replace("AM", "a.m.").replace("PM", "p.m.");
			} catch (Exception e) {
			}
			break;
		case ABSDOLLARS:
			currencyFormatter.setMinimumFractionDigits(2);
			currencyFormatter.setMaximumFractionDigits(2);
			if (doubleValue(inStr) < 0)
				outStr = currencyFormat(NEGATE.format(inStr));
			else
				outStr = currencyFormat(inStr);
			break;
		case ABSTRUNCDOLLARS:
			currencyFormatter.setMaximumFractionDigits(0);
			if (doubleValue(inStr) < 0)
				outStr = currencyFormat(NEGATE.format(inStr));
			else
				outStr = currencyFormat(inStr);
			break;
		case DOLLARS:
			currencyFormatter.setRoundingMode(RoundingMode.HALF_UP);
			currencyFormatter.setMinimumFractionDigits(2);
			currencyFormatter.setMaximumFractionDigits(2);
			outStr = currencyFormat(inStr);
			break;
		case TRUNCDOLLARS:
			currencyFormatter.setRoundingMode(RoundingMode.HALF_UP);
			currencyFormatter.setMaximumFractionDigits(0);
			outStr = currencyFormat(inStr);
			break;
		case TRUNCNUMBER:
			numberFormatter.setRoundingMode(RoundingMode.HALF_UP);
			numberFormatter.setMaximumFractionDigits(0);
			outStr = numberFormat(inStr);
			break;
		case ZEROTRUNCDOLLARS:
			if (doubleValue(inStr) % 1 == 0)
				outStr = TRUNCDOLLARS.format(inStr);
			else
				outStr = DOLLARS.format(inStr);
			break;
		case PERCENT:
			// Remove trailing zeroes
			outStr = inStr.indexOf(".") < 0 ? inStr : outStr.replaceAll("0*$", "").replaceAll("\\.$", ""); 				
			outStr = outStr + "%";
			break;
		case PERCENTWITHOUTPRECEEDINGZERO:
			outStr = inStr.indexOf(".") < 0 ? inStr : cleanZeroBeforeDecimal(inStr); 				
			outStr = outStr + "%";
			break;
		case APRPERCENT:
			// Remove trailing zeroes
			numberFormatter.setRoundingMode(RoundingMode.HALF_UP);
			numberFormatter.setMaximumFractionDigits(3);
			numberFormatter.setMinimumFractionDigits(3);
			outStr = numberFormat(inStr) + "%";
			break;
		case INTERESTRATE:
			// Remove trailing zeroes
			if (doubleValue(inStr) % 1 == 0)
				outStr = inStr.indexOf(".") < 0 ? inStr : outStr.replaceAll("0*$", "").replaceAll("\\.$", "");
			else {
				DecimalFormat df = new DecimalFormat("#.000");
				outStr = df.format(doubleValue(inStr));
				if (outStr.charAt(outStr.length()-1) == '0')
					outStr = outStr.substring(0, outStr.length()-1);
			}
			outStr = outStr + "%";
			break;
		case YEARS:
			outStr = Integer.toString((int)doubleValue(inStr)/12);
			break;
		case MONTHSORYEARS:
			int months = (int)doubleValue(inStr) + 1;
			if (months > 23)
				outStr = "year " + Integer.toString(months/12 + (months%12==0 ? 0 : 1));
			else
				outStr = "mo. " + Integer.toString(months-1);
			break;
		case YEARSMONTHS:
			months = (int)doubleValue(inStr);
			int years = months/12;
			int remmonths = months%12;
			if (months < 24)
				outStr = Integer.toString(months) + " months";
			else if (remmonths == 0)
				outStr = Integer.toString(years) + " years";
			else
				outStr = Integer.toString(years) + " yrs. " + Integer.toString(remmonths) + " mos.";
			break;
		case YEARSPLUSONE:
			outStr = Integer.toString(1 + (int)doubleValue(inStr)/12);
			break;
		case ROUNDUPYEARS:
			outStr = Integer.toString((int)(doubleValue(inStr)+11)/12);
			break;
		case ROUNDUPPLUSONEYEAR:
			outStr = Integer.toString((int)Math.ceil((doubleValue(inStr)+12)/12));
			break;
		case PHONENUMBER:
			if (inStr.length() > 6)
				outStr = inStr.substring(0,3) + "-" + inStr.substring(3, 6) + "-" + inStr.substring(6, inStr.length());
			break;
		case CAMEL:
			outStr = String.join(" ", inStr.replaceAll("\\s*-+\\s*", " - ").split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"));
			if (outStr.startsWith("Homeowners Association "))
				outStr = "HOA" + outStr.substring("Homeowners Association".length());
			break;
		case CAMELLOWER:
			outStr = CAMEL.format(inStr).toLowerCase();
			break;
		case NEGATE:
			outStr = String.valueOf(-doubleValue(inStr));
			break;
		case STRINGCLEAN:
			outStr = inStr.trim().replaceAll("\\s+"," ").replaceAll("\\u2018|\\u2019","'");
			break;
		case MONTH:
			switch (inStr) {
			case "--01":
				outStr = "January";
				break;
			case "--02":
				outStr = "February";
				break;
			case "--03":
				outStr = "March";
				break;
			case "--04":
				outStr = "April";
				break;
			case "--05":
				outStr = "May";
				break;
			case "--06":
				outStr = "June";
				break;
			case "--07":
				outStr = "July";
				break;
			case "--08":
				outStr = "August";
				break;
			case "--09":
				outStr = "September";
				break;
			case "--10":
				outStr = "October";
				break;
			case "--11":
				outStr = "November";
				break;
			case "--12":
				outStr = "December";
				break;
			default:
				outStr = "";
			}
			break;
		case INTEGERSUFFIX:
			int val = integerValue(inStr);
			int dd = val%100;
			if (dd > 10 && dd < 20) // teens
				outStr = Integer.toString(val)+"th";
			else // non teens
				switch (dd % 10) {
				case 1:
					outStr = Integer.toString(val)+"st";
					break;
				case 2:
					outStr = Integer.toString(val)+"nd";
					break;
				case 3:
					outStr = Integer.toString(val)+"rd";
					break;
				default:
					outStr = Integer.toString(val)+"th";
				}
			break;
		case INTEGERPLUSONESUFFIX:
			val = integerValue(inStr) + 1;
			dd = val%100;
			if (dd > 10 && dd < 20) // teens
				outStr = Integer.toString(val)+"th";
			else // non teens
				switch (dd % 10) {
				case 1:
					outStr = Integer.toString(val)+"st";
					break;
				case 2:
					outStr = Integer.toString(val)+"nd";
					break;
				case 3:
					outStr = Integer.toString(val)+"rd";
					break;
				default:
					outStr = Integer.toString(val)+"th";
				}
			break;
		default:
			break;
		}
		return outStr;
	}

	static public double doubleValue(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
		}
		return 0;
	}

	static public int integerValue(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
		}
		return 0;
	}
	
	static private String currencyFormat(String str) {
		double amount = doubleValue(str);
		if (amount == 0)
			return "$0";
		if (amount < 0)
			return "-" + currencyFormatter.format(-amount);
		return currencyFormatter.format(amount);
	}
	
	static private String numberFormat(String str) {
		double amount = doubleValue(str);
		return numberFormatter.format(amount);
	}
	static private String cleanZeroBeforeDecimal(String str){
		String percent = str;
		String deciLoc = str.substring(0, str.indexOf("."));
		if(deciLoc.length() == 1 && "0".equals(deciLoc))
			str = str.substring(1, str.length());
		 percent = str.replaceAll("0*$", "").replaceAll("\\.$", "");
		return percent;
	}
}
