package com.android.epu;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;

public class EpuCamera extends Activity {
  static int TAKE_PICTURE = 1;
  private Uri outputFileUri;
  private Intent data;
  private String fullpath;

  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	TakePhoto();
  }

  private void TakePhoto() {
	String path = Environment.getExternalStorageDirectory().getPath() + "/EPU/";
	File dir = new File(path);
	dir.mkdirs();

	Time today = new Time(Time.getCurrentTimezone());
	today.setToNow();
	String date = today.format("%Y-%m-%d_%H-%M-%S");

	fullpath = path + date + ".jpg";
	File file = new File(fullpath);
	outputFileUri = Uri.fromFile(file);

	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	startActivityForResult(intent, 0);

  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode != Activity.RESULT_CANCELED) {
	  Log.i("EPU result", "result cancelled ");
	  setResult(-1, data);
	  Intent report = new Intent(this,EpuReport.class);
	  report.putExtra("lati", this.getIntent().getDoubleExtra("lati", 0));
	  report.putExtra("longi", this.getIntent().getDoubleExtra("longi", 0));
	  report.putExtra("label", this.getIntent().getStringExtra("label"));
	  report.putExtra("img", fullpath);
	  startActivity(report);
	}else{
	  finish();
	}
  }
}
