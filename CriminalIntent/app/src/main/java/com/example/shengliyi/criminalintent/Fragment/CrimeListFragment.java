package com.example.shengliyi.criminalintent.Fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.Activity.CrimeListActivity;
import com.example.shengliyi.criminalintent.Activity.CrimePagerActivity;
import com.example.shengliyi.criminalintent.Entity.Crime;
import com.example.shengliyi.criminalintent.Entity.CrimeLab;


import java.util.ArrayList;

/**
 * Created by shengliyi on 2017/1/20.
 */

public class CrimeListFragment extends ListFragment {

    private ArrayList<Crime> crimes;
    private boolean subtitleVisible;
    private Button addCrimeButton;


    private static final int REQUEST_CRIME = 1;
    public static final String TAG = "CrimeListFragment";

    /*以下均为 Create 方法*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 开启状态栏功能键
        setHasOptionsMenu(true);
        setRetainInstance(true);

        subtitleVisible = false;  //初始化时因为子标题没有显示，所有subtitleVisible是false

        getActivity().setTitle(R.string.crimes_title);
        crimes = CrimeLab.getInstance(getActivity()).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter(crimes);
        setListAdapter(adapter);


    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container, false);

        // 这个 Button 是当列表中无数据时，默认空视图上的按钮
        addCrimeButton = (Button)view.findViewById(R.id.add_crime_button);
        addCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListCrime();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 如果 subtitleVisible 是 true，则设置标题栏的子标题
            if (subtitleVisible) {
                CrimeListActivity activity = (CrimeListActivity)getActivity();
                activity.getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }

        ListView listView = (ListView)view.findViewById(android.R.id.list);
        // 上下文操作模式只在 HONEYCOMB 中开始存在
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            /*注册浮动上下文菜单*/
            registerForContextMenu(listView);
        } else {
            /*设置上下文操作模式*/
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            /*上下文操作模式的监听器*/
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                // 创建上下文操作模式的菜单视图
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;        //这边要改成 return true; 默认为 false
                }

                // 上下文操作栏需要刷新使用新数据时调用
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                /*上下文操作模式的点击事件*/
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        // 实现删除列表中 Crime 对象的操作
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged(); // 更新适配器即更新视图
                            return true;
                        default:
                            return false;
                    }
                }

                // 退出上下文操作模式或所选菜单项操作已响应时被调用
                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return view;
    }

    // 创建操作栏视图
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        // 如果 subtitleVisible 为 true 且 showSubtitle 不为空，则将 showSubtitle 的
        // 文本设置为 hide subtitle
        if (subtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    // 创建上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    /*以下均为点击方法*/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Get  the Crime from the adapter
        // 根据 crime 在列表中的位置得到 crime 对象
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        //Log.d(TAG, c.getTitle() + " was clicked");

        //Start CrimeActivity
        // 使用 intent 跳转到 CrimePagerActivity
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivityForResult(intent, REQUEST_CRIME);
    }

    // 设置操作栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 操作栏中在 New Crime 键上设置功能，添加 Crime 对象
            case R.id.menu_item_new_crime:
                addListCrime();
                return true;
            /* 操作栏中在 Show Subtitle 键上设置功能，确认操作栏中的子标题是否存在
             * 如果不存在则设置子标题为指定文本，并设置 subtitleVisible 为 true，
             * 然后改变 Show Subtitle 键文本为 Hide Subtitle
             * 如果存在则设置子标题为空，并设置 subtitleVisible 为 false
             * 然后改变 Hide Subtitle 建文本为 Hide Subtitle
             * */
            case R.id.menu_item_show_subtitle:
                CrimeListActivity activity = (CrimeListActivity) getActivity();
                if (activity.getSupportActionBar().getSubtitle() == null) {
                    activity.getSupportActionBar().setSubtitle(R.string.subtitle);
                    subtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                }else {
                    activity.getSupportActionBar().setSubtitle(null);
                    subtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 上下文菜单中的点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);    // 通过位置得到 Crime 对象

        switch (item.getItemId()){
            // 删除当前位置的 Crime 对象
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /*通过CrimeAdapter类来简化ArrayAdapter，得到Adapter对象只需在构造器中传入Crimes实例即可*/
    private class CrimeAdapter extends ArrayAdapter<Crime>{

        // 重现构建构造器，只传入 Crimes 列表
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

            Crime c = getItem(position);    //根据位置得到 Crime 对象

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

    // 当碎片处于 Resume 状态时，更新 Adapter
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetInvalidated();
    }

    // 当碎片处于 Pause 状态是，存储 crimes 列表
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }


    // 在列表中添加 Crime 对象
    private void addListCrime(){
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivityForResult(intent, 0);
    }
}