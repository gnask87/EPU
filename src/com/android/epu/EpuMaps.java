package com.android.epu;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class EpuMaps extends MapActivity {
  private MapView mapView;

  private List<Overlay> mapOverlays;
  private Drawable drawable;
  private ItemizeOverlay itemizedOverlay;

  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.maps);

	mapView = (MapView) findViewById(R.id.view_map);
	mapView.setBuiltInZoomControls(true);

	// istantiate the overlay item
	mapOverlays = mapView.getOverlays();
	drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
	itemizedOverlay = new ItemizeOverlay(drawable,this);

//	Geocoder gc = new Geocoder(this, Locale.getDefault());
	
	// Double longi = (addresses.get(0).getLongitude()) * 1E6;
	// Double lati = (addresses.get(0).getLatitude()) * 1E6;
	Double lati = this.getIntent().getDoubleExtra("lati", 0);
	Double longi = this.getIntent().getDoubleExtra("longi", 0);

	GeoPoint point = new GeoPoint(lati.intValue(), longi.intValue());
	OverlayItem overlayitem = new OverlayItem(point, "prova", "testo di prova");
	itemizedOverlay.addOverlay(overlayitem);
	mapOverlays.add(itemizedOverlay);
	MapController mapController = mapView.getController();
	mapController.animateTo(point);
	mapController.setZoom(18);

  }

  @Override
  protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
  }

}
