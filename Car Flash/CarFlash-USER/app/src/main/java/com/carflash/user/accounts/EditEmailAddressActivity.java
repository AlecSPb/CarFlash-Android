package com.carflash.user.accounts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.carflash.user.R;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.HashMap;

public class EditEmailAddressActivity extends AppCompatActivity implements ApiManager.APIFETCHER{

    LinearLayout root ;
    ApiManager apimanager ;
    EditText email_edt_text ;
    GsonBuilder gsonBuilder;
    Gson gson;
    SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apimanager = new ApiManager(this , this  , this );
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        sessionManager  = new SessionManager(EditEmailAddressActivity.this);
        setContentView(R.layout.activity_edit_email_address);
        email_edt_text = (EditText) findViewById(R.id.email_edt_text);
        root = (LinearLayout) findViewById(R.id.root);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        findViewById(R.id.save_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if(email_edt_text.getText().toString().equals("")){
                    Toast.makeText(EditEmailAddressActivity.this, "Required Field Empty !", Toast.LENGTH_SHORT).show();
                }else {
                    if(com.carflash.user.samwork.Config.isEmailValid(email_edt_text.getText().toString())  && email_edt_text.getText().toString().contains(".com")){

                        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
                        bodyparameters.put("user_id" ,""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
                        bodyparameters.put("email" ,""+ email_edt_text.getText().toString());
                        apimanager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_UPDATE_EMAIL,  Apis.URL_UPDATE_EMAIL, bodyparameters , true,ApiManager.ACTION_SHOW_TOP_BAR);
                    }else {
                        Toast.makeText(EditEmailAddressActivity.this, "Please Enter valid Email Address !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_edt_text.setText("");
            }
        });






    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
            if(APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_UPDATE_EMAIL){
                if(rcheck.getResult() == 1){
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    sessionManager.createLoginSession(registration_response.getDetails().getUser_id() , registration_response.getDetails().getUser_name() , registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(),registration_response.getDetails().getUser_phone(),registration_response.getDetails().getUser_image(),registration_response.getDetails().getUser_password(),registration_response.getDetails().getLogin_logout(),registration_response.getDetails().getDevice_id(),
                            registration_response.getDetails().getFacebook_id(),registration_response.getDetails().getFacebook_mail(),registration_response.getDetails().getFacebook_image(),registration_response.getDetails().getFacebook_firstname(),registration_response.getDetails().getFacebook_lastname(),
                            registration_response.getDetails().getGoogle_id(),registration_response.getDetails().getGoogle_name(),registration_response.getDetails().getGoogle_mail(),registration_response.getDetails().getGoogle_image(), sessionManager.getUserDetails().get(SessionManager.LOGIN_TYPE) , registration_response.getDetails().getUnique_number());

                    Toast.makeText(this, "Email Updated !", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Some Problem occurred !", Toast.LENGTH_SHORT).show();

                }
            }}catch (Exception e){}


    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
