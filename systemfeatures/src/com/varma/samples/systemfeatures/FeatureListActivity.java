package com.varma.samples.systemfeatures;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeatureListActivity extends ListActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        PackageManager pm = getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        
        for(FeatureInfo feature: features){
        	Log.i("FeatureListActivity",getFeatureName(feature));
        }
        
        setListAdapter(new GestureAdaptor(this, R.layout.listitem, features));
    }
    
    private String getFeatureName(FeatureInfo feature){
    	String name = feature.name;
    	
    	if(null == name)
    		name = "OpenGL ES Version " + feature.reqGlEsVersion;
    	
    	return name;
    }
    
    private class GestureAdaptor extends ArrayAdapter<FeatureInfo>{
    	private FeatureInfo[] objects = null;
    	
		public GestureAdaptor(Context context, int textviewid, FeatureInfo[] objects) {
			super(context, textviewid, objects);
			
			this.objects = objects;
		}
		
		@Override
		public int getCount() {
			return ((null != objects) ? objects.length : 0);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public FeatureInfo getItem(int position) {
			return ((null != objects) ? objects[position] : null);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(null == view)
			{
				LayoutInflater vi = (LayoutInflater)FeatureListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.listitem, null);
			}
			
			FeatureInfo data = objects[position];
			
			if(null != data)
			{
				TextView name = (TextView)view.findViewById(R.id.feature_name);
		
				name.setText(getFeatureName(data));
			}
			
			return view;
		}
    }
}