package com.example.shengliyi.criminalintentclass.Entity;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private boolean mIsSolved;
    private Date mDate;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mIsSolved;
    }

    public void setSolved(boolean solved) {
        mIsSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
