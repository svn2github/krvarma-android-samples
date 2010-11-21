package com.varma.samples.rssreader2.db;

import com.varma.samples.rssreader2.data.RSSChannel;
import com.varma.samples.rssreader2.data.RSSItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdaptor {
	public static final String DB_NAME = "rssreader2.db";
	public static final String CHANNELS_TABLE = "channels";
	public static final String RSSFEEDS_TABLE = "rssfeeds";
	public static final int DB_VERSION = 1;
	
	public static final String CHANNEL_FIELD_ID = "_id";
	public static final String CHANNEL_FIELD_TITLE = "title";
	public static final String CHANNEL_FIELD_LINK = "link";
	public static final String CHANNEL_FIELD_DESCRIPTION = "description";
	public static final String CHANNEL_FIELD_LASTUPDATED = "lastupdated";
	
	public static final String RSSITEM_FIELD_ID = "_id";
	public static final String RSSITEM_FIELD_CHANNEL_ID = "channelid";
	public static final String RSSITEM_FIELD_TITLE = "title";
	public static final String RSSITEM_FIELD_LINK = "link";
	public static final String RSSITEM_FIELD_DESCRIPTION = "description";
	public static final String RSSITEM_FIELD_LASTUPDATED = "lastupdated";
	public static final String RSSITEM_FIELD_PUBDATE = "pubdate";
	public static final String RSSITEM_FIELD_CREATER = "creater";
	public static final String RSSITEM_FIELD_READ = "isread";
	
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	
	public DBAdaptor(Context context){
		dbHelper = new DBHelper(context);
	}
	
	public void open(){
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public long insertChannel(RSSChannel channel){
		ContentValues values = new ContentValues();
		
		values.put(CHANNEL_FIELD_TITLE,channel.getTitle());
		values.put(CHANNEL_FIELD_LINK,channel.getLink());
		values.put(CHANNEL_FIELD_DESCRIPTION,channel.getDescription());
		values.put(CHANNEL_FIELD_LASTUPDATED,channel.getLastupdated());
		
		return db.insert(DBAdaptor.CHANNELS_TABLE, null, values);
	}
	
	public long insertRSSItem(RSSItem item){
		ContentValues values = new ContentValues();
		
		values.put(RSSITEM_FIELD_CHANNEL_ID,item.getChannelid());
		values.put(RSSITEM_FIELD_TITLE,item.getTitle());
		values.put(RSSITEM_FIELD_LINK,item.getLink());
		values.put(RSSITEM_FIELD_DESCRIPTION,item.getDescription());
		values.put(RSSITEM_FIELD_PUBDATE,item.getLastupdated());
		values.put(RSSITEM_FIELD_CREATER,item.getCreater());
		values.put(RSSITEM_FIELD_READ,item.isRead());
		
		return db.insert(DBAdaptor.RSSFEEDS_TABLE, null, values);
	}
	
	public Cursor getChannel(String link){
		return db.query(DBAdaptor.CHANNELS_TABLE, 
				new String[] {
								CHANNEL_FIELD_ID, 
								CHANNEL_FIELD_TITLE, 
								CHANNEL_FIELD_LINK,
								CHANNEL_FIELD_DESCRIPTION,
								CHANNEL_FIELD_LASTUPDATED
							 }, 
				CHANNEL_FIELD_LINK + " = '" + link + "'",
				null,null,null,null);
	}
	
	public Cursor getChannels(){
		return db.query(DBAdaptor.CHANNELS_TABLE, 
				new String[] {
								CHANNEL_FIELD_ID, 
								CHANNEL_FIELD_TITLE, 
								CHANNEL_FIELD_LINK,
								CHANNEL_FIELD_DESCRIPTION,
								CHANNEL_FIELD_LASTUPDATED
							 }, 
				null,null,null,null,null);
	}
	
	public long getChannelIdFromLink(String link){
		long id = 0;
		Cursor cursor = getChannel(link);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			
			id = cursor.getInt(cursor.getColumnIndex(DBAdaptor.CHANNEL_FIELD_ID));
		}
		
		cursor.close();
		
		return id;
	}
	
	public Cursor getRssItems(long channelid){
		return db.query(DBAdaptor.RSSFEEDS_TABLE, 
				new String[] {
								RSSITEM_FIELD_ID, 
								RSSITEM_FIELD_TITLE, 
								RSSITEM_FIELD_PUBDATE, 
								RSSITEM_FIELD_DESCRIPTION,
								RSSITEM_FIELD_LINK,
								RSSITEM_FIELD_READ
							 }, 
				RSSITEM_FIELD_CHANNEL_ID + " = " + channelid,
				null,null,null,null);
	}
	
	public Cursor getRssItem(String link){
		return db.query(DBAdaptor.RSSFEEDS_TABLE, 
				new String[] {
								RSSITEM_FIELD_ID, 
								RSSITEM_FIELD_TITLE, 
								RSSITEM_FIELD_PUBDATE, 
								RSSITEM_FIELD_DESCRIPTION,
								RSSITEM_FIELD_LINK,
								RSSITEM_FIELD_READ
							 }, 
				RSSITEM_FIELD_LINK + " = '" + link + "'",
				null,null,null,null);
	}
}
