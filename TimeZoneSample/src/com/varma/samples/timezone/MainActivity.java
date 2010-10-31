package com.varma.samples.timezone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	protected static final int UPDATE_UI = 0;
	
	TimeZoneAdaptor timezoneAdaptor = null;
	List<TimeZoneData> timezonelist = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        timezonelist = new ArrayList<TimeZoneData>(); 
        timezoneAdaptor = new TimeZoneAdaptor(this,R.layout.timezoneview,timezonelist);
        
        setListAdapter(timezoneAdaptor);
    }
        
    @Override
	protected void onStart() {
		super.onStart();
		
		String[] listItems = TimeZone.getAvailableIDs();
        
        TimeZone timezone = null;
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a");
        Date now = new Date();
        
        for(int index=0; index<listItems.length; ++index)
        {
        	timezone = TimeZone.getTimeZone(listItems[index]);
        	
        	format.setTimeZone(timezone);
        	       	
        	timezonelist.add(new TimeZoneData(getDiaplayName(listItems[index]), format.format(now)));
        	        	
        	timezone = null;
        }
	}

    private String getDiaplayName(String timezonename)
    {
    	String displayname = timezonename;
    	int sep = timezonename.indexOf('/');
    	
    	if(-1 != sep)
    	{
    		displayname = timezonename.substring(0,sep) + ", " + timezonename.substring(sep + 1);
    		displayname = displayname.replace("_", " ");
    	}
    		
    	return displayname;
    }
    
	public class TimeZoneAdaptor extends ArrayAdapter<TimeZoneData> {
		
		List<TimeZoneData> objects = null;

		public TimeZoneAdaptor(Context context,int textViewResourceId,List<TimeZoneData> objects) {
			super(context, textViewResourceId, objects);
			
			this.objects = objects;
		}

		@Override
		public int getCount() {
			return ((null != objects) ? objects.size() : 0);
		}

		@Override
		public TimeZoneData getItem(int position) {
			return ((null != objects) ? objects.get(position) : null);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(null == view)
			{
				LayoutInflater vi = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = vi.inflate(R.layout.timezoneview, null);
			}
			
			TimeZoneData data = objects.get(position);
			
			if(null != data)
			{
				TextView textName = (TextView)view.findViewById(R.id.timezone_name);
				TextView textTime = (TextView)view.findViewById(R.id.timezone_time);
				
				textName.setText(data.name);
				textTime.setText(data.time);
			}
			
			return view;
		}
	}
}