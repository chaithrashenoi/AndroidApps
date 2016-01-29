package com.chaitra.photosharing;

public class ImageDescriptor {
	String userId;
	String userName;
	String imageName;
	String imageId;

	public ImageDescriptor(String userId, String userName, String imageName,
			String imageId) {
		this.userName = userName;
		this.userId = userId;
		this.imageName = imageName;
		this.imageId = imageId;
	}
}
