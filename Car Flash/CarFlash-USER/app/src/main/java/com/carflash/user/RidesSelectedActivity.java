package com.carflash.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.adapter.ReasonAdapter;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.CancelReasonCustomer;
import com.carflash.user.models.NewRideDetailsModel;
import com.carflash.user.others.MapUtils;
import com.carflash.user.samir.customviews.TypeFaceTextMonixBold;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.TrialModel;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RidesSelectedActivity extends AppCompatActivity implements ApiManager.APIFETCHER {

    String ride_id, ride_status, ride_type, ride_mode, pick_lat, pick_long, drop_lat, drop_long;

    ApiManager apiManager;
    Gson gson;
    NewRideDetailsModel ride_details_response;

    SessionManager sessionManager;
    CancelReasonCustomer cancelReasonCustomer_response;

    @Bind(R.id.tv_number)
    TextView carNumber;
    @Bind(R.id.start_time_txt)
    TextView startTimeTxt;
    @Bind(R.id.drop_time_txt)
    TextView dropTimeTxt;
    @Bind(R.id.total_distance_txt)
    TextView totalDistanceTxt;
    @Bind(R.id.ride_time_charges_txt)
    TextView rideTimeChargesTxt;
    @Bind(R.id.total_fare_txt)
    TextView totalFareTxt;
    @Bind(R.id.wating_charge_txt)
    TextView watingChargeTxt;
    @Bind(R.id.coupon_value_txt)
    TextView couponValueTxt;
    @Bind(R.id.total_gross_bill_txt)
    TextView totalGrossBillTxt;
    @Bind(R.id.payment_mode_txt)
    TextView paymentModeTxt;
    @Bind(R.id.payment_amount_done_txt)
    TextView paymentAmountDoneTxt;
    @Bind(R.id.coupon_code_txt)
    TextView coupon_code_txt;
    @Bind(R.id.ll_back_ride_select)
    LinearLayout llBackRideSelect;
    @Bind(R.id.iv_map)
    ImageView ivMap;
    @Bind(R.id.iv_image_driver)
    CircleImageView ivImageDriver;
    @Bind(R.id.tv_name_driver)
    TextView tvNameDriver;
    @Bind(R.id.rating_selected)
    RatingBar ratingSelected;
    @Bind(R.id.ll_driver_ki_detail)
    LinearLayout llDriverKiDetail;
    @Bind(R.id.iv_image_car)
    ImageView ivImageCar;
    @Bind(R.id.tv_name_car)
    TypeFaceTextMonixBold tvNameCar;
    @Bind(R.id.tv_car_ima)
    CircleImageView tvCarIma;
    @Bind(R.id.tv_rupee)
    TextView tvRupee;
    @Bind(R.id.tv_dis)
    TextView tvDis;
    @Bind(R.id.tv_time1)
    TextView tvTime1;
    @Bind(R.id.ll_bill)
    LinearLayout llBill;
    @Bind(R.id.tv_start_location)
    TextView tvStartLocation;
    @Bind(R.id.tv_end_location)
    TextView tvEndLocation;
    @Bind(R.id.ll_location_module)
    LinearLayout llLocationModule;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.ll_total_bill)
    LinearLayout llTotalBill;
    @Bind(R.id.ll_track_ride)
    LinearLayout llTrackRide;
    @Bind(R.id.ll_mail_invoice)
    LinearLayout llMailInvoice;
    @Bind(R.id.activity_rides_selected)
    LinearLayout activityRidesSelected;
    @Bind(R.id.ll_cancel_ride)
    LinearLayout llCancelRide;
    @Bind(R.id.schedule_layout)
    CardView schedule_layout;
    @Bind(R.id.booking_time)
    TextView booking_time;
    @Bind(R.id.schedule_time)
    TextView schedule_time;
    @Bind(R.id.night_time_charge_txt)
    TextView nightTimeChargeTxt;
    @Bind(R.id.peak_time_charge_txt)
    TextView peakTimeChargeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this );
        gson = new GsonBuilder().create();
        setContentView(R.layout.activity_rides_selected);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);


        try {
            ride_id = super.getIntent().getExtras().getString("ride_id");
            ride_status = super.getIntent().getExtras().getString("ride_status");
            ride_type = super.getIntent().getExtras().getString("ride_type");
            ride_mode = super.getIntent().getExtras().getString("ride_mode");
            String date_time = super.getIntent().getExtras().getString("date_time");

            pick_lat = getIntent().getExtras().getString("pick_lat");
            pick_long = getIntent().getExtras().getString("pick_long");
            drop_lat = getIntent().getExtras().getString("drop_lat");
            drop_long = getIntent().getExtras().getString("drop_long");
        } catch (Exception e) {

        }


//////////////////// setting view according to status
        setViewAccordingToStatus();


        HashMap<String, String> data = new HashMap<>();
        data.put("ride_mode", "" + ride_mode);
        data.put("booking_id", "" + ride_id);
        apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RIDE_DETAILS, "" + Apis.RideDetails, data, true,ApiManager.ACTION_SHOW_DIALOG);
        String url = "";
        if (drop_long.equals("") || drop_long == null) {

        } else {
            url = MapUtils.getDirectionsUrl(new LatLng(Double.parseDouble(pick_lat), Double.parseDouble(pick_long)), new LatLng(Double.parseDouble(drop_lat), Double.parseDouble(drop_long)), this);
        }


        apiManager.execution_method_without_result_check("mapimage", "" + url, true,ApiManager.ACTION_SHOW_DIALOG);


        llCancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                apiManager.execution_method_get(Config.ApiKeys.Key_Cancel_Ride , Apis.cancelRide+"?&reason_id="+cancelReasonCustomer_response.getMsg().get(position).getReason_id()+""+"&ride_id="+viewRideInfo_response.getDetails().getRide_id()+"&language_id="+new LanguageManager(TrackRideAactiviySamir.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                apiManager.execution_method_get(Config.ApiKeys.Key_cancelRideReason, Apis.cancelReason + "?&language_id=" + new LanguageManager(RidesSelectedActivity.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID), true,ApiManager.ACTION_SHOW_DIALOG);

            }
        });

        llMailInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(RidesSelectedActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                dialog.setContentView(R.layout.mail_dialog);
                dialog.setCancelable(false);

                final EditText mail_email_edt = (EditText) dialog.findViewById(R.id.mail_email_edt);
                mail_email_edt.setText("" + sessionManager.getEmail());

                dialog.findViewById(R.id.demo_ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        apiManager.execution_method_get("resend_mail", "" + Apis.resendInvoice + "?ride_id=" + ride_id + "&language_id=1" + "&user_email=" + mail_email_edt.getText().toString(), true,ApiManager.ACTION_SHOW_DIALOG);
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.back_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        llBackRideSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeActivity();
            }
        });

        llTrackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RidesSelectedActivity.this, TrackRideAactiviySamir.class)
                        .putExtra("ride_id", ride_id)
                        .putExtra("ride_status", ride_status));
                finish();
            }
        });

    }

    private void setViewAccordingToStatus() {
        if (ride_status.equals("1")) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.GONE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.VISIBLE);
        } else if (ride_status.equals("2")) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.GONE);
            llTotalBill.setVisibility(View.GONE);
            if (ride_type.equals("1")) {
                llDriverKiDetail.setVisibility(View.VISIBLE);
                llBill.setVisibility(View.GONE);
                llLocationModule.setVisibility(View.VISIBLE);
            } else if (ride_type.equals("2")) {
                llDriverKiDetail.setVisibility(View.GONE);
                llBill.setVisibility(View.GONE);
                llLocationModule.setVisibility(View.VISIBLE);
            }
        } else if (ride_status.equals(""+Config.Status.NORMAL_ACCEPTED_BY_DRIVER)) {
            llTrackRide.setVisibility(View.VISIBLE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.VISIBLE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_REJECTED_BY_DRIVER)) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.VISIBLE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_ARRIVED)) {
            llTrackRide.setVisibility(View.VISIBLE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.GONE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_RIDE_STARTED)) {
            llTrackRide.setVisibility(View.VISIBLE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.GONE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_RIDE_END)) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.VISIBLE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.VISIBLE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.VISIBLE);
            llCancelRide.setVisibility(View.GONE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_LATER_BOOKINg)) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.GONE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.VISIBLE);
        } else if (ride_status.equals(""+Config.Status.NORMAL_CANCEL_BY_DRIVER)) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.GONE);
        }else if (ride_status.equals(""+Config.Status.PARTIAL_ACCEPT)){
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.VISIBLE);
        }else if (ride_status.equals(""+Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN)) {
            llTrackRide.setVisibility(View.GONE);
            llMailInvoice.setVisibility(View.GONE);
            llDriverKiDetail.setVisibility(View.VISIBLE);
            llBill.setVisibility(View.GONE);
            llLocationModule.setVisibility(View.VISIBLE);
            llTotalBill.setVisibility(View.GONE);
            llCancelRide.setVisibility(View.GONE);
        }
    }


    private void loadImage(String script) {

        ArrayList<LatLng> pointstarter = new ArrayList<>();
        ArrayList<LatLng> pointend = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        TrialModel data = gson.fromJson("" + script, TrialModel.class);


        try {
            for (int i = 0; i < data.getRoutes().get(0).getLegs().get(0).getSteps().size(); i++) {
                pointstarter.add(new LatLng(data.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLat(), data.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLng()));
                pointend.add(new LatLng(data.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLat(), data.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLng()));
            }
        } catch (Exception e) {
            Toast.makeText(this, "crash handle successfully  !", Toast.LENGTH_SHORT).show();
        }


        String midpart = "";

        for (int i = 0; i < pointend.size(); i++) {
            midpart = midpart + "" + pointstarter.get(i).latitude + "," + pointstarter.get(i).longitude + "|";
        }


        try {
            String mainUrl = "http://maps.googleapis.com/maps/api/staticmap?" +
                    "size=" + (ivMap.getWidth()) + "x" + (ivMap.getHeight()) +
                    "&path=color:0x000000|weight:2|" + midpart + pointend.get(pointend.size() - 1).latitude + "," + pointend.get(pointend.size() - 1).longitude +
                    "&sensor=false" +
                    "&markers=icon:http://carflash.co.uk/triwl/s2.png|" + pointstarter.get(0).latitude + "," + pointstarter.get(0).longitude +
                    "&markers=icon:http://carflash.co.uk/triwl/s3.png|" + pointend.get(pointend.size() - 1).latitude + "," + pointend.get(pointend.size() - 1).longitude +
                    "&key=" + RidesSelectedActivity.this.getResources().getString(R.string.google_map_key);
            Log.d("** route image static map ", "" + mainUrl);

            Glide.with(this).load(mainUrl).into(ivMap);
        } catch (Exception e) {
            Toast.makeText(this, "Crash handled successfully !", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{switch (APINAME) {
            case Config.ApiKeys.KEY_REST_RIDE_DETAILS:
                ride_details_response = gson.fromJson("" + script, NewRideDetailsModel.class);
                tvNameDriver.setText("" + ride_details_response.getDetails().getDriver_name());
                tvNameCar.setText(ride_details_response.getDetails().getCar_type_name());
                carNumber.setText(ride_details_response.getDetails().getCar_number());
                tvRupee.setText(sessionManager.getCurrencyCode()+ ride_details_response.getDetails().getTotal_amount());
                tvDis.setText(ride_details_response.getDetails().getDistance() + " " + " " + RidesSelectedActivity.this.getResources().getString(R.string.distancesymbol));
                tvStartLocation.setText(ride_details_response.getDetails().getPickup_location());
                tvEndLocation.setText(ride_details_response.getDetails().getDrop_location());
                totalDistanceTxt.setText("" + ride_details_response.getDetails().getDistance() + " " + RidesSelectedActivity.this.getResources().getString(R.string.distancesymbol));
                totalFareTxt.setText(sessionManager.getCurrencyCode()+ "" + ride_details_response.getDetails().getAmount());
                startTimeTxt.setText("" + ride_details_response.getDetails().getBegin_time());
                dropTimeTxt.setText("" + ride_details_response.getDetails().getEnd_time());
                rideTimeChargesTxt.setText(sessionManager.getCurrencyCode()+ "" + ride_details_response.getDetails().getRide_time_price());
                watingChargeTxt.setText(sessionManager.getCurrencyCode()+ "" + ride_details_response.getDetails().getWaiting_price());
                totalGrossBillTxt.setText(sessionManager.getCurrencyCode()+ "" + ride_details_response.getDetails().getTotal_amount());
                paymentAmountDoneTxt.setText(sessionManager.getCurrencyCode()+ "" + ride_details_response.getDetails().getTotal_amount());
                booking_time.setText("" + ride_details_response.getDetails().getRide_date());
                schedule_time.setText("" + ride_details_response.getDetails().getLater_date() + " " + ride_details_response.getDetails().getLater_time());

                peakTimeChargeTxt.setText(sessionManager.getCurrencyCode()+""+ride_details_response.getDetails().getPeak_time_charge());
                nightTimeChargeTxt.setText(sessionManager.getCurrencyCode()+""+ride_details_response.getDetails().getNight_time_charge());

                tvTime1.setText(ride_details_response.getDetails().getDone_ride_time());

                if (ride_details_response.getDetails().getRide_type().equals("2")) {
                    schedule_layout.setVisibility(View.VISIBLE);
                } else {
                    schedule_layout.setVisibility(View.GONE);
                }


                paymentModeTxt.setText(getString(R.string.RIDE_SELECTED_ACTIVITY__total_amount_paid) + " (" + ride_details_response.getDetails().getPayment_option_name() + ")");

                if (ride_details_response.getDetails().getCoupon_code().equals("")) {
                    couponValueTxt.setVisibility(View.GONE);
                    coupon_code_txt.setVisibility(View.GONE);
                } else {
                    couponValueTxt.setVisibility(View.VISIBLE);
                    coupon_code_txt.setVisibility(View.VISIBLE);
                    couponValueTxt.setText("-" + sessionManager.getCurrencyCode()+ ride_details_response.getDetails().getCoupan_price());
                    coupon_code_txt.setText(getString(R.string.RIDE_SELECTED_ACTIVITY__couponapplied) + ride_details_response.getDetails().getCoupon_code() + ")");
                }


                tvStartLocation.setText(ride_details_response.getDetails().getPickup_location());
                tvEndLocation.setText(ride_details_response.getDetails().getDrop_location());


                Picasso.with(this)
                        .load(Apis.imageDomain + ride_details_response.getDetails().getDriver_image())
                        .placeholder(R.drawable.dummy_pic)
                        .error(R.drawable.dummy_pic)
                        .fit()
                        .into(ivImageDriver);
                Picasso.with(this)
                        .load(Apis.imageDomain + ride_details_response.getDetails().getCar_type_image())
                        .placeholder(R.drawable.dummy_pic)
                        .error(R.drawable.dummy_pic)
                        .fit()
                        .into(ivImageCar);

                if (!ride_details_response.getDetails().getDriver_rating().equals("")) {
                    Float rate = Float.valueOf(ride_details_response.getDetails().getDriver_rating());
                    ratingSelected.setRating(rate);
                }

                break;
            case "mapimage":
                loadImage("" + script);
                break;
            case "resend_mail":
                Toast.makeText(this, "Mail Send Successfully", Toast.LENGTH_SHORT).show();
                break;
            case Config.ApiKeys.Key_cancelRideReason:
                cancelReasonCustomer_response = gson.fromJson("" + script, CancelReasonCustomer.class);
                showReasonDialog();
                break;
            case Config.ApiKeys.Key_Cancel_Ride:
                ride_status = "2";
                Toast.makeText(this, R.string.RideCancelled, Toast.LENGTH_SHORT).show();
                setViewAccordingToStatus();
                break;

        }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

    public void showReasonDialog() {

        final Dialog dialog = new Dialog(RidesSelectedActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_cancel_reason);

        ListView lv_reasons = (ListView) dialog.findViewById(R.id.lv_reasons);
        lv_reasons.setAdapter(new ReasonAdapter(RidesSelectedActivity.this, cancelReasonCustomer_response));

        lv_reasons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                apiManager.execution_method_get(Config.ApiKeys.Key_Cancel_Ride, Apis.cancelRide + "?&reason_id=" + cancelReasonCustomer_response.getMsg().get(position).getReason_id() + "" + "&ride_id=" + ride_id + "&language_id=" + new LanguageManager(RidesSelectedActivity.this).getLanguageDetail().get(LanguageManager.LANGUAGE_ID), true,ApiManager.ACTION_SHOW_DIALOG);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        finalizeActivity();
    }

    private void finalizeActivity() {
        finish();

        try {
            TripHistoryActivity.ridesActivity.finish();
        } catch (Exception e) {

        }

        startActivity(new Intent(RidesSelectedActivity.this, TripHistoryActivity.class));
    }


}
