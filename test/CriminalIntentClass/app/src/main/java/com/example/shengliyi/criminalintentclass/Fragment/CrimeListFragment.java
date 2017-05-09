package com.example.shengliyi.criminalintentclass.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shengliyi.criminalintentclass.entity.Crime;
import com.example.shengliyi.criminalintentclass.entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.R;

import java.util.List;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeListFragment extends Fragment {

    private static final String IS_SUBTITLE_VISIBLE = "is_subtitle_visible";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private Button mEmptyButton;
    private int mCurrentPosition = -1;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;
    private ActionMode mActionMode;

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(IS_SUBTITLE_VISIBLE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // context 决定 mCallbacks 中的方法
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyButton = (Button) view.findViewById(R.id.empty_list_new_crime);

        mEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCrime();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem menuSubtitle = menu.findItem(R.id.menu_item_subtitle);
        if (mSubtitleVisible) {
            menuSubtitle.setTitle(R.string.hide_subtitle);
        } else {
            menuSubtitle.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_new_crime:
                addCrime();
                return true;
            case R.id.menu_item_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCrime() {
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);

        updateUI(); // 因为平板设备在创建 crime 时，列表不会消失，因此要马上更新列表
        mCallbacks.onCrimeSelected(crime);
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
//        String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //    更新界面——设置适配器
    public void updateUI() {
        List<Crime> crimes = CrimeLab.getInstance(getActivity()).getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyItemChanged(mCurrentPosition);
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        if (crimes.size() == 0)
            mEmptyButton.setVisibility(View.VISIBLE);
        else
            mEmptyButton.setVisibility(View.INVISIBLE);

        updateSubtitle();
    }

//    ViewHold
    private class CrimeHold extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private CheckBox mSolvedCheckBox;
        private TextView mDateTextView;

        private Crime mCrime;

        public CrimeHold(final View itemView) {
            super(itemView);


//        列表点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.onCrimeSelected(mCrime);

                }
            });


    mTitleTextView = (TextView) itemView
            .findViewById(R.id.list_item_crime_title);
    mDateTextView = (TextView) itemView
            .findViewById(R.id.list_item_crime_date);
    mSolvedCheckBox = (CheckBox) itemView
            .findViewById(R.id.list_item_crime_solved);
}

    public void bindCrime(Crime crime) {
        mCrime = crime;
        mTitleTextView.setText(crime.getTitle());
        mSolvedCheckBox.setChecked(crime.isSolved());
        mDateTextView.setText(crime.getDate().toString());

    }
}

//    Adapter
private class CrimeAdapter extends RecyclerView.Adapter<CrimeHold> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHold onCreateViewHolder(ViewGroup parent, int viewType) {
            // 先创建一个视图，然后将通过这个视图创建 CrimeHold 对象
            View view = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHold(view);
        }

        @Override
        public void onBindViewHolder(CrimeHold holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
             mCrimes = crimes;
        }
    }


}

