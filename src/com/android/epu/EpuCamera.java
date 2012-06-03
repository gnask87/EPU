package com.android.epu;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;

public class EpuCamera extends Activity {
	static int TAKE_PICTURE = 1;
	private Uri outputFileUri;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TakePhoto();
	}

	private void TakePhoto() {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/EPU/";
		File dir = new File(path);
		dir.mkdirs();

		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		String date = today.format("%Y-%m-%d_%H-%M-%S");

		String fullpath = path+ date+ ".jpg";
		File file = new File(fullpath);
		outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivity(intent);
		Intent data = new Intent();
		data.putExtra("fullpath", fullpath);
		setResult(TAKE_PICTURE, data);
		finish();
	}

}
