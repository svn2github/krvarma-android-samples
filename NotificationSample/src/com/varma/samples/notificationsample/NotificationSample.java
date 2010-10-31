package com.varma.samples.notificationsample;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class NotificationSample extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)findViewById(R.id.btn_default_notification)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btn_custom_notification)).setOnClickListener(btnClick);
    }
    
    private void addDefaultNotification(){
    	NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	int icon = R.drawable.icon;
    	CharSequence text = "Notification Text";
    	CharSequence contentTitle = "Notification Title";
    	CharSequence contentText = "Sample notification text.";
    	long when = System.currentTimeMillis();
    	
    	Intent intent = new Intent(this, NotificationViewer.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
    	Notification notification = new Notification(icon,text,when);
    	
    	long[] vibrate = {0,100,200,300};
    	notification.vibrate = vibrate;
    	
    	notification.ledARGB = Color.RED;
    	notification.ledOffMS = 300;
    	notification.ledOnMS = 300;
    	
    	notification.defaults |= Notification.DEFAULT_LIGHTS;
    	//notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    	
    	notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
    	
    	notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
    
    private void addCustomNotification(){
    	final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	int icon = R.drawable.icon;
    	CharSequence text = "Notification Sample";
    	long when = System.currentTimeMillis();
    	long[] vibrate = {0,100,200,300};
    	
    	final Notification notification = new Notification(icon,text,when);
    	Intent notificationIntent = new Intent(this, NotificationViewer.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	
    	final RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.customlayout);
    	
    	contentView.setTextViewText(R.id.custom_view_text, "Sample Notification");
    	contentView.setProgressBar(R.id.custom_view_progress, Constants.PROGRESS_MAX, 0, false);
    	contentView.setImageViewResource(R.id.custom_view_image, R.drawable.icon);
    	
    	notification.contentView = contentView;
    	notification.contentIntent = contentIntent;

    	notification.vibrate = vibrate;
    	
    	notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    	
    	Thread thread = new Thread(){
    		 public void run(){
    			 int progress = 0;
    			 int percentage = 0;
    			 
    			 notification.vibrate = null;
    			 
    			 while(++progress <= Constants.PROGRESS_MAX){
    				try{ Thread.sleep(50); }
					catch (InterruptedException e) {e.printStackTrace(); }
					
					percentage = (int)(((float)progress/(float)Constants.PROGRESS_MAX) * (float)100.0);
					
					contentView.setProgressBar(R.id.custom_view_progress, Constants.PROGRESS_MAX, progress, false);
					contentView.setTextViewText(R.id.custom_view_text, "" + percentage + "% completed");
					
					notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    			 }
    		 }
    	};
    	
    	thread.start();
    }
    
    private final View.OnClickListener btnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.btn_default_notification:
				{
					addDefaultNotification();
					
					break;
				}
				case R.id.btn_custom_notification:
				{
					addCustomNotification();
					
					break;
				}
			}
		}
	};
}