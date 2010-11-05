package com.varma.samples.sntpclient;

import android.util.Log;

public class AppLog {
	private static final String APP_TAG = "SNTPClient";
	
	public static int logString(String message){
		return Log.i(APP_TAG, message);
	}
}
