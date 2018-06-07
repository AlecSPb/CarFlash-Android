package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewRentalRidesDetailsModle;
import com.carflash.user.samir.customviews.TypeFaceTextMonixBold;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.typeface.TypefaceTextView;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RentalRidesSelectedActivity extends Activity implements ApiManager.APIFETCHER {

    String ride_id, ride_status, ride_type, ride_mode;
    ApiManager apiManager;
    SessionManager sessionManager;
    Gson gson;
    public static Activity activity;
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
    @Bind(R.id.start_time_txt)
    TextView startTimeTxt;
    @Bind(R.id.tv_start_location)
    TypefaceTextView tvStartLocation;
    @Bind(R.id.drop_time_txt)
    TextView dropTimeTxt;
    @Bind(R.id.tv_end_location)
    TypefaceTextView tvEndLocation;
    @Bind(R.id.ll_location_module)
    LinearLayout llLocationModule;
    @Bind(R.id.tv_ride_distance)
    TextView tvRideDistance;
    @Bind(R.id.total_hours_txt)
    TextView totalHoursTxt;
    @Bind(R.id.base_package_txt)
    TextView basePackageTxt;
    @Bind(R.id.base_package_price)
    TextView basePackagePrice;
    @Bind(R.id.extra_distance_txt)
    TextView extraDistanceTxt;
    @Bind(R.id.extra_distance_price_txt)
    TextView extraDistancePriceTxt;
    @Bind(R.id.extra_time_txt)
    TextView extraTimeTxt;
    @Bind(R.id.extra_time_price_txt)
    TextView extraTimePriceTxt;
    @Bind(R.id.total_price_txt)
    TextView totalPriceTxt;
    @Bind(R.id.coupon_txt)
    TextView couponTxt;
    @Bind(R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(R.id.total_payble_bottom)
    TextView totalPaybleBottom;
    @Bind(R.id.ll_track_ride)
    LinearLayout llTrackRide;
    @Bind(R.id.ll_mail_invoice)
    LinearLayout llMailInvoice;
    @Bind(R.id.ll_cancel_ride)
    LinearLayout llCancelRide;
    @Bind(R.id.activity_rides_selected)
    LinearLayout activityRidesSelected;
    @Bind(R.id.bill_layout)
    View bill_layout;
    @Bind(R.id.total_amount_paid_txt)
    TextView totalAmountPaidTxt;
    @Bind(R.id.night_time_txt)
    TextView nightTimeTxt;
    @Bind(R.id.night_time_price_txt)
    TextView nightTimePriceTxt;
    @Bind(R.id.peak_time_txt)
    TextView peakTimeTxt;
    @Bind(R.id.peak_time_price_txt)
    TextView peakTimePriceTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this , this , this );
        activity = this;
        gson = new GsonBuilder().create();
        setContentView(R.layout.activity_rental_rides_selected);
        ButterKnife.bind(this);




        ride_id = super.getIntent().getExtras().getString("ride_id");
        ride_status = super.getIntent().getExtras().getString("ride_status");
        ride_type = super.getIntent().getExtras().getString("ride_type");
        ride_mode = super.getIntent().getExtras().getString("ride_mode");
        String date_time = super.getIntent().getExtras().getString("date_time");
        tvTime1.setText(date_time);

        HashMap<String, String> data = new HashMap<>();
        data.put("ride_mode", "" + ride_mode);
        data.put("booking_id", "" + ride_id);

        apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RIDE_DETAILS, "" + Apis.RideDetails, data, true,ApiManager.ACTION_SHOW_DIALOG);

        setviewAccordingtostatus();


        llTrackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RentalRidesSelectedActivity.this, RentalTrackActivity.class);
                in.putExtra("ride_id", "" + ride_id);
                in.putExtra("ride_status", "" + ride_status);
                startActivity(in);
            }
        });

        findViewById(R.id.ll_back_ride_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeActivity();
            }
        });


        llMailInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RentalRidesSelectedActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
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
                        apiManager.execution_method_get("resend_mail", "" + Apis.resendInvoice + "?ride_id=" + ride_id + "&language_id=1" + "&user_email=" + mail_email_edt.getText().toString(), true ,ApiManager.ACTION_SHOW_TOP_BAR);
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

        findViewById(R.id.ll_cancel_ride).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("rental_booking_id", "" + ride_id);
                data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
                apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE, "" + Apis.CancelRentalRide, data, true , ApiManager.ACTION_SHOW_DIALOG);
            }
        });

    }

    private void setviewAccordingtostatus() {

        switch (ride_status) {
            case Config.Status.RENTAL_BOOKED:
                llCancelRide.setVisibility(View.VISIBLE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.GONE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.RENTAL_ACCEPTED:
                llCancelRide.setVisibility(View.VISIBLE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.VISIBLE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.RENTAL_ARRIVED:
                llCancelRide.setVisibility(View.GONE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.VISIBLE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.RENTAl_RIDE_STARTED:
                llCancelRide.setVisibility(View.GONE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.VISIBLE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.RENTAL_RIDE_REJECTED:
                // in this case ride will finished or alloted to the other driver as it is a case of Rental ride
                break;
            case Config.Status.RENTAL_RIDE_CANCEL_BY_USER:
                llCancelRide.setVisibility(View.GONE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.GONE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.RENTAL_RIDE_END:
                llCancelRide.setVisibility(View.GONE);
                bill_layout.setVisibility(View.VISIBLE);
                llTrackRide.setVisibility(View.GONE);
                llMailInvoice.setVisibility(View.VISIBLE);
                break;
            case Config.Status.RENTAL_RIDE_CANCEL_BY_DRIVER: // in this case ride will finished or alloted to the other driver as it is a case of Rental rides
                llCancelRide.setVisibility(View.GONE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.GONE);
                llMailInvoice.setVisibility(View.GONE);
                break;
            case Config.Status.PARTIAL_ACCEPT: // in this case ride will finished or alloted to the other driver as it is a case of Rental rides
                llCancelRide.setVisibility(View.VISIBLE);
                bill_layout.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.GONE);
                llMailInvoice.setVisibility(View.GONE);
                break;

        }
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{if (APINAME.equals("" + Config.ApiKeys.KEY_REST_RIDE_DETAILS)) {
            NewRentalRidesDetailsModle rental_response = gson.fromJson("" + script, NewRentalRidesDetailsModle.class);
            tvNameDriver.setText("" + rental_response.getDetails().getDriver_name());
            tvNameCar.setText(rental_response.getDetails().getCar_type_name());
            tvRupee.setText(sessionManager.getCurrencyCode() + rental_response.getDetails().getFinal_bill_amount());
            tvDis.setText(rental_response.getDetails().getTotal_distance_travel() + " " + " " + RentalRidesSelectedActivity.this.getResources().getString(R.string.distancesymbol));
            tvStartLocation.setText(rental_response.getDetails().getPickup_location());
            tvEndLocation.setText(rental_response.getDetails().getEnd_location());

            Picasso.with(this)
                    .load(Apis.imageDomain + rental_response.getDetails().getDriver_image())
                    .placeholder(R.drawable.dummy_pic)
                    .error(R.drawable.dummy_pic)
                    .fit()
                    .into(ivImageDriver);
            Picasso.with(this)
                    .load(Apis.imageDomain + rental_response.getDetails().getCar_type_image())
                    .placeholder(R.drawable.dummy_pic)
                    .error(R.drawable.dummy_pic)
                    .fit()
                    .into(ivImageCar);

            if (!rental_response.getDetails().getDriver_rating().equals("")) {
                Float rate = Float.valueOf(rental_response.getDetails().getUser_rating_star());
                ratingSelected.setRating(rate);
            }

            tvRideDistance.setText("" + rental_response.getDetails().getTotal_distance_travel());
            totalHoursTxt.setText("" + rental_response.getDetails().getTotal_time_travel());
            basePackageTxt.setText(getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__package) + rental_response.getDetails().getRental_package_distance() + " " + getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY___for) + " " + rental_response.getDetails().getRental_package_hours() + getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__hours));
            basePackagePrice.setText(sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getRental_package_price());
            extraDistanceTxt.setText(getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__extra_distance_travel) + rental_response.getDetails().getExtra_distance_travel() + " )");
            extraDistancePriceTxt.setText(sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getExtra_distance_travel_charge());
            extraTimePriceTxt.setText(getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__extra_time) + rental_response.getDetails().getExtra_hours_travel() + ")");
            extraTimePriceTxt.setText(sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getExtra_hours_travel_charge());
            totalPriceTxt.setText(sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getTotal_amount());
            totalPaybleBottom.setText(sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getFinal_bill_amount());
            startTimeTxt.setText("" + rental_response.getDetails().getBegin_time());
            dropTimeTxt.setText("" + rental_response.getDetails().getEnd_time());

            totalAmountPaidTxt.setText("" + RentalRidesSelectedActivity.this.getResources().getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__total_paid_amount) + " (" + rental_response.getDetails().getPayment_option_name() + ")");


            if (!rental_response.getDetails().getCoupan_code().equals("")) {
                couponPriceTxt.setVisibility(View.VISIBLE);
                couponTxt.setVisibility(View.VISIBLE);
                couponTxt.setText(RentalRidesSelectedActivity.this.getResources().getString(R.string.RENTAL_RIDE_SELECTED_ACTIVITY__coupon_applied) + " (" + rental_response.getDetails().getCoupan_code() + ")");
                couponPriceTxt.setText("" + sessionManager.getCurrencyCode() + "" + rental_response.getDetails().getCoupan_price());
            } else {
                couponPriceTxt.setVisibility(View.GONE);
                couponTxt.setVisibility(View.GONE);
            }
        }
            if (APINAME.equals("" + Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE)) {
                llCancelRide.setVisibility(View.GONE);
                llTrackRide.setVisibility(View.GONE);
                Toast.makeText(activity, "Ride cancelled !", Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finalizeActivity();
    }

    private void finalizeActivity() {
        try {
            TripHistoryActivity.ridesActivity.finish();
        } catch (Exception e) {

        }
        finish();
        startActivity(new Intent(RentalRidesSelectedActivity.this, TripHistoryActivity.class));
    }
}
