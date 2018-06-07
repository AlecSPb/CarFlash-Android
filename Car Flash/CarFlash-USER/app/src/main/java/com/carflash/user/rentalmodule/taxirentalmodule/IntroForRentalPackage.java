package com.carflash.user.rentalmodule.taxirentalmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class IntroForRentalPackage extends Activity implements ApiManager.APIFETCHER{

    ApiManager apiManager;
    View progress_bar ;
    ProgressBar initial_progress;
    Gson gson ;
    LinearLayout book_btn ;
    TextView vehicle_charter;
    LinearLayout intro_rental_layout;
    LinearLayout progress_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this );
        gson  = new GsonBuilder().create();
        setContentView(R.layout.activity_intro_for_rental_package);

        progress_layout = (LinearLayout)findViewById(R.id.progress_layout);
        initial_progress = (ProgressBar)findViewById(R.id.initial_progress);
        intro_rental_layout = (LinearLayout)findViewById(R.id.intro_rental_layout);
        book_btn = (LinearLayout) findViewById(R.id.book_btn);
    //   progress_bar = findViewById(R.id.progress_bar);
        vehicle_charter = (TextView)findViewById(R.id.vehicle_charter);

        Config.Base_Url = getIntent().getStringExtra(""+ Config.IntentKeys.Baseurl);
        Config.Image_Base_Url = getIntent().getStringExtra(""+ Config.IntentKeys.Image_base_url);
        Config.City_Id = getIntent().getStringExtra(""+ Config.IntentKeys.CityId);
        Config.User_id = getIntent().getStringExtra(""+ Config.IntentKeys.user_id);
        Config.User_latitude = getIntent().getStringExtra(""+ Config.IntentKeys.user_latitude);
        Config.User_longitude = getIntent().getStringExtra(""+ Config.IntentKeys.user_longitude);
        Config.User_location = getIntent().getStringExtra(""+ Config.IntentKeys.user_location);
        Config.currency_symbol = getIntent().getStringExtra(""+ Config.IntentKeys.currency_symboll);

        HashMap<String , String >data = new HashMap<>();
        data.put("city_id" , Config.City_Id);
/*

        if(Config.response == null){
            apiManager.execution_method_post(""+Config.ApiKyes.KEY_RENTAl_PACKAGE , ""+Config.APIS.RentalPackage , data);
        }
*/
        apiManager.execution_method_post(""+ Config.ApiKeys.KEY_RENTAl_PACKAGE , ""+ Apis.RentalPackage , data, true , ApiManager.ACTION_SHOW_TOAST);
        initial_progress.setVisibility(View.VISIBLE);
        book_btn.setVisibility(View.GONE);


        findViewById(R.id.book_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroForRentalPackage.this , RentalPackageActivity.class));
                finish();
            }
        });
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultStatusChecker resultStatusChecker = gson.fromJson(""+script ,ResultStatusChecker.class);
            if(resultStatusChecker.getStatus() == 1){
                RentalPackageResponse rentalPackageResponse = gson.fromJson(""+script, RentalPackageResponse.class);
                Log.e("**RENTAL_PACKAGE_RESPONSE-IF--", rentalPackageResponse.getDescription());
                initial_progress.setVisibility(View.GONE);
                intro_rental_layout.setVisibility(View.VISIBLE);
                book_btn.setVisibility(View.VISIBLE);
                Config.response = gson.fromJson("" +script,RentalPackageResponse.class);
                String charter_description = rentalPackageResponse.getDescription();
                vehicle_charter.setText(charter_description);
            }else {
                Log.e("**RENTAL_PACKAGE_RESPONSE-ELSE--", "rentalPackageResponse.getDescription()");

               // Toast.makeText(this, ""+resultStatusChecker.getStatus() +"  "+resultStatusChecker.getMessage(), Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
