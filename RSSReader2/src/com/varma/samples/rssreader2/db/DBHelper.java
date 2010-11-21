package com.varma.samples.rssreader2.db;

import com.varma.samples.rssreader2.app.AppLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
		
	private static final String CHANNELS_CREATE_SQL =
        "create table channels " + 
        "(" + 
        	"_id integer primary key autoincrement, " + 
        	"title text not null, " + 
        	"link text not null, " + 
        	"description text, " + 
        	"lastupdated text" +
        ");";
	
	private static final String RSSFEEDS_CREATE_SQL =
        "create table rssfeeds " + 
        "(" + 
        	"_id integer primary key autoincrement, " +
        	"channelid integer not null, " +
        	"title text not null, " + 
        	"link text not null, " + 
        	"description text, " + 
        	"lastupdated text, " +
        	"pubdate text, " +
        	"creater text, " +
        	"isread integer" +
        ");";

	
	public DBHelper(Context context) 
	{
		super(context, DBAdaptor.DB_NAME, null, DBAdaptor.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(CHANNELS_CREATE_SQL);
		db.execSQL(RSSFEEDS_CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		AppLog.logString("SQLiteOpenHelper.onUpgrade");
	}
}
