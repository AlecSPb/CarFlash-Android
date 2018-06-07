package com.carflash.user;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.carflash.user.adapter.SosAdapter;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewSosModel;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.models.SosRequestModel;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SosActivity extends Activity implements ApiManager.APIFETCHER {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.list)
    ListView list;

    ApiManager apiManager ;
    Gson gson ;
    SessionManager sessionManager ;
    SamLocationRequestService samLocationRequestService ;
    NewSosModel newsos_response = null ;
    private static final int TELEPHONE_PERM = 657;
    String NUMBER_To_CALL="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson =  new GsonBuilder().create();
        apiManager = new ApiManager(this , this , this );
        setContentView(R.layout.activity_sos);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        samLocationRequestService = new SamLocationRequestService(this , false);


        apiManager.execution_method_get(""+ Config.ApiKeys.KEY_SOS, ""+ Apis.Sos, true,ApiManager.ACTION_SHOW_TOAST);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(newsos_response != null){
                    try{
                        try {NUMBER_To_CALL = ""+newsos_response.getDetails().get(i).getSos_number() ;
                            callingTask(""+newsos_response.getDetails().get(i).getSos_number());} catch (Exception e) {}
                        samLocationRequestService.executeService(new SamLocationRequestService.SamLocationListener() {
                            @Override
                            public void onLocationUpdate(Location location) {
                                HashMap<String , String > data = new HashMap<String, String>();
                                data.put("ride_id" , ""+getIntent().getExtras().getString("ride_id"));
                                data.put("driver_id" , ""+getIntent().getExtras().getString("driver_id"));
                                data.put("user_id" , ""+sessionManager.getUserDetails().get(SessionManager.USER_ID));
                                data.put("sos_number" , ""+newsos_response.getDetails().get(i).getSos_number());
                                data.put("latitude" , ""+location.getLatitude());
                                data.put("longitude" , ""+location.getLongitude());
                                data.put("application" , "2");
                                apiManager.execution_method_post(""+Config.ApiKeys.SOS_REQUEST_NOTIFIER , ""+Apis.Sos_Request, data,true , ApiManager.ACTION_SHOW_TOAST);
                            }
                        });
                    }catch (Exception e){}
                }
            }
        });


    }



    @AfterPermissionGranted(TELEPHONE_PERM)
    public void callingTask(String number) throws Exception {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CALL_PHONE)) {
            try { // Have permission, do the thing!
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
            } catch (Exception e) {
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.this_app_need_telephony_permission), TELEPHONE_PERM, android.Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            callingTask(NUMBER_To_CALL);
        } catch (Exception e) {
        }
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{
            if(APINAME.equals(""+ Config.ApiKeys.KEY_SOS)){
                try{ newsos_response = gson.fromJson(""+script , NewSosModel.class);
                    list.setAdapter(new SosAdapter(this , newsos_response));}catch (Exception e){}
            }else if (APINAME.equals(""+Config.ApiKeys.SOS_REQUEST_NOTIFIER)){
                SosRequestModel sosRequestModel  = gson.fromJson(""+script , SosRequestModel.class);
                finish();
            }

        }catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
