package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.RideSessionEvent;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.models.ModelRideLoader;
import com.carflash.user.others.Constants;
import com.carflash.user.others.SingletonGson;
import com.carflash.user.samir.customviews.pulse.PulsatorLayout;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentalRideLoaderActivity extends Activity implements ApiManager.APIFETCHER {

    public static Activity activity;
    @Bind(R.id.root_layout)
    LinearLayout root_layout;

    Runnable mRunnable;
    Handler mHandler;
    ApiManager apiManager;
    DBHelper dbHelper;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.cancel)
    ImageView cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_ride_loader);
        mHandler = new Handler();
        dbHelper = new DBHelper(this);
        activity = this;
        apiManager = new ApiManager(this, this, this);
        ButterKnife.bind(this);
        root_layout.setMinimumHeight(Config.Screen_height(this));
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();

        FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + Config.Status.RENTAL_BOOKED, "Not yet generated", "0", Config.cities));

        dbHelper.clearTable();
        dbHelper.insertRow("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + Config.Status.RENTAL_BOOKED);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HashMap<String , String > data  = new HashMap<String, String>();
//                data.put("rental_booking_id" , ""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
//                data.put("user_id" , ""+new SessionManager(RentalRideLoaderActivity.this).getUserDetails().get(SessionManager.USER_ID));
//                data.put("cancel_reason_id" , "0");
//                apiManager.execution_method_post("sudden_cancel" , ""+ Apis.CancelRentalRide , data, true,ApiManager.ACTION_SHOW_TOAST);


                cancel.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                apiManager.execution_method_get("sudden_cancel", Apis.ride_cancel_by_user + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id) + "&auto=0" + "&ride_mode=2", false, ApiManager.ACTION_SHOW_TOAST);

                mHandler.removeCallbacks(mRunnable);
            }
        });


        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (Constants.isRentalRideloaderIsOpen) {
//                    HashMap<String , String > data  = new HashMap<String, String>();
//                    data.put("rental_booking_id" , ""+getIntent().getExtras().getString(""+Config.IntentKeys.Ride_id));
//                    data.put("user_id" , ""+new SessionManager(RentalRideLoaderActivity.this).getUserDetails().get(SessionManager.USER_ID));
//                    data.put("cancel_reason_id" , "0");
//                    apiManager.execution_method_post(""+Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE , ""+Apis.CancelRentalRide , data, true,ApiManager.ACTION_SHOW_TOAST);

                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + "" + Config.Status.RIDE_EXPIRE_AUTOMATICALLY_VIA_PULSE, "Not yet generated", "0", Config.cities));
                    dbHelper.deleteRowByid("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id));
                    apiManager.execution_method_get("" + Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE, Apis.ride_cancel_by_user + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id) + "&auto=0" + "&ride_mode=2", false, ApiManager.ACTION_SHOW_TOAST);

                }
            }
        };

        mHandler.postDelayed(mRunnable, 60000);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RideSessionEvent event) {
        try {
            if (event.getRide_id().equals("" + getIntent().getExtras().getString("ride_id"))) {

                if (event.getRide_status().equals("" + Config.Status.RENTAL_ACCEPTED) ||
                        event.getRide_status().equals("" + Config.Status.RENTAL_ARRIVED) ||
                        event.getRide_status().equals("" + Config.Status.RENTAl_RIDE_STARTED)) {  // driver accepted the request
                    finish();
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalTrackActivity.class)
                            .putExtra("ride_id", "" + event.getRide_id())
                            .putExtra("ride_status", "" + event.getRide_status()));
                }
                if (event.getRide_status().equals("" + Config.Status.RENTAL_RIDE_REJECTED)) {  // driver rejected the request
                    finish();
                    Toast.makeText(this, getResources().getString(R.string.no_drivers), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }
        Constants.isRentalRideloaderIsOpen = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.is_main_Activity_open = false;
        Constants.isRentalRideloaderIsOpen = false;

    }

    @Override
    public void onBackPressed() {

    }


    public void dialogForNoDriverAcceptRideDialooe() {
        final Dialog dialog = new Dialog(RentalRideLoaderActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_no_driver_accept_request_trial);

        dialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });


        try {
            dialog.show();
        } catch (Exception e) {
            Log.d("**exception_caught in trial ride confirm dialog activity ", "" + e.getMessage());
        }


    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try {
            ModelRideLoader modelRideLoader = SingletonGson.getInstance().fromJson("" + script, ModelRideLoader.class);
            if (APINAME.equals("sudden_cancel")) {
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_RIDE_CANCEL_BY_USER)) {
                    finish();
                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + Config.Status.RIDE_EXPIRE_BY_CLICKING_CROSS, "Not yet generated", "0", Config.cities));
                    dbHelper.deleteRowByid("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id));
                    Toast.makeText(activity, "You Just have Cancel the ride, You can book it again in order to get new ride. ", Toast.LENGTH_SHORT).show();
                }
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_ACCEPTED) || modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_ARRIVED) || modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAl_RIDE_STARTED)) {
                    // open track ride activity
                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + modelRideLoader.getDetails().getRide_id(), "" + modelRideLoader.getDetails().getRide_status(), "Not yet generated", "0", Config.cities));
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalTrackActivity.class).putExtra("ride_id", "" + modelRideLoader.getDetails().getRide_id()).putExtra("ride_status", "" + modelRideLoader.getDetails().getRide_status()));
                    finish();
                }
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_RIDE_END)) {
                    // open receipt Activity
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalReceiptActivity.class).putExtra("ride_id", "" + modelRideLoader.getDetails().getRide_id()));
                    finish();
                }
            }
            if (APINAME.equals("" + Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE)) {
                /// this need to be open after checking status
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_RIDE_CANCEL_BY_USER)) {
                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + Config.Status.RIDE_EXPIRE_BY_CLICKING_CROSS, "Not yet generated", "0", Config.cities));
                    dbHelper.deleteRowByid("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id));
                    dialogForNoDriverAcceptRideDialooe();
                }
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_ACCEPTED) || modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_ARRIVED) || modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAl_RIDE_STARTED)) {
                    // open track ride activity
                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + modelRideLoader.getDetails().getRide_id(), "" + modelRideLoader.getDetails().getRide_status(), "Not yet generated", "0", Config.cities));
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalTrackActivity.class).putExtra("ride_id", "" + modelRideLoader.getDetails().getRide_id()).putExtra("ride_status", "" + modelRideLoader.getDetails().getRide_status()));
                    finish();
                }
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.RENTAL_RIDE_END)) {
                    // open receipt Activity
                    startActivity(new Intent(RentalRideLoaderActivity.this, RentalReceiptActivity.class).putExtra("ride_id", "" + modelRideLoader.getDetails().getRide_id()));
                    finish();
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onFetchResultZero(String s) {

    }


}
