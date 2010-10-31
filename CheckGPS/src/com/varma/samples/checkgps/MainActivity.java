package com.varma.samples.checkgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        boolean isEnabled = isGPSenabled();
        
        displayGPSState(isEnabled);
        
        if(!isEnabled)
        {
        	buildAlertMessageNoGps();
        }
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE)
		{
			displayGPSState(isGPSenabled());
		}
	}

	private void displayGPSState(boolean isEnabled)
	{
		String status = "Your GPS is " + (isEnabled ? "Enabled" : "Disabled");
		    
		 ((TextView)findViewById(R.id.text_gpsstatus)).setText(status);
	}

	private boolean isGPSenabled()
    {	
    	final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	
    	return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        builder.setTitle("GPS State");
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        builder.setCancelable(false);
        
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
        		{
                	public void onClick(final DialogInterface dialog, final int id) 
                	{
                		launchGPSOptions(); 
                	}
        		});
        
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
        		{
                	public void onClick(final DialogInterface dialog, final int id) 
                	{
                		dialog.cancel();
                	}
        		});
        
        builder.create().show();
    }

    private void launchGPSOptions()
    {
    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	startActivityForResult(intent, REQUEST_CODE);
    }
}