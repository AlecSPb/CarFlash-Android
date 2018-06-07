package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.RatingResponse;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InvoiceActivity extends AppCompatActivity implements  ApiManager.APIFETCHER{

    TextView tv_date_invoice, tv_order_id, tv_payment_id, tv_amount , distance_txt ,wait_time_txt , wait_charge_txt , coupon_amount_txt , driver_name_txt , customer_name_txt , customer_phone_txt;
    LinearLayout  ll_rate;
    public static Activity invoiceactivity;

    LanguageManager languageManager;

    String ride_id;

    Dialog dialog;

    SessionManager sessionManager;
    String user_id, driverId, language_id,order_id;
    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoiceactivity = this;
        apiManager = new ApiManager(this , this , this );

        sessionManager = new SessionManager(this);

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        user_id = sessionManager.getUserDetails().get(SessionManager.USER_ID);
        driverId = super.getIntent().getExtras().getString("driverId");
        ride_id = super.getIntent().getExtras().getString("ride_id");

        tv_date_invoice = (TextView) findViewById(R.id.tv_date_invoice);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_payment_id = (TextView) findViewById(R.id.tv_payment_id);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        ll_rate = (LinearLayout) findViewById(R.id.ll_rate);
        /////////////////////////////////////////////////////////
        distance_txt = (TextView) findViewById(R.id.distance_txt);
        wait_time_txt = (TextView) findViewById(R.id.wait_time_txt);
        wait_charge_txt = (TextView) findViewById(R.id.wait_charge_txt);
        coupon_amount_txt = (TextView) findViewById(R.id.coupon_amount_txt);



        driver_name_txt = (TextView) findViewById(R.id.driver_name_txt);
        customer_name_txt = (TextView) findViewById(R.id.customer_name_txt);
        customer_phone_txt = (TextView) findViewById(R.id.customer_phone_txt);





        distance_txt.setText(""+getIntent().getExtras().getString("distance"));
        wait_time_txt.setText(""+getIntent().getExtras().getString("wait_time"));
        wait_charge_txt.setText(""+getIntent().getExtras().getString("wait_charge"));
        coupon_amount_txt.setText(""+getIntent().getExtras().getString("coupon_amount_txt"));

        driver_name_txt.setText(""+getIntent().getExtras().getString("driver_name_txt"));
        customer_name_txt.setText(""+getIntent().getExtras().getString("customer_name_txt"));
        customer_phone_txt.setText(""+getIntent().getExtras().getString("customer_phone_txt"));
        tv_payment_id.setText(super.getIntent().getExtras().getString("payment_method"));



        String paymentid = super.getIntent().getExtras().getString("payment_id");

        if (paymentid.equals("1")) {
            tv_date_invoice.setText(super.getIntent().getExtras().getString("payment_date"));
            order_id= super.getIntent().getExtras().getString("order_id");
            tv_order_id.setText(order_id);
            tv_amount.setText(sessionManager.getCurrencyCode()+super.getIntent().getExtras().getString("payment_amount"));
        } else {
            tv_date_invoice.setText(super.getIntent().getExtras().getString("payment_date"));
             order_id= super.getIntent().getExtras().getString("order_id");
            tv_order_id.setText(order_id);

            tv_amount.setText(sessionManager.getCurrencyCode()+super.getIntent().getExtras().getString("payment_amount"));
        }

        ll_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForRating();
            }
        });
    }

    public void dialogForRating() {
        dialog = new Dialog(InvoiceActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_rating);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        LinearLayout lldone = (LinearLayout) dialog.findViewById(R.id.lldone);
        final EditText comments  = (EditText) dialog.findViewById(R.id.comments);

        lldone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());
                if (rating.equals("0.0")) {
                    Toast.makeText(InvoiceActivity.this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();
                } else {
                    apiManager.execution_method_get(""+Config.ApiKeys.Key_Rating_Api , ""+ Apis.rating+"ride_id="+ride_id+"&user_id="+user_id+"&driver_id="+driverId+"&rating_star="+rating+"&comment="+comments.getText().toString().replace(" " , "%20")+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_DIALOG);
                }
            }
        });
        dialog.show();
    }



    @Override
    public void onBackPressed() {

        try {
            TrackRideAactiviySamir.trackRideActivity.finish();
        }catch (Exception e ){

        }

        try{
            ReceiptActivity.receipt_activity.finish();
        }catch (Exception e){

        }

        if(!Config.app_loaded_from_initial){
            finish();
            startActivity(new Intent(this , SplashActivity.class));
        }else{
            finish();
        }
        super.onBackPressed();
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.Key_Rating_Api)) {

                RatingResponse rating_response;
                rating_response = gson.fromJson(""+script, RatingResponse.class);
                if (rating_response.getResult() == 1) {
                    Toast.makeText(this, ""+this.getResources().getString(R.string.rating_successfull), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                    try{
                        TrackRideAactiviySamir.trackRideActivity.finish();
                    }catch (Exception e ){

                    }

                    try{
                        ReceiptActivity.receipt_activity.finish();
                    }catch (Exception e){

                    }

                    if(!Config.app_loaded_from_initial){
                        startActivity(new Intent(InvoiceActivity.this , SplashActivity.class));
                    }
                } else {
                    Toast.makeText(this, "" + rating_response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}

