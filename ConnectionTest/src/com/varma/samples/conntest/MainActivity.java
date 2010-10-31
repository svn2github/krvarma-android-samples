package com.varma.samples.conntest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ConnectivityReceiver receiver = null;
	private TextView txtNetworkInfo = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txtNetworkInfo = (TextView)findViewById(R.id.txtNetworkInfo);
        receiver = new ConnectivityReceiver();
        
        registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
    @Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
    	
		super.onDestroy();
	}
    
    private String getNetworkStateString(NetworkInfo.State state){
    	String stateString = "Unknown";
    	
    	switch(state)
    	{
    		case CONNECTED:		stateString = "Connected"; 		break;
    		case CONNECTING:	stateString = "Connecting"; 	break;
    		case DISCONNECTED:	stateString = "Disconnected"; 	break;
    		case DISCONNECTING:	stateString = "Disconnecting"; 	break;
    		case SUSPENDED:		stateString = "Suspended"; 		break;
    		default:			stateString = "Unknown"; 		break;
    	}
    	
    	return stateString;
    }

	private class ConnectivityReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			
			if(null != info)
			{
				String state = getNetworkStateString(info.getState());
				String stateString = info.toString().replace(',', '\n');
				
				String infoString = String.format("Network Type: %s\nNetwork State: %s\n\n%s",
						info.getTypeName(),state,stateString);
				
				Log.i("ConnTest",info.getTypeName());
				Log.i("ConnTest",state);
				Log.i("ConnTest",info.toString());
				
				txtNetworkInfo.setText(infoString);
			}
		}
    }
}