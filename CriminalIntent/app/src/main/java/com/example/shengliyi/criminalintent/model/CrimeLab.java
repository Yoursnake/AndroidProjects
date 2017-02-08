package com.example.shengliyi.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/1/20.
 */
/*
Crime工厂
通过 getInstance() 方法得到单例
对Crime列表做一个初始化
可以通过 getCrime(UUID) 和 getCrimes() 方法来得到 crime 和 crimes 列表
通过 addCrime(Crime crime) 方法可以添加 crime
*/
public class CrimeLab {
    private ArrayList<Crime> crimes;

    private static CrimeLab crimeLab;
    private Context appContext;

    private CrimeLab(Context appContext) {
        this.appContext = appContext;
        crimes = new ArrayList<Crime>();
    }

    public static CrimeLab getInstance(Context c) {
        if (crimeLab == null){
            crimeLab = new CrimeLab(c.getApplicationContext()); //不直接把c作为参数传给构造函数是为了保证单例总是有Context可以使用
        }
        return crimeLab;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : crimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime crime){
        crimes.add(crime);
    }

    public ArrayList<Crime> getCrimes(){
        return crimes;
    }
}
