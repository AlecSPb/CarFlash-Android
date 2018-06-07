package com.carflash.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.accounts.LoginActivity;
import com.carflash.user.accounts.RegistrationModel;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import java.util.HashMap;


public class ForgotPass_Verify_OTP extends AppCompatActivity implements ApiManager.APIFETCHER {

    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    RegistrationModel.OTP_Details otp_details;
    private TextView otpError_txt;
    private EditText otp_input, edt_enter_phone;
    private LinearLayout submit_otp_layout;
    SessionManager sessionManager ;
    Gson gson;
    String input_OTP, code, otp, input_phone_number;
    CountryCodePicker codePicker;
    Button generate_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(ForgotPass_Verify_OTP.this);
        otp_details = new RegistrationModel.OTP_Details();
        setContentView(R.layout.activity_forgotpass_verify_otp);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        generate_otp = (Button) findViewById(R.id.generate_otp);
        edt_enter_phone = (EditText)findViewById(R.id.edt_enter_phone);
        otp_input = (EditText)findViewById(R.id.otp_edt);
        otpError_txt = (TextView)findViewById(R.id.otp_verifier_txt);
        submit_otp_layout = (LinearLayout)findViewById(R.id.otp_submit);
        codePicker = (CountryCodePicker)findViewById(R.id.otp_ccp);


        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_number = edt_enter_phone.getText().toString().trim();
                Log.e("input_phone_number====", input_phone_number);
                code = codePicker.getSelectedCountryCodeWithPlus();
                Log.e("COUNTRY_CODE_PICKER===", code);
                if (input_phone_number.equals("")){
                    Toast.makeText(ForgotPass_Verify_OTP.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else {
                    getOTP(code+input_phone_number);

                }
            }
        });

        submit_otp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                input_OTP = otp_input.getText().toString().trim();

                if (input_OTP.equals("")) {
                    Toast.makeText(ForgotPass_Verify_OTP.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }
                else if (!otp_input.getText().toString().equals(otp)) {
                    Log.e("otp_details--elseIF", String.valueOf(otp_details.getOtp()));
                    Toast.makeText(ForgotPass_Verify_OTP.this, "Invalid OTP !", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("otp_details--lastelse", String.valueOf(getIntent().getStringExtra("otp")));
                    Log.e("otp_details--edittext", otp_input.getText().toString());

                    Intent intent = new Intent(ForgotPass_Verify_OTP.this, ForgotPass_ChangePass.class);
                    intent.putExtra("phone_number", code+input_phone_number);
                    startActivity(intent);

                }

            }
        });
    }


    private void getOTP(String phone) {

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" , phone);
        bodyparameters.put("flag" ,"1");
        apiManager.execution_method_post(Config.ApiKeys.KEY_FORGOTPASS_OTP ,""+  Apis.FORGOTPASS_OTP, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {

        if(APINAME.equals(""+Config.ApiKeys.KEY_FORGOTPASS_OTP)){
            RegistrationModel.ForgotPass_OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.ForgotPass_OTP_Details.class);
            //  finilalizeActivity();
            Log.e("**OTP_SCRIPT-----", String.valueOf(otp_response.getOtp() + otp_response.getOtp()));
            if (otp_response.getResult() == 1) {
                otp = otp_response.getOtp();

                Log.d("otp==normal sign up==", otp);
//                otp_input.setText("" + otp);
                otp_input.requestFocus();
                Toast.makeText(this, otp_response.getMsg(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, otp_response.getMsg(), Toast.LENGTH_SHORT).show();

            }
        }


    }


    @Override
    public void onFetchResultZero(String s) {

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPass_Verify_OTP.this, LoginActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
