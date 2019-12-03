
package com.example.Mobile_Programming_Dotori;

import android.graphics.drawable.Drawable;

public class listItem {

    private Drawable iconDrawable;
    private String titleStr;
    private int checkState;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setCheck(int check) {
        checkState = check;
    }

    public boolean getCheck() {
        if (this.checkState == 1)
            return true;
            else return false;
//        return this.checkState;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.titleStr;
    }
}
