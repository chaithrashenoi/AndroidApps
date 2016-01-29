package com.chaitra.photosharing;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SinglePhotoActivity extends Activity implements OnTouchListener {
	ImageDescriptor mImageDescList[];
	String mUserId;
	TextView mtextView;
	ImageView mImageView;
	Integer mImageIndex;
	int numPhotos = 0;
	private boolean swipeInProgress;
	private float startX;
	private float distanceX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_single_photo);

		mtextView = (TextView) findViewById(R.id.textViewSinglePhoto);
		mImageView = (ImageView) findViewById(R.id.imageViewSinglePhoto);
		mImageView.setOnTouchListener(this);

		mImageIndex = getIntent().getIntExtra("imageSelectIndex", 0);

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

		Log.i("singlephotoactivity", "photo start display index " + mImageIndex);
		mUserId = mImageDescList[0].userId;
		Log.i("singlephotoactivity", "photo display" + mUserId);

	}

	void updateImage() {
		String imageSelectName = mImageDescList[mImageIndex].imageName;
		String path = mUserId + "_" + imageSelectName;
		File photoFile = getFileStreamPath(path);
		String absPath = photoFile.getAbsolutePath();
		try {
			Bitmap thePhoto = BitmapFactory.decodeFile(absPath);
			mImageView.setImageBitmap(thePhoto);
			mtextView.setText(imageSelectName);
			Log.i("singlephotoactivity", "photo display" + imageSelectName);
			Log.i("singlephotoactivity", "hello");
		} catch (Throwable t) {
			Log.i("BitmapFactory.decodeFile", "did not work", t);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("imageIndex", mImageIndex);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageIndex = savedInstanceState.getInt("imageIndex");
	}

	@Override
	public void onResume() {
		super.onResume();
		updateImage();
	}

	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
			return handleActionDown(event);
		case MotionEvent.ACTION_UP: {
			boolean btouchTrue;
			btouchTrue = handleActionUp(event);
			if (btouchTrue) {
				if (distanceX > 10 && mImageIndex > 0) {
					mImageIndex--;
					Log.i("singlephotoactivity", "photo ID :" + numPhotos + ":"
							+ mImageIndex);
					updateImage();
				} else if (distanceX < -10 && mImageIndex < numPhotos - 1) {
					mImageIndex++;
					Log.i("singlephotoactivity", "photo ID :" + numPhotos + ":"
							+ mImageIndex);
					updateImage();
				}
			}
			return (btouchTrue);
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			swipeInProgress = false;
			return false;
		}
		return false;
	}

	private boolean handleActionDown(MotionEvent event) {
		swipeInProgress = true;
		startX = event.getX();
		return true;
	}

	private boolean handleActionUp(MotionEvent event) {
		if (!swipeInProgress)
			return false;
		float endX = event.getX();
		distanceX = endX - startX;
		Log.i("singlephotoactivity", "x swipe distance " + distanceX);
		swipeInProgress = false;
		return true;
	}

}
