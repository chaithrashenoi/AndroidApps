package com.chaitra.photosharing;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class HttpClientTaskDownloadPhoto extends
		AsyncTask<String, Void, String> {

	private ImageDescriptor[] photoDescList;
	private UserListActivity parentActivity;
	private int numPhotos;
	String mUserId;
	String mUserName;
	AndroidHttpClient httpclient;

	public String fetchPhotoList(String url) {
		String responseBody = null;
		try {
			httpclient = AndroidHttpClient.newInstance(null);
			Log.i("http", "closed");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpGet getMethod = new HttpGet(url);
			responseBody = httpclient.execute(getMethod, responseHandler);
		} catch (Throwable t) {
			Log.i("User", "did not work", t);
		}
		return responseBody;
	}

	protected String doInBackground(String... urls) {
		String urlUser;
		urlUser = urls[0] + "/" + urls[1];
		mUserId = urls[1];
		mUserName = urls[2];
		Log.i("download", "urluser =" + urlUser);

		String photoListData = fetchPhotoList(urls[0] + "/" + urls[1]);
		Log.i("download", "photoListData =" + photoListData);

		parseUserPhotoNames(photoListData);

		for (int i = 0; i < numPhotos; i++) {
			String url = "http://bismarck.sdsu.edu/photoserver/photo/24/"
					+ photoDescList[i].imageId;
			String path = photoDescList[i].userId + "_"
					+ photoDescList[i].imageName;

			if (false == parentActivity.CheckUserPhotoExists(photoDescList[i])) {
				Log.i("download", "download photourl =" + url);
				downloadPhoto(url, path);
			} else {
				Log.i("download", "skip photourl =" + url);
			}
		}
		return photoListData;
	}

	public void parseUserPhotoNames(String responseString) {
		JSONObject photos;
		JSONArray data = null;
		try {
			data = new JSONArray(responseString);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (data != null) {

			numPhotos = data.length();

			photoDescList = new ImageDescriptor[numPhotos];

			for (int i = 0; i < numPhotos; i++) {
				photos = null;
				try {

					photos = (JSONObject) data.get(i);
					String imageName = photos.getString("name");
					String imageId = photos.getString("id");
					photoDescList[i] = new ImageDescriptor(mUserId, mUserName,
							imageName, imageId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Log.i("user", "number of photos=" + numPhotos);
		}
	}

	protected void setActivity(UserListActivity activity) {
		parentActivity = activity;
	}

	public void onPostExecute(String responseString) {
		if (photoDescList.length > 0 && parentActivity != null) {
			Log.i("photolistact", " onPostExecute ShowUserPhotos star");
			parentActivity.ShowUserPhotos(photoDescList);
			Log.i("photolistact", " onPostExecute ShowUserPhotos end");
		}
		if (httpclient != null) {
			Log.i("http", "closed");
			httpclient.getConnectionManager().shutdown();
			httpclient.close();
		}
	}

	public void downloadPhoto(String url, String path) {
		HttpGet getMethod = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(getMethod);

			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			FileOutputStream stream = parentActivity.openFileOutput(path,
					Context.MODE_PRIVATE);
			OutputStream file = new BufferedOutputStream(stream);
			int next;
			while ((next = inputStream.read()) != -1) {
				file.write(next);
			}
			file.close();
			inputStream.close();
			entity.consumeContent();
		} catch (Throwable t) {
			Log.e("download", "downloading failed");
		}
	}
}
