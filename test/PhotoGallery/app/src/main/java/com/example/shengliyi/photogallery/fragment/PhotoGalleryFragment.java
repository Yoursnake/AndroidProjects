package com.example.shengliyi.photogallery.fragment;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shengliyi.photogallery.R;
import com.example.shengliyi.photogallery.entity.GalleryItem;
import com.example.shengliyi.photogallery.utils.FlickrFetchr;
import com.example.shengliyi.photogallery.utils.ThumbnailDownloader;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

import static java.lang.Thread.sleep;

/**
 * Created by shengliyi on 2017/5/7.
 */

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private final int MAX_PAGES = 30;

    private RecyclerView mPhotoRecyclerView;
    private ProgressBar mProgressBar;
    private List<GalleryItem> mItems;
    private FetchItemsTask mFetchItemsTask;
    private PhotoAdapter mPhotoAdapter;
    private Integer mNextPage;
    private int mLastPosition;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static PhotoGalleryFragment newInstance() {

        Bundle args = new Bundle();

        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFetchItemsTask = new FetchItemsTask();
        mNextPage = 1;
        mFetchItemsTask.execute(mNextPage);

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                        Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                        target.bindDrawable(drawable);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mProgressBar = (ProgressBar) view
                .findViewById(R.id.fragment_photo_gallery_progress_bar);

//        setupAdapter();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    // setupAdapter
//    private void setupAdapter() {
//        if (isAdded() && mItems != null) {
//            mPhotoAdapter = new PhotoAdapter(mItems);
//            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
//        }
//    }

    private void updateAdapter() {
        if (isAdded()) {
            if (mPhotoAdapter == null) {
                mPhotoAdapter = new PhotoAdapter(mItems);
                mPhotoRecyclerView.setAdapter(mPhotoAdapter);
            } else {
                mPhotoAdapter.addDate(mItems);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

//        private TextView mTitleTextView;
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView;
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindGalleryItem(GalleryItem item) {
            Picasso.with(getActivity())
                    .load(item.getUrl())
                    .placeholder(R.drawable.bill_up_close)
                    .into(mItemImageView);

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater()
                    .inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
//            holder.bindGalleryItem(item);
            Drawable placeHolder = ContextCompat.getDrawable(getActivity(),
                    R.drawable.bill_up_close);
//            holder.bindDrawable(placeHolder);
//            mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
            holder.bindGalleryItem(item);

//            for (int i = Math.max(0, position - 10); i < position + 10; i++) {
//                mThumbnailDownloader.queueThumbnail(holder, mGalleryItems.get(i).getUrl());
//            }
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void addDate(List<GalleryItem> items) {
            for (GalleryItem item : items) {
                mGalleryItems.add(item);
            }
        }

    }


    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            return new FlickrFetchr().fetchItems(params[0]);
//            try {
//                String result = new FlickrFetchr().getUrlString("https://www.sina.com.cn");
//                Log.i(TAG, "doInBackground: " + result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            updateAdapter();
            mProgressBar.setVisibility(View.GONE);

            // 当更新完 adapter 后设置滑动监听器，否则 adapter 为空时无法调用 getItemCount() 等方法
            final RecyclerView.OnScrollListener onDownScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                    // 找到移动后的最后位置
                    mLastPosition = layoutManager.findLastVisibleItemPosition();
                    // 如果目前状态为静止，且最后位置索引大于等于照片的数目
                    // 另外，之前的线程已处于完成状态则使用之前的线程爬取下一页的信息
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && mLastPosition >= mPhotoAdapter.getItemCount() - 1) {
                        if (mFetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
                            mNextPage++;

                            if (mNextPage <= MAX_PAGES) {
                                Toast.makeText(getActivity(), "Please wait to loading...", Toast.LENGTH_SHORT).show();
                                // AsyncTask 只能执行一次，所以需要新建
                                mFetchItemsTask = new FetchItemsTask();
                                mFetchItemsTask.execute(mNextPage);
                            } else {
                                Toast.makeText(getActivity(), "This is the end!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

            };

            mPhotoRecyclerView.addOnScrollListener(onDownScrollListener);

            mPhotoRecyclerView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // 按 1080p 三列的标准设置行数
                            int columns = mPhotoRecyclerView.getWidth() / 350;
                            // 重新设置 LayoutManager, Adapter, onDownScrollListener
                            mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));
                            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
                            mPhotoRecyclerView.addOnScrollListener(onDownScrollListener);
                            // 滚动到之前看到的位置
                            mPhotoRecyclerView.getLayoutManager().scrollToPosition(mLastPosition);
                            // 去除监听器，防止多次触发
                            mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }
}
