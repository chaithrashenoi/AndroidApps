package com.chaitra.photosharing;

import com.chaitra.photosharing.R;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class UserListActivity extends FragmentActivity {
	String userName[];
	private UserListFragment mListFragment;
	private PhotoListFragment mPhotoListFragment;
	UserListPhotoListDB photoDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("userlistact", " onCreate list called");
		photoDb = new UserListPhotoListDB(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		FragmentManager fraManager = getSupportFragmentManager();
		mListFragment = (UserListFragment) fraManager
				.findFragmentById(R.id.user_list_container);
		mPhotoListFragment = (PhotoListFragment) fraManager
				.findFragmentById(R.id.photo_list_container);

	}

	@Override
	public void onResume() {
		super.onResume();

		HttpClientTaskUserList task = new HttpClientTaskUserList();
		String url = "http://bismarck.sdsu.edu/photoserver/userlist";
		task.setActivity(this);
		task.execute(url);

	}

	public void onUserListSelected(UserDescriptor userDesc) {
		HttpClientTaskDownloadPhoto task = new HttpClientTaskDownloadPhoto();
		String[] url = { "http://bismarck.sdsu.edu/photoserver/userphotos",
				userDesc.userId, userDesc.userName };
		task.setActivity(this);
		task.execute(url);
	}

	public void fillUserNames(UserDescriptor userDescList[]) {

		photoDb.addUsers(userDescList);

		if (mListFragment == null) {
			Log.e("userlistact", "Failed for find fragment");
		} else {
			Log.i("userlistact", "fillUserNames");
			mListFragment.fillUserNames(userDescList);
		}
	}

	public boolean CheckUserPhotoExists(ImageDescriptor imageDesc) {
		return (photoDb.checkPhotoExists(imageDesc));
	}

	public void ShowUserPhotos(ImageDescriptor imageDescList[]) {

		photoDb.addPhotos(imageDescList);

		Log.i("userlistact", "ShowUserPhotos");
		int screenOrientation = getResources().getConfiguration().orientation;
		if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
			Intent go = new Intent(this, PhotoListActivity.class);

			int numPhotos = imageDescList.length;
			go.putExtra("numPhotos", numPhotos);
			for (int i = 0; i < numPhotos; i++) {
				go.putExtra("imageIdExtra" + i, imageDescList[i].imageId);
				go.putExtra("imageNameExtra" + i, imageDescList[i].imageName);
				go.putExtra("userIdExtra" + i, imageDescList[i].userId);
				go.putExtra("userNameExtra" + i, imageDescList[i].userName);
			}

			Log.i("userlistact", "strting photolistactivity");
			startActivity(go);
		} else {
			Log.i("userlistact", " mPhotoListFragment ShowUserPhotos");
			mPhotoListFragment.parentActivity = this;
			mPhotoListFragment.ShowUserPhotos(imageDescList);
		}
	}

}
