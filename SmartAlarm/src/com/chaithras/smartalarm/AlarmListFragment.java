package com.chaithras.smartalarm;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class AlarmListFragment extends ListFragment {

	
	AlarmDescriptor mSelectionAlarm;
	String mSelection;
	public SmartAlarmHome parentActivity;
	List<AlarmDescriptor> AlarmList = null;

	@SuppressLint("UseValueOf")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentActivity = (SmartAlarmHome)getActivity();
		parentActivity.setTitle(R.string.fragment_title);
		Log.i("Alarmlistfragment", "onCreate_Alarmlist_fragment");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Alarmlistfragment", "onDestroy_Alarmlist_fragment");
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onListItemClick(ListView l, View v, int Position, long id) {
		Log.d("Alarmlistfragment", "Alarm onListItemClick" );
		mSelectionAlarm = (AlarmDescriptor) getListAdapter().getItem(Position);
		Log.d("Alarmlistfragment", "Alarm selected hours "
				+ mSelectionAlarm.hours);
		AlarmDescriptor AlarmDesc = (AlarmDescriptor) getListAdapter()
					.getItem(Position);
		
		parentActivity.StartEditAlarmActivity(AlarmDesc);

	}

	public void ShowAlarmList(AlarmDescriptor AlarmDescList[]) {

		Log.i("Alarmlistfragment", "ShowUserAlarms Alarmlist fragment");

		AlarmList = new ArrayList<AlarmDescriptor>();

		for (int i = 0; i < AlarmDescList.length; i++) {

			AlarmDescriptor item = new AlarmDescriptor(AlarmDescList[i].hours, 
					AlarmDescList[i].minutes, AlarmDescList[i].id );

			AlarmList.add(item);

		}

		AlarmAdapter adapter = new AlarmAdapter(parentActivity,
				R.layout.fragment_alarm_list, AlarmList);

		setListAdapter(adapter);
	}

}
