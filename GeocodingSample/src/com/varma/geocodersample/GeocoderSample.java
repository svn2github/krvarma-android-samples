package com.varma.geocodersample;

import java.util.List;

import com.varma.interfaces.GPSCallback;
import com.varma.managers.GPSManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class GeocoderSample extends Activity implements GPSCallback{
    private static final int ERROR_GEOCODER = -2;
	private static final int ERROR_SUCCESS = 0;
	private static final int ERROR_LOCATION_TIMEOUT = -1;
	
	private static final int TIMEOUT_PERIOD = (5 * 60 * 1024);
	private static final int SLEEP_TIME = 1000;
	private static final int USE_CURRENT_LOCATION = 0;
    private static final int USE_LOCATION_NAME = 1;
    
    private static final int DIALOG_NO_INFO = 1;
    private static final int DIALOG_ERROR_GPS = 2;
    private static final int DIALOG_ERROR_GEOCODE = 3;
    
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Geocoder coder = null;
    private GPSManager gpsManager = null;
    
    private List<Address> addresses = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        coder = new Geocoder(this);
        gpsManager = new GPSManager();
        
        ((Button)findViewById(R.id.find_location)).setOnClickListener(onClick);
    }
    
    
    
    @Override
	protected Dialog onCreateDialog(int id)
	{
    	int stringid = 0;
    	
		switch(id)
		{
			case DIALOG_NO_INFO:			stringid = R.string.error_no_info;			break;
			case DIALOG_ERROR_GPS: 			stringid = R.string.error_gps_not_found;	break;
			case DIALOG_ERROR_GEOCODE:		stringid = R.string.error_geocoder;			break;
		}
		
		Dialog dialog = null;
		
		if(0 != stringid)
		{
			dialog = createAlertDialog(GeocoderSample.this,getString(R.string.app_name),getString(stringid));
		}
		else
		{
			dialog = super.onCreateDialog(id);
		}
		
		return dialog;
	}


	@Override
	public void onGPSUpdate(Location location)
	{
    	latitude = location.getLatitude();
    	longitude = location.getLongitude();
	}
    
    private int getcurrentLocationInfo()
    {
    	int result = ERROR_SUCCESS;
    	
    	boolean waitingForLocation = true;
    	long startTrackingTime = System.currentTimeMillis();
    	long currentTime = 0;
    	
    	while(waitingForLocation)
    	{
    		try { Thread.sleep(SLEEP_TIME); }
			catch (InterruptedException e) { e.printStackTrace(); }
			
			currentTime = System.currentTimeMillis();
    		
    		if(0.0 != latitude && 
    		   0.0 != longitude)
    		{
    			waitingForLocation = false;
    		}
    		else if((currentTime - startTrackingTime) >= TIMEOUT_PERIOD)
    		{
    			waitingForLocation = false;
    			result = ERROR_LOCATION_TIMEOUT;
    		}
    	}
    	
    	if(result == ERROR_SUCCESS)
    	{
    		try 
			{
    			addresses = coder.getFromLocation(latitude, longitude, 5);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				result = ERROR_GEOCODER;
			}
    	}
    	
    	gpsManager.stopListening();
    	gpsManager.setGPSCallback(null);
    	    	
    	return result;
    }
    
    private int getLocationInfo(final String locationName)
    {
    	int result = ERROR_SUCCESS;
    	
    	try 
		{
    		addresses = coder.getFromLocationName(locationName, 1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			result = ERROR_GEOCODER;
		}
    	
    	return result;
    }
    
	public ProgressDialog createProgressDialog(final Context context, final String message)
	{
		final ProgressDialog progressDialog = new ProgressDialog(context);
		
		progressDialog.setMessage(message);
		progressDialog.setProgressDrawable(getWallpaper());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		return progressDialog;
	}
	
	public static Dialog createAlertDialog(final Context context,final String title, final String message)
	{
		return new AlertDialog.Builder(context).setTitle(title).setMessage(message).create();
	}
    
    private String getGeocoderInfo(List<Address> addresses)
    {
    	StringBuilder builder = new StringBuilder();
    	Address address = null;
    	
    	for(int index=0; index<addresses.size(); ++index)
		{
    		address = addresses.get(index);
    		
			builder.append("Name: " + address.getLocality() + "\n");
			builder.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
			builder.append("Admin Area: " + address.getAdminArea() + "\n");
			builder.append("Country: " + address.getCountryName() + "\n");
			builder.append("Country Code: " + address.getCountryCode() + "\n");
			builder.append("Latitude: " + address.getLatitude() + "\n");
			builder.append("Longitude: " + address.getLongitude() + "\n\n");
		}
    	
    	return builder.toString();
    }
    
    private void hideKeyboard()
    {
    	//
		// @reference:
		// 		http://stackoverflow.com/questions/2342620/how-to-hide-keyboard-after-typing-in-edittext-in-android
		//
		InputMethodManager inputManager = (InputMethodManager) GeocoderSample.this.getSystemService(Context.INPUT_METHOD_SERVICE); 
		  
		inputManager.hideSoftInputFromWindow(GeocoderSample.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    private int getErrorDialogIdFromCode(int code)
    {
    	int dialogid = 0;
    	
    	switch(code)
    	{
    		case ERROR_LOCATION_TIMEOUT:	dialogid = DIALOG_ERROR_GPS;	break;
    		case ERROR_GEOCODER:			dialogid = DIALOG_ERROR_GPS;	break;
    	}
    	
    	return dialogid; 
    }
    
    private final View.OnClickListener onClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			CheckBox useLocation  = ((CheckBox)findViewById(R.id.use_current_location));
			String locationName = ((TextView)findViewById(R.id.location_name)).getText().toString();
			boolean isCurrentLocation = useLocation.isChecked();
			
			if(isCurrentLocation)
			{
				gpsManager.setGPSCallback(GeocoderSample.this);
				gpsManager.startListening(GeocoderSample.this);
			}
			else if(locationName.length() <= 0)
			{
				showDialog(DIALOG_NO_INFO);
				
				return;
			}
			
			new GeocodeTask().execute(isCurrentLocation ? USE_CURRENT_LOCATION : USE_LOCATION_NAME);
		}
	};

	private class GeocodeTask extends AsyncTask<Integer, Integer, Integer>
	{
		private String locationName = "";
		private ProgressDialog progress = null;
		
		@Override
		protected Integer doInBackground(Integer... task)
		{
			Integer result = 0;
			
			switch(task[0])
			{
				case USE_CURRENT_LOCATION:
				{
					result = getcurrentLocationInfo();
					
					break;
				}
				case USE_LOCATION_NAME:
				{
					result = getLocationInfo(locationName);
					
					break;
				}
			}
			
			return result;
		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			progress.dismiss();
			
			if(null!= addresses && addresses.size() > 0)
			{
				((TextView)findViewById(R.id.location_info)).setText(getGeocoderInfo(addresses));
			}
			else
			{
				showDialog(getErrorDialogIdFromCode(result));
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute()
		{ 	
			hideKeyboard();
			
			locationName = ((TextView)findViewById(R.id.location_name)).getText().toString();
			
			progress = createProgressDialog(GeocoderSample.this, getString(R.string.progress_message));
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}
	};	
}