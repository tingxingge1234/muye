/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.project.muye.utils;

import android.nfc.Tag;
import android.util.Log;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;

import cn.project.muye.I;
import cn.project.muye.MuYeApplication;
import cn.project.muye.R;
import cn.project.muye.data.RequestManager;

public class ImageUtils {
	public static final String TAG = ImageUtils.class.getName();
//	public static String getThumbnailImagePath(String imagePath) {
//		String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
//		path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
//		EMLog.d("msg", "original image path:" + imagePath);
//		EMLog.d("msg", "thum image path:" + path);
//		return path;
//	}
	
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;
		
	}
	
	
	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }


	public static void setNewGoodThumb(String goodsThumb, NetworkImageView nivThumb) {
		String path = I.DOWNLOAD_BOUTIQUE_IMG_URL+goodsThumb;
		nivThumb.setImageUrl(path, RequestManager.getImageLoader());
		nivThumb.setErrorImageResId(R.drawable.nopic);
		nivThumb.setDefaultImageResId(R.drawable.nopic);
	}
	public static void setThumb(String url, NetworkImageView imageView) {
		imageView.setDefaultImageResId(R.drawable.nopic);
		imageView.setErrorImageResId(R.drawable.nopic);
		imageView.setImageUrl(url,RequestManager.getImageLoader());
	}
	public static void setGoodDetailThumb(String colorImg, NetworkImageView imageView) {
		String url = MuYeApplication.SERVER_ROOT
				+"?"+I.KEY_REQUEST+"="+I.REQUEST_DOWNLOAD_COLOR_IMG
				+"&"+I.Color.COLOR_IMG+"="+colorImg;
		Log.e(TAG, "url=" + url);
		setThumb(url, imageView);
	}
}
