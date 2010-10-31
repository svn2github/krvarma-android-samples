package com.varma.samples.GPSSample;

import java.math.BigDecimal;

import com.varma.samples.interfaces.GPSCallback;
import com.varma.samples.managers.GPSManager;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class GPSSample extends Activity implements GPSCallback{
	private final static double[] multipliers = {
		1.0,1.0936133,0.001,0.000621371192
	};
	
	private final static String[] unitstrings = {
		"m","y","km","mi"
	};
	
	private GPSManager gpsManager = null;
	private int unitindex = 0;
	private double currentLon = 0;
	private double currentLat = 0;
	private double startLon = 0;
	private double startLat = 0;
	private boolean isMeasuring = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Spinner spinner = (Spinner) findViewById(R.id.unitspinner);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onMeasurementUnitClicked);
        
        gpsManager = new GPSManager();
        
        gpsManager.startListening(this);
        gpsManager.setGPSCallback(this);
        
        ((Button)findViewById(R.id.startmeasuring)).setOnClickListener(onButtonClicked);
        ((Button)findViewById(R.id.stopmeasuring)).setOnClickListener(onButtonClicked);
        
        enableButtons(false);
    }
    
    @Override
	public void onGPSUpdate(Location location)
	{
		currentLon = location.getLongitude();
		currentLat = location.getLatitude();
		
		if(isMeasuring){
			updateMeasurement();
		}
	}

	@Override
	protected void onDestroy() {
		gpsManager.stopListening();
		gpsManager.setGPSCallback(null);
		
		gpsManager = null;
		
		super.onDestroy();
	}
	
	private void startMeasuring(){
		isMeasuring = true;
		startLon = currentLon;
		startLat = currentLat;
		
		((TextView)findViewById(R.id.info)).setText(getString(R.string.measuring_info));
		
		enableButtons(true);
		
		updateMeasurement();
	}
	
	private void stopMeasuring(){
		isMeasuring = false;
		startLon = 0.0;
		startLat = 0.0;
		
		((TextView)findViewById(R.id.info)).setText(getString(R.string.start_measuring_info));
		((TextView)findViewById(R.id.distance)).setText("");
		
		enableButtons(false);
	}
	
	private void updateMeasurement(){
		double distance = calcGeoDistance(startLat,startLon,currentLat,currentLon) * multipliers[unitindex];
		String distanceText = "" + RoundDecimal(distance,2) + " " + unitstrings[unitindex];
		
		((TextView)findViewById(R.id.distance)).setText(distanceText);
	}
	
	private void enableButtons(boolean isMeasuring){
		((Button)findViewById(R.id.startmeasuring)).setEnabled(isMeasuring ? false : true);
		((Button)findViewById(R.id.stopmeasuring)).setEnabled(isMeasuring ? true : false);
	}
	
	private double calcGeoDistance(final double lat1, final double lon1, final double lat2, final double lon2)
	{
		double distance = 0.0;
		
		try
		{
			final float[] results = new float[3];
			
			Location.distanceBetween(lat1, lon1, lat2, lon2, results);
			
			distance = results[0];
		}
		catch (final Exception ex)
		{
			distance = 0.0;
		}
		
		return distance;
	}
	
	public double RoundDecimal(double value, int decimalPlace)
	{
		BigDecimal bd = new BigDecimal(value);
		
		bd = bd.setScale(decimalPlace, 6);
		
		return bd.doubleValue();
	}
	
	private AdapterView.OnItemSelectedListener onMeasurementUnitClicked = new AdapterView.OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			Log.i("Item","" + position + ", " +id);
			
			unitindex = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
		}
	};
	
	private View.OnClickListener onButtonClicked = new View.OnClickListener(){
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.startmeasuring:
				{
					startMeasuring();
					
					break;
				}
				case R.id.stopmeasuring:
				{
					stopMeasuring();
					
					break;
				}
			}
		}
	};
}