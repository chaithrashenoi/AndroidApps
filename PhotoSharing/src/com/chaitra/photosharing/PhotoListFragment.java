package com.chaitra.photosharing;

import java.util.ArrayList;
import java.util.List;

import com.chaitra.photosharing.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PhotoListFragment extends ListFragment {
	ImageDescriptor mSelectionPhoto;
	String mSelection;
	public Activity parentActivity;

	@SuppressLint("UseValueOf")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentActivity = getActivity();
		parentActivity.setTitle(R.string.fragment_title);
		Log.i("photolistfragment", "onCreate_photolist_fragment");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("photolistfragment", "onDestroy_photolist_fragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		ListAdapter adapter = getListAdapter();
		if (null != adapter) {
			ImageDescriptor desc = (ImageDescriptor) adapter.getItem(0);
			if (null != desc) {
				parentActivity.setTitle(desc.userName);
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int Position, long id) {
		mSelectionPhoto = (ImageDescriptor) getListAdapter().getItem(Position);
		Log.d("photolistfragment", "Photo selected name "
				+ mSelectionPhoto.imageName);
		Intent go = new Intent(getActivity(), SinglePhotoActivity.class);

		go.putExtra("imageSelectIndex", Position);
		Log.i("photolistfragment", "start singlePhotoActivity");
		int numPhotos = getListAdapter().getCount();
		go.putExtra("numPhotos", numPhotos);
		for (int i = 0; i < numPhotos; i++) {
			ImageDescriptor imageDesc = (ImageDescriptor) getListAdapter()
					.getItem(i);
			go.putExtra("imageIdExtra" + i, imageDesc.imageId);
			go.putExtra("imageNameExtra" + i, imageDesc.imageName);
			go.putExtra("userIdExtra" + i, imageDesc.userId);
			go.putExtra("userNameExtra" + i, imageDesc.userName);
		}
		startActivity(go);

	}

	public void ShowUserPhotos(ImageDescriptor imageDescList[]) {

		Log.i("photolistfragment", "ShowUserPhotos photolist fragment");
		List<ImageDescriptor> UserList = null;

		UserList = new ArrayList<ImageDescriptor>();

		for (int i = 0; i < imageDescList.length; i++) {

			ImageDescriptor item = new ImageDescriptor(imageDescList[i].userId,
					imageDescList[i].userName, imageDescList[i].imageName,
					imageDescList[i].imageId);

			UserList.add(item);

		}

		ImageAdapter adapter = new ImageAdapter(parentActivity,
				R.layout.fragment_photo_list, UserList);

		setListAdapter(adapter);
	}

}
