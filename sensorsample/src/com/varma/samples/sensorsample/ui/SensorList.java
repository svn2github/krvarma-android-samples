package com.varma.samples.sensorsample.ui;

import java.util.List;

import com.varma.samples.sensorsample.app.AppLog;
import com.varma.samples.sensorsample.app.R;
import com.varma.samples.sensorsample.app.SensorSampleApp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SensorList extends ListActivity {
	SensorListAdapter adapter = null;
	SensorSampleApp app = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        app = (SensorSampleApp)getApplication();
        adapter = new SensorListAdapter(this, R.layout.listitem, app.getSensorList());
        
        setListAdapter(adapter);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		AppLog.log(app.getSensorList().get(position).getName());
		
		startSensorTestActivity(position);
	}

	private void startSensorTestActivity(int position) {
		Intent intent = new Intent(this, SensorTestActivity.class);
		
		intent.putExtra(SensorSampleApp.SENSOR_INDEX, position);
		
		startActivity(intent);
	}

	private class SensorListAdapter extends ArrayAdapter<Sensor>{
    	private List<Sensor> objects = null;
    	
		public SensorListAdapter(Context context, int textviewid, List<Sensor> objects) {
			super(context, textviewid, objects);
			
			this.objects = objects;
		}
		
		@Override
		public int getCount() {
			return ((null != objects) ? objects.size() : 0);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Sensor getItem(int position) {
			return ((null != objects) ? objects.get(position) : null);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(null == view)
			{
				LayoutInflater vi = (LayoutInflater)SensorList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.listitem, null);
			}
			
			Sensor sensor = objects.get(position);
			
			if(null != sensor)
			{
				TextView txtName = (TextView)view.findViewById(R.id.txtName);
				TextView txtVendor = (TextView)view.findViewById(R.id.txtVendor);
				TextView txtVersion = (TextView)view.findViewById(R.id.txtVersion);
				
				txtName.setText(sensor.getName());
				txtVendor.setText("Vendor: " + sensor.getVendor());
				txtVersion.setText("Version: " + sensor.getVersion());
			}
			
			return view;
		}
    }
}