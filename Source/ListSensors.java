package com.example.myfirstapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageActivity extends Activity {

	@SuppressLint("NewApi")
	
	private SensorManager mSensorManager;
	private	Sensor mSensor;
	private final String TAG = "From SensorActivity: ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(30);
	    // textView.setText(message); 
	    //textView.setText("hard coded"); Works
	    //textView.setText(String.valueOf(GetNumSensors())); Works 
	    textView.setText(GetSensorNames()) ;
	    
	 // Set the text view as the activity layout
		
		setContentView(textView);
		// Show the Up button in the action bar.
		// Not in tutorial setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private int GetNumSensors(){
		
		int NumSensors = 1001 ;  // start with weird value 
		char MySensorList[] ;  // string of sensor names & vendors
		
		Log.i(TAG,"Getting Sensor List") ;
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		NumSensors = deviceSensors.size() ;
		return(NumSensors) ;
	
	}
	
private String GetSensorNames(){
		
		String MySensorList = "" ;  // string of sensor names
		final String TAG = "FromCode: ";
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for(int i=0; i < deviceSensors.size(); i++) {
			MySensorList = MySensorList + deviceSensors.get(i).getName() + "\n";
			//System.out.println(deviceSensors.get(i).getName()) ;
			//Log.i(TAG,deviceSensors.get(i).getName()) ; // Log one per line
			//Log.i(TAG,MySensorList) ;
		}
		
		//String MySensorList = new String(deviceSensors.get(6).getName()) ;
			
		if (isExternalStorageWritable()){
			Log.i(TAG,"External storage is writeable!!!") ;
			File dirObj = new File(Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_DOWNLOADS), "SMSensorData");
		    if (!dirObj.exists()) {
		        Log.e(TAG, "Directory not created or does not exist");
		    }
		    else {
		      File fileObj = new File(dirObj.getAbsolutePath(),"sensoroutput.txt") ;
		      FileWriter txtFile = null;
			  try {
				txtFile = new FileWriter(fileObj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				//txtFile.write("Test\nLine2\n",0,11);
				txtFile.write(MySensorList,0,MySensorList.length());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  try {
				txtFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		      
		}
		else {
			Log.i(TAG,"External storage is NOT writeable!!!") ;
		}
		
		return(MySensorList) ;
			
		}

/* Checks if external storage is available for read and write */
public static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
    }
    return false;
}

}
