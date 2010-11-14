package com.varma.samples.gpslogger.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.varma.samples.gpslogger.settings.AppSettings;
import com.varma.samples.gpslogger.utility.AppLog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;

public class GPSLoggerService extends Service implements LocationListener{
	// this is a hack and need to be changed, here the offset is the length of the tag XML "</Document></kml", 
	// we minus this offset from the end of the file and write the next Placemark entry.  
	private static final int KML_INSERT_OFFSET = 17;
	
	private static final int gpsMinTime = 500;
	private static final int gpsMinDistance = 0;
	private static final int TIMER_DELAY = 1000;
	private static final int GEOCODER_MAX_RESULTS = 5;
	
	private LocationManager manager = null;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private double altitude = 0.0;
	private Timer monitoringTimer = null;

	public GPSLoggerService() {
		AppLog.logString("GPSLoggerService.GPSLoggerService().");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		AppLog.logString("GPSLoggerService.onBind().");
		
		return null;
	}

	@Override
	public void onCreate() {
		AppLog.logString("GPSLoggerService.onCreate().");
		
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		AppLog.logString("GPSLoggerService.onStart().");
		
		startLoggingService();
		startMonitoringTimer();
		
		super.onStart(intent, startId);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		AppLog.logString("GPSLoggerService.onStartCommand().");
		
		startLoggingService();
		startMonitoringTimer();
		
		return Service.START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {
		AppLog.logString("GPSLoggerService.onLocationChanged().");
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		AppLog.logString("GPSLoggerService.onProviderDisabled().");
	}

	@Override
	public void onProviderEnabled(String provider) {
		AppLog.logString("GPSLoggerService.onProviderEnabled().");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		AppLog.logString("GPSLoggerService.onStatusChanged().");
	}
	
	private void startLoggingService(){
		if (manager == null)
		{
			manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		
		final Criteria criteria = new Criteria();
		
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		final String bestProvider = manager.getBestProvider(criteria, true);
		
		if (bestProvider != null && bestProvider.length() > 0)
		{
			manager.requestLocationUpdates(bestProvider, gpsMinTime,gpsMinDistance, this);
		}
		else
		{
			final List<String> providers = manager.getProviders(true);
			
			for (final String provider : providers)
			{
				manager.requestLocationUpdates(provider, gpsMinTime, gpsMinDistance, this);
			}
		}
	}
	
	private void stopLoggingService(){
		stopSelf();
	}
	
	private void startMonitoringTimer(){
		monitoringTimer = new Timer();

		monitoringTimer.scheduleAtFixedRate(
				new TimerTask()
				{
					@Override
					public void run()
					{
						if (longitude != 0.0 && latitude != 0.0)
						{
							monitoringTimer.cancel();
							monitoringTimer = null;
							
							manager.removeUpdates(GPSLoggerService.this);
							
							saveCoordinates(latitude, longitude, altitude, getLocationName(latitude,longitude));
							stopLoggingService();
						}
					}
				}, 
				GPSLoggerService.TIMER_DELAY,
				GPSLoggerService.TIMER_DELAY);
	}
	
	private String getLocationName(double latitude, double longiture){
		String name = "";
		Geocoder geocoder = new Geocoder(this);
		
		try {
			List<Address> address = geocoder.getFromLocation(latitude, longiture, GPSLoggerService.GEOCODER_MAX_RESULTS);
			
			name = address.get(0).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return name;
	}
	
	private void saveCoordinates(double latitude, double longitude, double altitude, String name){
		File folder = new File(Environment.getExternalStorageDirectory(), "GPSLogger");
		boolean isNew = false;

		if (!folder.exists())
		{
			folder.mkdirs();
			
			isNew = true;
		}
		
		try {
			File kmlFile = new File(folder.getPath(),AppSettings.getLogFileName(this));
	
			if (!kmlFile.exists())
			{
				kmlFile.createNewFile();
				
				isNew = true;
			}
			
			if(isNew)
			{
				FileOutputStream initialWriter = new FileOutputStream(kmlFile,true);
	
				String xml = "<?xml version=\"1.0\"?>" + 
							 "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" +
							 	"<Document>" + "</Document>" +
							 "</kml>";
				
				initialWriter.write(xml.getBytes());
				initialWriter.flush();
				initialWriter.close();
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			String dateString = sdf.format(new Date());
			
			String placemark = "<Placemark>" + 
									"<name>" + name + "</name>" +
							   		"<description>" + "Created by GPSLogger sample application" + "</description>" + 
							   		"<TimeStamp>" +
							   			"<when>" + dateString + "</when>" + 
							   		"</TimeStamp>" + 
							   		"<Point>" + 
							   		"<coordinates>" + String.valueOf(longitude) + "," + String.valueOf(latitude) + "," + String.valueOf(altitude) + "</coordinates>" + 
							   		"</Point>" + 
							   "</Placemark>" + 
							   "</Document>" + 
							   "</kml>";
			
			RandomAccessFile fileAccess = new RandomAccessFile(kmlFile, "rw");
			FileLock lock = fileAccess.getChannel().lock();
			
			fileAccess.seek((kmlFile.length() - GPSLoggerService.KML_INSERT_OFFSET));
			fileAccess.write(placemark.getBytes());
			
			lock.release();
			fileAccess.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}