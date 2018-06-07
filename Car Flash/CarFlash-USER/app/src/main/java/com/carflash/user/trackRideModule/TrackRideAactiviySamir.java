package com.carflash.user.trackRideModule;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.ChatActivity;
import com.carflash.user.PaymentActivity;
import com.carflash.user.R;
import com.carflash.user.SosActivity;
import com.carflash.user.SplashActivity;
import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.RideSessionEvent;
import com.carflash.user.StatusSession.TimelySessionService;
import com.carflash.user.TrialRideConfirmDialogActivity;
import com.carflash.user.TripHistoryActivity;
import com.carflash.user.adapter.ReasonAdapter;
import com.carflash.user.drawroutemaps.DrawRouteMaps;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.CancelReasonCustomer;
import com.carflash.user.models.NewChangeDropLocationModel;
import com.carflash.user.models.NewShareRideModel;
import com.carflash.user.models.ViewRideInfo;
import com.carflash.user.models.firebasemodels.DriverLocation;
import com.carflash.user.others.AerialDistance;
import com.carflash.user.others.AppDialogs;
import com.carflash.user.others.Constants;
import com.carflash.user.others.FirebaseChatEvent;
import com.carflash.user.others.FirebaseChatUtillistener;
import com.carflash.user.others.MapUtils;
import com.carflash.user.samir.customviews.TypeFaceGothic;
import com.carflash.user.samir.customviews.TypeFaceTextMonixRegular;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideSession;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import morxander.zaman.ZamanUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class TrackRideAactiviySamir extends AppCompatActivity implements OnMapReadyCallback, ApiManager.APIFETCHER, AppDialogs.AppDialogListeners {


    public static String TAG = "TrackRideAactiviySamir";
    public static double driver_latitude = 0.0;
    public static double driver_longitude = 0.0;
    public static float bearing_factor = 0;
    public static double driver_travelled_distance = 0;
    public static boolean fetching_in_progress = false;
    LatLng origin = null;
    LatLng destination = null;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static Activity trackRideActivity;


    Marker mm, drop_marker, animated_marker;

    GoogleMap mGoogleMap;
    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    RideSession ride_session;
    ApiManager apimanager;
    ViewRideInfo viewRideInfo_response;
    CancelReasonCustomer cancelReasonCustomer_response;
    String ride_status = "";
    DriverLocation driverLocation;
    SessionManager sessionManager;
    AppDialogs appDialogs;

    FirebaseChatUtillistener firebaseChatUtillistener;
    DBHelper dbHelper ;


    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.status_txt)
    TypeFaceTextMonixRegular status_txt;
    @Bind(R.id.distance_text)
    TypeFaceGothic distance_text;
    @Bind(R.id.cancel_btn)
    Button cancel_btn;
    @Bind(R.id.pick_location_txt)
    TextView pick_location_txt;
    @Bind(R.id.drop_location_txt)
    TextView drop_location_txt;
    @Bind(R.id.change_destination)
    LinearLayout change_destination;
    @Bind(R.id.rating_txt)
    TextView rating_txt;
    @Bind(R.id.driver_img)
    CircleImageView driver_img;
    @Bind(R.id.driver_name_txt)
    TextView driver_name_txt;
    @Bind(R.id.car_img)
    ImageView car_img;
    @Bind(R.id.car_type_name_txt)
    TextView car_type_name_txt;
    @Bind(R.id.car_model_name_txt)
    TextView car_model_name_txt;
    @Bind(R.id.car_number_txt)
    TextView car_number_txt;
    @Bind(R.id.call_driver_btn)
    TextView call_driver_btn;
    @Bind(R.id.share_btn)
    TextView share_btn;
    @Bind(R.id.firebase_status)
    TextView firebaseStatus;
    @Bind(R.id.debug_connectivity_txt)
    TextView debug_connectivity_txt;
    @Bind(R.id.debug_location_txt)
    TextView debug_location_txt;
    @Bind(R.id.debug_firebase_message)
    TextView debug_firebase_message;
    @Bind(R.id.debug_fetching_status)
    TextView debug_fetching_status;
    @Bind(R.id.debug_timstamp_for_driver_location)
    TextView debug_timstamp_for_driver_location;
    @Bind(R.id.debug_device_time_stamp)
    TextView debug_device_time_stamp;
    @Bind(R.id.debugger_block)
    LinearLayout debugger_block;
    @Bind(R.id.sos)
    LinearLayout sos;
    private static final int TELEPHONE_PERM = 657;
    @Bind(R.id.chat_message)
    TextView chatMessage;
    @Bind(R.id.message_layout)
    LinearLayout messageLayout;

    private void setDataAccordingToStatus(DriverLocation driverLocation) {
        try {
            switch (ride_status) {
                case "1":
                    Log.d(TAG, "Ride accepted successfully");
                    break;
                case "2":
                    Log.d(TAG, "Ride Cancelled");
                    break;
                case "3":
                    Log.d(TAG, "Driver Arriving Now. ");
                    if (mm == null) {
//                    setMarker(driverLocation);
                        origin = new LatLng(Double.parseDouble(driverLocation.driver_current_latitude), Double.parseDouble(driverLocation.getDriver_current_longitude()));
                        destination = new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getPickup_lat()), Double.parseDouble(viewRideInfo_response.getDetails().getPickup_long()));
                        drawRoute(origin, destination, mGoogleMap);
                    } else {
                        animateMarker(new LatLng(Double.parseDouble(driverLocation.driver_current_latitude), Double.parseDouble(driverLocation.driver_current_longitude)), Float.parseFloat("" + driverLocation.bearingfactor));
                    }
                    break;
                case "4":
                    Log.d(TAG, "Booking Failed. ");
                    break;
                case "5":
                    Log.d(TAG, "Ride Arrived on Door of Customer. ");
                    break;
                case "6":
                    Log.d(TAG, "Riding now, Customer is inside the car now.");
                    if (mm == null) {
                        destination = new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble(viewRideInfo_response.getDetails().getDrop_long()));
                        origin = new LatLng(Double.parseDouble(driverLocation.getDriver_current_latitude()), Double.parseDouble(driverLocation.getDriver_current_longitude()));
                        drawRoute(origin, destination, mGoogleMap);
//                    setMarker(driverLocation);
                    } else {
                        animateMarker(new LatLng(Double.parseDouble(driverLocation.driver_current_latitude), Double.parseDouble(driverLocation.driver_current_longitude)), Float.parseFloat("" + driverLocation.bearingfactor));
                    }
                    break;
                case "7":
                    Log.d(TAG, "Ride Ended. ");
                    break;
                case "8":
                    Log.d(TAG, "Ride scheduled. ");
                    break;
                case "9":
                    Log.d(TAG, "Ride Cancelled.");
                    break;
            }
        } catch (Exception e) {

        }
        this.driverLocation = driverLocation;
    }


    final Handler mHandeler = new Handler();
    Runnable mRunnable;


    public boolean ArrivedDialogShown = false;
    public boolean StartDialogShown = false;
    public boolean CancelDialogShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apimanager = new ApiManager(this, this, this);
        trackRideActivity = this;
        ride_session = new RideSession(this);
        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        firebaseChatUtillistener = new FirebaseChatUtillistener("" + getIntent().getExtras().getString("ride_id"));
        appDialogs = new AppDialogs(this, this);
        setContentView(R.layout.activity_track_ride_aactiviy_samir);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(Config.DriverRefrence);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            TrialRideConfirmDialogActivity.activity.finish();
        } catch (Exception e) {
        }
        startService(new Intent(TrackRideAactiviySamir.this, TimelySessionService.class));
        clearNotification();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apimanager.execution_method_get(Config.ApiKeys.Key_cancelRideReason, Apis.cancelReason + "?&language_id=" + new LanguageManager(TrackRideAactiviySamir.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID), true, ApiManager.ACTION_SHOW_DIALOG);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Config.app_loaded_from_initial) {
                    startActivity(new Intent(TrackRideAactiviySamir.this, SplashActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });

        change_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGooglePlaceAPiDialoge();
            }
        });


        call_driver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callingTask();
                } catch (Exception e) {
                }
            }
        });


        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ride_id", "" + getIntent().getExtras().getString("ride_id"));
                apimanager.execution_method_post("share", "" + Apis.ShareRideurl_API, data, true, ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });


        status_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = debugger_block.getVisibility();
                if (visibility == 0) {
                    debugger_block.setVisibility(View.GONE);
                } else {
                    debugger_block.setVisibility(View.VISIBLE);
                }
            }
        });


        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackRideAactiviySamir.this, SosActivity.class)
                        .putExtra("ride_id", "" + getIntent().getExtras().getString("ride_id"))
                        .putExtra("driver_id", "" + viewRideInfo_response.getDetails().getDriver_id()));
            }
        });


        findViewById(R.id.chat_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackRideAactiviySamir.this, ChatActivity.class)
                        .putExtra("ride_id", "" + getIntent().getExtras().getString("ride_id"))
                        .putExtra("driver_name", "" + viewRideInfo_response.getDetails().getDriver_name())
                        .putExtra("ride_status", "" + Config.getStatustext("" + viewRideInfo_response.getDetails().getRide_status(), TrackRideAactiviySamir.this))
                        .putExtra("driver_image", "" + Apis.imageDomain + viewRideInfo_response.getDetails().getDriver_image()));
            }
        });



        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackRideAactiviySamir.this, ChatActivity.class)
                        .putExtra("ride_id", "" + getIntent().getExtras().getString("ride_id"))
                        .putExtra("driver_name", "" + viewRideInfo_response.getDetails().getDriver_name())
                        .putExtra("ride_status", "" + Config.getStatustext("" + viewRideInfo_response.getDetails().getRide_status(), TrackRideAactiviySamir.this))
                        .putExtra("driver_image", "" + Apis.imageDomain + viewRideInfo_response.getDetails().getDriver_image()));
            }
        });




    }


    private void showUserMesageWithTimer(String message) {
        messageLayout.setVisibility(View.VISIBLE);
        chatMessage.setText(""+message);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageLayout.setVisibility(View.GONE);
            }
        }, 5000);

    }





    @AfterPermissionGranted(TELEPHONE_PERM)
    public void callingTask() throws Exception {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            try { // Have permission, do the thing!
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + viewRideInfo_response.getDetails().getDriver_phone()));
                if (ActivityCompat.checkSelfPermission(TrackRideAactiviySamir.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            } catch (Exception e) {
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.this_app_need_telephony_permission), TELEPHONE_PERM, Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            callingTask();
        } catch (Exception e) {
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FirebaseChatEvent event){
        try{
            String text = new ZamanUtil(Long.parseLong(""+event.timestamp)).getTime() ;
            if(text.equals("Just Now")|| text.equals("In a few seconds")){
                showUserMesageWithTimer(event.message);
            }
        }catch (Exception e){}
    }








    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RideSessionEvent event) {

        Log.d("" +TAG, "caught_event_ride_status="+event.getRide_status()+" ride_id="+event.getRide_id()+" intent ride_id="+getIntent().getExtras().getString("ride_id"));


        if (event.getRide_id().equals("" + getIntent().getExtras().getString("ride_id"))) {
            ride_status = event.getRide_status();
            setCancelbuttonAccordingToStatus();
            setSosVisibility(ride_status);
            if (event.getChanged_destination().equals("1")) {
                apimanager.execution_method_get(Config.ApiKeys.Key_ViewRideInfo, Apis.viewRideInfo + "?ride_id=" + getIntent().getExtras().getString("ride_id"), true, ApiManager.ACTION_SHOW_TOP_BAR);
                FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + event.getRide_id()).setValue(new RideSessionEvent("" + event.getRide_id(), "" + event.getRide_status(), "Not yet generated", "0", event.Chat));
            }
            switch (event.getRide_status()) {
                case Config.Status.NORMAL_ARRIVED:
                    cancel_btn.setEnabled(false);
                    if (!ArrivedDialogShown) {
                        appDialogs.showMessageDialog(false, ""  + this.getResources().getString(R.string.TRACK_RIDE_ACTIVITY__driver_arrived), "ride_arrived");
                        ArrivedDialogShown = true;
                    }
                    break;
                case Config.Status.NORMAL_RIDE_STARTED:
                    if (!StartDialogShown) {
                        appDialogs.showMessageDialog(false, "" + this.getResources().getString(R.string.TRACK_RIDE_ACTIVITY__ride_is_started), "start_dialog");
                        StartDialogShown = true;
                    }
                    break;
                case Config.Status.NORMAL_RIDE_END:
                    startActivity(new Intent(this, PaymentActivity.class).putExtra("DONE_RIDE_ID", "" + event.getDone_ride_id()).putExtra("ride_status", "" + event.getRide_status()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case Config.Status.NORMAL_CANCEL_BY_DRIVER:
                    if (!CancelDialogShown) {
                        dialogForCancelRideByDriver();
                    }
                case Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN:
                    if (!CancelDialogShown) {
                        dialogForCancelRideByAdmin();
                    }
                    break;
            }
        }
        if (event.getRide_id().equals("" + getIntent().getExtras().getString("ride_id")) && event.getChanged_destination().equals("1")) {
            apimanager.execution_method_get(Config.ApiKeys.Key_ViewRideInfo, Apis.viewRideInfo + "?ride_id=" + getIntent().getExtras().getString("ride_id"), true, ApiManager.ACTION_SHOW_TOP_BAR);
            FirebaseDatabase.getInstance().getReference("" + Config.RideTableReference).child("" + getIntent().getExtras().getString("ride_id")).setValue(new RideSessionEvent("" + getIntent().getExtras().getString("ride_id"), "" + ride_status, "Not yet generated", "0", event.Chat));
        }
        clearNotification();
    }

    private void dialogForCancelRideByAdmin() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_cancel_by_admin);
        dialog.setCancelable(false);

        LinearLayout ll_ok_cancel = (LinearLayout) dialog.findViewById(R.id.ll_ok_cancel);
        ll_ok_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                status_txt.setText(Config.getStatustext(ride_status, TrackRideAactiviySamir.this));
                finish();
                startActivity(new Intent(TrackRideAactiviySamir.this, TripHistoryActivity.class));

            }
        });
        dialog.show();
        CancelDialogShown = true;
    }


    @Override
    public void onMapReady(GoogleMap mMap) {
        mGoogleMap = mMap;
        MapUtils.setMapTheme(this, mMap);
        mGoogleMap.setMaxZoomPreference(18);

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                final Dialog dialog = new Dialog(TrackRideAactiviySamir.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = dialog.getWindow();
                dialog.setCancelable(true);
                window.setGravity(Gravity.CENTER);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.demo_trackrideactivity_dialog);
                final EditText meter_edt = (EditText) dialog.findViewById(R.id.meter_edt);
                meter_edt.setText("" + sessionManager.getUserDetails().get(SessionManager.TAIL_FACTOR));
                dialog.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            sessionManager.setTailFactor("" + Double.parseDouble("" + meter_edt.getText().toString()));
                        } catch (Exception e) {
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(TrackRideAactiviySamir.this, TimelySessionService.class));
        firebaseChatUtillistener.startChatListening();
        EventBus.getDefault().register(this);
        Constants.Track_RideActivity_is_open = 0;
        if (Config.isConnectingToInternet(TrackRideAactiviySamir.this)) {
            apimanager.execution_method_get(Config.ApiKeys.Key_ViewRideInfo, Apis.viewRideInfo + "?ride_id=" + getIntent().getExtras().getString("ride_id"), true, ApiManager.ACTION_SHOW_TOP_BAR);
        } else {
            Toast.makeText(trackRideActivity, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.Track_RideActivity_is_open = 1;
        EventBus.getDefault().unregister(this);
        mHandeler.removeCallbacks(mRunnable);
        firebaseChatUtillistener.stopChatListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.Track_RideActivity_is_open = 1;
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (APINAME.equals(Config.ApiKeys.Key_ViewRideInfo)) {
                viewRideInfo_response = gson.fromJson("" + script, ViewRideInfo.class);
                ride_status = viewRideInfo_response.getDetails().getRide_status();
                setCancelbuttonAccordingToStatus();
                if (viewRideInfo_response.getDetails().getRide_status().equals("3")) {  // driver is arriving
                    // here orifgin location should be driver live location
                    dbHelper.insertRow(viewRideInfo_response.getDetails().getRide_id() , viewRideInfo_response.getDetails().getRide_status());

                } else if (viewRideInfo_response.getDetails().getRide_status().equals("6")) {
                    try {
                        dbHelper.insertRow(viewRideInfo_response.getDetails().getRide_id() , viewRideInfo_response.getDetails().getRide_status());
                        destination = new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble(viewRideInfo_response.getDetails().getDrop_long()));
                    } catch (Exception e) {
                    }
                } else if (viewRideInfo_response.getDetails().getRide_status().equals("5")) {
                    // show main source to destination route
                    dbHelper.insertRow(viewRideInfo_response.getDetails().getRide_id() , viewRideInfo_response.getDetails().getRide_status());
                    showSourceToDestinationRoute();
                } else if (viewRideInfo_response.getDetails().getRide_status().equals("7")) {
                    startActivity(new Intent(this, PaymentActivity.class).putExtra("DONE_RIDE_ID", "" + viewRideInfo_response.getDetails().getDone_ride_id()).putExtra("ride_status", "" + viewRideInfo_response.getDetails().getDone_ride_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }


                status_txt.setText("" + Config.getStatustext("" + viewRideInfo_response.getDetails().getRide_status(), this));
                driver_name_txt.setText("" + viewRideInfo_response.getDetails().getDriver_name());
                rating_txt.setText("" + viewRideInfo_response.getDetails().getDriver_rating());
                car_type_name_txt.setText("" + viewRideInfo_response.getDetails().getCar_type_name());
                car_number_txt.setText("" + viewRideInfo_response.getDetails().getCar_number());
                car_model_name_txt.setText("" + viewRideInfo_response.getDetails().getCar_model_name());
                pick_location_txt.setText("" + viewRideInfo_response.getDetails().getPickup_location());
                drop_location_txt.setText("" + viewRideInfo_response.getDetails().getDrop_location());
                Glide.with(this).load("" + Apis.imageDomain + viewRideInfo_response.getDetails().getCar_type_image()).into(car_img);
                Glide.with(this).load("" + Apis.imageDomain + viewRideInfo_response.getDetails().getDriver_image()).into(driver_img);

                ////////////////////////////  part for changing destination
                resetMarkerAfterBeginTrip();

                try {
                    startRunnableProcess();
                } catch (Exception e) {
                }

            }
            if (APINAME.equals(Config.ApiKeys.Key_cancelRideReason)) {
                cancelReasonCustomer_response = gson.fromJson("" + script, CancelReasonCustomer.class);
                showReasonDialog();
            }
            if (APINAME.equals(Config.ApiKeys.Key_Cancel_Ride)) {
                try {
                    TrialRideConfirmDialogActivity.activity.finish();
                } catch (Exception e) {
                }
                finish();
            }
            if (APINAME.equals("share")) {
                NewShareRideModel share_response = gson.fromJson("" + script, NewShareRideModel.class);
                String sharing_url = "" + Apis.Shareurl + share_response.getDetails();
                String shareBody = "" + sharing_url;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Ride");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Select one option to share your current ride."));
            }
            if (APINAME.equals("" + Config.ApiKeys.KEY_CHANGE_DESTINATION)) {
                NewChangeDropLocationModel drop_change_response = gson.fromJson("" + script, NewChangeDropLocationModel.class);
                apimanager.execution_method_get(Config.ApiKeys.Key_ViewRideInfo, Apis.viewRideInfo + "?ride_id=" + getIntent().getExtras().getString("ride_id"), true, ApiManager.ACTION_SHOW_TOP_BAR);
            }

            setSosVisibility(viewRideInfo_response.getDetails().getRide_status());
        } catch (Exception e) {
        }


    }

    @Override
    public void onFetchResultZero(String s) {

    }


    private void setSosVisibility(String ride_status) {
        if (ride_status.equals("6")) {
            sos.setVisibility(View.VISIBLE);
        } else {
            sos.setVisibility(View.GONE);
        }
    }


    public void drawRoute(LatLng origin, LatLng destination, GoogleMap mMap) {
        mGoogleMap.clear();
        try {
            DrawRouteMaps.getInstance(this, this, sessionManager, null, false).draw(origin, destination, mMap);
        } catch (Exception e) {

        }


        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 500, 90));
        setMarker(driverLocation);
    }


    public void setMarker(DriverLocation response) {

        try {
            mm = MapUtils.setDrivermarker(this, mGoogleMap, new LatLng(Double.parseDouble(response.driver_current_latitude), Double.parseDouble(response.driver_current_longitude)), viewRideInfo_response.getDetails().getDriver_name());

            if (ride_status.equals("3")) {
                MapUtils.setDestinationMarkerForPickPoint(this, mGoogleMap, destination, "Customer Location ");
            } else {
                drop_marker = MapUtils.setDestinationMarkerForDropPoint(this, mGoogleMap, destination, "Customer Location ");
            }

            animated_marker = MapUtils.setanimatedicon(destination, mGoogleMap, R.drawable.ic_map_pin_current_user, "41e74c3c", "D0C0392B");

        } catch (Exception e) {
        }
    }


    public void resetMarkerAfterBeginTrip() {
        try {
            if (drop_marker != null) {  // this means that destination has been already set
                mGoogleMap.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DrawRouteMaps.getInstance(TrackRideAactiviySamir.this, TrackRideAactiviySamir.this, sessionManager, null, false).draw(new LatLng(Double.parseDouble("" + driverLocation.getDriver_current_latitude()), Double.parseDouble("" + driverLocation.driver_current_longitude)), new LatLng(Double.parseDouble("" + viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble("" + viewRideInfo_response.getDetails().getDrop_long())), mGoogleMap);
                        } catch (Exception e) {
                        }
                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(origin)
                                .include(destination).build();
                        Point displaySize = new Point();
                        getWindowManager().getDefaultDisplay().getSize(displaySize);

                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 500, 90));
                        setMarker(driverLocation);
                        try {
                            mm = MapUtils.setDrivermarker(TrackRideAactiviySamir.this, mGoogleMap, new LatLng(Double.parseDouble(driverLocation.driver_current_latitude), Double.parseDouble(driverLocation.driver_current_longitude)), viewRideInfo_response.getDetails().getDriver_name());
//                            drop_marker  =    MapUtils.setDestinationMarkerForDropPoint(TrackRideAactiviySamir.this , mGoogleMap , new LatLng(Double.parseDouble(""+viewRideInfo_response.getDetails().getDrop_lat()) , Double.parseDouble(""+viewRideInfo_response.getDetails().getDrop_long())),"");
//                            animated_marker = MapUtils.setanimatedicon(new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getDrop_lat()) , Double.parseDouble(viewRideInfo_response.getDetails().getDrop_long())),mGoogleMap, R.drawable.ic_map_pin_current_user , "41e74c3c" , "D0C0392B");
                        } catch (Exception e) {
                        }
                    }
                }, 1000);
            }
        } catch (Exception e) {
            Toast.makeText(trackRideActivity, "Exception while removing runnable.", Toast.LENGTH_SHORT).show();
        }
    }


    public void animateMarker(final LatLng toPosition, float bearingfactor) {

        Location startingLocation = new Location("starting point");
        startingLocation.setLatitude(mm.getPosition().latitude);
        startingLocation.setLongitude(mm.getPosition().longitude);
        Location endingLocation = new Location("ending point");
        endingLocation.setLatitude(toPosition.latitude);
        endingLocation.setLongitude(toPosition.longitude);
//        rotateMarker(mm,bearingfactor);

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(mm.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    mm.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (false) {
                            mm.setVisible(false);
                        } else {
                            mm.setVisible(true);
                        }
                    }
                } catch (Exception e) {
                    Log.d("*****" + TAG, "Exception => " + e.getMessage());
                }
            }
        });
    }


    public void setDriverDistance(double latitude, double longitude) {
        if (driver_latitude == 0.0 && driver_longitude == 0.0) {
            driver_travelled_distance = 0;
            driver_latitude = latitude;
            driver_longitude = longitude;
        } else {
            driver_travelled_distance = driver_travelled_distance + AerialDistance.aerialDistanceFunctionInMeters(driver_latitude, driver_longitude, latitude, longitude);
            driver_latitude = latitude;
            driver_longitude = longitude;
            try {
                if (driver_travelled_distance > Double.parseDouble("" + sessionManager.getUserDetails().get(SessionManager.TAIL_FACTOR))) {
                    drawRoute(new LatLng(latitude, longitude), destination, mGoogleMap);
                    driver_travelled_distance = 0.0;
                }
            } catch (Exception e) {
            }
        }

        Log.d("** setting distance to text ", "" + driver_travelled_distance);
        distance_text.setText("" + driver_travelled_distance);
    }


    //when driver is arrived
    private void showSourceToDestinationRoute() {
        mGoogleMap.clear();
        try {
            if (viewRideInfo_response.getDetails().getDrop_long().equals("")) {
                try {
                    mDatabaseReference.child("" + viewRideInfo_response.getDetails().getDriver_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DriverLocation driverLocation22 = dataSnapshot.getValue(DriverLocation.class);
                            try {
                                mm = MapUtils.setDrivermarker(TrackRideAactiviySamir.this, mGoogleMap, new LatLng(Double.parseDouble("" + driverLocation22.driver_current_latitude), Double.parseDouble("" + driverLocation22.driver_current_longitude)), viewRideInfo_response.getDetails().getDriver_name());
                                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble("" + driverLocation22.driver_current_latitude), Double.parseDouble("" + driverLocation22.driver_current_longitude))).zoom(Config.MapDefaultZoom).build()));

                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            debug_firebase_message.setText("Fetching error " + databaseError.getMessage());
                        }
                    });
                } catch (Exception e) {
                }
            } else {
                try {
                    LatLng source = new LatLng(Double.parseDouble("" + viewRideInfo_response.getDetails().getPickup_lat()), Double.parseDouble("" + viewRideInfo_response.getDetails().getPickup_long()));
                    LatLng Destination = new LatLng(Double.parseDouble("" + viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble("" + viewRideInfo_response.getDetails().getDrop_long()));
                    MapUtils.setGreedmarker(this, mGoogleMap, source);
                    MapUtils.setRedmarker(this, mGoogleMap, Destination);
                    DrawRouteMaps.getInstance(TrackRideAactiviySamir.this, TrackRideAactiviySamir.this, sessionManager, null, false).draw(source, Destination, mGoogleMap);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            Toast.makeText(trackRideActivity, "Unable To Extract Route Info. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /////////////// dialogs
    public void dialogForCancelRideByDriver() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_cancel_by_driver);
        dialog.setCancelable(false);

        LinearLayout ll_ok_cancel = (LinearLayout) dialog.findViewById(R.id.ll_ok_cancel);
        ll_ok_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                status_txt.setText(Config.getStatustext(ride_status, TrackRideAactiviySamir.this));
                finish();
                startActivity(new Intent(TrackRideAactiviySamir.this, TripHistoryActivity.class));

            }
        });
        dialog.show();
        CancelDialogShown = true;
    }


    public void showReasonDialog() {

        final Dialog dialog = new Dialog(TrackRideAactiviySamir.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_cancel_reason);

        ListView lv_reasons = (ListView) dialog.findViewById(R.id.lv_reasons);
        lv_reasons.setAdapter(new ReasonAdapter(TrackRideAactiviySamir.this, cancelReasonCustomer_response));

        lv_reasons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                apimanager.execution_method_get(Config.ApiKeys.Key_Cancel_Ride, Apis.cancelRide + "?&reason_id=" + cancelReasonCustomer_response.getMsg().get(position).getReason_id() + "" + "&ride_id=" + viewRideInfo_response.getDetails().getRide_id() + "&language_id=" + new LanguageManager(TrackRideAactiviySamir.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID), true, ApiManager.ACTION_SHOW_DIALOG);
                status_txt.setText(Config.getStatustext(ride_status, TrackRideAactiviySamir.this));
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void setCancelbuttonAccordingToStatus() {
        if (ride_status.equals("3")) {
            cancel_btn.setVisibility(View.VISIBLE);
        } else {
            cancel_btn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (!Config.app_loaded_from_initial) {
            startActivity(new Intent(TrackRideAactiviySamir.this, SplashActivity.class));
            finish();
        } else {
            finish();
        }
    }


    public void startRunnableProcess() throws Exception {

        mRunnable = new Runnable() {
            @Override
            public void run() {
                debug_connectivity_txt.setText("Internet Connectivity =  " + Config.isConnectingToInternet(TrackRideAactiviySamir.this));
                debug_device_time_stamp.setText("Latest fetching time" + Config.getTimeFromTimeStamp(System.currentTimeMillis()));
                if (Config.isConnectingToInternet(TrackRideAactiviySamir.this) && fetching_in_progress == false) {
                    try {
                        fetching_in_progress = true;
                        debug_fetching_status.setText("Fetching status = " + fetching_in_progress);
                        mDatabaseReference.child("" + viewRideInfo_response.getDetails().getDriver_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                            driverLocation = dataSnapshot.getValue(DriverLocation.class);
                                DriverLocation driverLocation22 = dataSnapshot.getValue(DriverLocation.class);
                                try {
                                    bearing_factor = Float.parseFloat("" + driverLocation22.bearingfactor);
                                } catch (Exception e) {
                                }
                                try {
                                    setDriverDistance(Double.parseDouble(driverLocation22.driver_current_latitude), Double.parseDouble(driverLocation22.driver_current_longitude));
                                    debug_location_txt.setText("Recent location = " + driverLocation22.driver_current_latitude + "," + driverLocation22.driver_current_longitude);
                                    debug_timstamp_for_driver_location.setText("Driver location uploaded time = " + Config.getTimeFromTimeStamp(Long.parseLong("" + driverLocation22.getTimestamp())));
                                } catch (Exception e) {
                                }
                                setDataAccordingToStatus(driverLocation22);
                                debug_firebase_message.setText("Data Fetched successfully");
                                fetching_in_progress = false;
                                debug_fetching_status.setText("Fetching status = " + fetching_in_progress);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                debug_firebase_message.setText("Fetching error " + databaseError.getMessage());
                            }
                        });
                    } catch (Exception e) {
                    }
                } else {
//                    Toast.makeText(TrackRideAactiviySamir.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

                mHandeler.postDelayed(mRunnable, 3000);
            }
        };
        runOnUiThread(mRunnable);
//        Thread t = new Thread(mRunnable);
//        t.start();
    }


    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    private void openGooglePlaceAPiDialoge() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(TrackRideAactiviySamir.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                apimanager.execution_method_get("" + Config.ApiKeys.KEY_CHANGE_DESTINATION, "" + Apis.change_drop_location + "drop_lat=" + place.getLatLng().latitude + "&drop_long=" + place.getLatLng().longitude + "&drop_location=" + place.getName() + "&app_id=1" + "&ride_id=" + viewRideInfo_response.getDetails().getRide_id(), true, ApiManager.ACTION_SHOW_TOP_BAR);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }


    @Override
    public void onDialogDismiss(String Dialogname) {
        switch (Dialogname) {
            case "start_dialog":
                mGoogleMap.clear();
                try {
                    destination = new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble(viewRideInfo_response.getDetails().getDrop_long()));
                } catch (Exception e) {
                }
                setMarker(driverLocation);
                status_txt.setText(Config.getStatustext(ride_status, TrackRideAactiviySamir.this));
                break;
            case "ride_arrived":
                status_txt.setText(Config.getStatustext(ride_status, TrackRideAactiviySamir.this));
                if (viewRideInfo_response.getDetails().getDrop_long().equals("")) {
                    mGoogleMap.clear();
                    try {
                        mDatabaseReference.child("" + viewRideInfo_response.getDetails().getDriver_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DriverLocation driverLocation22 = dataSnapshot.getValue(DriverLocation.class);
                                try {
                                    mm = MapUtils.setDrivermarker(TrackRideAactiviySamir.this, mGoogleMap, new LatLng(Double.parseDouble(driverLocation22.driver_current_latitude), Double.parseDouble(driverLocation22.driver_current_longitude)), viewRideInfo_response.getDetails().getDriver_name());
                                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble("" + driverLocation22.driver_current_latitude), Double.parseDouble("" + driverLocation22.driver_current_longitude))).zoom(Config.MapDefaultZoom).build()));
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                debug_firebase_message.setText("Fetching error " + databaseError.getMessage());
                            }
                        });
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        destination = new LatLng(Double.parseDouble(viewRideInfo_response.getDetails().getDrop_lat()), Double.parseDouble(viewRideInfo_response.getDetails().getDrop_long()));
                    } catch (Exception e) {
                    }
                    showSourceToDestinationRoute();
                }
                break;
        }
    }


}