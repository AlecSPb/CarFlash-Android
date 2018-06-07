package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.TimelySessionService;
import com.carflash.user.accounts.EditProfileActivity;
import com.carflash.user.accounts.LoginActivity;
import com.carflash.user.location.LocationAddress;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.holders.HolderCarType;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.ActiveRidesResponse;
import com.carflash.user.models.CallSupportModel;
import com.carflash.user.models.CarTypeResponseModel;
import com.carflash.user.models.ModelReportIssue;
import com.carflash.user.models.firebasemodels.DriverLocation;
import com.carflash.user.models.NewResultChecker;
import com.carflash.user.models.NewReverseGeocodeResponse;
import com.carflash.user.models.NewRideSyncMainmodel;
import com.carflash.user.others.Constants;
import com.carflash.user.others.DriverPool;
import com.carflash.user.others.LocationSession;
import com.carflash.user.others.MapUtils;

import com.carflash.user.manager.ApiManager;
import com.carflash.user.others.SingletonGson;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideLoadrActivity;
import com.carflash.user.samwork.RideSession;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sam.placer.PlaceHolderView;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class TrialActivity extends BaseActivity implements OnMapReadyCallback, ApiManager.APIFETCHER, HolderCarType.OncategorySelected, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener  {

    /////////////// variables and instances
    String  Key_Reverse_geocode  = "reverse_geocode";

    public static String PICK_LATITUDE = "";
    public static String  PICK_LONGITUDE  = "" ;
    public static String PICK_ADDRESS  = "" ;


    GoogleMap mGoogleMap;
    LocationAddress locationAddress;
    DatabaseReference mDatabaseReference;
    DatabaseReference mDatabaseReference_geofire;
    GeoFire geoFire;
    GeoQuery geoQuery;
    CarTypeResponseModel car_type_response;
     String ride_id, ride_status;

    LocationSession locationSession;

    BroadcastReceiver broadcastReceiver;
    DrawerLayout drawer;
    private static final String TAG = "TrialActivity";

    private String callSupport;
    DBHelper dbHelper;
    ModelReportIssue modelReportIssue ;

    private boolean EnableClick = false ;

    GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
        @Override
        public void onKeyEntered(final String key, final GeoLocation location) {
            try {
                mDatabaseReference.child("" + key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);
                            DriverPool.addDriverInLocalPool(driverLocation);
                            if (driverLocation.driver_online_offline_status.equals("1") && driverLocation.getDriver_car_type_id().equals("" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID))) {
                                MapUtils.setMarker("" + key, location.latitude, location.longitude, Float.parseFloat("" + driverLocation.bearingfactor), mGoogleMap, rideSession);
                            } else {
                                MapUtils.removeMarker("" + key, mGoogleMap);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {

            }
        }

        @Override
        public void onKeyExited(String key) {
            mDatabaseReference.child("" + key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{ DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);
                        DriverPool.removeDriverFromPool(driverLocation);}catch (Exception e){}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            MapUtils.removeMarker("" + key, mGoogleMap);
        }

        @Override
        public void onKeyMoved(final String key, final GeoLocation location) {
            mDatabaseReference.child("" + key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);
                        DriverPool.addDriverInLocalPool(driverLocation);
                        if (driverLocation.driver_online_offline_status.equals("1") && driverLocation.getDriver_car_type_id().equals("" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID))) {
                            MapUtils.setMarker("" + key, location.latitude, location.longitude, Float.parseFloat("" + driverLocation.bearingfactor), mGoogleMap, rideSession);
                        }
                        DriverPool.showallneardriverforallcategories(new LatLng(28.413500, 77.041534), car_type_response);}catch (Exception e){}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        @Override
        public void onGeoQueryReady() {
        }

        @Override
        public void onGeoQueryError(DatabaseError error) {
        }
    };


    ChildEventListener childEventListener = new ChildEventListener() {
        public void onChildAdded(DataSnapshot dataSnapshot, String previousKey) {

        }

        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            try{DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);
                String new_car_id = driverLocation.getDriver_car_type_id();
                String online_offline = driverLocation.getDriver_online_offline_status();
                String login_logout = driverLocation.getDriver_login_logout();


                if (rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID).equals(new_car_id) && online_offline.equals("1") && login_logout.equals("1")) {
//                MapUtils.setMarker(driverLocation, mGoogleMap , ride_session );
                } else {
                    MapUtils.removeMarker(driverLocation.getDriver_id(), mGoogleMap);
                }}catch (Exception e){}
        }

        public void onChildRemoved(DataSnapshot dataSnapshot) {
            System.out.println("ON CHILD REMOVED " + dataSnapshot.getKey());
        }

        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            System.out.println("ON CHILD MOVED  " + dataSnapshot.getKey() + " to UI after " + s);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("DADABASE ERROR  ");
        }
    };


    ////////////// views and layouts
    LinearLayout ll_for_ride;
    PlaceHolderView place_holder;
    TextView    dotted_line, tv_phone_number, tv_profile_email  , pick_lat_txt  , pick_long_txt , pick_address_txt , drop_lat_txt , drop_long_txt , drop_address_txt , drop_identifier_txt  , pick_identifier_txt ;
    ImageView pin_color ,iv_profile_pic;


    //////////////////// managers
    SessionManager sessionManager;
    LanguageManager languageManager;
    RideSession rideSession;
    View dragger_view ;


    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int SELECTED_LAYOUT = 1 ;
    int LOCATION_TYPE = 1 ; // 1 when fetched from api  , 2 when fetched from Google place Api
    int RESUME_FROM = 1 ; // if 1 then activity resume from any activity other than google place activity  else if zero then resume from google place activity .

    String CURRENT_CITY = "";




    String LATERDATE, LATERTIME;

    ApiManager apiManager;
    public static Activity mainActivity;

    LinearLayout drop_layout,pick_layout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Config.DriverRefrence);
        mDatabaseReference_geofire = FirebaseDatabase.getInstance().getReference(Config.GeoFireReference);
        geoFire = new GeoFire(mDatabaseReference_geofire);
        mainActivity = this;
        apiManager = new ApiManager(this , this , this );
        rideSession = new RideSession(this);
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);
        locationSession = new LocationSession(this);
        locationAddress = new LocationAddress();
        setContentView(R.layout.activity_trial);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_profile_email = (TextView) findViewById(R.id.tv_profile_email);
        dotted_line = (TextView) findViewById(R.id.dotted_line);
        pin_color = (ImageView) findViewById(R.id.pin_color);
        ll_for_ride = (LinearLayout) findViewById(R.id.ll_for_ride);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        place_holder = (PlaceHolderView) findViewById(R.id.place_holder);
        pick_lat_txt = (TextView) findViewById(R.id.pick_lat_txt);
        pick_long_txt = (TextView) findViewById(R.id.pick_long_txt);
        pick_address_txt = (TextView) findViewById(R.id.pick_address_txt);
        drop_lat_txt = (TextView) findViewById(R.id.drop_lat_txt);
        drop_long_txt = (TextView) findViewById(R.id.drop_long_txt);
        drop_address_txt = (TextView) findViewById(R.id.drop_address_txt);
        pick_identifier_txt = (TextView) findViewById(R.id.pick_identifier_txt);
        drop_identifier_txt = (TextView) findViewById(R.id.drop_identifier_txt);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        dragger_view = (View)findViewById(R.id.dragger_view);
        startService(new Intent(this, TimeService.class));

        pick_address_txt.setEnabled(true);
        drop_address_txt.setEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag_main);
        mapFragment.getMapAsync(this);
        setListeneronMenu();
        try {
            onNewIntent(getIntent());
        } catch (Exception e) {

        }
        Constants.is_main_Activity_open = true;

        startService(new Intent(this, TimeService.class));
        startService(new Intent(this, TimelySessionService.class));


        apiManager.execution_method_get("" +Config.ApiKeys.KEY_REPORT_ISSUE, ""+Apis.RepostIssueDetails,false , ApiManager.ACTION_DO_NOTHING);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(Constants.rideLoaderActivity == 1){
                    RideLoadrActivity.activity.finish();
                }
                ride_id = Config.Notification_RIDE_ID;
                ride_status = Config.Notification_RIDE_STATUS;

                if (ride_status.equals("3")) {
                    ride_id = Config.Notification_RIDE_ID;
                    ride_status = Config.Notification_RIDE_STATUS;
                    dialogForAcceptRide(ride_id , ride_status , "normal");
                }if(ride_status.equals("11")){
                    ride_id = Config.Notification_RIDE_ID;
                    ride_status = Config.Notification_RIDE_STATUS;
                    dialogForAcceptRide(ride_id , ride_status , "rental");
                }
            }
        };



        dragger_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LOCATION_TYPE = 1 ;
                return false;
            }
        });



        findViewById(R.id.ll_ride_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    drop_address_txt.setEnabled(true);
                    String pick_lat = pick_lat_txt.getText().toString() ;
                    String pick_longg = pick_long_txt.getText().toString() ;
                    String pick_location = pick_address_txt.getText().toString();
                    String drop_lat = drop_lat_txt.getText().toString();
                    String drop_longg = drop_long_txt.getText().toString();
                    String drop_location = drop_address_txt.getText().toString();
                    Log.e("DROP_LOCATION--", drop_location);
                    if(car_type_response.getDetails().size()<=0){
                        Toast.makeText(TrialActivity.this, TrialActivity.this.getResources().getString(R.string.TRIAL_ACTIVITY_no_cars_selected), Toast.LENGTH_SHORT).show();
                    }else{
                        if(Config.isConnectingToInternet(TrialActivity.this)){
                            if(pick_lat_txt.getText().toString().equals(""+drop_lat_txt)  && pick_long_txt.getText().toString().equals(""+drop_long_txt)){
                                Toast.makeText(TrialActivity.this, "Please select different drop location !", Toast.LENGTH_SHORT).show();
                            }if(pick_address_txt.getText().toString().equals(""+drop_address_txt.getText().toString())) {
                                Toast.makeText(TrialActivity.this, "Please select different drop location !", Toast.LENGTH_SHORT).show();
                            }else {
                                if(drop_location.equals("Set your drop point")){
                                    startActivityForResult(new Intent(TrialActivity.this, TrialRideConfirmDialogActivity.class)
                                            .putExtra(Config.IntentKeys.PICK_LATITUDE, "" + pick_lat)
                                            .putExtra(Config.IntentKeys.PICK_LONGITUDE, "" + pick_longg)
                                            .putExtra(Config.IntentKeys.PICK_LOCATION_TXT, "" + pick_location)
                                            .putExtra(Config.IntentKeys.DROP_LATITUDE, "" )
                                            .putExtra(Config.IntentKeys.DROP_LONGITUDE, "" )
                                            .putExtra(Config.IntentKeys.DROP_LOCATIOn_TXT, "" ) , 12);
                                }else{
                                    startActivityForResult(new Intent(TrialActivity.this, TrialRideConfirmDialogActivity.class)
                                            .putExtra(Config.IntentKeys.PICK_LATITUDE, "" + pick_lat)
                                            .putExtra(Config.IntentKeys.PICK_LONGITUDE, "" + pick_longg)
                                            .putExtra(Config.IntentKeys.PICK_LOCATION_TXT, "" + pick_location)
                                            .putExtra(Config.IntentKeys.DROP_LATITUDE, "" + drop_lat)
                                            .putExtra(Config.IntentKeys.DROP_LONGITUDE, "" + drop_longg)
                                            .putExtra(Config.IntentKeys.DROP_LOCATIOn_TXT, "" + drop_location) , 12);
                                }
                            }
                        }else {
                            Toast.makeText(TrialActivity.this, ""+TrialActivity.this.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(TrialActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        findViewById(R.id.ll_ride_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drop_lat = drop_lat_txt.getText().toString() ;
                String drop_location = drop_long_txt.getText().toString();

                if(car_type_response.getDetails().size()<=0){
                    Toast.makeText(TrialActivity.this, TrialActivity.this.getResources().getString(R.string.TRIAL_ACTIVITY_no_cars_selected), Toast.LENGTH_SHORT).show();
                }else{
                    if (drop_lat.equals("") || drop_location == null) {
                        Snackbar.make(drawer, R.string.please_enter_drop_location, Snackbar.LENGTH_SHORT).show();
                    } else {
                        if(Config.isConnectingToInternet(TrialActivity.this)){
                            if(pick_lat_txt.getText().toString().equals(""+drop_lat_txt)  && pick_long_txt.getText().toString().equals(""+drop_long_txt)){
                                Toast.makeText(TrialActivity.this, TrialActivity.this.getResources().getString(R.string.please_select_a_different_drop_location), Toast.LENGTH_SHORT).show();
                            }if(pick_address_txt.getText().toString().equals(""+drop_address_txt.getText().toString())) {
                                Toast.makeText(TrialActivity.this, TrialActivity.this.getResources().getString(R.string.please_select_a_different_drop_location), Toast.LENGTH_SHORT).show();
                            }else {
                                Calendar calendar = Calendar.getInstance();
                                DatePickerDialog dpd = DatePickerDialog.newInstance(TrialActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                dpd.setMinDate(calendar);
                                dpd.setAccentColor(TrialActivity.this.getResources().getColor(R.color.colorPrimary));
                                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                    }
                                });
                                dpd.show(getFragmentManager(), "Date Picker Dialog");
                            }
                        }else {
                            Toast.makeText(TrialActivity.this, ""+TrialActivity.this.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                        }

                }
            }
        });


        findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    drawer.openDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        findViewById(R.id.wallet_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrialActivity.this, ViewWalletMoneyActivity.class));
            }
        });


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SamLocationRequestService(TrialActivity.this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                    @Override
                    public void onLocationUpdate(Location location) {
                        LatLng mlat = new LatLng(location.getLatitude(), location.getLongitude());
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));

                    }
                });
            }
        });


        pick_layout= (LinearLayout) findViewById(R.id.pick_layout);
        pick_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SELECTED_LAYOUT == 1){  // already clicked over pickup layout
                    openGooglePlaceAPiDialoge();
                }
                SELECTED_LAYOUT = 1 ;
                LOCATION_TYPE = 2 ;
                dotted_line.setTextColor(Color.parseColor("#2ecc71"));
                pin_color.setColorFilter(ContextCompat.getColor(TrialActivity.this,R.color.icons_8_muted_green_1));
                if(pick_lat_txt.getText().toString().equals("")){
                    openGooglePlaceAPiDialoge();
                }else {
                    LatLng mlat = new LatLng(Double.parseDouble(""+pick_lat_txt.getText().toString()) , Double.parseDouble(""+pick_long_txt.getText().toString()));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));
                }


                pick_identifier_txt.setTypeface(null , Typeface.BOLD);
                drop_identifier_txt.setTypeface(null , Typeface.NORMAL);
                pick_address_txt.setTextSize(15);
                pick_address_txt.setTextColor(Color.parseColor("#333333"));
                drop_address_txt.setTextSize(12);
                drop_address_txt.setTextColor(Color.parseColor("#bdc3c7"));


            }
        });






        drop_layout= (LinearLayout) findViewById(R.id.drop_layout);
                drop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SELECTED_LAYOUT == 2){  // already clicked over drop layout
                    openGooglePlaceAPiDialoge();
                }
                SELECTED_LAYOUT = 2 ;
                LOCATION_TYPE = 2 ;
                dotted_line.setTextColor(Color.parseColor("#e74c3c"));
                pin_color.setColorFilter(ContextCompat.getColor(TrialActivity.this,R.color.icons_8_muted_red));
                if(drop_lat_txt.getText().toString().equals("")){
                    openGooglePlaceAPiDialoge();
                }else {
                    LatLng mlat = new LatLng(Double.parseDouble(""+drop_lat_txt.getText().toString()) , Double.parseDouble(""+drop_long_txt.getText().toString()));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));
                }

                pick_identifier_txt.setTypeface(null , Typeface.NORMAL);
                drop_identifier_txt.setTypeface(null , Typeface.BOLD);
                pick_address_txt.setTextSize(15);
                pick_address_txt.setTextColor(Color.parseColor("#bdc3c7"));
                drop_address_txt.setTextSize(12);
                drop_address_txt.setTextColor(Color.parseColor("#333333"));
            }
        });
    //    showDemoDialouge();


    }

    private void showUsetrImage() {
        try{if(sessionManager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_FACEBOOK)){
            Glide.with(this).load(""+sessionManager.getUserDetails().get(SessionManager.FACEBOOK_IMAGE)).into(iv_profile_pic);
        }else if (sessionManager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_GOOGLE)){
            Picasso.with(this).load(""+sessionManager.getUserDetails().get(SessionManager.GOOGLE_IMAGE)).placeholder(R.drawable.ic_google).into(iv_profile_pic);
        }else if (sessionManager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_NORAL)){
            Picasso.with(this).load(""+sessionManager.getUserDetails().get(SessionManager.USER_IMAGE)).placeholder(R.drawable.ic_google).into(iv_profile_pic);
        }}catch (Exception e ){}
    }


    public void dialogForAcceptRide(final String ride_id_d , final String ride_status_d  , final String type ) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_acceptride);
        dialog.setCancelable(false);

        LinearLayout ll_ok_accept = (LinearLayout) dialog.findViewById(R.id.ll_ok_accept);
        ll_ok_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("normal")){
                    startActivity(new Intent(TrialActivity.this, TrackRideAactiviySamir.class)
                            .putExtra("ride_id", ride_id_d)
                            .putExtra("ride_status", ride_status_d));
                }else if (type.equals("rental")){
                    startActivity(new Intent(TrialActivity.this, RentalTrackActivity.class)
                            .putExtra("ride_id", ride_id_d)
                            .putExtra("ride_status", ride_status_d));
                }

                dialog.dismiss();
                Config.ShowAcceptDialog_is_visible = false ;
            }
        });
        dialog.show();
        Config.ShowAcceptDialog_is_visible = true ;
    }


    private void showDemoDialouge() {
        if(!sessionManager.getUserDetails().get(SessionManager.UNIQUE_NUMBER).equals("")){
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.setContentView(R.layout.demo_main_dialog);
            dialog.setCancelable(false);

            dialog.findViewById(R.id.demo_ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    @Override
    protected void getConnectivityStatus(boolean val) {
        if (val) {
//            Toast.makeText(TrialActivity.this, "you are connected with internet ", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(TrialActivity.this, "you are out of network ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showUsetrImage ();
        clearNotification();
        registerReceiver(broadcastReceiver, new IntentFilter(Config.BROADCASTNAME));
        if (!sessionManager.isLoggedIn()) {
//            finish();
        }else {
            HashMap<String ,String> data = new HashMap<>();
            data.put("user_id" ,""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
            data.put("device_id" ,""+ FirebaseInstanceId.getInstance().getToken());
            data.put("unique_id" , ""+ Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
            data.put("flag" , "2");
            data.put("language_id" ,""+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
            apiManager.execution_method_post(Config.ApiKeys.KEY_UpdateDevice_ID , ""+Apis.deviceId, data, true,ApiManager.ACTION_SHOW_TOP_BAR);
        }


        ////////////////////////// this is the situation when user open app in one place and lock his phone after opening the app on other place the location should not be the same as previous
        if(drop_address_txt.getText().toString().equals(""+TrialActivity.this.getResources().getString(R.string.TRIAL_ACTIVITY_set_your_drop_point)) && RESUME_FROM == 1 ){
            try{
                LOCATION_TYPE = 1 ;
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng( Double.parseDouble(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LAT)) ,Double.parseDouble(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LONG)))).zoom(Config.MapDefaultZoom).build()));
            }catch (Exception e){
            }
        }


        if (Config.PostedRequest_RENTAL) {
            switch (Config.PostedRentalType) {
                case 1:
                    startActivity(new Intent(TrialActivity.this, RentalRideLoaderActivity.class)
                            .putExtra("" + Config.IntentKeys.Ride_id, "" + Config.rental_ride_now_response.getDetails().getRental_booking_id()));
                    break;
                case 2:
                    break;
            }
            Config.PostedRequest_RENTAL = false;
        }
        tv_phone_number.setText("" + sessionManager.getUserDetails().get(SessionManager.USER_PHONE));
        tv_profile_email.setText("" + sessionManager.getEmail());
        Constants.is_main_Activity_open = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Constants.is_main_Activity_open = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        DriverPool.Driver_pool.clear();
        Constants.is_main_Activity_open = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.is_main_Activity_open = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Constants.is_main_Activity_open = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Constants.is_main_Activity_open = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ride_status = extras.getString("ride_status");
            ride_id = extras.getString("ride_id");
            if (ride_status.equals("3")) {
                Toast.makeText(mainActivity, "Accepted by driver", Toast.LENGTH_SHORT).show();
            }

            if (ride_status.equals("5")) {
                startActivity(new Intent(this, TripHistoryActivity.class));
            } else if (ride_status.equals("6")) {
                startActivity(new Intent(this, TripHistoryActivity.class));
            } else if (ride_status.equals("7")) {
                startActivity(new Intent(this, PaymentActivity.class)
                        .putExtra("ride_id", ride_id)
                        .putExtra("ride_status", ride_status));
            } else if (ride_status.equals("9")) {
                startActivity(new Intent(this, TripHistoryActivity.class));
            }
        }
    }


    private void setListeneronMenu() {

        findViewById(R.id.menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        findViewById(R.id.book_a_ride).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

        findViewById(R.id.profile_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TrialActivity.this, EditProfileActivity.class) ,3);
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.trips_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrialActivity.this, TripHistoryActivity.class));
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.rate_card_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrialActivity.this, RateCardActivity.class)
                        .putExtra("city_id", "3")
                        .putExtra("city_name", "Dummy City")
                        .putExtra("car_type_id", rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID))
                        .putExtra("car_type_name", rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_NAME)));

                drawer.closeDrawers();
            }
        });


        findViewById(R.id.report_issue_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", ""+modelReportIssue.getDeatils(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + TrialActivity.this.getResources().getString(R.string.report_issue));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "" + TrialActivity.this.getResources().getString(R.string.send_email)));
                emailIntent.setType("text/plain");
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.call_support_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + callSupport.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(TrialActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                drawer.closeDrawers();
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String , String > data  = new HashMap<String, String>();
                data.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
                data.put("unique_id" , ""+ Settings.Secure.getString(TrialActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                apiManager.execution_method_post(""+com.carflash.user.samwork.Config.ApiKeys.KEY_LOGOUT , ""+ Apis.URL_LOGOUT , data , true,ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });



        findViewById(R.id.terms_and_condition_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrialActivity.this, TermsAndConditionActivity.class));
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.about_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrialActivity.this, AboutActivity.class));
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.payment_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrialActivity.this, CreditCardPaymentActivity.class));
                //.putExtra("From", "Navigation"))
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.customer_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrialActivity.this, CustomerSupportActivity.class));
                drawer.closeDrawers();
            }
        });



        findViewById(R.id.notification_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrialActivity.this , NotificationActivity.class));
                drawer.closeDrawers();
            }
        });


        findViewById(R.id.language_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String [] languages = new String[]{"English", "Spanish"} ;
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(TrialActivity.this);
                builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                builder.setTitle(R.string.select_language);
                builder.setItems(languages , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        switch (languages[index]){
                            case "English":
                                sessionManager.setLanguage("en");
                                break ;
                            case "Spanish":
                                sessionManager.setLanguage("es");
                                break ;
                        }
                        dialogInterface.dismiss();
                        startActivity(new Intent(TrialActivity.this , SplashActivity.class));
                        finish();
                    }
                });
                builder.show();
            }
        });
    }

   private void openGooglePlaceAPiDialoge() {

      // if(!EnableClick)
       if(!EnableClick ){
           try {

               AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                       .setTypeFilter(Place.TYPE_COUNTRY)
                       .setCountry("MX")
                       .build();


               Intent intent =new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(autocompleteFilter).build(TrialActivity.this);
               startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
           } catch (GooglePlayServicesRepairableException e) {
               // TODO: Handle the error.
           } catch (GooglePlayServicesNotAvailableException e) {
               // TODO: Handle the error.
           }
       }

       EnableClick = true ;
   }


    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapUtils.setMapTheme(this , googleMap);
        mGoogleMap.clear();
        mDatabaseReference.addChildEventListener(childEventListener);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        try{
            LatLng mlat = new LatLng(Double.parseDouble(""+getIntent().getExtras().getString(""+Config.IntentKeys.PICK_LATITUDE)) , Double.parseDouble(""+getIntent().getExtras().getString(Config.IntentKeys.PICK_LONGITUDE)));
//            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlat, Config.MapDefaultZoom));
        }catch (Exception e ){

        }




//        HashMap<String , String> data4  = new HashMap<String, String>();
//        data4.put("city" , ""+getIntent().getExtras().getString(""+Config.IntentKeys.CITY_NAME));
//        data4.put("user_lat" , ""+getIntent().getExtras().getString(Config.IntentKeys.PICK_LATITUDE));
//        data4.put("user_long" , ""+getIntent().getExtras().getString(Config.IntentKeys.PICK_LONGITUDE));
//        data4.put("language_id" , ""+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
//        apiManager.execution_method_post(Config.ApiKeys.Key_View_Cars,""+Apis.viewCars , data4);



        HashMap<String , String > data_activ_rides = new HashMap<>();
        data_activ_rides.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
        apiManager.execution_method_post(""+Config.ApiKeys.KEY_ACTIVES_RIDES,Apis.ActivesRides,data_activ_rides,false,ApiManager.ACTION_SHOW_TOAST);







        apiManager.execution_method_get(Config.ApiKeys.KEY_CallSupport , ""+Apis.callSupport  , false,ApiManager.ACTION_SHOW_TOP_BAR);



        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                MapUtils.slideiewToBottom(ll_for_ride);
            }
        });




        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                RESUME_FROM = 1 ;
                if(LOCATION_TYPE == 1){
                    apiManager.execution_method_get(""+Key_Reverse_geocode , ""+Apis.getAddress+mGoogleMap.getCameraPosition().target.latitude+"&longitude="+mGoogleMap.getCameraPosition().target.longitude , false,ApiManager.ACTION_SHOW_TOP_BAR);
                }
                MapUtils.showViewtooriginalPosition(ll_for_ride);
                geoQuery = geoFire.queryAtLocation(new GeoLocation(mGoogleMap.getCameraPosition().target.latitude, mGoogleMap.getCameraPosition().target.longitude), 3.0);
                MapUtils.removeAllMarkersThatNotSatisfiesCondition(new LatLng(mGoogleMap.getCameraPosition().target.latitude , mGoogleMap.getCameraPosition().target.longitude ));
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryEventListener(geoQueryEventListener);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        drop_layout.setEnabled(true);
        pick_layout.setEnabled(true);

        try {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                EnableClick = false;
                RESUME_FROM = 0 ;
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.d("*#*# getAddress" , ""+place.getAddress());
                    Log.d("*#*# getAttributions", ""+place.getAttributions());
                    Log.d("*#*# getLocale", ""+place.getLocale());
                    Log.d("*#*# getname", ""+place.getName());
                    Log.d("*#*# getId", ""+place.getId());
                    Log.d("*#*# geWebsiteURI", ""+place.getWebsiteUri());
                    LOCATION_TYPE = 2  ;
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude , place.getLatLng().longitude)).zoom(Config.MapDefaultZoom).build()));
                    if(SELECTED_LAYOUT == 1){
                        pick_address_txt.setText(""+place.getName());
                        pick_lat_txt.setText(""+place.getLatLng().latitude);
                        pick_long_txt.setText(""+place.getLatLng().longitude);
                        PICK_LATITUDE = ""+place.getLatLng().latitude;
                        PICK_LONGITUDE = ""+place.getLatLng().longitude ;
                        PICK_ADDRESS =""+ place.getName() ;
                    }else if (SELECTED_LAYOUT == 2){
                        drop_address_txt.setText(""+place.getName());
                        drop_lat_txt.setText(""+place.getLatLng().latitude);
                        drop_long_txt.setText(""+place.getLatLng().longitude);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            apiManager.execution_method_get("city_name_from_location" , ""+Apis.getAddress+mGoogleMap.getCameraPosition().target.latitude+"&longitude="+mGoogleMap.getCameraPosition().target.longitude , false,ApiManager.ACTION_SHOW_TOP_BAR);
                        }
                    }, 1200);


                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i("*****", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }
            }
        }catch (Exception e){
            Toast.makeText(mainActivity, "TrialActivity OnActivity rResult  "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

            if (resultCode == 3){
            try {
                JSONObject mainObject = new JSONObject(data.getStringExtra("MESSAGE"));
                if(mainObject.getString("result").equals("1")){
                    sessionManager.logoutUser();
                    startActivityForResult(new Intent(TrialActivity.this, LoginActivity.class).putExtra("config_base_url", Apis.Loginbaseurl).putExtra("cityLocation", "Dummy City"), 2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            if(resultCode == 2){
            try {
                JSONObject mainObject = new JSONObject(data.getStringExtra("MESSAGE"));
                if(!mainObject.getString("result").equals("1")){
                    finish();
                }else {
                //    showDemoDialouge();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == 12){
            if(resultCode==RESULT_OK){
                try {
                    JSONObject mainObject = new JSONObject(data.getStringExtra("MESSAGE"));
                    if(mainObject.getString("result").equals("1")){  /// refreash location in pick and drop
                        SELECTED_LAYOUT = 1 ;
                        drop_lat_txt.setText("");
                        drop_long_txt.setText("");
                        pick_address_txt.setText(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LOCATION_TEXT));
                        pick_lat_txt.setText(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LAT));
                        pick_long_txt.setText(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LONG));
                        drop_address_txt.setText(""+this.getResources().getString(R.string.TRIAL_ACTIVITY_set_your_drop_point));
                        pick_identifier_txt.setTypeface(null , Typeface.BOLD);
                        drop_identifier_txt.setTypeface(null , Typeface.NORMAL);
                        pick_address_txt.setTextSize(15);
                        pick_address_txt.setTextColor(Color.parseColor("#333333"));
                        drop_address_txt.setTextSize(12);
                        drop_address_txt.setTextColor(Color.parseColor("#bdc3c7"));
                        LatLng mlat = new LatLng(Double.parseDouble(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LAT)) ,Double.parseDouble(""+locationSession.getLocationDetails().get(LocationSession.KEY_CURRENT_LONG))) ;
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mlat).zoom(Config.MapDefaultZoom).build()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try{
            if(APINAME.equals(Config.ApiKeys.KEY_UpdateDevice_ID)){}
            if(APINAME.equals(Config.ApiKeys.KEY_CallSupport)) {
                CallSupportModel call_response = null;try{call_response = SingletonGson.getInstance().fromJson(""+script, CallSupportModel.class);}catch (Exception e){}
                if (call_response != null) {callSupport = call_response.getDetails().getDescription();}
            }
            if(APINAME.equals(Config.ApiKeys.Key_View_Cars)){
                try {place_holder.removeView(0);}catch (Exception e){}
                try{car_type_response  = SingletonGson.getInstance().fromJson(""+script , CarTypeResponseModel.class);}catch (Exception e){}
                place_holder.addView(new HolderCarType(this , car_type_response , this,rideSession , geoQuery , geoQueryEventListener , this ));
                sessionManager.setCurrencyCode(""+car_type_response.getCurrency_iso_code() , ""+car_type_response.getCurrency_unicode());
            }
            if(APINAME.equals("user_sync")){
                NewResultChecker rs = null;
                try{rs = SingletonGson.getInstance().fromJson(""+script , NewResultChecker.class);}catch (Exception e){}
                if(rs.getResult() == 1){
                    NewRideSyncMainmodel ride_sync_response   = null;
                    try{ride_sync_response   = SingletonGson.getInstance().fromJson(""+script , NewRideSyncMainmodel.class);}catch (Exception e){}

                }
            }
            if(APINAME.equals(""+Key_Reverse_geocode)){
                NewReverseGeocodeResponse response = null ;
                try{response  = SingletonGson.getInstance().fromJson(""+script , NewReverseGeocodeResponse.class);}catch (Exception e){}
                if(response != null){
                    if(SELECTED_LAYOUT == 1){
                        pick_address_txt.setText(""+response.getDetails());
                        pick_lat_txt.setText(""+mGoogleMap.getCameraPosition().target.latitude);
                        pick_long_txt.setText(""+mGoogleMap.getCameraPosition().target.longitude);
                        PICK_LATITUDE = ""+mGoogleMap.getCameraPosition().target.latitude ;
                        PICK_LONGITUDE = ""+mGoogleMap.getCameraPosition().target.longitude ;
                        PICK_ADDRESS = ""+response.getDetails();
                    }else if (SELECTED_LAYOUT == 2){
                        drop_address_txt.setText(""+response.getDetails());
                        drop_lat_txt.setText(""+mGoogleMap.getCameraPosition().target.latitude);
                        drop_long_txt.setText(""+mGoogleMap.getCameraPosition().target.longitude);
                    }

                    getcarsAccordingToCity(response.getCity_name());
                }
            }
            if(APINAME.equals("city_name_from_location")){
                NewReverseGeocodeResponse response  = null ;
                try{ response  = SingletonGson.getInstance().fromJson(""+script , NewReverseGeocodeResponse.class);}catch (Exception e){}
                if(response != null){
                    getcarsAccordingToCity(response.getCity_name());
                }
            }
            if(APINAME.equals(""+Config.ApiKeys.KEY_ACTIVES_RIDES)){
                ActiveRidesResponse activeRidesResponse = SingletonGson.getInstance().fromJson(""+script , ActiveRidesResponse.class);
                // storing the session into database
                if(activeRidesResponse.getDetails().get(0).getRide_mode().equals("1")){// that is normal ride
                    dbHelper.insertRow(activeRidesResponse.getDetails().get(0).getNormal_Ride().getRide_id() , activeRidesResponse.getDetails().get(0).getNormal_Ride().getRide_status());
                }else if (activeRidesResponse.getDetails().get(0).getRide_mode().equals("2")){ // that is of rental type
                    dbHelper.insertRow(activeRidesResponse.getDetails().get(0).getRental_Ride().getRental_booking_id() , activeRidesResponse.getDetails().get(0).getRental_Ride().getBooking_status());
                }



                // opening the screen accordingly
                if(activeRidesResponse.getDetails().get(0).getRide_mode().equals("1")){
                    startRideAccordingLy(activeRidesResponse.getDetails().get(0).getNormal_Ride().getDone_ride_id() ,activeRidesResponse.getDetails().get(0).getNormal_Ride().getRide_id(), activeRidesResponse.getDetails().get(0).getNormal_Ride().getRide_status());
                } if(activeRidesResponse.getDetails().get(0).getRide_mode().equals("2")){
                    startRideAccordingLy("NA" , ""+activeRidesResponse.getDetails().get(0).getRental_Ride().getRental_booking_id() , activeRidesResponse.getDetails().get(0).getRental_Ride().getBooking_status());
                }
            }
            if(APINAME.equals(""+Config.ApiKeys.KEY_REPORT_ISSUE)){
                modelReportIssue = SingletonGson.getInstance().fromJson("" +script, ModelReportIssue.class);
            }
            if(APINAME.equals(""+com.carflash.user.samwork.Config.ApiKeys.KEY_LOGOUT)){
                sessionManager.logoutUser();
                finilalizeActivity();
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onFetchResultZero(String s) {

    }

    public void startRideAccordingLy(String done_ride_id , String ride_id, String ride_status){
        Log.d("**ride_id_trial_activity===", ride_id);
        if(ride_status.equals("3") ||  ride_status.equals("5") || ride_status.equals("6") ){
            startActivity(new Intent(TrialActivity.this , TrackRideAactiviySamir.class).putExtra("ride_id" , ""+ride_id).putExtra("ride_status" , ""+ride_status));
        }if (ride_status.equals("7")){ // this means that ride ois completed but payment is not yet done
            startActivity(new Intent(TrialActivity.this , PaymentActivity.class).putExtra("DONE_RIDE_ID" , ""+done_ride_id).putExtra("RIDE_ID" , ""+ride_id));
        }if(ride_status.equals("11") ||  ride_status.equals("12") || ride_status.equals("13") ){
            startActivity(new Intent(TrialActivity.this , RentalTrackActivity.class).putExtra("ride_id" , ""+ride_id).putExtra("ride_status" , ""+ride_status));
        }if(ride_status.equals("16")){ // ride is ended by driver but payment is not yet done
            startActivity(new Intent(TrialActivity.this , RentalReceiptActivity.class).putExtra("ride_id" , ""+ride_id));
        }
    }


    private void getcarsAccordingToCity(String City_name) {
        try{
            Log.d("****"+TAG , "Current City name = "+City_name);
            if(CURRENT_CITY.equals("") || CURRENT_CITY == null){
                HashMap<String , String> data4  = new HashMap<String, String>();
                data4.put("city_name" , ""+City_name);
                data4.put("latitude" , ""+mGoogleMap.getCameraPosition().target.latitude);
                data4.put("longitude" , ""+mGoogleMap.getCameraPosition().target.longitude);
                if(SELECTED_LAYOUT == 1){
                    apiManager.execution_method_post(Config.ApiKeys.Key_View_Cars,""+Apis.NewRestApiBaseUrl+Apis.car_type , data4, true,ApiManager.ACTION_SHOW_DIALOG);
                    CURRENT_CITY = City_name;
                }


            }if(!CURRENT_CITY.equals(""+City_name)){
                HashMap<String , String> data4  = new HashMap<String, String>();
                data4.put("city_name" , ""+City_name);
                data4.put("latitude" , ""+mGoogleMap.getCameraPosition().target.latitude);
                data4.put("longitude" , ""+mGoogleMap.getCameraPosition().target.longitude);
                if(SELECTED_LAYOUT == 1){
                    apiManager.execution_method_post(Config.ApiKeys.Key_View_Cars,""+Apis.NewRestApiBaseUrl+Apis.car_type , data4, true,ApiManager.ACTION_SHOW_DIALOG);
                    CURRENT_CITY = City_name;
                }
            }else{
//                    Toast.makeText(is_main_Activity_open, "No need to run API ", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }

    }


    public void resolveGeoQueryAccordingtoLocation (){
        MapUtils.removeAllMarkers();
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(geoQueryEventListener);
    }



    @Override
    public void oncategorySelected(String category_id) {
        resolveGeoQueryAccordingtoLocation();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        LATERDATE = year + "/" + month + "/" + dayOfMonth;

        Log.d("**LATERDATE===", LATERDATE);

        Calendar calendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);


        if(Calendar.getInstance().getTime().getDate() == dayOfMonth){
            Date currentTime = Calendar.getInstance().getTime();
            Timepoint timepoint = new Timepoint(currentTime.getHours()+2 , currentTime.getMinutes() , currentTime.getSeconds());
            tpd.setMinTime(timepoint);
        }

        tpd.setAccentColor(this.getResources().getColor(R.color.colorPrimary));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        tpd.show(getFragmentManager(), "Time Picker Dialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        LATERTIME = hourString + ":" + minuteString;

        startActivity(new Intent(TrialActivity.this  , TrialRidelaterDialogActivity.class)
                .putExtra(Config.IntentKeys.PICK_LATITUDE , ""+pick_lat_txt.getText().toString())
                .putExtra(Config.IntentKeys.PICK_LONGITUDE ,""+ pick_long_txt.getText().toString() )
                .putExtra(Config.IntentKeys.PICK_LOCATION_TXT , ""+  pick_address_txt.getText().toString())
                .putExtra(Config.IntentKeys.DROP_LATITUDE , ""+ drop_lat_txt.getText().toString())
                .putExtra(Config.IntentKeys.DROP_LONGITUDE , ""+ drop_long_txt.getText().toString())
                .putExtra(Config.IntentKeys.DROP_LOCATIOn_TXT , ""+ drop_address_txt.getText().toString())
                .putExtra(Config.IntentKeys.TIME , ""+LATERTIME)
                .putExtra(Config.IntentKeys.DATE, ""+LATERDATE));
    }


    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    private void finilalizeActivity() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        JSONObject no_data = new JSONObject();
        try {
            if(sessionManager.isLoggedIn()){
                no_data.put("result" , "1");
            }else{
                no_data.put("result" , "0");
            }

            no_data.put("response","EditProfile  Activity Cancelled.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent=new Intent();
        intent.putExtra("MESSAGE",""+no_data);
        setResult(3,intent);
        finish();
        startActivity(new Intent(this , LoginActivity.class));
    }

}