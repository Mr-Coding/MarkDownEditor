package com.frank.markdowneditor.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;

public class SettingsData {
    private SharedPreferences sharedPreferences;
    private Editor            editor;

    public SettingsData(Context context, String fileName){
        sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public boolean getthemeColor(boolean defaultValue){
        return sharedPreferences.getBoolean("themeColor",defaultValue);
    }
    public boolean setthemeColor(boolean isOpenFindMd){
        return editor.putBoolean("themeColor",isOpenFindMd).commit();
    }


    public boolean isOpenFindMd(boolean defaultValue){
        return sharedPreferences.getBoolean("isOpenFindMd",defaultValue);
    }
    public boolean setOpenFindMd(boolean isOpenFindMd){
        return editor.putBoolean("isOpenFindMd",isOpenFindMd).commit();
    }


}
