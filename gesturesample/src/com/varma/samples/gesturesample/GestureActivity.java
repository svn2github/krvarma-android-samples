package com.varma.samples.gesturesample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GestureActivity extends Activity {
	public static GestureLibrary gesturelib = null;
	
	private TextView gesturesText = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        gesturelib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        gesturesText = (TextView)findViewById(R.id.text_gestures);
        
		if (!gesturelib.load()) {
			Log.w("GestureActivity", "could not load gesture library");
			
			finish();
		}
		
		GestureOverlayView gestureview = (GestureOverlayView)findViewById(R.id.gesture_view);
		Button button = (Button)findViewById(R.id.button_gestures);
		
		gestureview.addOnGesturePerformedListener(gesturelistener);
		button.setOnClickListener(btnClick);
    }
    
    private OnGesturePerformedListener gesturelistener = new OnGesturePerformedListener() {
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			ArrayList<Prediction> predictions = gesturelib.recognize(gesture);

			gesturesText.setText("\n\nGesture Predictions\n\n");
			
			if (predictions.size() > 1) {
				for(Prediction prediction: predictions){
					gesturesText.append("Name: " + prediction.name + ", Score: " + prediction.score + "\n");
				}
			}

		}
	};
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(GestureActivity.this,GestureList.class));
		}
	};
}