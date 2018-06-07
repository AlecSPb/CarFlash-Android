package com.carflash.user.accounts;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.R;
import com.carflash.user.TrialActivity;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoogleSignUpActivity extends AppCompatActivity implements ApiManager.APIFETCHER {


    ApiManager apiManager ;
    GsonBuilder gsonBuilder;
    Gson gson;
    SessionManager sessionManager ;
    TextView facebook_name_txt  , google_email_txt ;
    EditText phone_edt, otp_edt ;
    CircleImageView google_image ;
    CountryCodePicker ccp ;
    String otp, code,input_phone_number, input_OTP;
    Button generate_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiManager = new ApiManager(this , this , this  );
        sessionManager = new SessionManager(GoogleSignUpActivity.this);
        setContentView(R.layout.activity_google_sign_up);

        generate_otp = (Button) findViewById(R.id.generate_otp);
        facebook_name_txt = (TextView) findViewById(R.id.facebook_name_txt);
        google_email_txt = (TextView) findViewById(R.id.google_email_txt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        google_image = (CircleImageView) findViewById(R.id.google_image);
        facebook_name_txt.setText(""+ Config.google_Name);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        otp_edt = (EditText)findViewById(R.id.otp_edt);


        google_email_txt.setText(""+Config.google_mail);
        Picasso.with(this).load(""+Config.google_image).placeholder(R.drawable.ic_google).into(google_image);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_number = phone_edt.getText().toString().trim();
                Log.e("input_phone_number====", input_phone_number);
                code = ccp.getSelectedCountryCodeWithPlus();
                Log.e("COUNTRY_CODE_PICKER===", code);
                if (input_phone_number.equals("")){
                    Toast.makeText(GoogleSignUpActivity.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else {
                    getOTP(code+input_phone_number);

                }
            }
        });


        findViewById(R.id.ll_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                input_OTP = otp_edt.getText().toString();
//                Log.e("INPUT_OTP---", input_OTP);
//                if(input_OTP.equals("")){
//                    Toast.makeText(GoogleSignUpActivity.this, "Required field empty !", Toast.LENGTH_SHORT).show();
//                }else if (!otp_edt.getText().toString().equals(otp)) {
//                    Toast.makeText(GoogleSignUpActivity.this, "Invalid OTP !", Toast.LENGTH_SHORT).show();
//                }else {

                code = ccp.getSelectedCountryCodeWithPlus();
                input_phone_number = phone_edt.getText().toString().trim();
                if (input_phone_number.equals("")){
                    Toast.makeText(GoogleSignUpActivity.this, "Required field empty !", Toast.LENGTH_SHORT).show();
                }else {
                    SignupWithGoogle(Config.google_Id , Config.google_mail, Config.google_image, Config.google_Name, code+input_phone_number);
                }
            }
        });
    }

    public  void getOTP(String Phone){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" , Phone);
        bodyparameters.put("flag" , "1");

        Log.e("**HASHMAP_OTP", String.valueOf(bodyparameters));
        apiManager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_VERIFY_OTP,  Apis.SEND_OTP, bodyparameters , true,ApiManager.ACTION_SHOW_TOP_BAR);
    }


    public void                          SignupWithGoogle(String google_user_id , String mail , String image , String fullname , String Phone){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("google_id" ,""+ google_user_id);
        bodyparameters.put("google_name" ,""+ fullname);
        bodyparameters.put("google_mail" ,""+ mail);
        bodyparameters.put("google_image" ,""+ image);
        bodyparameters.put("phone" , Phone);
        bodyparameters.put("first_name" ,""+ fullname);
        bodyparameters.put("last_name" ,".");
        bodyparameters.put("user_email" ,""+mail);
        apiManager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_GOOGLE_SINGNUP,  Apis.URL_GOOGLE_SIGNUP, bodyparameters , true ,ApiManager.ACTION_SHOW_TOP_BAR);
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        if(APINAME.equals(""+com.carflash.user.samwork.Config.ApiKeys.KEY_VERIFY_OTP)){
            RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
            //  finilalizeActivity();
            Log.e("**OTP_SCRIPT-----", String.valueOf(otp_response.getMessage() + otp_response.getOtp() + otp_response.getStatus()));

            otp = otp_response.getOtp();
            Log.d("otp===google sign up==", otp);
            otp_edt.requestFocus();

        }else {
            try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
                Log.e("**OTP_SCRIPT-----", String.valueOf(script));

                if(rcheck.getResult() == 1){
                    RegistrationModel.OTP_Details otp_response = gson.fromJson("" + script, RegistrationModel.OTP_Details.class);
                    //  finilalizeActivity();
                }else {
                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                    Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){}
        }


        try {
            ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
            if (rcheck.getResult() == 1) {
                RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                sessionManager.createLoginSession(registration_response.getDetails().getUser_id(), registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(), registration_response.getDetails().getUser_phone(), registration_response.getDetails().getUser_image(), registration_response.getDetails().getUser_password(), registration_response.getDetails().getLogin_logout(), registration_response.getDetails().getDevice_id(),
                        registration_response.getDetails().getFacebook_id(), registration_response.getDetails().getFacebook_mail(), registration_response.getDetails().getFacebook_image(), registration_response.getDetails().getFacebook_firstname(), registration_response.getDetails().getFacebook_lastname(),
                        registration_response.getDetails().getGoogle_id(), registration_response.getDetails().getGoogle_name(), registration_response.getDetails().getGoogle_mail(), registration_response.getDetails().getGoogle_image(), SessionManager.LOGIN_GOOGLE, registration_response.getDetails().getUnique_number());
                finilalizeActivity();

            } else {
                ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                Toast.makeText(this, "" + rr.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onFetchResultZero(String s) {

    }






    private void finilalizeActivity() {


        try{
            LoginActivity.activity.finish();
        }catch (Exception e ){

        }

        try{
            TrialActivity.mainActivity.finish();
        }catch (Exception e ){

        }

        getLocation();

    }




    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(GoogleSignUpActivity.this, TrialActivity.class)
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(GoogleSignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


}
