package com.carflash.user.accounts;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by lenovo-pc on 5/15/2017.
 */

public class ConfigSession {




    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "config_pref";
    private static final String CONFIG = "config";
    GsonBuilder builder ;
    Gson gson ;




    public ConfigSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        builder = new GsonBuilder();
        gson = builder.create();
    }


    public void setConfig(String config){
        editor.putString(CONFIG, config);
        editor.commit();
    }



    public ConfigModelResponse getConfig() {
        ConfigModelResponse configresponse = gson.fromJson(pref.getString(CONFIG, "") , ConfigModelResponse.class);
        return configresponse;
    }




}
