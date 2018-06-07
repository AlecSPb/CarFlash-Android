package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.DoneRideInfo;
import com.carflash.user.models.PaymentSaved;
import com.carflash.user.models.PayWithCard;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.samwork.RideLoadrActivity;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.carflash.user.manager.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.text.SimpleDateFormat;

public class ReceiptActivity extends AppCompatActivity implements ApiManager.APIFETCHER{

    String TAG ="ReceiptActivity";
    String user_id;
    TextView tv_ride_fare, tv_driver_name, tv_ride_distance, tv_ride_time ,fare_txt , waiting_charge_txt, total_payble_fare_txt_large,  coupon_code_txt , coupon_price_txt, pick_location_txt, drop_location_txt , ride_time_charges_txt , night_charge_txt , peak_charge_txt ;
    ImageView iv_driver_pic;
    LinearLayout ll_make_payment , coupon_layout;
    public static Activity receipt_activity;
    SessionManager sessionManager ;

//    private  final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
//    private  final String CONFIG_CLIENT_ID = "AFcWxV21C7fd0v3bYYYRCpSSRl31AW.nrY8UUmkTDBx-TSEQlHYBvptc";
//    private  PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID);

    private static final int REQUEST_CODE_PAYMENT = 1;

    public static String rideId;
    TextView tv_change_text;
    String dateString;
    String orderid, userid, paymentmethod, paymentpaltform, paymentid, paymentamount, paymentdate, paymentstatus;


    LanguageManager languageManager;

    String language_id;
    DoneRideInfo doneRideInfo ;
    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        receipt_activity = this;
        apiManager = new ApiManager(this , this , this );
        tv_change_text = (TextView) findViewById(R.id.tv_change_text);
        tv_ride_fare = (TextView) findViewById(R.id.tv_ride_fare);
        tv_driver_name = (TextView) findViewById(R.id.tv_driver_name);
        tv_ride_distance = (TextView) findViewById(R.id.tv_ride_distance);
        tv_ride_time = (TextView) findViewById(R.id.tv_ride_time);
        iv_driver_pic = (ImageView) findViewById(R.id.iv_driver_pic);
        fare_txt = (TextView) findViewById(R.id.fare_txt);
        waiting_charge_txt = (TextView) findViewById(R.id.waiting_charge_txt);
        total_payble_fare_txt_large = (TextView) findViewById(R.id.total_payble_fare_txt_large);
        coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout);
        coupon_code_txt = (TextView) findViewById(R.id.coupon_code_txt);
        coupon_price_txt = (TextView) findViewById(R.id.coupon_price_txt);
        pick_location_txt = (TextView) findViewById(R.id.pick_location_txt);
        drop_location_txt = (TextView) findViewById(R.id.drop_location_txt);
        ride_time_charges_txt = (TextView) findViewById(R.id.ride_time_charges_txt);


        night_charge_txt = (TextView) findViewById(R.id.night_charge_txt);
        peak_charge_txt = (TextView) findViewById(R.id.peak_charge_txt);

        sessionManager = new SessionManager(this);
        rideId = super.getIntent().getExtras().getString("ride_id");
//        driverId = super.getIntent().getExtras().getString("driverId");
        user_id = new SessionManager(ReceiptActivity.this).getUserDetails().get(SessionManager.USER_ID);

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);


        apiManager.execution_method_get(""+Config.ApiKeys.Key_view_Done_Ride , ""+Apis.viewDoneRide+"done_ride_id="+rideId+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);
        clearNotification();
        ll_make_payment = (LinearLayout) findViewById(R.id.ll_make_payment);


        ll_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    long date = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
                    dateString = sdf.format(date);
                    apiManager.execution_method_get(""+Config.ApiKeys.Key_payment_api , ""+Apis.payment+"order_id="+rideId+"&user_id="+user_id+"&payment_id="+doneRideInfo.getMsg().getPayment_option_id()+"&payment_method="+"Cash"+"&payment_platform="+"Android"+"&payment_amount="+doneRideInfo.getMsg().getTotal_amount()+"&payment_date_time="+dateString+"&payment_status="+ReceiptActivity.this.getResources().getString(R.string.RENTAL_RECEIPT_ACTIVITY__done)+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });

            }



    private void shodialogForPaymentDone() {
        final Dialog dialog = new Dialog(ReceiptActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_payment_already_done);
        dialog.findViewById(R.id.ll_ok_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeActivity();
            }
        });

        dialog.show();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//            if (paymentConfirmation != null) {
//                try {
//                    String response = paymentConfirmation.toJSONObject().toString(4);
//                    GsonBuilder builder = new GsonBuilder();
//                    Gson gson = builder.create();
//                    PaypalModel pay_pal = new PaypalModel();
//                    pay_pal = gson.fromJson(response, PaypalModel.class);
//
//                    if (pay_pal != null) {
//                        apiManager.execution_method_get(""+Config.ApiKeys.Key_payment_api , ""+Apis.payment+"order_id="+rideId+"&user_id="+user_id+"&payment_id="+doneRideInfo.getMsg().getPayment_option_id()+"&payment_method="+"Paypal"+"&payment_platform="+"Android"+"&payment_amount="+doneRideInfo.getMsg().getTotal_amount()+"&payment_date_time="+pay_pal.response.create_time+"&payment_status="+pay_pal.response.state+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);
//
//                    } else {
//
//                    }
//                } catch (JSONException e) {
//                }
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            Toast.makeText(ReceiptActivity.this, "Cancelled ", Toast.LENGTH_SHORT).show();
//        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//            Toast.makeText(ReceiptActivity.this, "Invalid Payment ", Toast.LENGTH_SHORT).show();
//            Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//        }
//    }

    @Override
    public void onBackPressed() {
//     finalizeActivity ();
    }

    private void finalizeActivity() {
        try{
            TrackRideAactiviySamir.trackRideActivity.finish();
        }catch (Exception e){

        }
        if(!Config.app_loaded_from_initial){
            startActivity(new Intent(ReceiptActivity.this , SplashActivity.class));
            finish();
        }else {
            finish();
        }
    }


    public void closePreviousActivities (){
        try{
            RideLoadrActivity.activity.finish();
        }catch (Exception e){

        }

        try{
            TrialRideConfirmDialogActivity.activity.finish();
        }catch (Exception e){

        }
        try{TrackRideAactiviySamir.trackRideActivity.finish();}catch (Exception e){}
        finish();
    }




    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (APINAME.equals(Config.ApiKeys.Key_view_Done_Ride)) {
                doneRideInfo = new DoneRideInfo();
                doneRideInfo = gson.fromJson(""+script, DoneRideInfo.class);
                if (doneRideInfo.getResult() == 1) {
                    fare_txt.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getAmount());
                    tv_driver_name.setText(""+doneRideInfo.getMsg().getDriver_name());
                    total_payble_fare_txt_large.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getTotal_amount());
                    tv_ride_fare.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getTotal_amount());
                    waiting_charge_txt.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getWaiting_price());
                    pick_location_txt.setText(""+doneRideInfo.getMsg().getBegin_location());
                    drop_location_txt.setText(""+doneRideInfo.getMsg().getEnd_location());
                    tv_ride_distance.setText(""+doneRideInfo.getMsg().getDistance());
                    ride_time_charges_txt.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getRide_time_price());
                    night_charge_txt.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getNight_time_charge());
                    peak_charge_txt.setText(sessionManager.getCurrencyCode()+""+doneRideInfo.getMsg().getPeak_time_charge());
                    if(doneRideInfo.getMsg().getCoupons_code().equals("")){
                        coupon_layout.setVisibility(View.GONE);
                    }else{
                        coupon_layout.setVisibility(View.VISIBLE);
                        coupon_code_txt.setText("Coupon ("+doneRideInfo.getMsg().getCoupons_code()+")");
                        coupon_price_txt.setText("-"+sessionManager.getCurrencyCode()+doneRideInfo.getMsg().getCoupons_price());
                    }
                } else {
                    Toast.makeText(ReceiptActivity.this, "" + doneRideInfo.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            if (APINAME.equals(""+Config.ApiKeys.Key_payment_api)) {
                PaymentSaved paymentSaved;
                paymentSaved = gson.fromJson(""+script, PaymentSaved.class);
                if (paymentSaved.getResult() == 1) {
                    tv_change_text.setText(getString(R.string.payment_done));
                    ll_make_payment.setOnClickListener(null);
                    orderid = paymentSaved.getDetails().getOrder_id();
                    userid = paymentSaved.getDetails().getUser_id();
                    paymentmethod = paymentSaved.getDetails().getPayment_method();
                    paymentpaltform = paymentSaved.getDetails().getPayment_platform();
                    paymentid = paymentSaved.getDetails().getPayment_id();
                    paymentamount = paymentSaved.getDetails().getPayment_amount();
                    paymentdate = paymentSaved.getDetails().getPayment_date_time();
                    paymentstatus = paymentSaved.getDetails().getPayment_status();

                    Intent i = new Intent(ReceiptActivity.this, InvoiceActivity.class);
                    i.putExtra("order_id", orderid);
                    i.putExtra("payment_id", paymentid);
                    i.putExtra("payment_amount", paymentamount);
                    i.putExtra("payment_date", paymentdate);
                    i.putExtra("payment_status", paymentstatus);
                    i.putExtra("payment_platform", paymentpaltform);
                    i.putExtra("payment_method", paymentmethod);
                    i.putExtra("driverId", doneRideInfo.getMsg().getDriver_id());
                    i.putExtra("ride_id", doneRideInfo.getMsg().getRide_id());
                    i.putExtra("distance", doneRideInfo.getMsg().getDistance());
                    i.putExtra("wait_time", doneRideInfo.getMsg().getWaiting_time());
                    i.putExtra("wait_charge", doneRideInfo.getMsg().getWaiting_price());
                    i.putExtra("driver_name_txt", doneRideInfo.getMsg().getDriver_name());
                    i.putExtra("customer_name_txt", doneRideInfo.getMsg().getUsername());
                    i.putExtra("customer_phone_txt", doneRideInfo.getMsg().getUser_phone());
                    if(doneRideInfo.getMsg().getCoupons_price().equals("")){
                        i.putExtra("coupon_amount_txt", "- - - ");
                    }else{
                        i.putExtra("coupon_amount_txt", doneRideInfo.getMsg().getCoupons_price());
                    }
                    startActivity(i);
                    closePreviousActivities();
                } else {
                    shodialogForPaymentDone();
                }
            }
            if (APINAME.equals("Pay With Card")) {
                PayWithCard payWithCard = new PayWithCard();
                payWithCard = gson.fromJson(""+script, PayWithCard.class);
                if (payWithCard.getResult().toString().equals("1")) {
                    String status = payWithCard.getMsg();
                    String payment_id = payWithCard.getPaymentId();

                    long date = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
                    dateString = sdf.format(date);
                    apiManager.execution_method_get(""+Config.ApiKeys.Key_payment_api , ""+Apis.payment+"order_id="+rideId+"&user_id="+user_id+"&payment_id="+payment_id+"&payment_method="+"Credit Card"+"&payment_platform="+"Android"+"&payment_amount="+doneRideInfo.getMsg().getTotal_amount()+"&payment_date_time="+dateString+"&payment_status="+status+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);

                } else {
                    Toast.makeText(ReceiptActivity.this, "" + payWithCard.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
        }
    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
