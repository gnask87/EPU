package com.android.epu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EpuActivity extends ListActivity {
  /** Called when the activity is first created. */
  private final int REFRESH = 1;
  private ArrayList<String> listTags = new ArrayList<String>();
  private ArrayAdapter<String> tagAdapter;

  private String retrivedJSONTags;
  private ProgressDialog loading;

  private Double longi;
  private Double lati;
  
  private String label;
  private String fullpath;
  
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	loading = new ProgressDialog(this); 
	loading.setTitle("Loading ..."); 
	loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	
	initializeTagList();
	// Acquire a reference to the system Location Manager
	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	Log.i("EPU","Provider: "+locationManager.getProviders(true));
	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
	      longi = location.getLongitude()*1E6;
	      lati = location.getLatitude()*1E6;
	      Log.i("EPU","Location: "+longi);
	      Log.i("EPU","Location: "+lati);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };

	// Register the listener with the Location Manager to receive location updates
	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//	loading();
  }

  /**
   * Load the tags
   */
  private void initializeTagList() {
	new AsyncTask<Void, Void, Void>() {
	  @Override
	  protected Void doInBackground(Void... arg0) {
		// retrive tags which can takes some time
		retrivedJSONTags = "";
		retrivedJSONTags = retriveTags();
		return null;
	  }

	  protected void onPostExecute(Void result) {
		loading();
		loading.dismiss();
	  }

	  protected void onPreExecute() {
		loading.show();
	  }

	}.execute();
  }

  private void loading() {
	try {
//	  Log.i("EPU JSON", retriveTags());
	  JSONObject jsonArray = new JSONObject(retrivedJSONTags);
	  Log.i("EPU", "START");
	  JSONArray array = new JSONArray(jsonArray.getString("tags"));
	  Log.i("EPU", "Lunghezza: " + array.length());
	  /* Contiene i tag recuperati dal server */
	  listTags.clear();
	  for (int i = 0; i < array.length(); i++) {
		listTags.add(((JSONObject) array.get(i)).getString("label"));
	  }
	  tagAdapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, listTags);
	  setListAdapter(tagAdapter);
	} catch (Exception e) {
	  // e.printStackTrace();
	  Toast.makeText(getApplicationContext(), "Errore " + e, Toast.LENGTH_LONG)
		  .show();
	  e.printStackTrace();
	  Log.i("EPU", "ERRORE: " + e.toString());
	}
  }

  /**
   * Retrive the tags page.
   * 
   * @return the json formatted tags page
   */
  private String retriveTags() {
	String hq_host = Utils.hq_host + Utils.tags;
	Log.i("EPU hq_host", hq_host);
//	String hq_host = "http://tizipic.mine.nu/tags";
	
	StringBuilder builder = new StringBuilder();
	HttpClient client = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(hq_host);
	try {
	  HttpResponse response = client.execute(httpGet);
	  StatusLine statusLine = response.getStatusLine();
	  int statusCode = statusLine.getStatusCode();
	  if (statusCode == 200) {
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
			content));
		String line;
		while ((line = reader.readLine()) != null) {
		  builder.append(line);
		}
	  } else {
		Log.i("EPU", "Failed to download file");
	  }
	} catch (ClientProtocolException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}

	return builder.toString();
  }

  /**
   * Show 
   */
  public boolean onCreateOptionsMenu(Menu menu) {
	menu.add(0, REFRESH, 0, "Refresh");
	return true;
  }

  /**
   * @see android.app.Activity#onOptionsItemSelected(MenuItem)
   */
  public boolean onOptionsItemSelected(MenuItem M) {
	switch (M.getItemId()) {
	case 1:
	  // Refresh tag list 
	  initializeTagList();
	}
	return true;
  }
  
  public void onConfigurationChanged(){
	super.onContentChanged();
	Log.i("EPU","changed config");
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
	// call the camera activity with the string tag
	label = this.getListAdapter().getItem(position).toString();
	Log.i("EPU", ""+label);
	Intent camera = new Intent(this,EpuCamera.class);
	camera.putExtra("lati", lati);
	camera.putExtra("longi", longi);
	camera.putExtra("label", label);
	camera.putExtra("img", fullpath);
	startActivityForResult(camera,EpuCamera.TAKE_PICTURE);
	super.onListItemClick(l, v, position, id);
	}
  
  public void onActivityResult(int requestCode,int resultCode,Intent data){
	super.onActivityResult(requestCode,resultCode,data);
	Log.i("EPU result","RISULTATO "+requestCode);
//	if(resultCode==EpuCamera.TAKE_PICTURE){
//	  fullpath = data.getStringExtra("fullpath");
//	  Log.i("EPU PATH",fullpath);
//	  Intent report = new Intent(this,EpuReport.class);
//	  report.putExtra("lati", lati);
//	  report.putExtra("longi", longi);
//	  report.putExtra("label", label);
//	  report.putExtra("img", fullpath);
//	  startActivity(report);
//	}
  }
}
