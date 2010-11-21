package com.varma.samples.rssreader2.ui;

import java.util.ArrayList;

import com.varma.samples.rssreader2.utility.Utility;
import com.varma.samples.rssreader2.xmlparser.RSSParser;
import com.varma.samples.rssreader2.R;
import com.varma.samples.rssreader2.app.AppLog;
import com.varma.samples.rssreader2.app.RSSReaderApp;
import com.varma.samples.rssreader2.data.RSSChannel;
import com.varma.samples.rssreader2.data.RSSItem;
import com.varma.samples.rssreader2.db.DBAdaptor;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChannelsActivity extends ListActivity {
	private ChannelsAdapter adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setAdapter();
        setButtonClickListener();
    }

	private void setAdapter() {
		Cursor channels = ((RSSReaderApp)getApplication()).getDBAdaptor().getChannels();
                
        adapter = new ChannelsAdapter(this, R.layout.channelitem, channels);
        
        setListAdapter(adapter);
	}
	
	private void setButtonClickListener(){
		((Button)findViewById(R.id.btn_add_channel)).setOnClickListener(btnClick);
	}
	
	private void retrieveRSSFeeds(final String feedlink) {
		AppLog.logString(feedlink);
		
		new RSSFetchTask().execute(feedlink);
	}
	
	private void displayAddChannelDialog(){
		final FrameLayout layout = new FrameLayout(this);
		final EditText input = new EditText(this);
		
		input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		layout.addView(input, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		layout.setPadding(10,10,10,10);
		
		new AlertDialog.Builder(this)
		        .setView(layout)
		        .setTitle(getString(R.string.add_rss_channel_title))
		        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						retrieveRSSFeeds(input.getText().toString());
					}
				})
		        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				.show();
	}
    
	private void showRSSItems(long channelid){
		Intent intent = new Intent(this,RSSItemActivity.class);
		
		intent.putExtra(RSSReaderApp.CHANNEL_KEY, channelid);
		
		startActivity(intent);
	}
	
	private void retrieveAndInsertRSSChannel(String... params) {
		ArrayList<RSSItem> list = new ArrayList<RSSItem>();
		RSSParser rssHandler = new RSSParser(list);
		RSSChannel channel = new RSSChannel();

		Utility.retrieveRSSFeed(params[0], list, channel, rssHandler);
		
		channel.setTitle(rssHandler.getChannel().getTitle());
		channel.setDescription(rssHandler.getChannel().getDescription());
		channel.setLink(rssHandler.getChannel().getLink());
		channel.setLastupdated(rssHandler.getChannel().getLastupdated());
		
		DBAdaptor dbAdapter = ((RSSReaderApp)getApplication()).getDBAdaptor();
		
		long channelid = dbAdapter.getChannelIdFromLink(channel.getLink());
		
		if(0 == channelid)
			channelid = dbAdapter.insertChannel(channel);
		
		for(RSSItem item: list){
			item.setChannelid(channelid);
			
			dbAdapter.insertRSSItem(item);
		}
	}
	
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		AppLog.logString(v.getTag().toString());
		
		showRSSItems(Long.parseLong(v.getTag().toString()));
	}

	private class ChannelsAdapter extends SimpleCursorAdapter{

		public ChannelsAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 
					new String[] { DBAdaptor.CHANNEL_FIELD_TITLE, 
								   DBAdaptor.CHANNEL_FIELD_DESCRIPTION,
								   DBAdaptor.CHANNEL_FIELD_LINK, 
								   DBAdaptor.CHANNEL_FIELD_LASTUPDATED
								 },
					new int[] { 
								   R.id.title, 
								   R.id.description, 
								   R.id.link, 
								   R.id.lastupdated 
							  });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView)view.findViewById(R.id.title);
			TextView description = (TextView)view.findViewById(R.id.description);
			TextView link = (TextView)view.findViewById(R.id.link);
			TextView lastupdated = (TextView)view.findViewById(R.id.lastupdated);
			
			title.setText(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_TITLE)));
			
			description.setText(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_DESCRIPTION)));

			link.setText(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_LINK)));
			
			lastupdated.setText(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_LASTUPDATED)));
			
			view.setTag(cursor.getString(
					cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_ID)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.channelitem, parent, false);
			
			bindView(view, context, cursor);
			
			return view;
		}
    }
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btn_add_channel:{
					displayAddChannelDialog();
					
					break;
				}
			}
		}
	};
	
	private class RSSFetchTask extends AsyncTask<String, Void, String>{
		private ProgressDialog progress = null;
		@Override
		protected String doInBackground(String... params) {
			retrieveAndInsertRSSChannel(params);
			
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			
			adapter.notifyDataSetChanged();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(ChannelsActivity.this);
			
			progress.setMessage("Fetching RSS Feeds...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.show();
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	};
}