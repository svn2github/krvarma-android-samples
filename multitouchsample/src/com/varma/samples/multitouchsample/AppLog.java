package com.varma.samples.multitouchsample;

import android.util.Log;

public class AppLog {
	private final static String APP_TAG = "com.varma.samples.multitouchsample";
	
	public static int log(String message){
		return Log.i(APP_TAG,message);
	}
}
