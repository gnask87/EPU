package com.android.epu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class EpuMaps extends MapActivity {
  private MapView mapView;

  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.maps);

	mapView = (MapView) findViewById(R.id.view_map);
	mapView.setBuiltInZoomControls(true);

	Geocoder gc = new Geocoder(this, Locale.getDefault());
	MapController mapController = mapView.getController();
	try {
	  List<Address> addresses = gc.getFromLocationName(
		  "Via mario salazzari, lugagnano, Italy", 1);
	  Double longi = (addresses.get(0).getLongitude())*1E6;
	  Double lati = (addresses.get(0).getLatitude())*1E6;
	  GeoPoint point = new GeoPoint(lati.intValue(),longi.intValue());
	  mapController.animateTo(point);
	  mapController.setZoom(18);
	} catch (IOException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}

  }

  @Override
  protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
  }

}
