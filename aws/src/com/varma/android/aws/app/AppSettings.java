package com.varma.android.aws.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {
	private static final String IS_SERVICE_STARTED = "isServiceStarted";
	
	public AppSettings(Context context)
	{
	}
	
	public static boolean isServiceStarted(Context context)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

		return pref.getBoolean(IS_SERVICE_STARTED, false);
	}
	
	public static void setServiceStarted(Context context, boolean isStarted){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(IS_SERVICE_STARTED, isStarted);
		editor.commit();
	}
}
