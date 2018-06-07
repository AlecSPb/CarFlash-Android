package com.carflash.user;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;

import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.others.FirebaseUtils;
import com.carflash.user.others.LocationSession;
import com.carflash.user.samwork.Config;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo-pc on 4/28/2017.
 */

public class TimeService extends Service {

    SamLocationRequestService sam_location ;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    FirebaseUtils firebaseUtils;
    LocationSession locationSession ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        sam_location = new SamLocationRequestService(this, false);
        locationSession = new LocationSession(this);
        firebaseUtils = new FirebaseUtils(this);
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, Config.Location_Updation_Interval);
    }



    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    updateLocation();
                }

            });
        }

        private void updateLocation() {
            sam_location.executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    locationSession.setLocationLatLong(""+ location.getLatitude() , ""+location.getLongitude());
                    try {
                        locationSession.setLocationAddress("----");
                    }catch (Exception e){
                        locationSession.setLocationAddress(""+e.getMessage());
                    }

                    firebaseUtils.updateLocation_with_text();
                }
            });
        }

    }
}