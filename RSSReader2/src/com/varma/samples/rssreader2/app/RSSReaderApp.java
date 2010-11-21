package com.varma.samples.rssreader2.app;
import com.varma.samples.rssreader2.db.DBAdaptor;

import android.app.Application;
import android.content.res.Configuration;


public class RSSReaderApp extends Application {
	public static final String CHANNEL_KEY = "channelid";
	
	private DBAdaptor dbAdaptor = null;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		dbAdaptor = new DBAdaptor(this);
		
		dbAdaptor.open();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		dbAdaptor.close();
		
		super.onTerminate();
	}
	
	public DBAdaptor getDBAdaptor(){
		return dbAdaptor;
	}
}
