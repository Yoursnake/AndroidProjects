package com.example.shengliyi.photogallery.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shengliyi on 2017/5/9.
 */

public class PhotoBean {
    public static final String STATUS_OK = "ok", STATUS_FAILED = "fail";

    @SerializedName("photos")
    private PhotosInfo mPhotosInfo;
    @SerializedName("stat")
    private String mStatus;

    public PhotosInfo getPhotosInfo() {
        return mPhotosInfo;
    }

    public void setPhotosInfo(PhotosInfo photosInfo) {
        mPhotosInfo = photosInfo;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public class PhotosInfo {
        @SerializedName("photo")
        List<GalleryItem> mPhoto;

        public List<GalleryItem> getPhoto() {
            return mPhoto;
        }
    }
}
