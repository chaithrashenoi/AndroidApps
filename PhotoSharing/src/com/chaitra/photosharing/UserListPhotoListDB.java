package com.chaitra.photosharing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserListPhotoListDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "photosharing.db";
	private static final int DATABASE_VERSION = 1;

	public UserListPhotoListDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("photodb", "photoDB constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase photosharingDb) {
		Log.i("photodb", "photoDB Adding tables");

		photosharingDb.execSQL("CREATE TABLE IF NOT EXISTS " + "USERLIST"
				+ " (" + "USERID" + " TEXT PRIMARY KEY," + "USERNAME" + " TEXT"
				+ ");");

		photosharingDb.execSQL("CREATE TABLE IF NOT EXISTS " + "PHOTOLIST"
				+ " (" + "PHOTOID" + " TEXT PRIMARY KEY," + "PHOTONAME"
				+ " TEXT," + "USERID" + " TEXT," + "USERNAME" + " TEXT" + ");");
		Log.i("photodb", "photoDB Adding tables done ");
	}

	public void addUsers(UserDescriptor userDescList[]) {

		SQLiteDatabase photosharingDb = getWritableDatabase();

		for (Integer i = 0; i < userDescList.length; i++) {
			Cursor result = photosharingDb.rawQuery(
					"select * from USERLIST where USERID=?",
					new String[] { userDescList[i].userId });
			int rowCount = result.getCount();
			if (rowCount == 0) {
				Log.i("photodb", "photoDB Adding user:"
						+ userDescList[i].userName);
				ContentValues newUser = new ContentValues(1);
				newUser.put("USERNAME", userDescList[i].userName);
				newUser.put("USERID", userDescList[i].userId);
				photosharingDb.insert("USERLIST", null, newUser);
			} else {
				Log.i("photodb", "photoDB found user:"
						+ userDescList[i].userName);
			}
		}
	}

	public void addPhotos(ImageDescriptor imageDesc[]) {
		SQLiteDatabase photosharingDb = getWritableDatabase();

		for (Integer i = 0; i < imageDesc.length; i++) {
			Cursor result = photosharingDb.rawQuery(
					"select * from PHOTOLIST where USERID=? AND PHOTOID=?",
					new String[] { imageDesc[i].userId, imageDesc[i].imageId });
			int rowCount = result.getCount();
			if (rowCount == 0) {
				Log.i("photodb", "photoDB Adding photo:"
						+ imageDesc[i].userName + imageDesc[i].imageId);
				ContentValues newPhoto = new ContentValues(1);
				newPhoto.put("USERNAME", imageDesc[i].userName);
				newPhoto.put("USERID", imageDesc[i].userId);
				newPhoto.put("PHOTONAME", imageDesc[i].imageName);
				newPhoto.put("PHOTOID", imageDesc[i].imageId);
				photosharingDb.insert("PHOTOLIST", null, newPhoto);
			} else {
				Log.i("photodb", "photoDB found photo:" + imageDesc[i].userName
						+ imageDesc[i].imageId);
			}
		}
	}

	public boolean checkPhotoExists(ImageDescriptor imageDesc) {

		SQLiteDatabase photosharingDb = getWritableDatabase();

		Cursor result = photosharingDb.rawQuery(
				"select * from PHOTOLIST where USERID=? AND PHOTOID=?",
				new String[] { imageDesc.userId, imageDesc.imageId });
		int rowCount = result.getCount();
		if (rowCount == 0) {
			return (false);
		}
		return (true);
	}

	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
	}
}
