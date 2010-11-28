package com.varma.samples.patternwallpaper;

import com.varma.samples.patternwallpaper.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PatternWallpaperSettings extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getPreferenceManager().setSharedPreferencesName(
                PatternWallpaper.PATTERN_WALLPAPER_PREF_NAME);
		
		addPreferencesFromResource(R.xml.patternwallpaper_settings);
	}
	
}
