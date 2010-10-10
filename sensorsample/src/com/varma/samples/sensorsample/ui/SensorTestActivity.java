package com.varma.samples.sensorsample.ui;

import com.varma.samples.sensorsample.app.AppLog;
import com.varma.samples.sensorsample.app.R;
import com.varma.samples.sensorsample.app.SensorSampleApp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorTestActivity extends Activity 
								implements SensorEventListener{
	SensorSampleApp app = null;
	int sensorIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensortest);
		
		app = (SensorSampleApp)getApplication();
		sensorIndex = getIntent().getIntExtra(SensorSampleApp.SENSOR_INDEX, 0);
		
		String name = ((SensorSampleApp)getApplication()).getSensorList().get(sensorIndex).getName();
		
		setTextViewText(R.id.txtSensorName, "Testing sensor using:\n" + name);
	}
	
	@Override
	protected void onStop() {
		AppLog.log("Unregistering event listener");
		
		app.getSensorManager().unregisterListener(this);
		
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		AppLog.log("Registering event listener");
		
		app.getSensorManager().registerListener(this, 
				app.getSensorList().get(sensorIndex), SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		setTextViewText(R.id.txtSensorAccuracy,"Sensor Accuracy " + getSensorAccuracyString(accuracy));
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		String sensorValues = "Sensor timestamp: " + event.timestamp + "\n\n";
		
		for(int index=0; index<event.values.length; ++index){
			sensorValues += ("Sensor Value #" + (index + 1) + ": " + event.values[index] + "\n");
		}
		
		setTextViewText(R.id.txtSensorValues,sensorValues);
	}
	
	private void setTextViewText(int id,String text){
		((TextView)findViewById(id)).setText(text);
	}
	
	private String getSensorAccuracyString(int accuracy) {
		String accuracyString = "Unknown";
		
		switch(accuracy)
		{
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: accuracyString = "High"; break;
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW: accuracyString = "Low"; break;
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: accuracyString = "Medium"; break;
			case SensorManager.SENSOR_STATUS_UNRELIABLE: accuracyString = "Unreliable"; break;
			default: accuracyString = "Unknown"; break;
		}
		
		return accuracyString;
	}
}
