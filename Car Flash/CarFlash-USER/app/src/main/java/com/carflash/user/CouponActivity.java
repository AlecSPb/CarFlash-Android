package com.carflash.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.ApplyCouponResponse;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CouponActivity extends FragmentActivity implements  ApiManager.APIFETCHER{
    public static Activity couponActivity;
    EditText couponcode;
    TextView tv_coupon_invalid, tv_coupon_error, tv_coupon_text;
    SessionManager sessionManager ;
    ApiManager apiManager;


    LanguageManager languageManager;
    LinearLayout ll_cancel, ll_apply, ll_back_coupons;

    String coupon_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this , this , this );
        setContentView(R.layout.activity_coupon);
        couponActivity = this;

        tv_coupon_invalid = (TextView) findViewById(R.id.tv_coupon_invalid);
   //     tv_coupon_error = (TextView) findViewById(R.id.tv_coupon_error);
        tv_coupon_text = (TextView) findViewById(R.id.tv_coupon_text);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_apply = (LinearLayout) findViewById(R.id.ll_apply);
        ll_back_coupons = (LinearLayout) findViewById(R.id.ll_back_coupons);
        couponcode = (EditText) findViewById(R.id.editTextcc);
        tv_coupon_text.setVisibility(View.GONE);

        couponcode.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));

        languageManager = new LanguageManager(this);
        final String language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                finish();
                overridePendingTransition(R.anim.animation5, R.anim.animation6);
            }
        });

        ll_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_text = couponcode.getText().toString();
                apiManager.execution_method_get(""+ Config.ApiKeys.KeyApplyCoupon, ""+ Apis.applyCoupon+"?coupon_code="+coupon_text+"&user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);
            }
        });

        ll_back_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+ Config.ApiKeys.KeyApplyCoupon)) {
                ApplyCouponResponse applyCouponResponse;
                applyCouponResponse = gson.fromJson(""+script, ApplyCouponResponse.class);

                if (applyCouponResponse.getResult() == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("coupon_code", coupon_text);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    tv_coupon_text.setVisibility(View.GONE);
               //     tv_coupon_error.setVisibility(View.VISIBLE);
                    tv_coupon_invalid.setVisibility(View.VISIBLE);
                    couponcode.setText("");
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
