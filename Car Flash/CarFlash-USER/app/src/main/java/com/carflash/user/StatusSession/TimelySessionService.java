package com.carflash.user.StatusSession;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.carflash.user.models.firebasemodels.ChatModel;
import com.carflash.user.samwork.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimelySessionService  extends Service{

    private DBHelper dbHelper ;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    boolean is_method_running = false ;

    private static String TAG = "TimelySessionService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dbHelper = new DBHelper(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("RideTable");
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimelySessionService.TimeDisplayTimerTask(), 0, 3000);
    }



    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fetchnodeData();
                }
            });
        }

        private void fetchnodeData() {
            final ArrayList<String> ids = dbHelper.getAllRideId() ;
            if(is_method_running == false){
                if(ids.size() > 0){
                    is_method_running = true ;
                    try{
                        myRef.child(""+ids.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG , "Fetched data  --- RideId:"+ids.get(0)+"  Ride Status:"+dataSnapshot.child("ride_status").getValue());
                                is_method_running = false ;
                                try{ if(dataSnapshot.child("ride_status").getValue().equals(Config.Status.NORMAL_CANCEL_BY_USER)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.NORMAL_REJECTED_BY_DRIVER)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.NORMAL_RIDE_END)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.NORMAL_CANCEL_BY_DRIVER)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.RENTAL_RIDE_REJECTED)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.RENTAL_RIDE_CANCEL_BY_USER) ||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.RENTAL_RIDE_END)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.RENTAL_RIDE_CANCEL_BY_DRIVER)||
                                        dataSnapshot.child("ride_status").getValue().equals(Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN)){ // if ride is cancelled by driver or user or expired remove it from database
                                    dbHelper.deleteRowByid(""+ids.get(0));
                                }else{
                                    dbHelper.updateRow(""+ids.get(0) , ""+dataSnapshot.child("ride_status").getValue());
                                }

                                    GenericTypeIndicator<List<ChatModel>> t = new GenericTypeIndicator<List<ChatModel>>() {};
                                    List<ChatModel> yourStringArray = dataSnapshot.child("Chat").getValue(t);

                                    EventBus.getDefault().post(new RideSessionEvent(ids.get(0) ,
                                            ""+dataSnapshot.child("ride_status").getValue() ,
                                            ""+dataSnapshot.child("done_ride_id").getValue() ,
                                            ""+dataSnapshot.child("changed_destination").getValue(),
                                            yourStringArray));
                                    }catch (Exception e){Log.e(""+TAG  , "Exception in onDataChanged :"+e.getMessage());}
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                is_method_running = false ;
                                Log.e(""+TAG , "Data Fetched from firebase cancelled "+databaseError.getMessage());
                            }
                        });
                    }catch (Exception e){
                        Log.e(""+TAG , "TAXI EXCEPTION "+e.getMessage());
                        is_method_running = false ;
                    }
                }else {
                    Log.i(""+TAG , "You don't have any ride id to update");
                }
            }
        }
    }
}
