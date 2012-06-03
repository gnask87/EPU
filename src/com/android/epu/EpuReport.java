package com.android.epu;

import java.io.ByteArrayOutputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EpuReport extends Activity {
  private Double longi;
  private Double lati;
  private Bitmap bpm;
  private String events;
  private EditText desc;
  private EditText user;
  private String fullpath;
  private ProgressDialog loading;

  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.report);
	loading = new ProgressDialog(this);
	loading.setTitle("Sending ...");
	loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	// Retrive position info
	lati = this.getIntent().getDoubleExtra("lati", 0);
	longi = this.getIntent().getDoubleExtra("longi", 0);
	events = this.getIntent().getStringExtra("label");
	fullpath = this.getIntent().getStringExtra("img");

	Log.i("EPU fullpath", "" + fullpath);

	ImageView img = (ImageView) findViewById(R.id.image_repo);
	bpm = BitmapFactory.decodeFile(fullpath);

	desc = (EditText) findViewById(R.id.description);
	user = (EditText) findViewById(R.id.usertext);

	img.setImageBitmap(bpm);
  }

  // public void sendRepo(View v) {
  // // send information to the server
  // try {
  // executeMultipartPost2();
  // } catch (Exception e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // Log.e("EPU upload catch",e.toString());
  // }
  // Log.i("EPU", "sending report");
  // }

  private void showMap() {
	// show google maps activity
	Intent maps = new Intent(this, EpuMaps.class);
	maps.putExtra("lati", lati);
	maps.putExtra("longi", longi);
	startActivity(maps);
  }

  /*
   * Send Report
   */
  public void sendReport(View v) {
	new AsyncTask<Void, Void, Void>() {
	  @Override
	  protected Void doInBackground(Void... arg0) {
		// retrive tags which can takes some time
		executeMultipartPost();
		return null;
	  }

	  protected void onPostExecute(Void result) {
		showMap();
		loading.dismiss();
	  }

	  protected void onPreExecute() {
		loading.show();
	  }

	}.execute();
  }

  private void executeMultipartPost() {
	try {
	  String remote = Utils.hq_host + Utils.upload + events;
	  Log.i("EPU remote", remote);
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  bpm.compress(CompressFormat.JPEG, 75, bos);
	  byte[] data = bos.toByteArray();
	  HttpClient httpClient = new DefaultHttpClient();
	  HttpPost postRequest = new HttpPost(remote);
	  ByteArrayBody bab = new ByteArrayBody(data, fullpath);
	  // File file= new File("/mnt/sdcard/forest.png");
	  // FileBody bin = new FileBody(file);
	  MultipartEntity reqEntity = new MultipartEntity(
		  HttpMultipartMode.BROWSER_COMPATIBLE);
	  reqEntity.addPart("img", bab);
	  reqEntity.addPart("Lat", new StringBody("" + lati / 1E6));
	  reqEntity.addPart("Lng", new StringBody("" + longi / 1E6));
	  reqEntity.addPart("op", new StringBody("" + user.getText()));
	  reqEntity.addPart("desc", new StringBody("" + desc.getText()));
	  postRequest.setEntity(reqEntity);
	  httpClient.execute(postRequest);

	} catch (Exception e) {
	  // handle exception here
	  e.printStackTrace();
	  Log.e("EPU multiparts", "" + e.getMessage());
	}
  }
}
