package com.example.shengliyi.criminalintentclass.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengliyi.criminalintentclass.Activity.CrimeActivity;
import com.example.shengliyi.criminalintentclass.Activity.CrimeListActivity;
import com.example.shengliyi.criminalintentclass.Activity.CrimePagerActivity;
import com.example.shengliyi.criminalintentclass.Entity.Crime;
import com.example.shengliyi.criminalintentclass.Entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.R;

import java.util.List;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private int mCurrentPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //    更新界面——设置适配器
    private void updateUI() {
        List<Crime> crimes = CrimeLab.getInstance(getActivity()).getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(mCurrentPosition);
        }

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
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    mCurrentPosition = getAdapterPosition();
                    startActivity(intent);
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
    }


}
