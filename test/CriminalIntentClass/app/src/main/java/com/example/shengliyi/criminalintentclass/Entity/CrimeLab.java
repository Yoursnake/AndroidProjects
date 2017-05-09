package com.example.shengliyi.criminalintentclass.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.shengliyi.criminalintentclass.database.CrimeBaseHelper;
import com.example.shengliyi.criminalintentclass.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeLab {

//    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    private static CrimeLab sCrimeLab;

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
//        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime) {
//        mCrimes.add(crime);
        ContentValues values = getContentValues(crime);

        mDataBase.insert(CrimeTable.NAME, null, values);
    }
    public void removeCrime(Crime crime) {
//        mCrimes.remove(crime);
        String uuidString = crime.getId().toString();

        mDataBase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " =?",
                new String[] { uuidString });
    }
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDataBase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " =?",
                new String[] { uuidString });
    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                CrimeTable.NAME,
                null,   // Columns - null select all columns
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes() {
//        return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }
    public Crime getCrime(UUID id) {
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(id)) {
//                return crime;
//            }
//        }

        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " =?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }

    }

    public File getPhotoFile (Crime crime) {
        File externalFileDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileDir == null) {
            return null;
        }

        return new File(externalFileDir, crime.getPhotoFileName());
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        values.put(CrimeTable.Cols.TELEPHONE_NUMBER, crime.getTelephoneNumber());

        return values;
    }


}
