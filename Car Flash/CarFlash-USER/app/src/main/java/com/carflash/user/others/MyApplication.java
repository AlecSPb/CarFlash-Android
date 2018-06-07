package com.carflash.user.others;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;



/**
 * Created by Bhuvneshwar on 12/17/2016.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }
}