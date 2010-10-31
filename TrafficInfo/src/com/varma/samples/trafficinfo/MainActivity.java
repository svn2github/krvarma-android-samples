package com.varma.samples.trafficinfo;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView infoView = (TextView)findViewById(R.id.traffic_info);
        String info = "";
        
        info += "Mobile Interface:\n";
        info += ("\tReceived: " + TrafficStats.getMobileRxBytes() + " bytes / " + TrafficStats.getMobileRxPackets() + " packets\n");
        info += ("\tTransmitted: " + TrafficStats.getMobileTxBytes() + " bytes / " + TrafficStats.getMobileTxPackets() + " packets\n");
        
        info += "All Network Interface:\n";
        info += ("\tReceived: " + TrafficStats.getTotalRxBytes() + " bytes / " + TrafficStats.getTotalRxPackets() + " packets\n");
        info += ("\tTransmitted: " + TrafficStats.getTotalTxBytes() + " bytes / " + TrafficStats.getTotalTxPackets() + " packets\n");
        
        infoView.setText(info);
    }
}