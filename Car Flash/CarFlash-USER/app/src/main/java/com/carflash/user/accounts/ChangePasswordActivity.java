package com.carflash.user.accounts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carflash.user.R;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity implements ApiManager.APIFETCHER {


    EditText new_password_edt ,  confirm_password_edt  ;
    LinearLayout root ;
    ApiManager apiManager ;
    SessionManager sessionManager  ;
    GsonBuilder gsonBuilder;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(ChangePasswordActivity.this);
        new_password_edt = (EditText) findViewById(R.id.new_password_edt);
        confirm_password_edt = (EditText) findViewById(R.id.confirm_password_edt);
        root = (LinearLayout) findViewById(R.id.root);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();




        findViewById(R.id.change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new_password_edt.getText().toString().equals("") || confirm_password_edt.getText().toString().equals("")){

                    Toast.makeText(ChangePasswordActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                }else {
                    if(new_password_edt.getText().toString().equals(confirm_password_edt.getText().toString())){
                        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
                        bodyparameters.put("user_id" ,""+ getIntent().getExtras().getString(com.carflash.user.samwork.Config.IntentKeys.KEY_USER_ID));
                        bodyparameters.put("old_password" ,""+ getIntent().getExtras().getString(com.carflash.user.samwork.Config.IntentKeys.KEY_OLD_PASSWORD));
                        bodyparameters.put("new_password" ,""+ confirm_password_edt.getText().toString());
                        apiManager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_CHANGE_PASSWORD, Apis.URL_CHANGE_PASSWORD, bodyparameters , true,ApiManager.ACTION_SHOW_TOP_BAR);

                    }else {
                        Toast.makeText(ChangePasswordActivity.this, "Password does not matches", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }





    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);
            if (rcheck.getResult() == 1) {
                Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Something problem occured", Toast.LENGTH_SHORT).show();
            }}catch (Exception e){
        }


    }


    @Override
    public void onFetchResultZero(String s) {

    }

}
