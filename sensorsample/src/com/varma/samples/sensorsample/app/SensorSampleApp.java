package com.varma.samples.sensorsample.app;

import java.util.List;

import android.app.Application;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorSampleApp extends Application{
	public static final String SENSOR_INDEX = "sensorsample.sensor_index";
	
	SensorManager manager = null;
	List<Sensor> sensors = null;
	
	public SensorManager getSensorManager() {
		return manager;
	}
	
	public List<Sensor> getSensorList() {
		return sensors;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensors = manager.getSensorList(Sensor.TYPE_ALL);
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
