package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.StatusSession.RideSessionEvent;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.adapter.PaymentAdapter;
import com.carflash.user.drawroutemaps.DrawRouteMaps;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.DistanceMatrixResponseModel;

import com.carflash.user.models.NewRideEstimateModel;
import com.carflash.user.models.RideNow;
import com.carflash.user.models.ViewPaymentOption;
import com.carflash.user.others.MapUtils;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samir.customviews.TypeFaceTextMonixRegular;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideLoadrActivity;
import com.carflash.user.samwork.RideSession;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrialRideConfirmDialogActivity extends FragmentActivity implements OnMapReadyCallback, ApiManager.APIFETCHER {


    GoogleMap mGooglemap;
    RideSession rideSession;
    SessionManager sessionManager;
    ApiManager apiManager;
    LanguageManager languageManager;
    @Bind(R.id.est_travel_time_layout)
    LinearLayout estTravelTimeLayout;
    @Bind(R.id.fare_layout)
    LinearLayout fareLayout;

    String paystack_id, paystack_authCode, paystack_last4;

    public static Activity activity;

    GsonBuilder builder;
    Gson gson;
     RideNow rideNow;

    DistanceMatrixResponseModel distance_response;
    ViewPaymentOption viewPaymentOption;

    String ride_id, ride_status;


    Handler mhandler;
    Runnable mRunnable;


    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    private String PAYMENT_ID;
    private String SELECTED_PICK_LATITUDE;
    private String SELECTED_PICK_LONGITUDE;
    private String SELECTED_PICK_LOCATION = "";
    private String SELECTED_DROP_LATITUDE;
    private String SELECTED_DROP_LONGITUDE;
    private String SELECTED_DROP_LOCATION = "";
    private String COUPON_CODE = "";

    private String CREDIT_CARD_ID = "";


    @Bind(R.id.source_txt)
    TextView source_txt;
    @Bind(R.id.destination_txt)
    TextView destination_txt;
    @Bind(R.id.est_time_txt)
    TextView est_time_txt;
    @Bind(R.id.est_amount_txt)
    TextView est_amount_txt;
    @Bind(R.id.car_image)
    ImageView car_image;
    @Bind(R.id.payment_txt)
    TextView payment_txt;
    @Bind(R.id.coupon_tx)
    TextView coupon_tx;
    @Bind(R.id.root)
    CardView root;

    @Bind(R.id.map_progress)
    TypeFaceTextMonixRegular map_progress;

    DBHelper dbHelper ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        rideSession = new RideSession(this);
        sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);
        apiManager = new ApiManager(this , this , this );
        mhandler = new Handler();
        builder = new GsonBuilder();
        gson = builder.create();
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_trial_ride_confirm_dialog);
        ButterKnife.bind(this);
        try {
            SELECTED_PICK_LATITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LATITUDE);
            SELECTED_PICK_LONGITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LONGITUDE);
            SELECTED_PICK_LOCATION = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LOCATION_TXT);
            SELECTED_DROP_LATITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LATITUDE);
            SELECTED_DROP_LONGITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LONGITUDE);
            SELECTED_DROP_LOCATION = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LOCATIOn_TXT);
        } catch (Exception e) {

        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        root.setMinimumWidth(width);


        HashMap<String, String> data3 = new HashMap<>();
        data3.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
        data3.put("user_id", sessionManager.getUserDetails().get(SessionManager.USER_ID));

        Log.d("**LANGUUAEGE_ID***", languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));

        apiManager.execution_method_post(Config.ApiKeys.KEY_PaymentOption, "" + Apis.viewPaymentOption, data3, true,ApiManager.ACTION_SHOW_TOAST);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag_main);
        mapFragment.getMapAsync(this);


        source_txt.setText("" + SELECTED_PICK_LOCATION);
        destination_txt.setText("" + SELECTED_DROP_LOCATION);
        Glide.with(this).load("" + Apis.imageDomain + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_IMAGE)).into(car_image);


        findViewById(R.id.change_destination_lout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openGooglePlaceAPiDialoge();
            }
        });

        findViewById(R.id.trialRide_confirm_lout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    HashMap<String, String> data5 = new HashMap<String, String>();
                    data5.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
                    data5.put("coupon_code", "" + COUPON_CODE);
                    data5.put("pickup_lat", "" + SELECTED_PICK_LATITUDE);
                    data5.put("pickup_long", "" + SELECTED_PICK_LONGITUDE);
                    data5.put("pickup_location", "" + SELECTED_PICK_LOCATION);
                    data5.put("drop_lat", "" + SELECTED_DROP_LATITUDE);
                    data5.put("drop_long", "" + SELECTED_DROP_LONGITUDE);
                    data5.put("drop_location", "" + SELECTED_DROP_LOCATION);
                    data5.put("car_type_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID));
                    data5.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                    data5.put("payment_option_id", "" + PAYMENT_ID);
                    data5.put("card_id", "" + CREDIT_CARD_ID);
                    apiManager.execution_method_post(Config.ApiKeys.Key_Rice_Now, Apis.rideNow, data5, true, ApiManager.ACTION_SHOW_TOAST);
                }
        });


        findViewById(R.id.ll_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TrialRideConfirmDialogActivity.this, CouponActivity.class), 101);
                overridePendingTransition(R.anim.animation3, R.anim.animation4);
            }
        });


        findViewById(R.id.payment_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForSelectPayment();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RideSessionEvent event){
        if(event.getRide_id().equals(""+rideNow.getDetails().getRide_id())){
            ride_id = event.getRide_id();
            ride_status = event.getRide_status();
            switch (event.getRide_status()){
                case Config.Status.NORMAL_ACCEPTED_BY_DRIVER:
                        dialogForAcceptRide();
                    try{
                        RideLoadrActivity.activity.finish();
                    }catch (Exception e){

                    }
                    break;

                case Config.Status.NORMAL_RIDE_STARTED:
                    dialogForAcceptRide();
                    try{
                        RideLoadrActivity.activity.finish();
                    }catch (Exception e){

                    }
                    break;


                case Config.Status.NORMAL_ARRIVED:
                    dialogForAcceptRide();
                    try{
                        RideLoadrActivity.activity.finish();
                    }catch (Exception e){

                    }
                    break;


                case Config.Status.NORMAL_RIDE_END:
                    dialogForAcceptRide();
                    try{
                        RideLoadrActivity.activity.finish();
                    }catch (Exception e){

                    }
                    break;
                case Config.Status.NORMAL_REJECTED_BY_DRIVER:
                    if (!Config.ShowReject_is_visible) {
                        dialogForRejectRide();
                    }
                    try{
                        RideLoadrActivity.activity.finish();
                    }catch (Exception e){

                    }
                    break;
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;
        MapUtils.setMapTheme(this, mGooglemap);
        mGooglemap.clear();

        try{if (SELECTED_DROP_LONGITUDE.equals("")) {
            LatLng origin = new LatLng(Double.parseDouble("" + SELECTED_PICK_LATITUDE), Double.parseDouble("" + SELECTED_PICK_LONGITUDE));
            MapUtils.setDestinationMarkerForPickPoint(this, mGooglemap, origin, "" + SELECTED_PICK_LOCATION);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, Config.MapDefaultZoom));
            estTravelTimeLayout.setVisibility(View.INVISIBLE);
            fareLayout.setVisibility(View.INVISIBLE);
            map_progress.setVisibility(View.GONE);
        } else {
            estTravelTimeLayout.setVisibility(View.VISIBLE);
            fareLayout.setVisibility(View.VISIBLE);
            LatLng origin = new LatLng(Double.parseDouble("" + SELECTED_PICK_LATITUDE), Double.parseDouble("" + SELECTED_PICK_LONGITUDE));
            LatLng destination = new LatLng(Double.parseDouble("" + SELECTED_DROP_LATITUDE), Double.parseDouble("" + SELECTED_DROP_LONGITUDE));
            MapUtils.setDestinationMarkerForPickPoint(this, mGooglemap, origin, "Customer Location ");
            MapUtils.setDestinationMarkerForDropPoint(this, mGooglemap, destination, "Customer Location ");
            try {
                DrawRouteMaps.getInstance(this, this, sessionManager, map_progress , true).draw(origin, destination, mGooglemap);
            } catch (Exception e) {
            }
            apiManager.execution_method_without_result_check(Config.ApiKeys.Key_Google_Distance_matrix, MapUtils.getDistancematrixUrl(origin, destination, this ),false,ApiManager.ACTION_SHOW_TOAST);
        }}catch (Exception e){}
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try{if (APINAME.equals(Config.ApiKeys.Key_Google_Distance_matrix)) {
            distance_response = gson.fromJson("" + script, DistanceMatrixResponseModel.class);
            if (!distance_response.getRows().get(0).getElements().get(0).getStatus().equals("ZERO_RESULTS")) {
                est_time_txt.setText("" + distance_response.getRows().get(0).getElements().get(0).getDuration().getText());
                HashMap<String, String> data = new HashMap();
                data.put("distance", "" + distance_response.getRows().get(0).getElements().get(0).getDistance().getValue());
                data.put("city_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_CITY_ID));
                data.put("car_type_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID));
                data.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                data.put("pickup_lat", "" + SELECTED_PICK_LATITUDE);
                data.put("ride_time",""+distance_response.getRows().get(0).getElements().get(0).getDuration().getValue());
                data.put("pickup_long", "" + SELECTED_PICK_LONGITUDE);
                apiManager.execution_method_post(Config.ApiKeys.Key_Estimate, "" + Apis.rideEstimate, data, true,ApiManager.ACTION_SHOW_TOAST);
            } else {
                Toast.makeText(this, R.string.you_have_requested_unauthorised_route, Toast.LENGTH_SHORT).show();
                finilalizeActivity("0");
            }

        }
            if (APINAME.equals(Config.ApiKeys.Key_Estimate)) {
                NewRideEstimateModel ride_estimate_response = gson.fromJson("", NewRideEstimateModel.class);
                ride_estimate_response = gson.fromJson("" + script, NewRideEstimateModel.class);
                if (ride_estimate_response.getResult() == 1) {
                    est_amount_txt.setText(""+sessionManager.getCurrencyCode() + ride_estimate_response.getMsg());
//                    est_time_txt.setText("" + ride_estimate_response.getEstimatetime());
                }
            }
            if (APINAME.equals(Config.ApiKeys.KEY_PaymentOption)) {
                viewPaymentOption = gson.fromJson("" + script, ViewPaymentOption.class);
                payment_txt.setText("" + viewPaymentOption.getMsg().get(0).getPayment_option_name());
                PAYMENT_ID = viewPaymentOption.getMsg().get(0).getPayment_option_id();
            }
            if (APINAME.equals(Config.ApiKeys.Key_Rice_Now)) {
                ResultCheckForBookRide rs = gson.fromJson("" + script, ResultCheckForBookRide.class);
                if (rs.getResult() == 1) {

                    rideNow = gson.fromJson("" + script, RideNow.class);
                    if (rideNow.getResult() == 1) {
                        FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+rideNow.getDetails().getRide_id()).setValue(new RideSessionEvent(""+rideNow.getDetails().getRide_id() , ""+rideNow.getDetails().getRide_status() , "Not yet generated" , "0",Config.cities));

                        dbHelper.clearTable();
                        dbHelper.insertRow(rideNow.getDetails().getRide_id() , rideNow.getDetails().getRide_status());
                        startActivity(new Intent(TrialRideConfirmDialogActivity.this, RideLoadrActivity.class).putExtra(Config.IntentKeys.Ride_id, "" + rideNow.getDetails().getRide_id()));
                    } else {
                        Toast.makeText(activity, "" + rideNow.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogForNoDriverAvailable(rs.getMsg());
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

    private void openGooglePlaceAPiDialoge() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(TrialRideConfirmDialogActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    public void dialogForSelectPayment() {
        final Dialog dialog = new Dialog(TrialRideConfirmDialogActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_option);

        ListView lv_payment_option = (ListView) dialog.findViewById(R.id.lv_payment_option);
        lv_payment_option.setAdapter(new PaymentAdapter(TrialRideConfirmDialogActivity.this, viewPaymentOption));

        lv_payment_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (viewPaymentOption.getMsg().get(position).getPayment_option_id().equals("3")) {

                    PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPayment_option_id();
                    startActivityForResult(new Intent(TrialRideConfirmDialogActivity.this, CreditCardPaymentActivity.class).putExtra("payment", "normal"), 103);

                } else {
                    payment_txt.setText("" + viewPaymentOption.getMsg().get(position).getPayment_option_name());
                    PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPayment_option_id();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

//booking confirm dialog
    public void dialogForAcceptRide()  {
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
                if(new DBHelper(TrialRideConfirmDialogActivity.this).getAllRideId().contains(""+ride_id)){
                    startActivity(new Intent(TrialRideConfirmDialogActivity.this, TrackRideAactiviySamir.class)
                            .putExtra("ride_id", ride_id)
                            .putExtra("ride_status", ride_status));
                    dialog.dismiss();
                    finilalizeActivity("1");
                    Config.ShowAcceptDialog_is_visible = false;
                }else {
                    finilalizeActivity("0");
                }

            }
        });
        dialog.show();
        Config.ShowAcceptDialog_is_visible = true;
    }


    public void dialogForNoDriverAvailable(String msg) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_no_driver_available);
        dialog.setCancelable(false);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("" + msg);

        dialog.findViewById(R.id.ll_ok_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finilalizeActivity("0");
            }
        });

        dialog.show();
    }


    public void dialogForRejectRide() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_rejectride);
        dialog.setCancelable(false);

        LinearLayout ll_ok_reject = (LinearLayout) dialog.findViewById(R.id.ll_ok_reject);
        ll_ok_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Config.ShowReject_is_visible = false;
                startActivity(new Intent(TrialRideConfirmDialogActivity.this, TripHistoryActivity.class));
                finilalizeActivity("0");
            }
        });
        dialog.show();
        Config.ShowReject_is_visible = true;
    }


    @Override
    protected void onActivityResult(int req, int res, Intent data) {

        super.onActivityResult(req, res, data);
        if (res == Activity.RESULT_OK) {
            try {
                if (req == 101) {
                    COUPON_CODE = data.getStringExtra("coupon_code");
                    coupon_tx.setText("Coupon Applied \n" + data.getStringExtra("coupon_code"));
                }
                if (req == 103) {
                    String ID=data.getStringExtra("CREDIT_ID");
                    payment_txt.setText("Credit Card");
                    PAYMENT_ID = "3";
                    CREDIT_CARD_ID = ID;
                }
            } catch (Exception e) {
                Log.e("res ", e.toString());
            }

        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        try {EventBus.getDefault().register(this);}catch (Exception e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(mRunnable);
        try{EventBus.getDefault().unregister(this);}catch (Exception e){}
    }


    private class ResultCheckForBookRide {

        /**
         * result : 0
         * msg : No Driver Available
         */

        private int result;
        private String msg;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    private void finilalizeActivity(String result) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        JSONObject no_data = new JSONObject();
        try {
            no_data.put("result", "" + result);

            no_data.put("response", "Login  Activity Cancelled.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "" + no_data);
        setResult(12, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finilalizeActivity("0");
    }
}



