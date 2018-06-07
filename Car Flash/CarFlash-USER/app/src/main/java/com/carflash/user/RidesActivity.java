package com.carflash.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.holders.HolderRideHistoryRentalDesign;
import com.carflash.user.models.NewRideHistoryModel;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.holders.HolderRideHistorySecondDesign;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam.placer.PlaceHolderView;

import java.util.HashMap;

public class RidesActivity extends AppCompatActivity implements ApiManager.APIFETCHER {

    LinearLayout ll_back_rides;
    TextView tv_all_rides;
    public static Activity ridesActivity;

    SessionManager sessionManager;
    ApiManager apiManager ;
    Gson gson ;


    PlaceHolderView place_holder  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        apiManager = new ApiManager(this , this , this );
        setContentView(R.layout.activity_rides);
        ridesActivity = this;
        place_holder = (PlaceHolderView) findViewById(R.id.place_holder);

        sessionManager = new SessionManager(this);

        ll_back_rides = (LinearLayout) findViewById(R.id.ll_back_rides);
        tv_all_rides = (TextView) findViewById(R.id.tv_all_rides);
        ll_back_rides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HashMap<String , String> data = new HashMap<>();
        data.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
        apiManager.execution_method_post(""+ Config.ApiKeys.KEY_REST_RIDE_HISTORY, ""+ Apis.RideHistory , data, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{NewRideHistoryModel ride_history_response = gson.fromJson(""+script , NewRideHistoryModel.class);
            for(int i = 0 ; i < ride_history_response.getDetails().size() ; i ++){
                if(ride_history_response.getDetails().get(i).getRide_mode().equals("1")){  // normal ride
                    place_holder.addView(new HolderRideHistorySecondDesign(RidesActivity.this , place_holder , ride_history_response.getDetails().get(i).getNormal_Ride() , sessionManager ));
                }else if (ride_history_response.getDetails().get(i).getRide_mode().equals("2")){  // rental rides
                    place_holder.addView(new HolderRideHistoryRentalDesign(RidesActivity.this , place_holder , ride_history_response.getDetails().get(i).getRental_Ride() , sessionManager));
                }
            }}catch (Exception e){}
    }

    @Override
    public void onFetchResultZero(String s) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{RentalTrackActivity.trackRideActivity.finish();}catch (Exception e){}
        try{TrackRideAactiviySamir.trackRideActivity.finish();}catch (Exception e){}
    }
}
