package com.guo.attribute;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ͼƬ�첽������ɺ�ص�
 * 
 * @author yanbin
 * 
 */
public interface OnImageDownload {
	void onDownloadSucc(Bitmap bitmap, String c_url, ImageView mImageView);
}
