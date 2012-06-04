package database;

import java.util.ArrayList;

import com.mongodb.DBCursor;

public class DBUtils {


	public static String fetch(DBCursor c) {
		ArrayList<String> list = new ArrayList<String>();
 		while (c.hasNext()) {
			list.add(c.next().toString());
		}
 		return list.toString();
	}
	
}
