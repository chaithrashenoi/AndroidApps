package com.chaithras.smartalarm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.app.Activity;
import android.content.Intent;

public class SmartAlarmEdit extends Activity {
	int hours;
	int minutes;
	int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_alarm_edit);

		Bundle personData=getIntent().getExtras();
		hours =personData.getInt("hours");
		minutes =personData.getInt("minutes");
		id =personData.getInt("id");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePickerEdit);
		timePicker.setCurrentHour(hours);
		timePicker.setCurrentMinute(minutes);
	}
	
	
	public void onFinish(View button) {
		TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePickerEdit);

		hours   = timePicker.getCurrentHour();
		minutes = timePicker.getCurrentMinute();
		
		Log.i("alarmdb", "time_picker edit" + hours + ":" + minutes);
		Intent toReturn = getIntent();
		toReturn.putExtra("hours", hours);
		toReturn.putExtra("minutes", minutes);
		toReturn.putExtra("id", id);
		toReturn.putExtra("delete", 0);
		setResult(RESULT_OK, toReturn);
		finish();
	}
		
	public void onCancel(View button) {
		finish();
	}	
	
}	
