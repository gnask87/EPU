package database;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Queries {

	public static String addTag(String tag, int priority) {

		DBCollection tagsC = DAO.get().getTags(); 

		BasicDBObject doc = new BasicDBObject();
		doc.put("label", tag);

		if (tagsC.find(doc).count() > 0)
			return null;

		doc.put("priority", priority);

		tagsC.insert(doc);
		return doc.get("_id").toString();


	}
	
	
	public static List<DBObject> getMarkers(String tag) {

		DBObject q = new BasicDBObject();
		q.put("tag", tag);
		
		DBCollection imgs = DAO.get().getImages(); 
		return imgs.find(q).toArray();
		
	}
	
	public static List<DBObject> getTags() {

		DBCollection tagsC = DAO.get().getTags(); 
		return tagsC.find().toArray();
		
	}
	
	
	public static String storeImage(DBObject obj) {

		DBCollection imgs = DAO.get().getImages(); 
		imgs.insert(obj);
		return obj.get("_id").toString();
		
	}

}
