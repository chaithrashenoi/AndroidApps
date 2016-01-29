package com.chaitra.photosharing;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<ImageDescriptor> {

	Activity activity;

	public ImageAdapter(Context context, int resourceId,
			List<ImageDescriptor> items) {

		super(context, resourceId, items);
		this.activity = (Activity) context;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView textView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ImageDescriptor imageDesc = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fragment_photo_list, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageviewIcon);
			holder.textView = (TextView) convertView
					.findViewById(R.id.textViewIcon);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String path = imageDesc.userId + "_" + imageDesc.imageName;
		File photoFile = activity.getFileStreamPath(path);
		String absPath = photoFile.getAbsolutePath();
		try {
			Bitmap thePhoto = BitmapFactory.decodeFile(absPath);
			holder.imageView.setScaleType(ScaleType.CENTER_CROP);
			holder.imageView.setImageBitmap(thePhoto);
			holder.textView.setText(imageDesc.imageName);
		} catch (Throwable t) {
			Log.i("BitmapFactory.decodeFile", "did not work", t);
		}

		return convertView;
	}
}