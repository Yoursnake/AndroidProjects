package com.example.shengliyi.criminalintentclass.Entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeLab {

    private List<Crime> mCrimes = new ArrayList<>();

    private static CrimeLab sCrimeLab;

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context mContext) {
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            if (i%2 == 0) {
                crime.setSolved(true);
            }
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
