package com.varma.samples.ttssample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener{
    private static final int ENABLE_SPEAK_BUTTON = 1;
	private static final String END_OF_SPEECH = "END";
	private static final int LANGUAGE_MENU = 0;
	private static final int SPEED_MENU = 1000;
	private static final String demoText = "Hi, This application demonstrates the use of TTS Engine in Android. " + 
										   "Type some text and press the Speak button the start. " +
										   "To change the language, use the menu Language and to change the speech rate, use the menu Speed."; 

	private TextToSpeech tts = null;
	private ArrayList<Locale> availableLocales = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        availableLocales = new ArrayList<Locale>();
        tts = new TextToSpeech(this,this);
        
        ((Button)findViewById(R.id.button_clear)).setOnClickListener(onButtonClik);
        ((Button)findViewById(R.id.button_speak)).setOnClickListener(onButtonClik);
        
        ((EditText)findViewById(R.id.text)).addTextChangedListener(textWather);
        ((EditText)findViewById(R.id.text)).setText(demoText);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		boolean result = super.onCreateOptionsMenu(menu);
		String menutitle = "";
		
		SubMenu lanugageMenu = menu.addSubMenu(0,LANGUAGE_MENU, Menu.NONE, "Language");
		
		lanugageMenu.setHeaderTitle("Select Language");
		
		for(int index=0; index<availableLocales.size(); ++index)
		{
			menutitle = availableLocales.get(index).getDisplayLanguage() + " (" + availableLocales.get(index).getDisplayCountry() + ")";
			
			lanugageMenu.add(0, LANGUAGE_MENU + index + 1, Menu.NONE, menutitle);
		}
		
		SubMenu speedMenu = menu.addSubMenu(0,SPEED_MENU, Menu.NONE, "Speed");
		
		speedMenu.add(0, SPEED_MENU + 1, Menu.NONE, "Very Slow");
		speedMenu.add(0, SPEED_MENU + 2, Menu.NONE, "Slow");
		speedMenu.add(0, SPEED_MENU + 3, Menu.NONE, "Normal");
		speedMenu.add(0, SPEED_MENU + 4, Menu.NONE, "Fast");
		speedMenu.add(0, SPEED_MENU + 5, Menu.NONE, "Very Fast");
		
		return result;
	}

	@Override
	public void onInit(int status)
	{
		boolean isAvailable = (TextToSpeech.SUCCESS == status);
		
		if(isAvailable)
		{
			EnumerateAvailableLanguages();
			
			tts.setOnUtteranceCompletedListener(onUtteranceCompleted);
						
			((Button)findViewById(R.id.button_speak)).setEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		boolean result = true;
		int itemId = item.getItemId();
		
		if(itemId > SPEED_MENU)
		{
			setTextToSpeechRate(itemId - SPEED_MENU - 1);
		}
		else if(itemId > LANGUAGE_MENU && itemId < SPEED_MENU)
		{
			setTextToSpeechLocale(itemId - LANGUAGE_MENU - 1);
		}
		
		return result;
	}
	
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		boolean result = true;
		
		long id = info.id - Menu.FIRST; 
		
		Log.i("TTSDemo","MenuItem Selectd: " + id);
		
		return result;
	}

	@Override
	protected void onDestroy()
	{
		tts.shutdown();
		
		super.onDestroy();
	}
	
	private void setTextToSpeechRate(int index)
	{
		float rate = (float)1.0;
		
		switch(index)
		{
			case 0: rate = (float)0.1; break;
			case 1: rate = (float)0.5; break;
			case 2: rate = (float)1.0; break;
			case 3: rate = (float)1.5; break;
			case 4: rate = (float)2.0; break;
		}
		
		Log.i("TTSDemo","SpeechRate: " + rate + "(" + index + ")");
		
		tts.setSpeechRate(rate);
	}
	
	private void setTextToSpeechLocale(int index)
	{
		tts.setLanguage(availableLocales.get(index));
		
		Log.i("TTSDemo", "Language: " + availableLocales.get(index).getDisplayLanguage() + " (" + availableLocales.get(index).getDisplayCountry() + ")");
	}
	
	private void EnumerateAvailableLanguages()
	{
		Locale locales[] = Locale.getAvailableLocales();
		
		for(int index=0; index<locales.length; ++index)
		{
			if(TextToSpeech.LANG_COUNTRY_AVAILABLE == tts.isLanguageAvailable(locales[index]))
			{
				Log.i("TTSDemo", locales[index].getDisplayLanguage() + " (" + locales[index].getDisplayCountry() + ")");
				
				availableLocales.add(locales[index]);
			}
		}
	}
	
	private final View.OnClickListener onButtonClik = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.button_clear:
				{
					((EditText)findViewById(R.id.text)).setText("");
					
					break;
				}
				case R.id.button_speak:
				{
					String text = ((EditText)findViewById(R.id.text)).getText().toString();
					HashMap<String, String> hash = new HashMap<String, String>();
				
					hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,END_OF_SPEECH);
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, hash);
					
					((Button)findViewById(R.id.button_speak)).setEnabled(false);
					
					break;
				}
			}
		}
	};
	
	private final TextWatcher textWather = new TextWatcher() 
	{
		@Override
		public void afterTextChanged(Editable editable) 
		{
			((Button)findViewById(R.id.button_clear)).setEnabled(editable.toString().length() > 0);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) 
		{
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) 
		{
		}
	};
	
	private final TextToSpeech.OnUtteranceCompletedListener onUtteranceCompleted = new TextToSpeech.OnUtteranceCompletedListener()
	{
		@Override
		public void onUtteranceCompleted(String utteranceId)
		{
			if(0 == utteranceId.compareToIgnoreCase(END_OF_SPEECH))
			{
				handler.sendEmptyMessage(ENABLE_SPEAK_BUTTON);
			}
		}
	};
	
	private final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case ENABLE_SPEAK_BUTTON:
				{
					((Button)findViewById(R.id.button_speak)).setEnabled(true);
					
					break;
				}
			}
		}
	};
}