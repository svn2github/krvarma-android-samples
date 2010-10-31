package com.varma.samples.netinfo;

import com.varma.utils.netutils.NetInfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        NetInfo netInfo = new NetInfo(this);
        String ipAddress = netInfo.getIPAddress();
        
        ((TextView)findViewById(R.id.info_text)).setText(ipAddress);
    }
}