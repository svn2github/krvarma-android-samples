package com.varma.samples.applauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainAvtivity extends ListActivity {
	PackageManager packageManager = null;
	List<ApplicationInfo> applist = null;
	ApplicationAdaptor listadaptor = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        packageManager = getPackageManager();
        
        new LoadApplicationTask().execute();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean result = true;
    	
		switch(item.getItemId())
		{
			case R.id.menu_about:
			{
				displayAboutDialog();
				
				break;
			}
			default:
			{
				result = super.onOptionsItemSelected(item);
				
				break;
			}
		}
		
		return result;

    }
    
    
    private void displayAboutDialog()
	{
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setTitle(getString(R.string.app_name));
    	builder.setMessage(getString(R.string.app_desc));
    	
    	builder.show();
	}
        
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ApplicationInfo app = applist.get(position);
		try 
		{
			Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
			
			if(null != intent)
			{
				startActivity(intent);
			}
		} 
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(MainAvtivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e) 
		{
			Toast.makeText(MainAvtivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list)
    {
    	ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>(); 
    	
    	for(ApplicationInfo info: list)
    	{
    		try {
    			if(null != packageManager.getLaunchIntentForPackage(info.packageName))
    			{	
    				applist.add(info);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
		return applist;
    }
    
    private class LoadApplicationTask extends AsyncTask<Void, Void, Void>
    {
    	private ProgressDialog progress = null;
    	
		@Override
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(
	        		packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			
	        listadaptor = new ApplicationAdaptor(MainAvtivity.this, R.layout.appview, applist);
	        
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadaptor);
			
			progress.dismiss();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(
					MainAvtivity.this, null, "Loading application info...");
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
    }
    
	private class ApplicationAdaptor extends ArrayAdapter<ApplicationInfo>
    {
    	private List<ApplicationInfo> objects = null;

		public ApplicationAdaptor(Context context, 
				int textViewResourceId,
				List<ApplicationInfo> objects) 
		{
			super(context, textViewResourceId, objects);
			
			this.objects = objects;
		}
    	
		@Override
		public int getCount() {
			return ((null != objects) ? objects.size() : 0);
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return ((null != objects) ? objects.get(position) : null);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if(null == view)
			{
				LayoutInflater vi = (LayoutInflater)MainAvtivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.appview, null);
			}
			
			ApplicationInfo data = objects.get(position);
			
			if(null != data)
			{
				TextView textName = (TextView)view.findViewById(R.id.app_name);
				ImageView iconview = (ImageView)view.findViewById(R.id.app_icon);
				
				textName.setText(data.loadLabel(packageManager) + " (" + data.packageName + ")");
				iconview.setImageDrawable(data.loadIcon(packageManager));
			}
			
			return view;
		}
    };
}