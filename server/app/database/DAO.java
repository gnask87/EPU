package database;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;


public class DAO {

	private static DAO instance = null;
	Mongo m;
	DB db;
	DBCollection tags;
	DBCollection images;
	private DAO() {
		try {
			m = new Mongo("localhost", 27017);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		db = m.getDB("epu");
		tags = db.getCollection("tags");
		images = db.getCollection("images");
	}
	public static DAO get() {

		if (instance == null)
			instance = new DAO();
		return instance;
	}
	
	public DBCollection getCollection(String name) {
		return get().getCollection(name);
	}
	
	public DBCollection getTags() {
		return tags;
	}	

	public DBCollection getImages() {
		return images;
	}	
	
}
