package com.varma.samples.gesturesample;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GestureList extends ListActivity {
	private static final int PATH_COLOR = Color.rgb(255,255,0);
	private static final int BITMAP_INSET = 8;
	private static final int BITMAP_SIZE = 32;

	private class GestureItem extends Object{
		public String name;
		public Bitmap bitmap;
		int stroks;
		
		public GestureItem(String name, Bitmap bitmap,int stroks){
			this.name = name;
			this.bitmap = bitmap;
			this.stroks = stroks;
		}
	}
	
	GestureAdaptor gestureAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gestureAdapter = new GestureAdaptor(this, R.layout.listitem, getGestureList(GestureActivity.gesturelib));
		
		setListAdapter(gestureAdapter);
	}
	
	private List<GestureItem> getGestureList(GestureLibrary lib){
		ArrayList<GestureItem> list = new ArrayList<GestureItem>();
		Set<String> gestureentries = lib.getGestureEntries();
		ArrayList<Gesture> gestures = null;
		
		for(String name: gestureentries){
			gestures = lib.getGestures(name);
			
			for(Gesture gesture: gestures){
				list.add(new GestureItem(name,
						gesture.toBitmap(BITMAP_SIZE,BITMAP_SIZE,BITMAP_INSET,PATH_COLOR),
						gesture.getStrokesCount()));
			}
		}
		
		return list;
	}
	
	private class GestureAdaptor extends ArrayAdapter<GestureItem>{
    	private List<GestureItem> objects = null;
    	
		public GestureAdaptor(Context context, int textviewid, List<GestureItem> objects) {
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
		public GestureItem getItem(int position) {
			return ((null != objects) ? objects.get(position) : null);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(null == view)
			{
				LayoutInflater vi = (LayoutInflater)GestureList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.listitem, null);
			}
			
			GestureItem data = objects.get(position);
			
			if(null != data)
			{
				TextView name = (TextView)view.findViewById(R.id.gesture_name);
				
				name.setText("Name: " + data.name + "\nStroks: " + data.stroks);
				name.setCompoundDrawablesWithIntrinsicBounds(
						new BitmapDrawable(data.bitmap),null,null,null);
			}
			
			return view;
		}
    }
}
