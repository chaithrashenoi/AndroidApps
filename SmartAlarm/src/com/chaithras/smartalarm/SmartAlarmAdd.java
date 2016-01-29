package com.chaithras.smartalarm;

import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.app.Activity;
import android.content.Intent;

public class SmartAlarmAdd extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_alarm_add);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Calendar c = Calendar.getInstance(); 
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
		
		Log.i("alarmdb", "Calendar add" + curHours + ":" + curMinutes);

		TimePicker timePicker = (TimePicker) this.findViewById(R.id.alarmTimeAdd);
		timePicker.setCurrentHour(curHours);
		timePicker.setCurrentMinute(curMinutes);
	}
	
	
	public void onFinish(View button) {

		TimePicker timePicker = (TimePicker) this.findViewById(R.id.alarmTimeAdd);
       

		int hours   = timePicker.getCurrentHour();
		int minutes = timePicker.getCurrentMinute();
	
		Log.i("alarmdb", "time_picker add" + hours + ":" + minutes);

		Intent toReturn = getIntent();
			toReturn.putExtra("hours", hours);
			toReturn.putExtra("minutes", minutes);
			Log.i("alarmdb", "setResult add");
			setResult(RESULT_OK, toReturn);
			Log.i("alarmdb", "onFinish add");
			finish();
		}
		
	public void onCancel(View button) {
		finish();
	}	
		
	
	}	
