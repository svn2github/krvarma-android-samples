package com.varma.samples.gpslogger.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.varma.samples.gpslogger.R;
import com.varma.samples.gpslogger.receivers.AlarmReceiver;
import com.varma.samples.gpslogger.settings.AppSettings;
import com.varma.samples.gpslogger.utility.AppLog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private int currentIntervalChoice = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setAppInfo();
        addButtonListeners();
        enableControls();
    }

	private void addButtonListeners() {
		((Button)findViewById(R.id.start_logging)).setOnClickListener(btnClick);
		((Button)findViewById(R.id.logging_interval)).setOnClickListener(btnClick);
	}

	private void setAppInfo() {
		TextView txtInfo = (TextView)findViewById(R.id.app_info);
        
        txtInfo.setText(Html.fromHtml(getString(R.string.app_info)));
        
        Linkify.addLinks(txtInfo, Linkify.ALL);
	}
	
	private void toggleLogging(boolean isStart, int interval){
		AlarmManager manager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
		PendingIntent loggerIntent = PendingIntent.getBroadcast(this, 0,new Intent(this,AlarmReceiver.class), 0);
		
		if(isStart){
			manager.cancel(loggerIntent);
			
			AppSettings.setServiceRunning(this, false);
			
			AppLog.logString("Service Stopped.");
		}
		else{
			setLogFileName();
			
			long duration = interval * 60 * 1000;
			
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime(), duration, loggerIntent);
			
			AppSettings.setServiceRunning(this, true);
			
			AppLog.logString("Service Started with interval " + interval + ", Logfile name: " + AppSettings.getLogFileName(this));
		}
	}
	
	private void enableControls(){
		boolean isServiceRunning = AppSettings.getServiceRunning(this);
		String buttonText = getString(R.string.start_logging);
		
		if(isServiceRunning){
			buttonText = getString(R.string.stop_logging);
			
			((Button)findViewById(R.id.logging_interval)).setEnabled(false);
		}
		else{
			((Button)findViewById(R.id.logging_interval)).setEnabled(true);
		}
		
		((Button)findViewById(R.id.start_logging)).setText(buttonText);
	}
	
	private void changeLoggingIntercal(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String loggingIntervals[] = { "5 minutes", "15 minutes", "30 minutes", "1 hour" }; 
		
    	builder.setTitle(getString(R.string.logging_interval));
    	builder.setSingleChoiceItems(loggingIntervals, currentIntervalChoice, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentIntervalChoice = which;
				
				setLoggingInterval(currentIntervalChoice);
				
				dialog.dismiss();
			}
		});
    	
    	builder.show();
	}
	
	private void setLoggingInterval(int intervalChoice){
		int interval = 5;
		
		switch(intervalChoice){
			case 0: 	interval = 5; 	break;
			case 1: 	interval = 15; 	break;
			case 2: 	interval = 30; 	break;
			case 3: 	interval = 60; 	break;
			default:	interval = 5; 	break;
		}
		
		AppSettings.setLoggingInterval(this, interval);
	}
	
	public void setLogFileName(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(new Date());
		String filename = "GPSLog." + dateString + ".kml";
		
		AppSettings.setLogFileName(this, filename);
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.start_logging:{
					toggleLogging(AppSettings.getServiceRunning(MainActivity.this), 
								  AppSettings.getLoggingInterval(MainActivity.this));
					
					enableControls(); 	
					
					break;
				}
				case R.id.logging_interval:{
					changeLoggingIntercal();
					
					break;
				}
			}
		}
	};
}