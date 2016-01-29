package com.chaithras.smartalarm;

import java.util.List;
import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AlarmAdapter extends ArrayAdapter<AlarmDescriptor> {

	SmartAlarmHome activity;

	public AlarmAdapter(Context context, int resourceId,
			List<AlarmDescriptor> items) {

		super(context, resourceId, items);
		this.activity = (SmartAlarmHome) context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView time;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		AlarmDescriptor AlarmDesc = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fragment_alarm_list, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView
					.findViewById(R.id.time);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Log.i("alarmdb", "holder" + AlarmDesc.hours + ":" + AlarmDesc.minutes);
		int hours = AlarmDesc.hours;
		int minutes = AlarmDesc.minutes;
		String ampm = "AM";
		
		if(hours >= 12){
			ampm="PM";
		}
		if(hours >= 13){
			hours =  hours-12;
		}
		if(hours == 0){
			hours =  12;
		}
		
		holder.time.setText( hours + ":" + String.format("%02d", minutes) + " " + ampm);

		ImageButton btn = (ImageButton)convertView.findViewById(R.id.listDelete);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
        		AlarmDescriptor AlarmDesc = getItem(position);
        		activity.deleteAlarm(AlarmDesc);
            }
        });
        
		return convertView;
	}
}