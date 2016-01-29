package com.chaitra.photosharing;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserListAdapter extends ArrayAdapter<UserDescriptor> {

	Activity activity;

	public UserListAdapter(Context context, int resourceId,
			List<UserDescriptor> items) {
		super(context, resourceId, items);
		this.activity = (Activity) context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView textViewUserName;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		UserDescriptor userDesc = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fragment_user_list, null);
			holder = new ViewHolder();
			holder.textViewUserName = (TextView) convertView
					.findViewById(R.id.textViewUserName);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.textViewUserName.setText(userDesc.userName);

		return convertView;
	}
}
