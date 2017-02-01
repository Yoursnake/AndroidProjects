package com.example.shengliyi.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.activity.CrimeActivity;
import com.example.shengliyi.criminalintent.activity.CrimePagerActivity;
import com.example.shengliyi.criminalintent.model.Crime;
import com.example.shengliyi.criminalintent.model.CrimeLab;


import java.util.ArrayList;

/**
 * Created by shengliyi on 2017/1/20.
 */

public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> crimes;
    private static final int REQUEST_CRIME = 1;
    public static final String TAG = "CrimeListFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        crimes = CrimeLab.getInstance(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(crimes);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Get  the Crime from the adapter
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
//        Log.d(TAG, c.getTitle() + " was clicked");

        //Start CrimeActivity
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivityForResult(intent, REQUEST_CRIME);
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(), 0, crimes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //如果没有View，创建视图
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            Crime c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(new DateFormat().format("E MM dd HH:mm:ss z yyyy",c.getDate()));

            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME){
            //Handle result
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetInvalidated();
    }
}