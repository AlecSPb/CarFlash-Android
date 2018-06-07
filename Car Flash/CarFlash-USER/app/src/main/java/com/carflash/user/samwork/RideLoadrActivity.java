package com.carflash.user.samwork;

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

import com.carflash.user.PaymentActivity;
import com.carflash.user.R;
import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.RideSessionEvent;
import com.carflash.user.TrialRideConfirmDialogActivity;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.ModelRideLoader;
import com.carflash.user.others.Constants;
import com.carflash.user.others.SingletonGson;
import com.carflash.user.samir.customviews.pulse.PulsatorLayout;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideLoadrActivity extends Activity implements ApiManager.APIFETCHER {

    public static Activity activity;
    @Bind(R.id.root_layout)
    LinearLayout root_layout;

    Runnable mRunnable;
    Handler mHandler;
    ApiManager apiManager;
    SessionManager sessionManager;
    DBHelper dbHelper;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.cancel)
    ImageView cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_loadr);
        mHandler = new Handler();
        activity = this;
        apiManager = new ApiManager(this, this, this);
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        ButterKnife.bind(this);
        root_layout.setMinimumHeight(Config.Screen_height(this));
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);

                apiManager.execution_method_get("sudden_cancel", Apis.ride_cancel_by_user + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)+"&auto=0"+"&ride_mode=1", false, ApiManager.ACTION_SHOW_TOAST);


                FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + Config.Status.RIDE_EXPIRE_BY_CLICKING_CROSS, "Not yet generated", "0", Config.cities));
                dbHelper.deleteRowByid("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id));
                mHandler.removeCallbacks(mRunnable);
            }
        });



        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (Constants.rideLoaderActivity == 1) {
                    apiManager.execution_method_get(Config.ApiKeys.Key_Cancel_by_user, Apis.ride_cancel_by_user + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)+"&auto=1"+"ride_mode=1", false, ApiManager.ACTION_SHOW_TOAST);

                    FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id)).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id), "" + "" + Config.Status.RIDE_EXPIRE_AUTOMATICALLY_VIA_PULSE, "Not yet generated", "0", Config.cities));
                    dbHelper.deleteRowByid("" + getIntent().getExtras().getString("" + Config.IntentKeys.Ride_id));
                }
            }
        };
        mHandler.postDelayed(mRunnable, 60000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.is_main_Activity_open = true;
        Constants.rideLoaderActivity = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.is_main_Activity_open = false;
        Constants.rideLoaderActivity = 0;
    }


    public void dialogForNoDriverAcceptRideDialooe() {
        final Dialog dialog = new Dialog(RideLoadrActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
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
                TrialRideConfirmDialogActivity.activity.finish();
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
            if (APINAME.equals("sudden_cancel")) {
                progressBar.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                ModelRideLoader modelRideLoader = SingletonGson.getInstance().fromJson("" + script, ModelRideLoader.class);
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.NORMAL_CANCEL_BY_USER)) {
                    finish();
                    Toast.makeText(activity, R.string.you_just_have_cancelled_the_ride, Toast.LENGTH_SHORT).show();
                }
                if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.NORMAL_ACCEPTED_BY_DRIVER) ||
                        modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.NORMAL_ARRIVED) ||
                        modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.NORMAL_RIDE_STARTED)) {
                    finish();
                    startActivity(new Intent(RideLoadrActivity.this, TrackRideAactiviySamir.class).putExtra("ride_id", "" + modelRideLoader.getDetails().getRide_id()));
                } else if (modelRideLoader.getDetails().getRide_status().equals("" + Config.Status.NORMAL_RIDE_END)) {
                    finish();
                    startActivity(new Intent(RideLoadrActivity.this, PaymentActivity.class)
                            .putExtra("DONE_RIDE_ID", "" + modelRideLoader.getDetails().getRide_id()));
                }

            } else {
                dialogForNoDriverAcceptRideDialooe();
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
