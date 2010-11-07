package com.varma.samples.exifinfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PictureListActivity extends ListActivity {
	private PictureCursorAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Cursor pictures = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null, null, null, null);
        
        if(null != pictures)
        {
        	pictures.moveToFirst();
        
        	adapter = new PictureCursorAdapter(this, R.layout.listitem, pictures);
        	
        	setListAdapter(adapter);
        }
    }
    
    @Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		
		String filepath = (String) view.getTag();
		Intent intent = new Intent(this, ExifInfoActivity.class);
		
		intent.putExtra(ExifInfoActivity.FILE_PATH_KEY, filepath);
		
		startActivity(intent);
	}

	private class PictureCursorAdapter extends SimpleCursorAdapter{

		public PictureCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 
					new String[] { MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.SIZE},
					new int[] { R.id.displayname, R.id.path, R.id.size });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView)view.findViewById(R.id.displayname);
			TextView path = (TextView)view.findViewById(R.id.path);
			TextView size = (TextView)view.findViewById(R.id.size);
			
			title.setText(cursor.getString(
					cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));
			
			path.setText(cursor.getString(
					cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
			
			int sizeIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE);  
			
			size.setText(android.text.format.Formatter.formatFileSize(PictureListActivity.this, cursor.getLong(sizeIndex)));
			
			view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.listitem, parent, false);
			
			bindView(v, context, cursor);
			
			return v;
		}
    }
}