package com.varma.samples.gpslogger.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
	public static final String GPSLOGGER_PREF_NAME = "GPSLogger_8879461315648797161";
	public static final String SERVICE_STATE = "isServiceRunning";
	public static final String LOGGING_INTERVAL = "loggingInterval";
	public static final String LOG_FILE = "logFile";
	
	public static boolean getServiceRunning(Context context){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		
		return pref.getBoolean(SERVICE_STATE, false);
	}
	
	public static void setServiceRunning(Context context, boolean isRunning){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean(SERVICE_STATE, isRunning);
		editor.commit();
	}
	
	public static int getLoggingInterval(Context context){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		
		return pref.getInt(LOGGING_INTERVAL, 5);
	}
	
	public static void setLoggingInterval(Context context, int interval){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();

		editor.putInt(LOGGING_INTERVAL, interval);
		editor.commit();
	}
	
	public static String getLogFileName(Context context){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		
		return pref.getString(LOG_FILE, "");
	}
	
	public static void setLogFileName(Context context,String filename){
		SharedPreferences pref = context.getSharedPreferences(GPSLOGGER_PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString(LOG_FILE, filename);
		editor.commit();
	}

}
