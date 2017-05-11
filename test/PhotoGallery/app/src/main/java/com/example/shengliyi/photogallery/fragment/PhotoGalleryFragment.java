package com.example.shengliyi.photogallery.fragment;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengliyi.photogallery.R;
import com.example.shengliyi.photogallery.entity.GalleryItem;
import com.example.shengliyi.photogallery.utils.FlickrFetchr;

import java.util.List;

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

    public static PhotoGalleryFragment newInstance() {

        Bundle args = new Bundle();

        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);
        return fragment;
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

        final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
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

        mPhotoRecyclerView.addOnScrollListener(onScrollListener);

        mPhotoRecyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 按 1080p 三列的标准设置行数
                int columns = mPhotoRecyclerView.getWidth() / 350;
                // 重新设置 LayoutManager, Adapter, onScrollListener
                mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));
                mPhotoRecyclerView.setAdapter(mPhotoAdapter);
                mPhotoRecyclerView.addOnScrollListener(onScrollListener);
                // 滚动到之前看到的位置
                mPhotoRecyclerView.getLayoutManager().scrollToPosition(mLastPosition);
                // 去除监听器，防止多次触发
                mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //  向上滑动
//        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//                // 移动后的初始位置
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && firstVisibleItemPosition == 0) {
//                    if (mFetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
//                        mNextPage--;
//
//                        if (mNextPage >= 1) {
//                            Toast.makeText(getActivity(), "Please wait to loading...", Toast.LENGTH_SHORT).show();
//                            // AsyncTask 只能执行一次，所以需要新建
//                            mFetchItemsTask = new FetchItemsTask();
//                            mFetchItemsTask.execute(mNextPage);
//                        } else {
//                            Toast.makeText(getActivity(), "This is the top!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFetchItemsTask = new FetchItemsTask();
        mNextPage = 1;
        mFetchItemsTask.execute(mNextPage);
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

        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.getCaption());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            holder.bindGalleryItem(item);
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
        }
    }
}
