package com.example.shengliyi.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/1/18.
 */

public class Crime {

    private String title;
    private UUID id;
    private Date date;
    private boolean solved;

    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return title;
    }
}
