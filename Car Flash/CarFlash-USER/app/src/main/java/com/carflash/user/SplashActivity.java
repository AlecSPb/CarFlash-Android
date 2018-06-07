package com.carflash.user;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.StatusSession.TimelySessionService;
import com.carflash.user.accounts.LoginActivity;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.ModelAppVersion;
import com.carflash.user.others.AppUtils;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vansuita.library.CheckNewAppVersion;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


// Hierarchy
// Check Permission --> Check GPS Connectivity --> Check Internet Connectivity --> fetch Remote Config --> Check Build variant --> Check Session Login Status --> Run Handler (2 sec )--> main Activity
public class SplashActivity extends BaseInternetCheckActivity implements ApiManager.APIFETCHER {

    private static final String TAG = "SplashActivity";
    @Bind(R.id.loading_txt)
    TextView loadingText;
    private boolean is_gps_dialog_shown = false;
    private boolean is_internet_dialog_is_shown = false;
    private boolean is_version_dialog_is_shown = false;
    ApiManager apiManager;
    DatabaseReference mDatabaseReference;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    Gson gson;
    ModelAppVersion modelAppVersion;
    public static Activity splash;
    SessionManager sessionManager ;
    LanguageManager languageManager;
    FrameLayout fl_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        apiManager = new ApiManager(this, this , this );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Config.app_loaded_from_initial = true ;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Config.DriverRefrence);
        gson = new GsonBuilder().create();
        splash = this;
        getkeyhash();
        sessionManager = new SessionManager(this );
        startService(new Intent(this, TimeService.class));
        startService(new Intent(this, TimelySessionService.class));
        languageManager=new LanguageManager(this);
        fl_root= (FrameLayout) findViewById(R.id.fl_root);

        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
            Log.i(TAG, "Checking Permission On Splash");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        } else {
            startGPSCheck();
        }
        
    }


    private void startInternetCheckProcess() {
        Log.i(TAG, "Now Checking net Connectivity");
        if (AppUtils.isNetworkConnected(this)) {
            Log.i(TAG, "Internet Connectivity Status " + true);
            try {
                fetchRemoteConfig();
            } catch (Exception e) {
            }
        } else {
            Log.i(TAG, "Internet Connectivity Status " + false + ", Now Showing Internet Dialog");
            if (!is_internet_dialog_is_shown) {
                showInternetDialog();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!AppUtils.checkGPSisOnOrNot(SplashActivity.this)) {
            showGPSDialog();
        } else {
            Log.i(TAG, "Now GPS Status = " + true);
            startInternetCheckProcess();
        }
    }

    private void fetchRemoteConfig() throws Exception {
        Log.i(TAG, "Fetching  Remote Config");
        long cacheExpiration = 3600;
        Log.i("" + TAG, "Started fetching Configurations");
        HashMap<String , String > data  = new HashMap<>();
        data.put("application_version" , "0");
        data.put("flag" , "2");
        data.put("application" , "1");
        apiManager.execution_method_post("" + Config.ApiKeys.APP_VERSIONS, "" + Apis.AppVersions , data,false , ApiManager.ACTION_DO_NOTHING);
    }

    private void checkForVersionUpdation() throws JSONException {
        new CheckNewAppVersion(this).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
            @Override
            public void onTaskComplete(final CheckNewAppVersion.Result result) {
                Log.i("" + TAG, "App store version code " + result.getNewVersionCode());
                Log.i("" + TAG, "Current version of App" + result.getOldVersionCode());
                Log.i("" + TAG, "Has new version available " + result.hasNewVersion());


                try {
                    if (result.hasNewVersion() && modelAppVersion.getDetails().getAndroid_user_current_version().contains(""+result.getNewVersionCode()) &&modelAppVersion.getDetails().getAndroid_user_mandantory_update().contains("1")) {
                        Log.i(TAG, "Now Showing app update dialog with mandatory approach");
                        loadingText.setText(R.string.some_man_datory_is_available);
                        showUpdationDialog(true, result);
                    } else if (result.hasNewVersion() && modelAppVersion.getDetails().getAndroid_user_current_version().equals("" + result.getNewVersionCode()) && modelAppVersion.getDetails().getAndroid_user_mandantory_update().contains("")) {
                        Log.i(TAG, "Now Showing app update dialog with Non mandatory approach");
                        loadingText.setText(R.string.non_mandatory_update);
                        showUpdationDialog(false, result);
                    }else if (result.hasNewVersion() && !modelAppVersion.getDetails().getAndroid_user_current_version().equals("" + result.getNewVersionCode())){
                        Log.i(TAG, "Now Showing app update dialog with Non mandatory approach because unable to judge from back end ");
                        loadingText.setText(R.string.non_mandatory_update);
                        showUpdationDialog(false, result);
                    } else if (!result.hasNewVersion()) {
                        Log.i(TAG, "Initiating splash process");
                        loadingText.setText(R.string.app_is_up_to_date);
                        startCheckingLoginProcedure();
                    } else{
                        loadingText.setText("Something went wrong");
                    }
                } catch (Exception e) {
                    Toast.makeText(SplashActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();


    }

    private void startCheckingLoginProcedure() {
        Log.i(TAG, "Checking login status in session");
        languageManager.createLanguageSession();
        if (sessionManager.isLoggedIn()) {
            Log.i(TAG, "user is already logged in ");
            getLocation();
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    Config.mDataSnapShot = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (!(sessionManager.isLoggedIn())) {
            Log.i(TAG, "user is not logged in ");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Config.mDataSnapShot = dataSnapshot;
                    startLoginPart();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        }



    public void showGPSDialog() {

        if (!is_gps_dialog_shown) {
            Log.i(TAG, "Now GPS Status = " + false + ", Now Showing Dialog");


            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.enable_app_location);
            dialog.setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();

        }
//        if (!is_gps_dialog_shown) {
//            Log.i(TAG, "Now GPS Status = " + false + ", Now Showing Dialog");
//            new CFAlertDialog.Builder(this)
//                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                    .setTitle(R.string.enable_app_location)
//                    .setMessage(R.string.in_order_to_use_app_settings)
//                    .addButton(SplashActivity.this.getString(R.string.open_location_settings), -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            SplashActivity.this.startActivity(myIntent);
//                            dialogInterface.dismiss();
//                            is_gps_dialog_shown = false;
//                        }
//                    }).setCancelable(false).show();
//            is_gps_dialog_shown = true;
//        }

    }

    private void showInternetDialog() {

        Snackbar.make(fl_root, ""+SplashActivity.this.getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .setAction(""+SplashActivity.this.getResources().getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                }).setDuration(Snackbar.LENGTH_INDEFINITE).show();

//        new CFAlertDialog.Builder(this)
//                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                .setTextGravity(Gravity.CENTER)
//                .setTitle(R.string.no_internet_connectivity)
//                .setHeaderView(R.layout.dialog_header_no_internet)
//                .setMessage(R.string.it_seems_you_are_out_of_internet_connection)
//                .addButton(SplashActivity.this.getString(R.string.retry), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        is_internet_dialog_is_shown = false;
//                        startInternetCheckProcess();
//                    }
//                }).setCancelable(false).show();
//        is_internet_dialog_is_shown = true;
    }

    private void showUpdationDialog(final boolean is_maindatory, final CheckNewAppVersion.Result result) {
        if (!is_version_dialog_is_shown) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.new_version_is_avaiable);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    is_version_dialog_is_shown = false;
                    result.openUpdateLink();
                }
            });

            if (!is_maindatory) {
                dialog.setNegativeButton(R.string.do_it_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        is_version_dialog_is_shown = false;
                        startCheckingLoginProcedure();
                    }
                });
            }
            dialog.show();
            is_version_dialog_is_shown = true;
        }
    }


    private void showAppmaintainanceDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.your_app_is_in_maintainance);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                finish();

            }
        });
        dialog.show();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (AppUtils.hasPermissions(SplashActivity.this, PERMISSIONS)) {
                    startGPSCheck();
                } else {
                    Log.i("" + TAG, "Some Permissions are missing");
                }
                return;
            }
        }
    }

    private void
    startGPSCheck() {
        Log.i(TAG, "Checking GPS status");
        if (!AppUtils.checkGPSisOnOrNot(SplashActivity.this)) {
            showGPSDialog();
        } else {
            Log.i(TAG, "Now GPS Status = " + true);
            startInternetCheckProcess();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        Log.i(TAG, "Successfully fetched the remote config");
        modelAppVersion = gson.fromJson("" + script, ModelAppVersion.class);
        try {
            Log.i(TAG, "Checking version of application.");
            if(modelAppVersion.getDetails().getAndroid_user_maintenance_mode().equals("1")){
                showAppmaintainanceDialog();
            } else{
                checkForVersionUpdation();
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onFetchResultZero(String s) {

    }





    @SuppressLint("LongLogTag")
    private void getkeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.carflash.user", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Exception in founding key", "name not found =>" + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Exception in founding key", "No such algo =>" + e);
        }
    }




    private void getLocation() {
        try {
            new SamLocationRequestService(this,true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(SplashActivity.this, TrialActivity.class)
                                .putExtra(Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(SplashActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            });
        } catch (Exception e) {
        }
    }



    private void startLoginPart() {
        try {
            new SamLocationRequestService(this , true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                                .putExtra(Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(SplashActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            });
        } catch (Exception e) {
        }
    }


}
