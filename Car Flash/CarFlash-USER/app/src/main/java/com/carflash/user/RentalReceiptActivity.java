package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;

import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewDoneRidemodel;
import com.carflash.user.samir.customviews.TypeFaceTextMonixRegular;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RentalReceiptActivity extends Activity implements ApiManager.APIFETCHER {

    ApiManager apiManager;
    Gson gson;
    NewDoneRidemodel done_ride_response;
    Dialog dialog;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    String email;
    String authorization_url, callback_url;
    PayResponse tokenGenrator;
    String reference_number;
    int status;
    LinearLayout rating_layout;
    RelativeLayout webLayout;
    PaystackResponseModel responseModel;
    WebView webView;

    @Bind(R.id.pick_location_txt)
    TextView pickLocationTxt;
    /*@Bind(R.id.start_meter_txt)
    TextView startMeterTxt;*/
    @Bind(R.id.drop_location_txt)
    TextView dropLocationTxt;
   /* @Bind(R.id.end_meter_txt)
    TextView endMeterTxt;*/
    @Bind(R.id.total_payble_top)
    TextView totalPaybleTop;/*
    @Bind(R.id.tv_ride_distance)
    TextView tvRideDistance;*/
    @Bind(R.id.total_hours_txt)
    TextView totalHoursTxt;
    @Bind(R.id.base_package_txt)
    TextView basePackageTxt;
    @Bind(R.id.base_package_price)
    TextView basePackagePrice;/*
    @Bind(R.id.extra_distance_price_txt)
    TextView extraDistancePriceTxt;*/
   /* @Bind(R.id.extra_time_txt)
    TextView extraTimeTxt;*/
   /* @Bind(R.id.extra_time_price_txt)
    TextView extraTimePriceTxt;
*/    @Bind(R.id.total_price_txt)
    TextView totalPriceTxt;
    @Bind(R.id.coupon_txt)
    TextView couponTxt;
    @Bind(R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(R.id.total_payble_bottom)
    TextView totalPaybleBottom;
  /*  @Bind(R.id.night_time_txt)
    TextView nightTimeTxt;*/
    /*@Bind(R.id.night_time_price_txt)
    TextView nightTimePriceTxt;*/
 /*   @Bind(R.id.peak_time_txt)
    TextView peakTimeTxt;*//*
    @Bind(R.id.peak_time_price_txt)
    TextView peakTimePriceTxt;*/
    TypeFaceTextMonixRegular tvChangeText;
    LinearLayout ll_make_payment;
    RatingBar ratingBar;
    ImageView ok;
    private static final int REQUEST_RENTAL_CODE_PAYMENT = 144;
    private Boolean responseCode;
    String pay_amount;
    int payment_result;
    CircleImageView driverImage;
    EditText comments;
    TextView driver_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        gson = new GsonBuilder().create();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("" + this.getResources().getString(R.string.loading));
        setContentView(R.layout.activity_rental_receipt);
        ButterKnife.bind(this);
      //  PaystackSdk.initialize(this);
        webLayout = (RelativeLayout) findViewById(R.id.webLayout);
        webView = (WebView) findViewById(R.id.webview);
        email = sessionManager.getEmail();
        Log.d("**EMAIL=====", email);

        rating_layout = (LinearLayout) findViewById(R.id.rating_layout);
        driver_name = (TextView) findViewById(R.id.driver_name_txt);
        comments = (EditText) findViewById(R.id.comment_edt);
        driverImage = (CircleImageView) findViewById(R.id.driver_image);
        ll_make_payment = (LinearLayout) findViewById(R.id.ll_make_payment);
        ok = (ImageView) findViewById(R.id.ok);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        HashMap<String, String> data = new HashMap<>();
        data.put("rental_booking_id", "" + getIntent().getExtras().getString("ride_id"));
        apiManager.execution_method_post(Config.ApiKeys.KEY_REST_DONE_RIDE_INFO, "" + Apis.Done_Ride_Info, data, true,ApiManager.ACTION_SHOW_DIALOG);


//        Log.d("**********coming from " , ""+getIntent().getExtras().get("coming_from"));

        clearNotification();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingBar.getRating());
                if (rating.equals("0.0")) {
                    Toast.makeText(RentalReceiptActivity.this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("rating_star", "" + ratingBar.getRating());
                    data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                    data.put("comment", ""+comments.getText().toString());
                    data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
                    data.put("driver_id", "" + done_ride_response.getDetails().getDriver_id());
                    data.put("app_id", "1");
                    apiManager.execution_method_post(Config.ApiKeys.KEY_RENTAL_RATING, Apis.Rating, data, true,ApiManager.ACTION_SHOW_DIALOG);
                }            }
        });

        ll_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showpaymentMethod();
                    ok.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        finalizeActivity();
//    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{switch (APINAME) {
            case Config.ApiKeys.KEY_RENTAL_MAKE_PAYMENT:
//                ResultStatusChecker rs = gson.fromJson("" + script, ResultStatusChecker.class);
//                if (rs.getStatus() == 1) {
                dialogForRating();
//                }else {
//                    shodialogForPaymentDone();
//                }
                break;
            case Config.ApiKeys.KEY_REST_DONE_RIDE_INFO:
                switch (APINAME) {
                    case Config.ApiKeys.KEY_REST_DONE_RIDE_INFO:
                        done_ride_response = gson.fromJson("" + script, NewDoneRidemodel.class);
                        if (done_ride_response.getDetails().getPayment_status().equals("0")) {
                            ll_make_payment.setVisibility(View.VISIBLE);
                            ok.setVisibility(View.GONE);
                            rating_layout.setVisibility(View.GONE);
                            setViewPaymentNotDone();
                        }else {
                            ok.setVisibility(View.VISIBLE);
                            setViewPaymentDone();
                        }
                        break;
                    case Config.ApiKeys.KEY_REST_RATING:
                        finalizeActivity();
                        break;
                }
                break;

            case Config.ApiKeys.Key_payment_api:
                PaymentSave_ResultCheck rs = gson.fromJson("" + script, PaymentSave_ResultCheck.class);

                if (rs.status.equals("1")) {
                    ll_make_payment.setVisibility(View.GONE);
                    rating_layout.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);
                }
                break;

            case Config.ApiKeys.KEY_RENTAL_RATING:
//                if (rs_check.getStatus() == 1) {
               finalizeActivity();

                break;
        }}catch (Exception e){}

        if (APINAME.equals(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK)) {
            // progressBar.setVisibility(View.GONE);
            tokenGenrator = gson.fromJson("" + script, PayResponse.class);
            status = tokenGenrator.getResult();

            Log.d("**STATUS_CARD_PAYMENT--", String.valueOf(status));

            reference_number = tokenGenrator.getDetails().getData().getReference();
            Log.d("**REFERENCE_NUMBER---", reference_number);

            authorization_url = tokenGenrator.getDetails().getData().getAuthorization_url();
            Log.d("**authorization_url---", authorization_url);

            callback_url = tokenGenrator.getDetails().getCall_back_url();
            Log.d("**callback_url---", callback_url);

            //  if (!reference_number.equals(null) || status == false){
            if (status == 1) {
                webLayout.setVisibility(View.VISIBLE);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new MyBrowser());
                webView.loadUrl(authorization_url);

                /*
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.d("**URL_ONPAGEFINISH", url);
                       // finish();
                    }
                });*/

            } else {

                Toast.makeText(this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
            }

        }

        //confirm payment
        if(APINAME.equals(Config.ApiKeys.CONFIRM_PAYMENT)){
            Log.d("**OBJECTSCRIPT_CONFIRM_PAYMENT--", String.valueOf(script));
            responseModel = gson.fromJson("" + script, PaystackResponseModel.class);
            int result = responseModel.getResult();

            if (result == 1){
                Log.d("**result--", String.valueOf(result));
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                data.put("amount_paid", "" + done_ride_response.getDetails().getFinal_bill_amount());
                data.put("payment_status", "Done");
                data.put("payment_id", "2");
                Log.d("**rental receipt debug====", "rental receipt debug");
                apiManager.execution_method_post("" + Config.ApiKeys.KEY_RENTAL_MAKE_PAYMENT, "" + Apis.URL_RENTAL_PAYMENT, data, true,ApiManager.ACTION_SHOW_DIALOG);                webView.canGoBack();

            }else {
                //for unsuccessful payment, close the webview and show the payment screen to the user
                Toast.makeText(this, "Transaction is unsuccessful. Please try again!", Toast.LENGTH_SHORT).show();
                webLayout.setVisibility(View.GONE);
            }

            Log.e("RESULT_CONFIRM_PAYMENT---", String.valueOf(result));
        }

    }

    @Override
    public void onFetchResultZero(String s) {

    }


    private void finalizeActivity() {
        finish();
        try {
            RentalTrackActivity.trackRideActivity.finish();
        } catch (Exception e) {

        }

        if (!Config.app_loaded_from_initial) {
            startActivity(new Intent(RentalReceiptActivity.this, SplashActivity.class));
        }
    }

    private void setViewPaymentDone() {

        pickLocationTxt.setText("" + done_ride_response.getDetails().getBegin_location());
        dropLocationTxt.setText("" + done_ride_response.getDetails().getEnd_location());
       // startMeterTxt.setText("" + done_ride_response.getDetails().getStart_meter_reading());
       // endMeterTxt.setText("" + done_ride_response.getDetails().getEnd_meter_reading());
        Glide.with(this).load(Apis.imageDomain + "" + done_ride_response.getDetails().getDriverimage()).into(driverImage);
        driver_name.setText("" + done_ride_response.getDetails().getDrivername());

        totalPaybleTop.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getFinal_bill_amount());
       // tvRideDistance.setText("" + done_ride_response.getDetails().getTotal_distance_travel());
        totalHoursTxt.setText("" + done_ride_response.getDetails().getTotal_time_travel());

        basePackageTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__package) + done_ride_response.getDetails().getRental_package_distance() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__for) + done_ride_response.getDetails().getRental_package_hours() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__hours));
        basePackagePrice.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getRental_package_price());

      /*  extraDistanceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_distance_travel) + done_ride_response.getDetails().getExtra_distance_travel() + " )");
        extraDistancePriceTxt.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getExtra_distance_travel_charge());
*/
      /*  extraTimePriceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_time) + sessionManager.getCurrencyCode()+ done_ride_response.getDetails().getExtra_hours_travel() + ")");
        extraTimePriceTxt.setText("" + done_ride_response.getDetails().getExtra_hours_travel_charge());
*/
        totalPriceTxt.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getTotal_amount());  /// need to be changes later
        totalPaybleBottom.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getFinal_bill_amount());

        if (!done_ride_response.getDetails().getCoupan_code().equals("")) {
            couponPriceTxt.setVisibility(View.VISIBLE);
            couponTxt.setVisibility(View.VISIBLE);
            couponTxt.setText("Coupon Applied " + "(" + done_ride_response.getDetails().getCoupan_code() + ")");
            couponPriceTxt.setText("" + sessionManager.getCurrencyCode()+ done_ride_response.getDetails().getCoupan_price());
        } else {
            couponPriceTxt.setVisibility(View.GONE);
            couponTxt.setVisibility(View.GONE);
        }
    }


    private void setViewPaymentNotDone() {

        pickLocationTxt.setText("" + done_ride_response.getDetails().getBegin_location());
        dropLocationTxt.setText("" + done_ride_response.getDetails().getEnd_location());
        // startMeterTxt.setText("" + done_ride_response.getDetails().getStart_meter_reading());
        // endMeterTxt.setText("" + done_ride_response.getDetails().getEnd_meter_reading());

        totalPaybleTop.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getFinal_bill_amount());
        // tvRideDistance.setText("" + done_ride_response.getDetails().getTotal_distance_travel());
        totalHoursTxt.setText("" + done_ride_response.getDetails().getTotal_time_travel());

        basePackageTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__package) + done_ride_response.getDetails().getRental_package_distance() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__for) + done_ride_response.getDetails().getRental_package_hours() + " " + getString(R.string.RENTAL_RECEIPT_ACTIVITY__hours));
        basePackagePrice.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getRental_package_price());

      /*  extraDistanceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_distance_travel) + done_ride_response.getDetails().getExtra_distance_travel() + " )");
        extraDistancePriceTxt.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getExtra_distance_travel_charge());
*/
      /*  extraTimePriceTxt.setText(getString(R.string.RENTAL_RECEIPT_ACTIVITY__extra_time) + sessionManager.getCurrencyCode()+ done_ride_response.getDetails().getExtra_hours_travel() + ")");
        extraTimePriceTxt.setText("" + done_ride_response.getDetails().getExtra_hours_travel_charge());
*/
        totalPriceTxt.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getTotal_amount());  /// need to be changes later
        totalPaybleBottom.setText(sessionManager.getCurrencyCode()+ "" + done_ride_response.getDetails().getFinal_bill_amount());

        if (!done_ride_response.getDetails().getCoupan_code().equals("")) {
            couponPriceTxt.setVisibility(View.VISIBLE);
            couponTxt.setVisibility(View.VISIBLE);
            couponTxt.setText("Coupon Applied " + "(" + done_ride_response.getDetails().getCoupan_code() + ")");
            couponPriceTxt.setText("" + sessionManager.getCurrencyCode()+ done_ride_response.getDetails().getCoupan_price());
        } else {
            couponPriceTxt.setVisibility(View.GONE);
            couponTxt.setVisibility(View.GONE);
        }
    }

    public void dialogForRating() {
        dialog = new Dialog(RentalReceiptActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_rating);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        LinearLayout lldone = (LinearLayout) dialog.findViewById(R.id.lldone);
        final EditText comments = (EditText) dialog.findViewById(R.id.comments);

        lldone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());
                if (rating.equals("0.0")) {
                    Toast.makeText(RentalReceiptActivity.this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("rating_star", "" + ratingBar.getRating());
                    data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                    data.put("comment", "" + comments.getText().toString());
                    data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
                    data.put("driver_id", "" + done_ride_response.getDetails().getDriver_id());
                    data.put("app_id", "1");
                    apiManager.execution_method_post(Config.ApiKeys.KEY_RENTAL_RATING, Apis.Rating, data, true,ApiManager.ACTION_SHOW_DIALOG);
                }
            }
        });
        dialog.show();
    }


    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }



    private void shodialogForPaymentDone() {
        final Dialog dialog = new Dialog(RentalReceiptActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
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

    private void showpaymentMethod() throws Exception {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RentalReceiptActivity.this);
        builderSingle.setTitle(R.string.Payment_Failed_select_payment_option);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RentalReceiptActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Pay with Card");
        arrayAdapter.add("CASH");

        builderSingle.setNegativeButton("" + this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(RentalReceiptActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle(R.string.are_you_syre_you_want_to_proceed_with);
                builderInner.setPositiveButton("" + RentalReceiptActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (strName.equals("Pay with Card")) {
                            try {
                              //  Toast.makeText(RentalReceiptActivity.this, "paystack---one", Toast.LENGTH_SHORT).show();
                                startPaystackPayment();
                            } catch (Exception e) {
                                Toast.makeText(RentalReceiptActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (strName.equals("CASH")) {

                            HashMap<String, String> data = new HashMap<String, String>();
                            data.put("rental_booking_id",""+ done_ride_response.getDetails().getRental_booking_id());
                            data.put("amount_paid", ""+done_ride_response.getDetails().getFinal_bill_amount());
                            data.put("payment_status", "CASH");
                            data.put("payment_id", "1" );

                            apiManager.execution_method_post("" + Config.ApiKeys.Key_payment_api, "" + Apis.URL_RENTAL_PAYMENT,  data, true, ApiManager.ACTION_SHOW_TOP_BAR);
                        }
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    private void startPaystackPayment() throws Exception {
      //  Toast.makeText(this, "paystack_one----1", Toast.LENGTH_SHORT).show();
       /* Intent intent = new Intent(RentalReceiptActivity.this, PayWithPaystackActivity.class);
        intent.putExtra("pay_amount", "300");
        intent.putExtra("user_id", sessionManager.getUserDetails().get(SessionManager.USER_ID));
        startActivityForResult(intent, REQUEST_RENTAL_CODE_PAYMENT);
*/
        pay_amount = done_ride_response.getDetails().getFinal_bill_amount();

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("user_id",sessionManager.getUserDetails().get(SessionManager.USER_ID));
        hashMap.put("email", email);
        hashMap.put("amount",pay_amount);
        hashMap.put("ride_id", getIntent().getExtras().getString("ride_id"));
        hashMap.put("ride_mode", "2");

        Log.d("**HASHMAP===", String.valueOf(hashMap));

        apiManager.execution_paystack(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK,  Apis.PAY_WITH_PAYSTACK, hashMap, true, ApiManager.ACTION_SHOW_DIALOG);
    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("RESULT_CODE--", String.valueOf(resultCode));

        switch (REQUEST_RENTAL_CODE_PAYMENT) {
            case 144:
                try {
                    responseCode = data.getExtras().getBoolean("response");
                    Log.d("**responseCode_paymentFailed--", String.valueOf(responseCode));

                    payment_result = data.getExtras().getInt("payment_result");
                    Log.e("PAYMNET__RESULT-----", String.valueOf(payment_result));
                    //  Toast.makeText(this, "receivedData" + responseCode, Toast.LENGTH_SHORT).show();
                    if (payment_result == 1){
                        ok.setVisibility(View.VISIBLE);
                        //set the data
                   //     apiManager.execution_method_get("" + Config.ApiKeys.Key_payment_api, "" + Apis.payment + "order_id=" + doneRideInfo.getMsg().getDone_ride_id() + "&user_id=" + doneRideInfo.getMsg().getUser_id() + "&payment_id=" + doneRideInfo.getMsg().getPayment_option_id() + "&payment_method=" + "Cash" + "&payment_platform=" + "Android" + "&payment_amount=" + doneRideInfo.getMsg().getTotal_payable_amount().replace("" + Config.currency_symboll, "") + "&payment_date_time=" + "Anything" + "&payment_status=" + "Anything" + "&language_id=1", true, ApiManager.ACTION_SHOW_TOP_BAR);
                    }else {
                        ok.setVisibility(View.GONE);

                    }
                }catch (Exception e){
                    // Toast.makeText(this, "onActivity Result exception  "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }*/


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            Log.d("**LOAD OVERRIDE URL==",  url);
            Log.d("**LOAD OVERRIDE view==", String.valueOf(view));

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("**LOAD PAGEFINISH URL==",  url);
            Log.d("**created url==", callback_url+"?trxref=" + reference_number + "&reference=" + reference_number);

            if (url.equals(callback_url+"?trxref=" + reference_number + "&reference=" + reference_number) || url.equals("https://standard.paystack.co/close")){
                Log.d("**created url==", callback_url+"?trxref=" + reference_number + "&reference=" + reference_number);
                HashMap<String, String> body = new HashMap<String, String>();
                body.put("token", reference_number);
                body.put("user_id", sessionManager.getUserDetails().get(SessionManager.USER_ID));

                apiManager.execution_method_post(Config.ApiKeys.CONFIRM_PAYMENT,  Apis.CONFIRM_PAYMENT, body, true, ApiManager.ACTION_SHOW_DIALOG);
                Log.e("**API_CONFIRM_PAYMENT===",Apis.CONFIRM_PAYMENT+"token="+reference_number);
                Log.d("**USER_ID===", sessionManager.getUserDetails().get(SessionManager.USER_ID));

            }
        }
    }
}
