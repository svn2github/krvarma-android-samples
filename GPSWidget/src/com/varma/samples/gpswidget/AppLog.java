package com.varma.samples.gpswidget;

import android.util.Log;

public class AppLog {
	private static final String APP_TAG = "GPSWidget";
	
	public static int logString(String message){
		return Log.i(APP_TAG, message);
	}
}
