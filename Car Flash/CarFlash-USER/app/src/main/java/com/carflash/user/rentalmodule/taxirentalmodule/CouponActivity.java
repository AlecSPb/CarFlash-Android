package com.carflash.user.rentalmodule.taxirentalmodule;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.carflash.user.R;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class CouponActivity extends FragmentActivity implements ApiManager.APIFETCHER {

    public static Activity couponActivity;
    EditText couponcode;
    TextView tv_coupon_invalid, tv_coupon_error, tv_coupon_text;
    LinearLayout ll_cancel, ll_apply, ll_back_coupons;
    ProgressDialog pd;
    String coupon_text;
    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        couponActivity = this;

        tv_coupon_invalid = (TextView) findViewById(R.id.tv_coupon_invalid);
     //   tv_coupon_error = (TextView) findViewById(R.id.tv_coupon_error);
      //  tv_coupon_text = (TextView) findViewById(R.id.tv_coupon_text);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_apply = (LinearLayout) findViewById(R.id.ll_apply);
        ll_back_coupons = (LinearLayout) findViewById(R.id.ll_back_coupons);
        couponcode = (EditText) findViewById(R.id.editTextcc);

        couponcode.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.loading));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        apiManager = new ApiManager(this, this , this);

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
                apiManager.execution_method_get(""+ Config.ApiKeys.KEY_RENTAl_Coupons , ""+ Apis.coupon+"?coupon_code="+coupon_text+"&user_id="+ Config.User_id+"&language_id=1",true , ApiManager.ACTION_SHOW_TOAST);

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

            if (APINAME.equals(""+ Config.ApiKeys.KEY_RENTAl_Coupons)) {
                DeviceId deviceId;
                deviceId = gson.fromJson(""+script, DeviceId.class);

                if (deviceId.getResult().toString().equals("1")) {
                    Intent intent = new Intent();
                    intent.putExtra("coupon_code", coupon_text);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                   // tv_coupon_text.setVisibility(View.GONE);
                  //  tv_coupon_error.setVisibility(View.VISIBLE);
                    tv_coupon_invalid.setVisibility(View.VISIBLE);
                    couponcode.setText("");
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
