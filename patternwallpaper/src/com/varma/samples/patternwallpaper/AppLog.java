package com.varma.samples.patternwallpaper;

import android.util.Log;

public class AppLog {
	private static final String APP_TAG = "PatternWallpaper";
	
	public static int logString(String message){
		return Log.i(APP_TAG, message);
	}
}
