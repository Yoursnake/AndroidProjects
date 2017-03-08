package com.example.shengliyi.criminalintent.Entity;

import android.content.Context;
import android.util.Log;

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
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> crimes;    //因为CrimeLab是单例，所以CrimeLab对象的crimes列表是唯一的
    private CriminalIntentJSONSerializer serializer;    //该对象用于存储和读取json数据

    private static CrimeLab crimeLab;   //此对象即单例
    private Context appContext;     //对象所在的上下文环境

    private CrimeLab(Context appContext) {
        this.appContext = appContext;
        serializer = new CriminalIntentJSONSerializer(appContext, FILENAME);

        /*先尝试读取数据，如果数据不存在，则新生成一个列表对象，并在日志中提示*/
        try {
            crimes = serializer.loadCrimes();
        } catch (Exception e) {
            crimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
    }

    //通过该方法得到CrimeLab的单例
    public static CrimeLab getInstance(Context c) {
        if (crimeLab == null){
            //不直接把c作为参数传给构造函数是为了保证单例总是有Context可以使用
            crimeLab = new CrimeLab(c.getApplicationContext());
        }
        return crimeLab;
    }

    //通过id来得到Crime的对象
    public Crime getCrime(UUID id){
        for (Crime crime : crimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    //在crimes列表中添加Crime对象
    public void addCrime(Crime crime){
        crimes.add(crime);
    }

    //在crimes列表中移除Crime对象
    public void deleteCrime(Crime crime) {
        crimes.remove(crime);
    }

    //将Crimes对象存储为json数据
    public boolean saveCrimes(){
        try{
            serializer.saveCrimes(crimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG, "Error saving crimes: ", e); //实际开发中可以使用Toast或对话框来提醒用户
            return false;
        }
    }

    //得到唯一的Crimes列表
    public ArrayList<Crime> getCrimes(){
        return crimes;
    }
}
