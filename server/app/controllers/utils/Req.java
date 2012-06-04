package controllers.utils;

import java.util.Map;

public class Req {

	public static String get(Map<String,String[]> r, String k) {
		String [] v = r.get(k);
		if (v==null) return "";
		String value = "";
		for (int i = 0 ; i < v.length ; i++) {
			value+=v[i];
		}
		return value;
	}
	

	
}
