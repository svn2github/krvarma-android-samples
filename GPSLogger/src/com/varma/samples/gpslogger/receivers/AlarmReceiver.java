package com.varma.samples.gpslogger.receivers;

import com.varma.samples.gpslogger.service.GPSLoggerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context,GPSLoggerService.class));
	}
}
