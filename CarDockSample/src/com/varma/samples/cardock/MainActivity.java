package com.varma.samples.cardock;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		UiModeManager manager = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
		
		manager.disableCarMode(UiModeManager.DISABLE_CAR_MODE_GO_HOME);
		
		super.onDestroy();
	}
}