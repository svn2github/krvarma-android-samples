package com.varma.samples.rssreader2.app;

import android.util.Log;

public class AppLog {
	private static final String APP_TAG = "RSSReader2";
	
	public static int logString(String message){
		return Log.i(APP_TAG, message);
	}
}
