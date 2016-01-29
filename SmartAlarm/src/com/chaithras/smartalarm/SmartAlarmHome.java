package com.chaithras.smartalarm;






import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;


public class SmartAlarmHome extends FragmentActivity {
	public AlarmListFragment mAlarmListFragment;
	AlarmListDB alarmDb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_alarm_home);
		alarmDb = new AlarmListDB(this);

		FragmentManager fraManager = getSupportFragmentManager();
		mAlarmListFragment = (AlarmListFragment) fraManager
				.findFragmentById(R.id.alarm_list_container);


	}
	
	private void updateAlarmListFragment() {
		int numOfAlarms;
		numOfAlarms = alarmDb.getAlarmCount();
		AlarmDescriptor[] alarmDescList = new AlarmDescriptor[numOfAlarms];
			
		for (Integer i = 0; i < numOfAlarms; i++) {
			alarmDescList[i] = new AlarmDescriptor(0, 0, 0);
		}
	
		alarmDb.getAlarms(alarmDescList);
		mAlarmListFragment.parentActivity = this;
		mAlarmListFragment.ShowAlarmList(alarmDescList);
		for (Integer i = 0; i < numOfAlarms; i++) {
			AddAlarmIntent(alarmDescList[i]);
		}
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		updateAlarmListFragment();
		Log.i("alarmdb", "onResume add" );
	
	}

	
	private void AddAlarmIntent(AlarmDescriptor alarmDesc) {
		Calendar c = Calendar.getInstance(); 
		long alarmTimeLeftMili =  (1000 * (long)((alarmDesc.hours*3600 + alarmDesc.minutes*60)-
				(c.get(Calendar.HOUR_OF_DAY)*3600 + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND))));
		if(alarmTimeLeftMili < 0) {
			alarmTimeLeftMili =  (1000 * (long)(((24+alarmDesc.hours)*3600 + alarmDesc.minutes*60)-
					(c.get(Calendar.HOUR_OF_DAY)*3600 + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND))));			
		}
		Intent intent = new Intent(this, SmartAlarmRing.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, alarmDesc.id, intent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + alarmTimeLeftMili, pendingIntent);
		Log.d("alarmdb", alarmDesc.id+":add alarm time" + alarmTimeLeftMili);
	}

	private void DeleteAlarmIntent(AlarmDescriptor alarmDesc) {
		Intent intent = new Intent(this, SmartAlarmRing.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,alarmDesc.id, intent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Log.d("alarmdb", alarmDesc.id+":delete alarm time");
		alarmManager.cancel(pendingIntent);
	}

	
	protected void onActivityResult( int requestCode, int resultCode, Intent data) {
		Log.i("alarmdb", "onActivityResult:"+requestCode+ ":" + resultCode );
		if(requestCode == 2 )
		{
			AlarmDescriptor alarmDesc = new AlarmDescriptor(0, 0, 0);
			if( resultCode == RESULT_OK ) {
				int delete = data.getIntExtra("delete",0);
				alarmDesc.hours = data.getIntExtra("hours", -1);
				alarmDesc.minutes = data.getIntExtra("minutes", 0);
				alarmDesc.id = data.getIntExtra("id", 0);
				Log.i("alarmdb", "onActivityResult:alarm_edit_container:"+alarmDesc.hours );
				
				if( delete == 1) {
					deleteAlarm(alarmDesc);
				} 
				else {
					alarmDb.editAlarm(alarmDesc);
					updateAlarmListFragment();
				}
			}
		}
		else if(requestCode == 1 )
		{
			AlarmDescriptor alarmDesc = new AlarmDescriptor(0, 0, 0);
			if( resultCode == RESULT_OK ) {
				alarmDesc.hours = data.getIntExtra("hours", 12);
				alarmDesc.minutes = data.getIntExtra("minutes", 0);
				Log.i("alarmdb", "Adding"+alarmDesc.hours+":"+alarmDesc.minutes );
				alarmDb.addAlarm(alarmDesc);
				updateAlarmListFragment();
			}
		}
	}

	public void deleteAlarm(AlarmDescriptor AlarmDesc) {
		alarmDb.deleteAlarm(AlarmDesc);
		updateAlarmListFragment();
		DeleteAlarmIntent(AlarmDesc);
		Log.d("alarmdb", "DeleteAlarmIntent");
	}

	public void StartEditAlarmActivity(AlarmDescriptor AlarmDesc) {
		Intent go = new Intent(this, SmartAlarmEdit.class);
		go.putExtra("id", AlarmDesc.id);
		go.putExtra("hours", AlarmDesc.hours);
		go.putExtra("minutes", AlarmDesc.minutes);
		Log.i("Alarmlistfragment", "start singleAlarmEdit:"+AlarmDesc.id);
		startActivityForResult(go, 2);
	}

	public void StrtAddAlarmActivity(View button) {
		Intent go = new Intent(this, SmartAlarmAdd.class);
		Log.i("Alarmlistfragment", "start singleAlarmAdd");
		startActivityForResult(go, 1);
	}
}