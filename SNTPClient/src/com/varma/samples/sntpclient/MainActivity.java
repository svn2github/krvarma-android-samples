package com.varma.samples.sntpclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.varma.samples.sntp.NtpMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String DEFAULT_NTP_SERVER = "pool.ntp.org";
	private static final int SNTP_PORT = 123;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView textServer = (TextView)findViewById(R.id.ntp_server);
        Button btnConnect = (Button)findViewById(R.id.connect);
        
        textServer.setText(DEFAULT_NTP_SERVER);
        btnConnect.setOnClickListener(onButtonClick);
        
    }
    
    private View.OnClickListener onButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView textServer = (TextView)findViewById(R.id.ntp_server);
			String serverName = textServer.getText().toString();
			
			new SNTPClient().execute(serverName);
		}
	};  
	
	private double retrieveSNTPTime(String... params) throws SocketException,	UnknownHostException, IOException {
		String serverName = params[0];
		DatagramSocket socket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getByName(serverName);
		byte[] buffer = new NtpMessage().toByteArray();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SNTP_PORT);
		
		NtpMessage.encodeTimestamp(packet.getData(), 40,
				(System.currentTimeMillis()/1000.0) + 2208988800.0);
		
		socket.send(packet);
		
		packet = new DatagramPacket(buffer, buffer.length);
		
		socket.receive(packet);
		
		// Process response
		NtpMessage message = new NtpMessage(packet.getData());
		
		// Display response
		AppLog.logString("NTP server: " + serverName);
		AppLog.logString(message.toString());
		
		socket.close();
		
		return message.transmitTimestamp;
	}
	
	private class SNTPClient extends AsyncTask<String, Void, Integer>{
		private ProgressDialog progress = null;
		private double ntpTime = 0;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				ntpTime = retrieveSNTPTime(params);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Integer result) {
			TextView textSystemTime = (TextView)findViewById(R.id.system_time);
			TextView textNtpTime = (TextView)findViewById(R.id.ntp_time);
			
			double utc = ntpTime - (2208988800.0);
			
			// milliseconds
			long ms = (long) (utc * 1000.0);
			
			// date/time
			String date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date(ms));
			
			// fraction
			double fraction = ntpTime - ((long) ntpTime);
			String fractionSting = new DecimalFormat(".000000").format(fraction);
			
			textSystemTime.setText("System Time:\n" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.S").format(new Date()));
			textNtpTime.setText("NTP Time:\n" + date + fractionSting);
			
			progress.dismiss();
			progress = null;
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(MainActivity.this);
			
			progress.setMessage("Retrieving timestamp...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			progress.show();
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}