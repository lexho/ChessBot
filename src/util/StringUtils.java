package util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	/**
	 * split a string into single strings
	 * @param str the string that should be splitted
	 * @return a list of strings
	 */
	public static List<String> splitString(String str, char sep) {
		List<String> str_splitted= new ArrayList<String>();
		
		int e = str.indexOf(sep);
		
		while(e != -1) {
			str_splitted.add(str.substring(0, e));
			//System.out.println("add: " + cmd.substring(0, e));
			str = str.substring(e + 1);
			e = str.indexOf(sep);
		}
		
		/* Add last word */
		//System.out.println("add: " + cmd);
		str_splitted.add(str);
		
		return str_splitted;
	}
	
	public static List<String> splitString(String str, String sep) {
		List<String> str_splitted= new ArrayList<String>();
		
		int e = str.indexOf(sep);
		
		while(e != -1) {
			str_splitted.add(str.substring(0, e));
			//System.out.println("add: " + cmd.substring(0, e));
			str = str.substring(e + sep.length());
			e = str.indexOf(sep);
		}
		
		/* Add last word */
		//System.out.println("add: " + cmd);
		str_splitted.add(str);
		
		return str_splitted;
	}

}
