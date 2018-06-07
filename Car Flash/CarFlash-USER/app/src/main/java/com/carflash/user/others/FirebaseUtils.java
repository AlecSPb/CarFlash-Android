package com.carflash.user.others;

import android.content.Context;
import android.os.Build;

import com.carflash.user.models.firebasemodels.UserLocation;
import com.carflash.user.samwork.Config;
import com.carflash.user.manager.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo-pc on 3/31/2017.
 */

public class FirebaseUtils {


    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    SessionManager sessionmanager ;
    LocationSession applocation_manager;
    Context context ;

    private String TAG = "FireBaseUtil";


    public FirebaseUtils(Context context){
        this.context = context ;
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(Config.Users);
        applocation_manager  = new LocationSession(context);
        sessionmanager = new SessionManager(context);
    }





    public  void updateLocation_with_text(){
        final UserLocation mUserLocatoin = new UserLocation(sessionmanager.getUserDetails().get(SessionManager.USER_ID),applocation_manager.getLocationDetails().get(LocationSession.KEY_CURRENT_LAT),applocation_manager.getLocationDetails().get(LocationSession.KEY_CURRENT_LONG),applocation_manager.getLocationDetails().get(LocationSession.KEY_CURRENT_LOCATION_TEXT) , ""+ (System.currentTimeMillis()/1000), ""+android.os.Build.MODEL,
                sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME),sessionmanager.getUserDetails().get(SessionManager.USER_LAST_NAME),sessionmanager.getUserDetails().get(SessionManager.USER_PHONE),
                sessionmanager.getUserDetails().get(SessionManager.USER_EMAIL),sessionmanager.getUserDetails().get(SessionManager.USER_PASSWORD),sessionmanager.getUserDetails().get(SessionManager.USER_ONLINE_OFFLINE),
                sessionmanager.getUserDetails().get(SessionManager.USER_DEVICE_ID),
                sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_ID),sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_MAIL), sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_IMAGE) , sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_FIRSTNAME), sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_LASTNAME),
                sessionmanager.getUserDetails().get(SessionManager.GOOGLE_ID),sessionmanager.getUserDetails().get(SessionManager.GOOGLE_NAME), sessionmanager.getUserDetails().get(SessionManager.GOOGLE_MAIL), sessionmanager.getUserDetails().get(SessionManager.GOOGLE_IMAGE),
                sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE));
//        mDatabaseReference.child(sessionmanager.getUserDetails().get(SessionManager.USER_ID)).setValue(mUserLocatoin);
        List<UserLocation>  data = new ArrayList<>();
        data.add(mUserLocatoin);
//        mDatabaseReference.child(sessionmanager.getUserDetails().get(SessionManager.USER_ID)).setValue( data );
        if(sessionmanager.isLoggedIn()){
            mDatabaseReference.child("/"+sessionmanager.getUserDetails().get(SessionManager.USER_ID)+"/"+""+ Build.BRAND+"_"+android.os.Build.MODEL+"_"+ Build.SERIAL).setValue(mUserLocatoin);
        }

    }

}


