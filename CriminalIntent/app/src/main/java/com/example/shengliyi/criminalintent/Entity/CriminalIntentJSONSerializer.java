package com.example.shengliyi.criminalintent.Entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by shengliyi on 2017/2/10.
 */

public class CriminalIntentJSONSerializer {

    private Context context;
    private String fileName;

    public CriminalIntentJSONSerializer(Context context, String fileName){
        this.context = context;
        this.fileName = fileName;
    }

    /* 1.将crimes中的crime对象全部转换为json并存储在JSONArray中
     * 2.将JSONArray对象直接存储在fileName路径下
     * */
    public void saveCrimes(ArrayList<Crime> crimes)
            throws JSONException, IOException{
        //Build an array in JSON
        //先构造一个json数组用于存储crime转换成的json数据
        JSONArray array = new JSONArray();
        for (Crime crime : crimes) {
            array.put(crime.toJSON());
        }

        //Write the file to disk
        //在对象所在的上下文中创建输出流，并将JsonArray对象直接写入fileName路径
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            //最终关闭输出流
            if (writer != null) {
                writer.close();
            }
        }
    }

    /* 1.先从fileName路径中读取JSONArray存储的二进制数据
    *  2.然后将二进制数据通过JSONTokener解析成JSONArray对象
    *  3.从JSONArray对象中取出一个个的json数据并通过Crime的构造方法解析json生成Crime对象，并存入ArrayList中
    */
    public ArrayList<Crime> loadCrimes()
            throws IOException, JSONException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuilder
            /*通过上下文环境的fileName路径打开输入流*/
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                //Line breaks are omitted and irrelevant
                //使用BufferReader对象的readLine方法将每次读到的数据append到StringBuilder对象上
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            //用JSONTokener解析读取到的数据
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //Build the array of crimes from JSONObjects
            //将数据都json数据转换成Crime对象，并添加到列表中
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //Ignore this one; it happens when starting fresh
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return crimes;
    }
}
