package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.RideSessionEvent;
import com.carflash.user.adapter.ReasonAdapter;
import com.carflash.user.drawroutemaps.DrawRouteMaps;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.CancelReasonCustomer;
import com.carflash.user.models.firebasemodels.DriverLocation;
import com.carflash.user.models.NewRideInfoModel;
import com.carflash.user.others.AppDialogs;
import com.carflash.user.others.Constants;
import com.carflash.user.others.LocationSession;
import com.carflash.user.others.MapUtils;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideSession;
import com.carflash.user.urls.Apis;
import com.carflash.user.manager.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RentalTrackActivity extends AppCompatActivity implements OnMapReadyCallback , ApiManager.APIFETCHER , AppDialogs.AppDialogListeners{



    public static Activity trackRideActivity ;

    GoogleMap mGoogleMap ;
    FirebaseDatabase database ;
    DatabaseReference mDatabaseReference ;
    CancelReasonCustomer cancelReasonCustomer_response ;
    LocationSession locationSession ;
    SessionManager sessionManager ;
    RideSession ride_session ;
    Button cancel_btn ;
    TextView  status_txt, driver_name_txt, rating_txt, car_type_name_txt, car_number_txt,car_model_name_txt, pick_location_txt ;
    ImageView car_img, driver_img ;
    ApiManager apimanager ;
    private static final int TELEPHONE_PERM = 657;


    NewRideInfoModel ride_info_response  ;

    private String RIDE_ID = "";
    private String RIDE_STATUS = "";

    Gson gson ;
    private LatLng destination ;
    private LatLng origin;
    Marker mm ;
    AppDialogs appDialogs;
    DBHelper dbHelper ;

    public boolean ArrivedDialogShown = false;
    public boolean StartDialogShown = false;
    public boolean CancelDialogShown = false;
    

    private void setDataAccordingToStatus(DriverLocation driverlocation) {

        switch (RIDE_STATUS){
            case Config.Status.RENTAL_ACCEPTED:
                cancel_btn.setVisibility(View.VISIBLE);
                status_txt.setText("Arriving Now");
//                origin = new LatLng(Double.parseDouble(driverlocation.driver_current_latitude) , Double.parseDouble(driverlocation.getDriver_current_longitude()));
//                destination = new LatLng(Double.parseDouble(ride_info_response.getDetails().getPickup_lat()) , Double.parseDouble(ride_info_response.getDetails().getPickup_long()));
//                drawRoute(origin,destination,mGoogleMap);
                break;
            case Config.Status.RENTAL_ARRIVED:
                cancel_btn.setVisibility(View.GONE);
                status_txt.setText("Driver Arrived");
                break;
            case Config.Status.RENTAl_RIDE_STARTED:
                cancel_btn.setVisibility(View.GONE);
                status_txt.setText("Riding Now");
                break;
            case Config.Status.RENTAL_RIDE_END:
                cancel_btn.setVisibility(View.GONE);
                status_txt.setText("Ride Ended");
                break;

        }

    }


    final Handler mHandeler = new Handler();
    Runnable mRunnable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apimanager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        appDialogs = new AppDialogs(this, this);
        locationSession = new LocationSession(this);
        trackRideActivity = this;
        dbHelper = new DBHelper(this);
        ride_session = new RideSession(this);
        gson = new GsonBuilder().create() ;
        setContentView(R.layout.activity_rental_track);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        status_txt = (TextView) findViewById(R.id.status_txt);
        driver_name_txt = (TextView) findViewById(R.id.driver_name_txt);
        rating_txt = (TextView) findViewById(R.id.rating_txt);
        car_type_name_txt = (TextView) findViewById(R.id.car_type_name_txt);
        car_number_txt = (TextView) findViewById(R.id.car_number_txt);
        car_model_name_txt = (TextView) findViewById(R.id.car_model_name_txt);
        pick_location_txt = (TextView) findViewById(R.id.pick_location_txt);
        driver_img = (ImageView) findViewById(R.id.driver_img);
        car_img = (ImageView) findViewById(R.id.car_img);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        database =  FirebaseDatabase.getInstance() ;
        mDatabaseReference = database.getReference(Config.DriverRefrence);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        clearNotification();

        RIDE_ID = getIntent().getExtras().getString("ride_id");
        RIDE_STATUS = getIntent().getExtras().getString("ride_status");


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apimanager.execution_method_get(Config.ApiKeys.Key_cancelRideReason , Apis.cancelReason+"?&language_id="+new LanguageManager(RentalTrackActivity.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID), true,ApiManager.ACTION_SHOW_TOP_BAR);
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Config.app_loaded_from_initial){
                    startActivity(new Intent(RentalTrackActivity.this , SplashActivity.class));
                    finish();
                }else {
                    finalizeActivity();
                }

            }
        });

        findViewById(R.id.call_driver_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callingTask();
                } catch (Exception e) {
                }
            }
        });

    }



    @AfterPermissionGranted(TELEPHONE_PERM)
    public void callingTask() throws Exception {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CALL_PHONE)) {
            try { // Have permission, do the thing!
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ride_info_response.getDetails().getDriver_phone()));
                if (ActivityCompat.checkSelfPermission(RentalTrackActivity.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            } catch (Exception e) {
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.this_app_need_telephony_permission), TELEPHONE_PERM, android.Manifest.permission.CALL_PHONE);
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
    public void onMessageEvent(RideSessionEvent event){
        try{
            if(event.getRide_id().equals(""+getIntent().getExtras().getString("ride_id")) ){
                RIDE_ID = event.getRide_id();
                RIDE_STATUS = event.getRide_status();
                switch (RIDE_STATUS){
                    case Config.Status.RENTAL_ACCEPTED:
                        cancel_btn.setVisibility(View.VISIBLE);
                        status_txt.setText("Arriving Now");
                        break;
                    case Config.Status.RENTAL_ARRIVED:
                        cancel_btn.setVisibility(View.GONE);
                        status_txt.setText("Driver Arrived");
                        if (!ArrivedDialogShown) {
                            appDialogs.showMessageDialog(false, "" + this.getResources().getString(R.string.TRACK_RIDE_ACTIVITY__driver_arrived), "ride_arrived");
                            ArrivedDialogShown = true;
                        }
                        break;
                    case Config.Status.RENTAl_RIDE_STARTED:
                        cancel_btn.setVisibility(View.GONE);
                        status_txt.setText("Riding Now");
                        if (!StartDialogShown) {
                            appDialogs.showMessageDialog(false, "" + this.getResources().getString(R.string.TRACK_RIDE_ACTIVITY__ride_is_started), "start_dialog");
                            StartDialogShown = true;
                        }
                        break;
                    case Config.Status.RENTAL_RIDE_END:
                        cancel_btn.setVisibility(View.GONE);
                        status_txt.setText("Ride Ended");
                        break;
                    case Config.Status.RENTAL_RIDE_CANCEL_BY_DRIVER:
                        showCancelDialogByDriver();
                        break ;
                    case Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN:
                        showCancelDialogByAdmin();
                        break ;
                }

                if(RIDE_STATUS.equals(""+Config.Status.RENTAL_RIDE_END)){
                    startActivity(new Intent(RentalTrackActivity.this , RentalReceiptActivity.class)
                            .putExtra("ride_id" , ""+RIDE_ID)
                            .putExtra("coming_from" , "rental track activity"));
                    finish();
                }
            }}catch (Exception e ){}

    }

    private void showCancelDialogByAdmin() {
        final Dialog dialog = new Dialog(RentalTrackActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.ride_cancel_dialog_by_admin);
        dialog.findViewById(R.id.ll_ok_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeActivity();
            }
        });

        dialog.show();
    }


    private void showCancelDialogByDriver() {
        final Dialog dialog = new Dialog(RentalTrackActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.ride_cancel_dialog_by_driver);
        dialog.findViewById(R.id.ll_ok_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeActivity();
            }
        });

        dialog.show();
    }


    @Override
    public void onMapReady(GoogleMap mMap) {
        mGoogleMap = mMap;
        MapUtils.setMapTheme(this , mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {EventBus.getDefault().register(this);}catch (Exception e){}
        Constants.isRentaltrackActivityIsOpen = true;
        HashMap<String , String > data = new HashMap<>();
        data.put("rental_booking_id" , ""+RIDE_ID);
        apimanager.execution_method_post(Config.ApiKeys.KEY_REST_RIDE_INFO, ""+Apis.Ride_Info , data, true ,ApiManager.ACTION_SHOW_TOP_BAR);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.isRentaltrackActivityIsOpen = false;
        try{EventBus.getDefault().unregister(this);}catch (Exception e){}
        mHandeler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.isRentaltrackActivityIsOpen = false;
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try{if(APINAME.equals(""+Config.ApiKeys.KEY_REST_RIDE_INFO)){
            ride_info_response = gson.fromJson(""+script , NewRideInfoModel.class);
            driver_name_txt.setText(""+ride_info_response.getDetails().getDriver_name());
            pick_location_txt.setText(""+ride_info_response.getDetails().getPickup_location());
            car_type_name_txt.setText(""+ride_info_response.getDetails().getCar_type_name());
            car_number_txt.setText(""+ride_info_response.getDetails().getCar_number());
            car_model_name_txt.setText(""+ride_info_response.getDetails().getCar_model_name());
            rating_txt.setText(""+ride_info_response.getDetails().getRating());
            Glide.with(RentalTrackActivity.this).load(""+Apis.imageDomain+""+ride_info_response.getDetails().getDriver_image()).into(driver_img);
            Glide.with(RentalTrackActivity.this).load(""+Apis.imageDomain+""+ride_info_response.getDetails().getCar_type_image()).into(car_img);

            dbHelper.clearTable();
            dbHelper.insertRow(""+ride_info_response.getDetails().getRental_booking_id() , ""+ride_info_response.getDetails().getBooking_status());
            startRunnableProcess();
        }if(APINAME.equals(""+Config.ApiKeys.Key_cancelRideReason)){
            cancelReasonCustomer_response = gson.fromJson(""+script, CancelReasonCustomer.class);
            showReasonDialog();
        }if(APINAME.equals(""+Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE)){
            finalizeActivity();
        }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }



    private void finalizeActivity() {
        try{
            TripHistoryActivity.ridesActivity.finish();
        }catch (Exception e){

        }try{
            RentalRidesSelectedActivity.activity.finish();
        }catch (Exception e){

        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if(!Config.app_loaded_from_initial){
            startActivity(new Intent(RentalTrackActivity.this , SplashActivity.class));
            finish();
        }else{
            finalizeActivity();
        }
    }


    public void startRunnableProcess(){
        mRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    mDatabaseReference.child(""+ride_info_response.getDetails().getDriver_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DriverLocation driverLocation22 = dataSnapshot.getValue(DriverLocation.class);

                            try{animatemarker (driverLocation22);}catch (Exception e){}
                            setDataAccordingToStatus(driverLocation22);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e ){
                }
                mHandeler.postDelayed(mRunnable, 3000);
            }
        };
        Thread t = new Thread(mRunnable);
        t.start();
    }


    public  void animatemarker(final DriverLocation driverLocation) {

        LatLng mlat = new LatLng(Double.parseDouble(""+driverLocation.getDriver_current_latitude()) , Double.parseDouble(""+driverLocation.getDriver_current_longitude()));
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));


        if(mm == null){
            mm = MapUtils.setDrivermarker(this,mGoogleMap, new LatLng(Double.parseDouble(""+driverLocation.getDriver_current_latitude()) , Double.parseDouble(""+driverLocation.getDriver_current_longitude())) , driverLocation.getDriver_name());
        }

    }



    public void drawRoute (LatLng origin  , LatLng destination , GoogleMap mMap  ){
        mGoogleMap.clear();
        try{
            DrawRouteMaps.getInstance(this , this , sessionManager, null , false).draw(origin, destination, mMap);
        }catch (Exception e){

        }
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 500, 90));
    }


    public void showReasonDialog() {

        final Dialog dialog = new Dialog(RentalTrackActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_cancel_reason);

        ListView lv_reasons = (ListView) dialog.findViewById(R.id.lv_reasons);
        lv_reasons.setAdapter(new ReasonAdapter(RentalTrackActivity.this, cancelReasonCustomer_response));

        lv_reasons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String , String > data  = new HashMap<String, String>();
                data.put("rental_booking_id" , ""+RIDE_ID);
                data.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
                data.put("reason_id" , ""+cancelReasonCustomer_response.getMsg().get(position).getReason_id());
                apimanager.execution_method_post(""+Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE , ""+Apis.CancelRentalRide , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void clearNotification() {
        int notification_id = Integer.parseInt(""+sessionManager.getUserDetails().get(SessionManager.NOTIFICSTION_IDS));
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        try{RentalRideLoaderActivity.activity.finish();}catch (Exception e){}
    }

    @Override
    public void onDialogDismiss(String Dialogname) {
    }
}