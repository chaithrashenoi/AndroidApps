package com.chaitra.photosharing;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class PhotoListActivity extends FragmentActivity {
	ImageDescriptor mImageDescList[];
	PhotoListFragment mPhotoListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("photolistact", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		int numPhotos = 0;
		numPhotos = getIntent().getIntExtra("numPhotos", 0);
		Log.i("photolistact", "numPhotos =" + numPhotos);
		mImageDescList = new ImageDescriptor[numPhotos];
		for (int i = 0; i < numPhotos; i++) {
			String imageName, imageId, userId, userName;
			imageName = getIntent().getStringExtra("imageNameExtra" + i);
			imageId = getIntent().getStringExtra("imageIdExtra" + i);
			userId = getIntent().getStringExtra("userIdExtra" + i);
			userName = getIntent().getStringExtra("userNameExtra" + i);
			mImageDescList[i] = new ImageDescriptor(userId, userName,
					imageName, imageId);
		}

		for (int i = 0; i < numPhotos; i++) {
			String path = mImageDescList[i].userId + "_"
					+ mImageDescList[i].imageName;

			Log.i("photolistact", "photopath =" + path);

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("photolistact", "onResume");
		FragmentManager fraManager = getSupportFragmentManager();
		mPhotoListFragment = (PhotoListFragment) fraManager
				.findFragmentById(R.id.photo_list_container);
		if (mPhotoListFragment == null) {
			Log.e("photolistact", "Failed for find fragment");
		}
		if (mPhotoListFragment != null) {
			mPhotoListFragment.ShowUserPhotos(mImageDescList);
		}
	}

}
