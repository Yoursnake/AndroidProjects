package com.example.shengliyi.photogallery.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.shengliyi.photogallery.activity.PhotoGalleryActivity;
import com.example.shengliyi.photogallery.entity.GalleryItem;
import com.example.shengliyi.photogallery.entity.PhotoBean;
import com.example.shengliyi.photogallery.fragment.PhotoGalleryFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengliyi on 2017/5/7.
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "0dd2bf7a825d7517b3b7be1aee77adc9";
    private static final String FETCH_RECENT_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

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

    public List<GalleryItem> fetchRecentPhotos(int page) {
        String url = buildUrl(FETCH_RECENT_METHOD, null, page);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query, int page) {
        String url = buildUrl(SEARCH_METHOD, query, page);
        return downloadGalleryItems(url);
    }

    public String buildUrl(String method, String query, int page) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method);

        if (method.equals(SEARCH_METHOD)) {
            builder = builder.appendQueryParameter("text", query);
            page = 1;
            PhotoGalleryFragment.setNextPage(1);
        }

        builder = builder.appendQueryParameter("page", Integer.toString(page));

        return builder.build().toString();
    }

    private List<GalleryItem> downloadGalleryItems(String url) {

        List<GalleryItem> items = new ArrayList<>();

        // 表示查询第几页的数据
        // https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=0dd2bf7a825d7517b3b7be1aee77adc9&format=json&nojsoncallback=1&extras=url_s
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received json:" + jsonString);

            // 通过 gson 解析 json，需要提前创建 bean，一层就是一个类
            Gson gson = new Gson();
            PhotoBean photoBean = gson.fromJson(jsonString, PhotoBean.class);
            items = photoBean.getPhotosInfo().getPhoto();

//            for (GalleryItem item : items) {
//                Log.i(TAG, item.toString2());
//            }

//            通过普通方法解析 json
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
