package com.carflash.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.carflash.user.accounts.RegistrationModel;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;


public class Verify_OTP extends AppCompatActivity implements ApiManager.APIFETCHER {

    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    RegistrationModel.OTP_Details otp_details;
    private TextView otpError_txt;
    private EditText otp_input, edt_enter_phone;
    private LinearLayout submit_otp_layout;
    SessionManager sessionManager ;
    Gson gson;
    String code, input_phone_number, otp;
    CountryCodePicker codePicker;
    private static final int KEY_REGISTER = 110;
    Button generate_otp;
    LinearLayout submit_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(Verify_OTP.this);
        otp_details = new RegistrationModel.OTP_Details();
        setContentView(R.layout.activity_verify__otp);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        submit_otp = (LinearLayout) findViewById(R.id.otp_submit);
        generate_otp = (Button) findViewById(R.id.generate_otp);
        edt_enter_phone = (EditText)findViewById(R.id.edt_enter_phone);
        otp_input = (EditText)findViewById(R.id.otp_edt);
        otpError_txt = (TextView)findViewById(R.id.otp_verifier_txt);
        submit_otp_layout = (LinearLayout)findViewById(R.id.otp_submit);
        codePicker = (CountryCodePicker)findViewById(R.id.otp_ccp);

        submit_otp.setEnabled(false);

        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_number = edt_enter_phone.getText().toString().trim();
                Log.e("input_phone_number====", input_phone_number);
                code = codePicker.getSelectedCountryCodeWithPlus();
                Log.e("COUNTRY_CODE_PICKER===", code);
                if (input_phone_number.equals("")){
                    Toast.makeText(Verify_OTP.this, R.string.required_field_missing, Toast.LENGTH_SHORT).show();
                }else {
                    getOTP(code+input_phone_number);

                }
            }
        });

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp_input.getText().toString().equals("")) {
                    Toast.makeText(Verify_OTP.this, R.string.required_field_missing, Toast.LENGTH_SHORT).show();
                } else if (!otp_input.getText().toString().equals(otp)) {
                    Toast.makeText(Verify_OTP.this, R.string.invalid_otp, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("phone_number", code + input_phone_number);
                    setResult(Activity.RESULT_OK, intent);
                    finish();


                }
            }
        });
    }


    private void getOTP(String phone) {
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" , phone);
        bodyparameters.put("flag" ,"1");
        apiManager.execution_method_post(Config.ApiKeys.KEY_VERIFY_OTP ,""+  Apis.SEND_OTP, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {

        if(APINAME.equals(""+Config.ApiKeys.KEY_VERIFY_OTP)){
            submit_otp.setEnabled(true);

            try{
                RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
                Log.e("**OTP_SCRIPT-----", String.valueOf(script));

                if(otp_response.getStatus()== 1){
                    Log.e("**RCHHECKK---", String.valueOf(otp_response.getStatus()));
                    otp =otp_response.getOtp();
                    Toast.makeText(this, otp_response.getMessage(), Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(this, ""+otp_response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){}

//            RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
//            //  finilalizeActivity();
//            Log.e("**OTP_SCRIPT-----", String.valueOf(otp_response.getMessage() + otp_response.getOtp() + otp_response.getStatus()));
//            otp = otp_response.getOtp();
//
//            Log.d("otp==normal sign up==", otp);
//            otp_input.setText(""+otp);
//            otp_input.requestFocus();
//            if(otp_response.getStatus() == 1){
//               // otp_input.setText(""+otp);
//            }

        }
//        else {
//            try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
//                Log.e("**OTP_SCRIPT-----", String.valueOf(script));
//
//                if(rcheck.getResult() == 1){
//                    Log.e("**RCHHECKK---", String.valueOf(rcheck.getResult()));
//                    RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
//                }else {
//                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
//                    Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }catch (Exception e){}
        //}



    }


    @Override
    public void onFetchResultZero(String s) {

    }

}

