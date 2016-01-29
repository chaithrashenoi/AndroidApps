package com.chaitra.photosharing;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class HttpClientTaskUserList extends AsyncTask<String, Void, String> {

	private UserDescriptor[] userDescList;
	private UserListActivity parentActivity;
	AndroidHttpClient httpclient;

	public String fetchUserList(String... urls) {
		String responseBody = null;
		try {
			httpclient = AndroidHttpClient.newInstance(null);
			Log.i("userlisttask", "http open");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpGet getMethod = new HttpGet(urls[0]);
			responseBody = httpclient.execute(getMethod, responseHandler);
		} catch (Throwable t) {
			Log.i("userlisttask", "did not work", t);
		}

		return responseBody;
	}

	protected String doInBackground(String... urls) {

		return fetchUserList(urls);

	}

	protected void setActivity(UserListActivity activity) {
		parentActivity = activity;
	}

	public void onPostExecute(String responseString) {
		int i = 0;
		JSONObject person;
		JSONArray data = null;
		try {
			data = new JSONArray(responseString);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (data != null) {

			int numUsers = data.length();

			Log.i("userlisttask", "responseString= " + responseString);

			userDescList = new UserDescriptor[numUsers];

			if (userDescList == null) {
				Log.i("userlisttask", "userDescList allocation failed");
			}

			for (i = 0; i < numUsers; i++) {
				person = null;
				try {

					person = (JSONObject) data.get(i);
					String userName, userId;
					userName = person.getString("name");
					userId = person.getString("id");
					userDescList[i] = new UserDescriptor(userName, userId);
					Log.i("userlisttask", "userName =" + userName + "userId ="
							+ userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Log.i("userlisttask", "number of users=" + numUsers);

			if (numUsers > 0 && parentActivity != null) {
				parentActivity.fillUserNames(userDescList);
			}

			if (httpclient != null) {
				Log.i("userlisttask", "http closed");
				httpclient.getConnectionManager().shutdown();
				httpclient.close();
			}
		}
	}

}
