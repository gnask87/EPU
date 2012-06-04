package controllers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import play.libs.Comet;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import views.html.dashboard;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

import controllers.utils.Req;
import database.Queries;
import events.Message;
import events.Notifier;

public class Application extends Controller {

	private static String ok = "{\"success:\":true}";

	public static Result index() {
		return ok(dashboard.render());
	}

	public static Result add(String tag, String priority) {

		String out = Queries.addTag(tag, Integer.valueOf(priority));

		if (out != null) {

			return ok(ok);
		} else
			return badRequest("TAG ALREADY EXISTS: " + tag);
	}

	public static Result tags() {
		System.out.println("Application.tags()");
		
		return ok("{\"tags\":"+new Gson().toJson(Queries.getTags())+"}").as("application/json");
	}
	
	
	
	public static Result markers(String tag) {

		Gson gson = new Gson();
		return ok(gson.toJson(Queries.getMarkers(tag)));
	}

	

	public static Result upload(String tag) {
		System.out.println("Application.upload()");
		MultipartFormData data = request().body().asMultipartFormData();
		
		System.out.println(request().body());
		
		String id = UUID.randomUUID().toString();
		
		try {
			
			//System.out.println(Req.get(data.asFormUrlEncoded(), "prova"));
			
			File img = data.getFile("img").getFile();
			
			System.out.println(img);
			
			boolean success = img.renameTo(new File(Config.imageDir, id));
			if (!success) throw new RuntimeException("ERROR IN SAVE IMG");
			
			//Image thumb = ImageIO.read(new File(Config.imageDir, id)).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
			BufferedImage thumb = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			thumb.createGraphics().drawImage(ImageIO.read(new File(Config.imageDir, id)).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
			ImageIO.write(thumb, "jpg", new File(Config.imageDir, id+"_min"));
			
			System.out.println("Application.upload()");
		
			
			
			System.out.println(data.asFormUrlEncoded());
			double Lat = Double.valueOf(Req.get(data.asFormUrlEncoded(), "Lat"));
			double Lng = Double.valueOf(Req.get(data.asFormUrlEncoded(), "Lng"));
			Date now = new Date(System.currentTimeMillis());
			String op = Req.get(data.asFormUrlEncoded(), "op");
			String desc = Req.get(data.asFormUrlEncoded(), "desc");
			
			
			BasicDBObject obj = new BasicDBObject();
			obj.put("tag", tag);
			obj.put("src", "/img/" + id);
			obj.put("Lat", Lat);
			obj.put("Lng", Lng);
			obj.put("op", op);
			obj.put("time", now);
			obj.put("desc", desc);
			//obj.put("thumb", "/thumb/" + id);

			Queries.storeImage(obj);
			Notifier.notifierActor.tell(new Message("test", obj));
			return ok("");
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError("Error in params");
		}

	}

	public static Result img(String id) {

		return ok(new java.io.File(Config.imageDir+"/"+id)).as("image");
		
	}

	public static Result thumb(String id) {

		return ok(new java.io.File(Config.imageDir+"/thumb/"+id)).as("image");
		
	}
	
	public static Result events(final String tag) {
		return ok(new Comet("parent.Events.apply") {
			public void onConnected() {
				Notifier.notifierActor.tell(new Message(tag, this));
			}
		});
	}

	public static Result test() {

		BasicDBObject obj = new BasicDBObject();

		obj.put("tag", "terremotoEmilia");
		obj.put("src", "assets/images/img.png");
		obj.put("Lat", 46.066667);
		obj.put("Lng", 11.116667);

		Notifier.notifierActor.tell(new Message("test", obj));

		return ok("");
	}

}