package com.varma.samples.exifinfo;

import java.io.IOException;

import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.TextView;

public class ExifInfoActivity extends Activity {
	public static final String FILE_PATH_KEY = "file_path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exif);
		
		Bundle bundle = getIntent().getExtras();
		
		if(null != bundle){
			String filepath = bundle.getString(FILE_PATH_KEY);
			
			try {
				ExifInterface exif = new ExifInterface(filepath);
				StringBuilder builder = new StringBuilder();
				
				builder.append("Date & Time: " + getExifTag(exif,ExifInterface.TAG_DATETIME) + "\n\n");
				builder.append("Flash: " + getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
				builder.append("Focal Length: " + getExifTag(exif,ExifInterface.TAG_FOCAL_LENGTH) + "\n\n");
				builder.append("GPS Datestamp: " + getExifTag(exif,ExifInterface.TAG_FLASH) + "\n");
				builder.append("GPS Latitude: " + getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE) + "\n");
				builder.append("GPS Latitude Ref: " + getExifTag(exif,ExifInterface.TAG_GPS_LATITUDE_REF) + "\n");
				builder.append("GPS Longitude: " + getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE) + "\n");
				builder.append("GPS Longitude Ref: " + getExifTag(exif,ExifInterface.TAG_GPS_LONGITUDE_REF) + "\n");
				builder.append("GPS Processing Method: " + getExifTag(exif,ExifInterface.TAG_GPS_PROCESSING_METHOD) + "\n");
				builder.append("GPS Timestamp: " + getExifTag(exif,ExifInterface.TAG_GPS_TIMESTAMP) + "\n\n");
				builder.append("Image Length: " + getExifTag(exif,ExifInterface.TAG_IMAGE_LENGTH) + "\n");
				builder.append("Image Width: " + getExifTag(exif,ExifInterface.TAG_IMAGE_WIDTH) + "\n\n");
				builder.append("Camera Make: " + getExifTag(exif,ExifInterface.TAG_MAKE) + "\n");
				builder.append("Camera Model: " + getExifTag(exif,ExifInterface.TAG_MODEL) + "\n");
				builder.append("Camera Orientation: " + getExifTag(exif,ExifInterface.TAG_ORIENTATION) + "\n");
				builder.append("Camera White Balance: " + getExifTag(exif,ExifInterface.TAG_WHITE_BALANCE) + "\n");
				
				TextView info = (TextView)findViewById(R.id.exifinfo);
				
				info.setText(builder.toString());
				
				builder = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			setTitle(filepath);
		}
	}
	
	private String getExifTag(ExifInterface exif,String tag){
		String attribute = exif.getAttribute(tag);
		
		return (null != attribute ? attribute : "");
	}
	
	private void setTitle(String filepath){
		int pos = filepath.lastIndexOf("/");
		String title = filepath;
		
		if(-1 != pos)
		{
			title = filepath.substring(pos + 1);
		}
		
		super.setTitle(getString(R.string.exif_info) + ": " + title);
	}
}
