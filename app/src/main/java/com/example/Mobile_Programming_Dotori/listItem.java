package com.example.list;

import android.graphics.drawable.Drawable;

public class listItem {

    private Drawable iconDrawable;
    private String titleStr;
    private Boolean checkState;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setCheck(boolean check) {
        checkState = check;
    }

    public boolean getCheck() {
        return this.checkState;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.titleStr;
    }
}

