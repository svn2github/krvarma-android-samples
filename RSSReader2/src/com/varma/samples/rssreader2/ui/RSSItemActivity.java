package com.varma.samples.rssreader2.ui;

import com.varma.samples.rssreader2.R;
import com.varma.samples.rssreader2.app.RSSReaderApp;
import com.varma.samples.rssreader2.db.DBAdaptor;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RSSItemActivity extends ListActivity {
	private RSSItemAdapter adapter = null;
	private int backColors[] = { Color.WHITE, Color.BLACK };
	private int textColors[] = { Color.BLACK, Color.WHITE };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rssitem);
		
		long channelid = getIntent().getExtras().getLong(RSSReaderApp.CHANNEL_KEY);
		DBAdaptor dbAdaptor = ((RSSReaderApp)getApplication()).getDBAdaptor();
		Cursor cursor = dbAdaptor.getRssItems(channelid);
		
		adapter = new RSSItemAdapter(this, R.layout.rssitemview, cursor);
		
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View view, int position, long id) {
		super.onListItemClick(l, view, position, id);
		
		String link = view.getTag().toString();
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(link));
		
		TextView title = (TextView)view.findViewById(R.id.txtTitle);
		TextView date = (TextView)view.findViewById(R.id.txtDate);
		TextView description = (TextView)view.findViewById(R.id.txtDescription);
		
		title.setBackgroundColor(backColors[1]);
		title.setTextColor(textColors[1]);
		
		date.setBackgroundColor(backColors[1]);
		date.setTextColor(textColors[1]);
		
		description.setBackgroundColor(backColors[1]);
		description.setTextColor(textColors[1]);
		
		startActivity(intent);
	}
	
	private class RSSItemAdapter extends SimpleCursorAdapter{

		public RSSItemAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 
					new String[] { DBAdaptor.RSSITEM_FIELD_TITLE, 
								   DBAdaptor.RSSITEM_FIELD_PUBDATE,
								   DBAdaptor.RSSITEM_FIELD_DESCRIPTION, 
								 },
					new int[] { 
								   R.id.txtTitle, 
								   R.id.txtDate, 
								   R.id.txtDescription, 
							  });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView)view.findViewById(R.id.txtTitle);
			TextView date = (TextView)view.findViewById(R.id.txtDate);
			TextView description = (TextView)view.findViewById(R.id.txtDescription);
			
			int isRead = cursor.getInt(
					cursor.getColumnIndex(DBAdaptor.RSSITEM_FIELD_READ));
			
			
			title.setText(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.RSSITEM_FIELD_TITLE)));
			
			date.setText("on" + cursor.getString(
					cursor.getColumnIndex(DBAdaptor.RSSITEM_FIELD_PUBDATE)));

			description.setText(Html.fromHtml(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.RSSITEM_FIELD_DESCRIPTION))));
			
			view.setTag(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.RSSITEM_FIELD_LINK)));
			
			title.setBackgroundColor(backColors[isRead]);
			title.setTextColor(textColors[isRead]);
			
			date.setBackgroundColor(backColors[isRead]);
			date.setTextColor(textColors[isRead]);
			
			description.setBackgroundColor(backColors[isRead]);
			description.setTextColor(textColors[isRead]);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.rssitemview, parent, false);
			
			bindView(view, context, cursor);
			
			return view;
		}
    }
}
