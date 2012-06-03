package com.android.epu;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class EpuReport extends Activity {
  private Double longi;
  private Double lati;
  private Bitmap bpm;

  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.report);
	// Retrive position info
	lati = this.getIntent().getDoubleExtra("lati", 0);
	longi = this.getIntent().getDoubleExtra("longi", 0);
	ImageView img = (ImageView) findViewById(R.id.image_repo);
	bpm = BitmapFactory.decodeFile("/sdcard/img.jpg");
	img.setImageBitmap(bpm);
  }

  public void sendRepo(View v) {
	// send information to the server
	String hq_host = "http://192.168.1.235:9000/upload/terremotoEmilia";
	try {
	  executeMultipartPost2();
	} catch (Exception e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	  Log.e("EPU upload catch",e.toString());
	}
	Log.i("EPU", "sending report");
	// show google maps activity
	Intent maps = new Intent(this, EpuMaps.class);
	maps.putExtra("lati", lati);
	maps.putExtra("longi", longi);
	startActivity(maps);
  }

  private void executeMultipartPost() throws Exception {
    HttpURLConnection conn;
	String exsistingFileName = "/sdcard/img.jpg";
	String address = "http://192.168.1.235:9000/upload/terremotoEmilia";
	HttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost(address);
	List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	pairs.add(new BasicNameValuePair("prova", "value1"));
	post.setEntity(new UrlEncodedFormEntity(pairs));
	HttpResponse response = client.execute(post);
	Log.i("EPU POST", "Done");
	  
  }
  
	public void executeMultipartPost2() throws Exception {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bpm.compress(CompressFormat.JPEG, 75, bos);
			byte[] data = bos.toByteArray();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
					"http://192.168.1.235:9000/upload/terremotoEmilia");
			ByteArrayBody bab = new ByteArrayBody(data, "/sdcard/img.jpg");
			// File file= new File("/mnt/sdcard/forest.png");
			// FileBody bin = new FileBody(file);
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("img", bab);
			reqEntity.addPart("prova", new StringBody("CAZZZOOOOOOOO"));
			postRequest.setEntity(reqEntity);
			httpClient.execute(postRequest);
			
		} catch (Exception e) {
			// handle exception here
			Log.e(e.getClass().getName(), e.getMessage());
		}
	}
}
