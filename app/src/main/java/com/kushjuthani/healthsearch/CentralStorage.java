package com.kushjuthani.healthsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CentralStorage {
    //shared preference is like a repositary
    SharedPreferences preferences;
    SharedPreferences.Editor editor;//help to write in sp
    Context myContext;

    public CentralStorage(Context myContext)
    {
        this.myContext=myContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(myContext);
        editor=preferences.edit();
    }
    public void setData(String key,String value)
    {
        editor.putString(key,value);
        System.out.println("Centeral Storage is seting data. setDate is working");
        editor.commit();
    }

    public String getData(String key)
    {

        System.out.println("Centeral Storage is returning data. getData working");
        return preferences.getString(key,"");
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

    public void removeData(String key)
    {
        editor.remove(key);
        editor.commit();
    }
}
