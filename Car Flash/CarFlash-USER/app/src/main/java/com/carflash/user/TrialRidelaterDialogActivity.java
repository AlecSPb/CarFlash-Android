package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.adapter.PaymentAdapter;
import com.carflash.user.drawroutemaps.DrawRouteMaps;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.DistanceMatrixResponseModel;
import com.carflash.user.models.RideEstimate;
import com.carflash.user.models.RideNow;
import com.carflash.user.models.ViewPaymentOption;
import com.carflash.user.others.MapUtils;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samir.customviews.TypeFaceTextMonixRegular;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideSession;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrialRidelaterDialogActivity extends FragmentActivity implements OnMapReadyCallback, ApiManager.APIFETCHER, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    GoogleMap mGooglemap;
    RideSession rideSession;
    ApiManager apiManager;
    LanguageManager languageManager;
    SessionManager sessionManager;
    GsonBuilder builder;
    Gson gson;
    DistanceMatrixResponseModel distance_response;
    ViewPaymentOption viewPaymentOption;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Bind(R.id.map_progress)
    TypeFaceTextMonixRegular mapProgress;
    private String PAYMENT_ID, LATERDATE, LATERTIME;
    private String SELECTED_PICK_LATITUDE;
    private String SELECTED_PICK_LONGITUDE;
    private String SELECTED_PICK_LOCATION = "";
    private String SELECTED_DROP_LATITUDE;
    private String SELECTED_DROP_LONGITUDE;
    private String SELECTED_DROP_LOCATION = "";
    private String COUPON_CODE = "";
    private String CREDIT_CARD_ID = "";


    @Bind(R.id.source_txt) TextView source_txt;
    @Bind(R.id.destination_txt) TextView destination_txt;
    @Bind(R.id.selected_date_txt) TextView selected_date_txt;
    @Bind(R.id.selected_time_txt) TextView selected_time_txt;
    @Bind(R.id.car_image) ImageView car_image;
    @Bind(R.id.payment_txt) TextView payment_txt;
    @Bind(R.id.coupon_tx) TextView coupon_tx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rideSession = new RideSession(this);
        languageManager = new LanguageManager(this);
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        builder = new GsonBuilder();
        gson = builder.create();
        setContentView(R.layout.activity_trial_ridelater);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag_main);
        mapFragment.getMapAsync(this);


        try {
            SELECTED_PICK_LATITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LATITUDE);
            SELECTED_PICK_LONGITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LONGITUDE);
            SELECTED_PICK_LOCATION = "" + getIntent().getExtras().getString(Config.IntentKeys.PICK_LOCATION_TXT);
            SELECTED_DROP_LATITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LATITUDE);
            SELECTED_DROP_LONGITUDE = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LONGITUDE);
            SELECTED_DROP_LOCATION = "" + getIntent().getExtras().getString(Config.IntentKeys.DROP_LOCATIOn_TXT);
            LATERDATE = "" + getIntent().getExtras().getString("" + Config.IntentKeys.DATE);
            LATERTIME = "" + getIntent().getExtras().getString("" + Config.IntentKeys.TIME);
        } catch (Exception e) {

        }


        if (!SELECTED_DROP_LATITUDE.equals("")) {
            LatLng origin = new LatLng(Double.parseDouble("" + SELECTED_PICK_LATITUDE), Double.parseDouble("" + SELECTED_PICK_LONGITUDE));
            LatLng destination = new LatLng(Double.parseDouble("" + getIntent().getExtras().getString("" + Config.IntentKeys.DROP_LATITUDE)), Double.parseDouble("" + getIntent().getExtras().getString("" + Config.IntentKeys.DROP_LONGITUDE)));
            apiManager.execution_method_without_result_check(Config.ApiKeys.Key_Google_Distance_matrix, MapUtils.getDistancematrixUrl(origin, destination, this) , false,ApiManager.ACTION_SHOW_TOAST);
        }


        HashMap<String, String> data3 = new HashMap<>();
        data3.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
        data3.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));

        apiManager.execution_method_post(Config.ApiKeys.KEY_PaymentOption, "" + Apis.viewPaymentOption, data3, true,ApiManager.ACTION_SHOW_TOAST);


        selected_date_txt.setText(LATERDATE);
        selected_time_txt.setText(LATERTIME);
        source_txt.setText("" + SELECTED_PICK_LOCATION);
        destination_txt.setText("" + SELECTED_DROP_LOCATION);
        Glide.with(this).load("" + Apis.imageDomain + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_IMAGE)).into(car_image);


        findViewById(R.id.change_destination_lout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openGooglePlaceAPiDialoge();
            }
        });

        findViewById(R.id.confirm_lout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        findViewById(R.id.ll_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TrialRidelaterDialogActivity.this, CouponActivity.class), 101);
                overridePendingTransition(R.anim.animation3, R.anim.animation4);
            }
        });


        findViewById(R.id.payment_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForSelectPayment();
            }
        });

        findViewById(R.id.date_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(TrialRidelaterDialogActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(calendar);
                dpd.setAccentColor(TrialRidelaterDialogActivity.this.getResources().getColor(R.color.colorPrimary));
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                dpd.show(getFragmentManager(), "Date Picker Dialog");
            }
        });


        findViewById(R.id.time_layout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(TrialRidelaterDialogActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                tpd.setAccentColor(TrialRidelaterDialogActivity.this.getResources().getColor(R.color.colorPrimary));
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                    }
                });
                tpd.show(getFragmentManager(), "Time Picker Dialog");
            }
        });


        findViewById(R.id.confirm_lout_btn).setOnClickListener(new View.OnClickListener() {
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
                data5.put("later_date", "" + LATERDATE);
                data5.put("later_time", "" + LATERTIME);
                data5.put("car_type_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID));
                data5.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                data5.put("payment_option_id", "" + PAYMENT_ID);
                data5.put("card_id", "" + CREDIT_CARD_ID);
                apiManager.execution_method_post(Config.ApiKeys.Key_Ride_Later, Apis.rideLater, data5, true,ApiManager.ACTION_SHOW_TOAST);
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGooglemap = googleMap;
        MapUtils.setMapTheme(this, mGooglemap);

        if (SELECTED_DROP_LATITUDE.equals("")) {
            LatLng origin = new LatLng(Double.parseDouble("" + SELECTED_PICK_LATITUDE), Double.parseDouble("" + SELECTED_PICK_LONGITUDE));
            MapUtils.setDestinationMarkerForPickPoint(this, mGooglemap, origin, "" + SELECTED_PICK_LOCATION);
//            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(origin).zoom(Config.MapDefaultZoom).build()));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, Config.MapDefaultZoom));



            mapProgress.setVisibility(View.GONE);
        } else {
            LatLng origin = new LatLng(Double.parseDouble("" + SELECTED_PICK_LATITUDE), Double.parseDouble("" + SELECTED_PICK_LONGITUDE));
            LatLng destination = new LatLng(Double.parseDouble("" + getIntent().getExtras().getString("" + Config.IntentKeys.DROP_LATITUDE)), Double.parseDouble("" + getIntent().getExtras().getString("" + Config.IntentKeys.DROP_LONGITUDE)));
            MapUtils.setDestinationMarkerForPickPoint(this, mGooglemap, origin, "Customer Location ");
            MapUtils.setDestinationMarkerForDropPoint(this, mGooglemap, destination, "Customer Location ");
            try {
                DrawRouteMaps.getInstance(this, this, sessionManager, mapProgress, true).draw(origin, destination, mGooglemap);
            } catch (Exception e) {
            }
        }
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try{if (APINAME.equals(Config.ApiKeys.Key_Google_Distance_matrix)) {
            distance_response = gson.fromJson("" + script, DistanceMatrixResponseModel.class);

            HashMap<String, String> data = new HashMap();
            data.put("distance", "" + distance_response.getRows().get(0).getElements().get(0).getDistance().getValue());
            data.put("city_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_CITY_ID));
            data.put("car_type_id", "" + rideSession.getRideSessionDetails().get(RideSession.SELECTED_CATEGORY_ID));
            data.put("language_id", "" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
            data.put("pickup_lat", SELECTED_PICK_LATITUDE);
            data.put("pickup_long", SELECTED_PICK_LONGITUDE);
            apiManager.execution_method_post(Config.ApiKeys.Key_Estimate, "" + Apis.rideEstimate, data, true,ApiManager.ACTION_SHOW_TOAST);
        }
            if (APINAME.equals(Config.ApiKeys.Key_Estimate)) {
                RideEstimate ride_estimate;
                ride_estimate = gson.fromJson("" + script, RideEstimate.class);
                if (ride_estimate.getResult() == 1) {
//                    Toast.makeText(this, "" + ride_estimate.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            if (APINAME.equals(Config.ApiKeys.KEY_PaymentOption)) {
                viewPaymentOption = gson.fromJson("" + script, ViewPaymentOption.class);
                payment_txt.setText("" + viewPaymentOption.getMsg().get(0).getPayment_option_name());
                PAYMENT_ID = viewPaymentOption.getMsg().get(0).getPayment_option_id();
            }
            if (APINAME.equals(Config.ApiKeys.Key_Ride_Later)) {
                RideNow rideNow;
                rideNow = gson.fromJson("" + script, RideNow.class);
                if (rideNow.getResult() == 1 ) {
                    dialogForRideScheduledSuccessfully();
                } else {
                    dialogForRideScheduledUnSuccessfully();
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }


    private void openGooglePlaceAPiDialoge() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(TrialRidelaterDialogActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    public void dialogForSelectPayment() {
        final Dialog dialog = new Dialog(TrialRidelaterDialogActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_option);

        ListView lv_payment_option = (ListView) dialog.findViewById(R.id.lv_payment_option);
        lv_payment_option.setAdapter(new PaymentAdapter(TrialRidelaterDialogActivity.this, viewPaymentOption));

        lv_payment_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (viewPaymentOption.getMsg().get(position).getPayment_option_name().equals("Credit Card")) {
                    startActivityForResult(new Intent(TrialRidelaterDialogActivity.this, AddCardActivity.class).putExtra("From", "PaymentFailedActivity"), 103);
                } else {
                    payment_txt.setText("" + viewPaymentOption.getMsg().get(position).getPayment_option_name());
                }
                PAYMENT_ID = viewPaymentOption.getMsg().get(position).getPayment_option_id();

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        LATERDATE = dayOfMonth + "/" + month + "/" + year;
        selected_date_txt.setText("" + LATERDATE);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        LATERTIME = hourString + ":" + minuteString;

        selected_time_txt.setText("" + LATERTIME);
    }


    public void dialogForRideScheduledSuccessfully() {
        final Dialog dialog = new Dialog(TrialRidelaterDialogActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_ride_schedule_successfully);

        dialog.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogForRideScheduledUnSuccessfully() {
        final Dialog dialog = new Dialog(TrialRidelaterDialogActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_ride_schedule_unsuccessfully);

        dialog.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
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
            } catch (Exception e) {
                Log.e("res ", e.toString());
            }
        }
    }


}
