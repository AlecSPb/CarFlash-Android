package com.carflash.user.others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.carflash.user.R;
import com.carflash.user.TimeService;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by lenovo-pc on 4/7/2017.
 */

public class LocationSession {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LocationPrefrences";
    LocationEvent mLocationEvent ;

    public static String TAG = "****LOCATIONSESSION";


    public static final String KEY_CURRENT_LAT = "current_lat";
    public static final String KEY_CURRENT_LONG = "current_long";
    public static final String KEY_CURRENT_LOCATION_TEXT = "current_location_text";
    public static final String KEY_BEARING_FACTOR  = "bearing_factor";
    public static final String KEY_LOCATION_SERVICE_STARTED  = "location_service_started";
    public static final String KEY_METER_VALUE = "meter+value";




    public void startLocationService(){
        if(!isLocationserviceStarted()){
            _context.startService(new Intent(_context, TimeService.class));
            editor.putBoolean(KEY_LOCATION_SERVICE_STARTED , true);
            editor.commit();
        }
    }

    public boolean isLocationserviceStarted (){
        return pref.getBoolean(KEY_LOCATION_SERVICE_STARTED, false);
    }


    public LocationSession(Context context) {
        mLocationEvent = new LocationEvent();
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLocationLatLong(String current_lat ,
                                   String current_long ) {
        editor.putString(KEY_CURRENT_LAT, current_lat);
        editor.putString(KEY_CURRENT_LONG, current_long);
        editor.commit();
    }



    public void setLocationAddress(String current_location_txt  ) {
        if(current_location_txt == null || current_location_txt.equals("null")){
            mLocationEvent.setLocationWithAddress(getLocationDetails().get(KEY_CURRENT_LAT), getLocationDetails().get(KEY_CURRENT_LONG) , ""+_context.getResources().getString(R.string.no_internet_connection), Float.parseFloat(getLocationDetails().get(LocationSession.KEY_BEARING_FACTOR)));
            EventBus.getDefault().post(mLocationEvent);
           }
        else{
            mLocationEvent.setLocationWithAddress(getLocationDetails().get(KEY_CURRENT_LAT), getLocationDetails().get(KEY_CURRENT_LONG) , current_location_txt, Float.parseFloat(getLocationDetails().get(LocationSession.KEY_BEARING_FACTOR)));
            EventBus.getDefault().post(mLocationEvent);
            editor.putString(KEY_CURRENT_LOCATION_TEXT, current_location_txt);
            editor.commit();
        }
    }


    public void setBearingFactor(String bearing_factor  ) {
        editor.putString(KEY_BEARING_FACTOR, bearing_factor);
        editor.commit();
    }



    public HashMap<String, String> getLocationDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_CURRENT_LAT , pref.getString(KEY_CURRENT_LAT , ""));
        user.put(KEY_CURRENT_LONG , pref.getString(KEY_CURRENT_LONG , ""));
        user.put(KEY_CURRENT_LOCATION_TEXT , pref.getString(KEY_CURRENT_LOCATION_TEXT , "Not yet fetched"));
        user.put(KEY_BEARING_FACTOR , pref.getString(KEY_BEARING_FACTOR , "0.0"));
        user.put(KEY_METER_VALUE , pref.getString(KEY_METER_VALUE , "0.0"));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }




}
