package com.carflash.user.samwork;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by user on 3/18/2017.
 */

public class RideSession {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RidePrefrences";

    public static final String PICK_UP_LATITUDE = "pickup_lat";
    public static final String PICK_UP_LONGITUDE = "pick_up_long";
    public static final String PICK_UP_ADDRESS = "pick_up_address";

    public static final String DROP_OFF_LATITUDE = "drop_off_lat";
    public static final String DROP_OFF_LONGITUDE = "drop_off_long";
    public static final String DROP_OFF_ADDRESS = "drop_off_address";


    public static final String SELECTED_CATEGORY_ID = "selected_cat_id";
    public static final String SELECTED_CATEGORY_NAME = "selected_cat_name";
    public static final String SELECTED_CATEGORY_IMAGE = "selected_cat_image";
    public static final String SELECTED_CATEGORY_CITY_ID= "selected_cat_city_id";


    public static final String CURRENT_LATITUDE = "current_lat";
    public static final String CURRENT_LONGITUDE= "current_long";
    public static final String CURRENT_ADDRESS = "current_address_tx";



    public RideSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void  setCategory(String cat_id  , String cat_name , String cat_image , String car_city_id){
        editor.putString(SELECTED_CATEGORY_ID, cat_id);
        editor.putString(SELECTED_CATEGORY_NAME, cat_name);
        editor.putString(SELECTED_CATEGORY_IMAGE, cat_image);
        editor.putString(SELECTED_CATEGORY_CITY_ID, car_city_id);
        editor.commit();
    }



//////////////  current attributes
    public void  set_current_location(String current_lat  , String current_long){
        editor.putString(CURRENT_LATITUDE, current_lat);
        editor.putString(CURRENT_LONGITUDE, current_long);

        editor.commit();
    }
    public void  set_current_address_txt(String current_address_txt){
        editor.putString(CURRENT_ADDRESS, current_address_txt);
        editor.commit();
    }
    public LatLng getCurrentLatLong(){
        return new LatLng(Double.parseDouble(""+pref.getString(CURRENT_LATITUDE, "")) , Double.parseDouble(""+pref.getString(CURRENT_LONGITUDE, "")));
    }



///////////  pick up attributes
    public void setPickupLocation(String pick_up_lat, String pick_up_long  ) {
    editor.putString(PICK_UP_LATITUDE, pick_up_lat);
    editor.putString(PICK_UP_LONGITUDE, pick_up_long);
    editor.commit();
}
    public void setPickup_address_text(String pick_up_address) {
        editor.putString(PICK_UP_ADDRESS, pick_up_address);
        editor.commit();
    }
    public LatLng getPickUpLatlong(){
        return new LatLng(Double.parseDouble(""+pref.getString(PICK_UP_LATITUDE, "")) , Double.parseDouble(""+pref.getString(PICK_UP_LONGITUDE, "")));
    }



///////// drop off attributes
    public void setDropOffLocation(String drop_off_lat, String drop_off_long ) {
        editor.putString(DROP_OFF_LATITUDE, drop_off_lat);
        editor.putString(DROP_OFF_LONGITUDE, drop_off_long);
        editor.commit();
    }
    public void setDropOffLocation_txt(String dropo_off_address  ) {
        editor.putString(DROP_OFF_ADDRESS, dropo_off_address);
        editor.commit();
    }
    public LatLng getDropOffLatlong(){
        return new LatLng(Double.parseDouble(""+pref.getString(DROP_OFF_LATITUDE, "")) , Double.parseDouble(""+pref.getString(DROP_OFF_LONGITUDE, "")));
    }


    public HashMap<String, String> getRideSessionDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(PICK_UP_LATITUDE, pref.getString(PICK_UP_LATITUDE, ""));
        user.put(PICK_UP_LONGITUDE, pref.getString(PICK_UP_LONGITUDE, ""));
        user.put(PICK_UP_ADDRESS, pref.getString(PICK_UP_ADDRESS, ""));

        user.put(DROP_OFF_LATITUDE, pref.getString(DROP_OFF_LATITUDE, ""));
        user.put(DROP_OFF_LONGITUDE, pref.getString(DROP_OFF_LONGITUDE, ""));
        user.put(DROP_OFF_ADDRESS, pref.getString(DROP_OFF_ADDRESS, ""));

        user.put(SELECTED_CATEGORY_ID, pref.getString(SELECTED_CATEGORY_ID, ""));
        user.put(SELECTED_CATEGORY_IMAGE, pref.getString(SELECTED_CATEGORY_IMAGE, ""));
        user.put(SELECTED_CATEGORY_NAME, pref.getString(SELECTED_CATEGORY_NAME, ""));
        user.put(SELECTED_CATEGORY_CITY_ID, pref.getString(SELECTED_CATEGORY_CITY_ID, ""));

        return user;
    }


    public void clearCurrentRide() {
        editor.putString(DROP_OFF_LATITUDE, "");
        editor.putString(DROP_OFF_LONGITUDE, "");
        editor.putString(DROP_OFF_ADDRESS, "");
        editor.commit();
    }




}
