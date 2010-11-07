package com.varma.samples.camera.ui;

import com.varma.samples.camera.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		((Button)findViewById(R.id.start_camera)).setOnClickListener(onButtonClick);
	}
	
	private void startCameraActivity() {
		Intent intent = new Intent(IntroActivity.this,CameraActivity.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
		
		finish();
	}
	
	private View.OnClickListener onButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.start_camera:
				{
					startCameraActivity();
					
					break;
				}
			}
		}
	};
}
