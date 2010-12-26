package com.varma.android.aws.ui;

import com.varma.android.aws.R;
import com.varma.android.aws.app.AppSettings;
import com.varma.android.aws.service.HTTPService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AWSActivity extends Activity {
    private static final int PREFERENCE_REQUEST_CODE = 1001;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prepareViews();
        setButtonHandlers();
        setButtonText(AppSettings.isServiceStarted(this));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean result = true;
    	
        switch (item.getItemId()) {
	        case R.id.menuPreference:{
	        	startActivityForResult(new Intent(this,AWSPreferenceActivity.class), PREFERENCE_REQUEST_CODE);
	        	
	            break;
	        }
	        case R.id.menuAbout:{
	        	displayAboutDialog();
	        	
	        	break;
	        }
	        default:{
	            result = super.onOptionsItemSelected(item);
	        }
        }
        
        return result;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case PREFERENCE_REQUEST_CODE:{
				break;
			}
		}
	}
	
	private void displayAboutDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(getString(R.string.about_title))
			   .setMessage(getString(R.string.app_info))
			   .setCancelable(false)
			   .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   dialog.dismiss();
				   }
			   	})
			   .create()
			   .show();
	}

	private void setButtonHandlers() {
		((Button)findViewById(R.id.btnStartStop)).setOnClickListener(btnClick);
	}

	private void prepareViews() {
		TextView txtInfo = (TextView)findViewById(R.id.txtInfo);
		
		Linkify.addLinks(txtInfo, Linkify.ALL);
	}
	
	private void setButtonText(boolean isServiceRunning){
		((Button)findViewById(R.id.btnStartStop)).setText(
				getString(isServiceRunning ? R.string.stop_caption : R.string.start_caption));
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btnStartStop:{
					Intent intent = new Intent(AWSActivity.this,HTTPService.class);
					
					if(AppSettings.isServiceStarted(AWSActivity.this)){
						stopService(intent);
						
						AppSettings.setServiceStarted(AWSActivity.this, false);
						setButtonText(false);
					}
					else{
						startService(intent);
						
						AppSettings.setServiceStarted(AWSActivity.this, true);
						setButtonText(true);
					}
					
					break;
				}
			}
		}
	};
}