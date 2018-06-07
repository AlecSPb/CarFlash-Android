package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.QueryResponseModel;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomerSupportActivity extends Activity implements  ApiManager.APIFETCHER{


    @Bind(R.id.user_name_edt)EditText user_name_edt;
    @Bind(R.id.email_edt) EditText email_edt;
    @Bind(R.id.pone_edt) EditText pone_edt;
    @Bind(R.id.query_edt) EditText query_edt;
    @Bind(R.id.root)
    LinearLayout root;

    SessionManager sessionManager ;
    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this , this , this );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        ButterKnife.bind(this);


        findViewById(R.id.ll_back_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_name_edt.getText().toString().equals("")  || email_edt.getText().toString().equals("") || query_edt.getText().toString().equals("") ){
                    Snackbar.make(root , R.string.please_enter_the_maindatory_details , Snackbar.LENGTH_SHORT).show();
                }else {
                    HashMap<String , String > data = new HashMap<String, String>();
                    data.put("application" , "1");
                    data.put("name" , ""+user_name_edt.getText().toString());
                    data.put("email" , ""+email_edt.getText().toString());
                    data.put("phone" , ""+pone_edt.getText().toString());
                    data.put("query" , ""+query_edt.getText().toString());
                    data.put("driver_id" , "");
                    data.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
                    apiManager.execution_method_post(""+Config.ApiKeys.KEY_CUSTOMER_SUPPORT , ""+Apis.CustomerSupport , data, true,ApiManager.ACTION_SHOW_TOP_BAR);                   }
            }
        });

    }


    public void dialogForQueryComplete(String message ) {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_query_complete);
        dialog.setCancelable(false);
        TextView quer_response_message  = (TextView) dialog.findViewById(R.id.quer_response_message);
        quer_response_message.setText(""+message);

        LinearLayout ll_update = (LinearLayout) dialog.findViewById(R.id.ll_ok);
        ll_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if(APINAME.equals(""+Config.ApiKeys.KEY_CUSTOMER_SUPPORT)){
                QueryResponseModel data_response;
                data_response = gson.fromJson(""+script, QueryResponseModel.class);
                if(data_response.getStatus() == 1){
                    dialogForQueryComplete(""+data_response.getMessage());
                }
            }}catch (Exception e){}

    }


    @Override
    public void onFetchResultZero(String s) {

    }


}

