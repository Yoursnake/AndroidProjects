package com.example.shengliyi.criminalintent.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.Fragment.CrimeFragment;
import com.example.shengliyi.criminalintent.Entity.Crime;
import com.example.shengliyi.criminalintent.Entity.CrimeLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/2/1.
 */
/*注意这边继承的不是SingleFragmentActivity，然需要手动动态添加碎片*/
public class CrimePagerActivity extends AppCompatActivity{

    ViewPager viewPager;
    ArrayList<Crime> crimes;


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //通过上下文环境生成ViewPager对象
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);    //在res/values/ids.xml中注册了id
        setContentView(viewPager);      //将视图设置为ViewPager对象

        //由于这里CrimeLab是单例，所以crimes和CrimeListFragment中的是同一个
        crimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        /*给ViewPager对象设置适配器(Adapter)，适配器为 FragmentStatePagerAdapter 对象
        * 重写适配器对象的 getItem 和 getCount 对象即可获得碎片对象*/
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override   /*适配器中已经写了添加碎片的方法因此这边只需要返回需要添加的对象即可*/
            public Fragment getItem(int position) {

                Crime crime = crimes.get(position);//通过Crime对象在crimes列表中的位置确定Crime的对象

                //这边通过 CrimeFragment 的 newInstance 方法来返回对象是因为这样可以在碎片中添加数据
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override   //该方法写crimes列表的大小
            public int getCount() {
                return crimes.size();
            }
        });

        /*通过设置这个监听器可以在页面切换时改变标题*/
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Crime crime = crimes.get(position);
                if (crime.getTitle() != null) {
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 得到从 CrimeListActivity 发来的 id 数据
        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        // 通过得到的 id 设置 ViewPager对象的当前页
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
