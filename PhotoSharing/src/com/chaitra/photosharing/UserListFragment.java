package com.chaitra.photosharing;

import java.util.ArrayList;
import java.util.List;

import com.chaitra.photosharing.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class UserListFragment extends ListFragment {

	UserDescriptor mSelectionUser;

	@SuppressLint("UseValueOf")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.fragment_title);

	}

	@Override
	public void onListItemClick(ListView l, View v, int Position, long id) {
		UserListActivity activity = (UserListActivity) getActivity();
		mSelectionUser = (UserDescriptor) getListAdapter().getItem(Position);
		Log.d("ListFragment", "Fragment selected item "
				+ mSelectionUser.userName);

		activity.onUserListSelected(mSelectionUser);
	}

	public String getItemNameSelected() {
		return (mSelectionUser.userName);
	}

	public void fillUserNames(UserDescriptor userDescList[]) {

		List<UserDescriptor> UserList = null;

		UserList = new ArrayList<UserDescriptor>();

		for (int i = 0; i < userDescList.length; i++) {
			Log.i("ListFragment", "fillUserNames" + i);
			UserDescriptor item = new UserDescriptor(userDescList[i].userName,
					userDescList[i].userId);

			UserList.add(item);

		}
		UserListAdapter adapter = new UserListAdapter(getActivity(),
				R.layout.fragment_user_list, UserList);

		Log.i("ListFragment", "setListAdapter");
		setListAdapter(adapter);
	}

}
