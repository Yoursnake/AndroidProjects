package com.example.shengliyi.photogallery.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.shengliyi.photogallery.activity.PhotoGalleryActivity;
import com.example.shengliyi.photogallery.entity.GalleryItem;
import com.example.shengliyi.photogallery.entity.PhotoBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengliyi on 2017/5/7.
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "0dd2bf7a825d7517b3b7be1aee77adc9";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + "with:" +
                        urlSpec);
            }

            int readNum = 0;
            byte[] temp = new byte[1024];
            while ((readNum = in.read(temp)) > 0) {
                out.write(temp, 0, readNum);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {

        List<GalleryItem> items = new ArrayList<>();


        // https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=0dd2bf7a825d7517b3b7be1aee77adc9&format=json&nojsoncallback=1&extras=url_s
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received json:" + jsonString);

            Gson gson = new Gson();
            PhotoBean photoBean = gson.fromJson(jsonString, PhotoBean.class);
            items = photoBean.getPhotosInfo().getPhoto();

//            JSONObject jsonBody = new JSONObject(jsonString);
//            parseItem(items, jsonBody);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to fetch json:", e);
        }

        return items;
    }

    private void parseItem(List<GalleryItem> items, JSONObject jsonObject) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");



        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setCaption(photoJsonObject.getString("title"));
            item.setId(photoJsonObject.getString("id"));

            if (!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }
}
