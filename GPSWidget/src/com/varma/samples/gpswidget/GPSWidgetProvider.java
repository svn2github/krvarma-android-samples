package com.varma.samples.gpswidget;

import java.util.List;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

public class GPSWidgetProvider extends AppWidgetProvider {
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, InfoActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gpswidget);
            views.setOnClickPendingIntent(R.id.txtInfo, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        
        context.startService(new Intent(context,GPSWidgetService.class));
	}
	
	public static class GPSWidgetService extends Service{
		private LocationManager manager = null;
		
		private LocationListener listener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			@Override
			public void onProviderEnabled(String provider) {}
			@Override
			public void onProviderDisabled(String provider) {}
			
			@Override
			public void onLocationChanged(Location location) {
				AppLog.logString("Service.onLocationChanged()");
				
				updateCoordinates(location.getLatitude(),location.getLongitude());
				
				stopSelf();
			}
		};
		
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		@Override
		public void onCreate() {
			super.onCreate();
			
			AppLog.logString("Service.onCreate()");
			
			manager = (LocationManager)getSystemService(LOCATION_SERVICE);
		}

		@Override
		public void onStart(Intent intent, int startId) {
			super.onStart(intent, startId);
			
			waitForGPSCoordinates();
		}

		@Override
		public void onDestroy() {
			stopListening();
			
			AppLog.logString("Service.onDestroy()");
			
			super.onDestroy();
		}

		public int onStartCommand(Intent intent, int flags, int startId) {
			waitForGPSCoordinates();
			
			AppLog.logString("Service.onStartCommand()");
			
			return super.onStartCommand(intent, flags, startId);
		}
		
		private void waitForGPSCoordinates() {
			startListening();
		}
		
		private void startListening(){
			AppLog.logString("Service.startListening()");
			
			final Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			final String bestProvider = manager.getBestProvider(criteria, true);

			if (bestProvider != null && bestProvider.length() > 0) {
				manager.requestLocationUpdates(bestProvider, 500, 10, listener);
			}
			else {
				final List<String> providers = manager.getProviders(true);

				for (final String provider : providers) {
					manager.requestLocationUpdates(provider, 500, 10, listener);
				}
			}
		}
		
		private void stopListening(){
			try {
				if (manager != null && listener != null) {
					manager.removeUpdates(listener);
				}

				manager = null;
			}
			catch (final Exception ex) {

			}
		}
		
		private void updateCoordinates(double latitude, double longitude){
			Geocoder coder = new Geocoder(this);
			List<Address> addresses = null;
			String info = "";
			
			AppLog.logString("Service.updateCoordinates()");
			AppLog.logString(info);
			
			try 
			{
    			addresses = coder.getFromLocation(latitude, longitude, 2);
    			
    			if(null != addresses && addresses.size() > 0){
    				int addressCount = addresses.get(0).getMaxAddressLineIndex();
    				
    				if(-1 != addressCount){
    					for(int index=0; index<=addressCount; ++index){
    						info += addresses.get(0).getAddressLine(index);
    						
    						if(index < addressCount)
    							info += ", ";
    					}
    				}
    				else
    				{
    					info += addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getAdminArea();
    				}
    			}
    			
    			AppLog.logString(addresses.get(0).toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			coder = null;
			addresses = null;
			
			if(info.length() <= 0){
				info = "lat " + latitude + ", lon " + longitude;
			}
			else{
				info += ("\n" + "(lat " + latitude + ", lon " + longitude + ")");
			}
			
			RemoteViews views = new RemoteViews(getPackageName(), R.layout.gpswidget);
			
			views.setTextViewText(R.id.txtInfo, info);
			
			ComponentName thisWidget = new ComponentName(this, GPSWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
		}
	}
}
