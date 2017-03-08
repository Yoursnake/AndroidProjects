package com.example.shengliyi.criminalintent.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.shengliyi.criminalintent.R;

import java.util.ArrayList;

/**
 * Created by shengliyi on 2017/1/20.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    //这边定义了一个抽象方法用于创建 Fragment 对象
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        /*通过getSupportFragmentManager方法得到 FragmentManager 对象
        * 如果不是兼容包则使用 getFragmentManager 方法
        * */
        FragmentManager fm = getSupportFragmentManager();
        //通过 id 查找布局中是否有 Fragment 实例
        Fragment fragment = fm.findFragmentById(R.id.crime_title_layout);

        /*如果碎片存在则不再替换，如果碎片为空则开启事务替换碎片*/
        if(fragment == null){
            /*这里 fragment 通过 createFragment 抽象方法得到
            * 因此继承 SingleFragmentActivity 类的类只需重写 createFragment 方法
            * */
            fragment = createFragment();
            fm.beginTransaction()
                    .replace(R.id.crime_title_layout,fragment)
                    .commit();
        }

    }
}
