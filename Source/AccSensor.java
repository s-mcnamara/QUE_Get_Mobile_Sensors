package com.example.myfirstapp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

public class SensorActivity2 extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private	Sensor mSensor;
	private final String TAG = "From SensorActivity2: ";
	private final int NanoToMilli = 1000000 ;
	int CallCount = 0 ;
	private File dirObj, fileObj ;
	FileWriter txtFile = null;
	private boolean OKtoWrite = false ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_activity2);
		// Open output file, if all is successful, call MonitorSensor()
				 if (DisplayMessageActivity.isExternalStorageWritable()){
						Log.i(TAG,"External storage is writeable!!!") ;
						dirObj = new File(Environment.getExternalStoragePublicDirectory(
					            Environment.DIRECTORY_DOWNLOADS), "SMSensorData");
					    if (!dirObj.exists()) {
					        Log.e(TAG, "Directory not created or does not exist");
					    }
					    else {
					      String FileName = new String("accsensoroutput.txt") ;
					      //FileNum++ ;
					      Log.i(TAG,FileName) ;
					      fileObj = new File(dirObj.getAbsolutePath(),FileName) ;
						  try {
							//txtFile = new FileWriter(fileObj);
							txtFile = new FileWriter(fileObj,true);  // Append to existing file
						  } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						  }
						  Date Now = new Date() ;
						  String OutStr = String.format(Locale.US,"Starting Accelerometer Sensor Monitor at %s\n",Now.toString()) ;
						  Log.i(TAG,OutStr) ;
						  try {
							txtFile.write(OutStr,0,OutStr.length());
						  } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						  }
						  OKtoWrite = true ;
				          MonitorSensor() ;
					    }  // end else
				 }  // end if (DisplayMessageActivity.isExternalStorageWriteable() 
	} // end SensorActivity2 class

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor_activity2, menu);
		return true;
	}
	
	private void MonitorSensor() {
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    
	} // end MonitorSensor

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
      // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
    	
      // write data every 150 times the function is called
      if (++CallCount == 150) { 
    	  if (OKtoWrite){
            // The accelerometer sensor returns three values.
            float motionX = event.values[0];
            float motionY = event.values[1] ;
            float motionZ = event.values[2] ;
            Time EvtTime = new Time(event.timestamp/NanoToMilli);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
            String TimeString = sdf.format(EvtTime) ;
            Log.i(TAG,TimeString) ;
            String OutStr = String.format(Locale.US,"Time: %s X: %4.2f Y: %4.2f Z: %4.2f\n",
            		TimeString,motionX,motionY,motionZ) ;
            
            CallCount = 0 ;
            Log.i(TAG,OutStr) ; 
            try {
			    //txtFile.write(OutputLine,0,OutputLine.length());
			    txtFile.write(OutStr,0,OutStr.length());
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			    e.printStackTrace();
		    }
    	  }
    	  else {
		      Log.i(TAG,"External storage is NOT writeable!!!") ;
		  }
	   }
	   
    }  // end onSensorChanged

    @Override
    protected void onResume() {
    	final int delay30Sec = 300000000 ;  
      super.onResume();
      mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
      //mSensorManager.registerListener(this, mSensor, delay30Sec);
    }

    @Override
    protected void onStop() {
      super.onPause();
      Log.i(TAG,"Unregistering listener") ;
      mSensorManager.unregisterListener(this);
      if (OKtoWrite) {
    	  Log.i(TAG,"Closing txtFile") ;
    	  try {
			txtFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }
}  // end SensorActivity2 class
