package com.carflash.user.accounts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.carflash.user.ForgotPass_Verify_OTP;
import com.carflash.user.R;
import com.carflash.user.TrialActivity;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.urls.Apis;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import io.reactivex.annotations.NonNull;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener , ApiManager.APIFETCHER{


    EditText  edt_pass_login, edt_phone_login ;
    GsonBuilder gsonBuilder;
    Gson gson;
    public static Activity activity;
    LinearLayout   email_layout, phone_layout;
    TextView app_name;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 112;
    private String TAG = "** Apporio Login Activity";
    FrameLayout root ;
    ApiManager api_manager ;
    SessionManager sessionmanager ;
    CountryCodePicker ccp;

    private CallbackManager mCallbackManager;
    FirebaseAuth mAuth;


    private void creatInitialObjects() {
        sessionmanager = new SessionManager(LoginActivity.this);
        api_manager = new ApiManager(this , this , this );




        gsonBuilder = new GsonBuilder();

        gson = gsonBuilder.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                Log.i("accessToken", ""+loginResult.getAccessToken().getUserId());
                Config.facebook_ID =""+ loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("faebook response ", ""+object);
                        // Get facebook data from login
                        FacebookModel facebookdata = gson.fromJson(""+object , FacebookModel.class);
                        Config.facebook_Mail =""+ facebookdata.getEmail();
                        Config.facebook_Image =""+ "http://graph.facebook.com/"+facebookdata.getId()+"/picture?type=large";
                        Config.facebook_firstname =""+facebookdata.getFirst_name();
                        Config.facebook_Lastname =""+facebookdata.getLast_name();
                        Config.facebook_Id =""+facebookdata.getId();
                        LoginWithfacebook(""+ Config.facebook_ID);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancel() {
                Log.d("** facebook login canceled ", "Facebook login canceled");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onError(FacebookException exception) {
                Log.d("** facebook exception occur while login ", "" + exception.getLocalizedMessage());
            }
        });



    }


    public void LoginWithfacebook(String UserId){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("facebook_id" ,""+ UserId);
        api_manager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN, Apis.URL_FACEBOOK_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }
    private void init(){
        activity = this;
        edt_pass_login = (EditText) findViewById(R.id.edt_pass_login);
        edt_phone_login = (EditText) findViewById(R.id.edt_phone_login);
        email_layout = (LinearLayout) findViewById(R.id.email_layout);
        phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
        app_name = (TextView) findViewById(R.id.app_name);
        root = (FrameLayout) findViewById(R.id.root);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

    }
    private void setListeners(){





        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if(edt_phone_login.getText().toString().equals("") || edt_pass_login.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, ""+LoginActivity.this.getResources().getString(R.string.require_field_missing), Toast.LENGTH_SHORT).show();
                }else{
                    /*if(com.carflash.user.samwork.Config.ismobileValid(edt_phone_login.getText().toString())){
                        PhoneSignin(edt_phone_login.getText().toString() , edt_pass_login.getText().toString());
                    }else {
                        Toast.makeText(LoginActivity.this, ""+LoginActivity.this.getResources().getString(R.string.please_enter_a_valid_phone), Toast.LENGTH_SHORT).show();*/
                    PhoneSignin(edt_phone_login.getText().toString() , edt_pass_login.getText().toString());
                }

            }
        });


        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, NormalSignUpActivity.class));
            }
        });


        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPass_Verify_OTP.class));
            }
        });



        findViewById(R.id.facebook_btn_lout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends" , "email","user_birthday"));

            }
        });

        findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creatInitialObjects();
        setContentView(R.layout.activity_login_secong);
        init();
        setListeners();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }if(requestCode == 2){
            try {
                JSONObject mainObject = new JSONObject(data.getStringExtra("MESSAGE"));
                if(mainObject.getString("result").equals("1")){
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("GOOGLE_DATA Email" , ""+acct.getEmail());
            Log.d("GOOGLE_DATA ID" , ""+acct.getId());
            Log.d("GOOGLE_DATA Display name" , ""+acct.getDisplayName());
            Log.d("GOOGLE_DATA phojt url " , ""+acct.getPhotoUrl());
            Log.d("GOOGLE_DATA auth code" , ""+acct.getServerAuthCode());

            Config.google_Id = ""+acct.getId();
            Config.google_mail = ""+acct.getEmail();
            Config.google_image = ""+acct.getPhotoUrl();
            Config.google_Name = ""+acct.getDisplayName();


            LoginWithGoogle(""+acct.getId());
        } else {
            Log.d(""+TAG ,"UI updation is false");
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }





    public void PhoneSignin(String Phone , String password){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("phone" ,"+"+ccp.getSelectedCountryCode()+ Phone);
        bodyparameters.put("password" ,""+ password);
        api_manager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_PHONE_LOGIN, Apis.URL_PHONE_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }

    public void LoginWithGoogle(String UserId){
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("google_id" ,""+ UserId);
        api_manager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN, Apis.URL_GOOGLE_LOGIN, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
    }








    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_PHONE_INFO){
                if(rcheck.getResult() == 1){
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    startActivity(new Intent(LoginActivity.this , ChangePasswordActivity.class)
                            .putExtra(com.carflash.user.samwork.Config.IntentKeys.KEY_USER_ID , ""+registration_response.getDetails().getUser_id())
                            .putExtra(""+com.carflash.user.samwork.Config.IntentKeys.KEY_OLD_PASSWORD,""+registration_response.getDetails().getUser_password()));
                }else{
                    Toast.makeText(activity, ""+LoginActivity.this.getResources().getString(R.string.we_dont_find_any_account), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(rcheck.getResult() == 1){
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    String login_type = "";
                    if(APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_PHONE_LOGIN){
                        login_type = SessionManager.LOGIN_NORAL;
                    }else if (APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN){
                        login_type = SessionManager.LOGIN_FACEBOOK;
                    }else if (APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN){
                        login_type = SessionManager.LOGIN_GOOGLE;
                    }else if (APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_DEMO_USER){
                        login_type = SessionManager.LOGIN_NORAL;
                    }

                    sessionmanager.createLoginSession(registration_response.getDetails().getUser_id(),
                            registration_response.getDetails().getUser_name() ,
                            registration_response.getDetails().getUser_name(),
                            registration_response.getDetails().getUser_email(),
                            registration_response.getDetails().getUser_phone(),
                            registration_response.getDetails().getUser_image(),
                            registration_response.getDetails().getUser_password(),
                            registration_response.getDetails().getLogin_logout(),
                            registration_response.getDetails().getDevice_id(),
                            registration_response.getDetails().getFacebook_id(),
                            registration_response.getDetails().getFacebook_mail(),
                            registration_response.getDetails().getFacebook_image(),
                            registration_response.getDetails().getFacebook_firstname(),
                            registration_response.getDetails().getFacebook_lastname(),
                            registration_response.getDetails().getGoogle_id(),
                            registration_response.getDetails().getGoogle_name(),
                            registration_response.getDetails().getGoogle_mail(),
                            registration_response.getDetails().getGoogle_image(),
                            login_type,
                            registration_response.getDetails().getUnique_number());

                    getLocation();
                }else {
                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);
                    if(APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_PHONE_LOGIN  || APINAME.equals(""+ Apis.URL_DEMO_SIGNUP)){
                        Loginvaluechecker value = gson.fromJson(""+script , Loginvaluechecker.class);
                        Toast.makeText(activity, ""+value.getMessage(), Toast.LENGTH_SHORT).show();
                    }else if (APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_FACEBOOK_LOGIN){
                        startActivity(new Intent(LoginActivity.this , FaceBookSignupAcivity.class));
                    }else if (APINAME == com.carflash.user.samwork.Config.ApiKeys.KEY_GOOGLE_LOGIN){
                        if( mGoogleApiClient.isConnected()){
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onResult(Status status) {
                                            Log.d("User logged out successfuly " , ""+status.toString());
                                        }
                                    });
                        }
                        startActivity(new Intent(LoginActivity.this , GoogleSignUpActivity.class));
                    }
                }
            }}catch (Exception e){}
    }

    @Override
    public void onFetchResultZero(String s) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



    @SuppressLint("LongLogTag")
    private Bundle getFacebookData(JSONObject object) throws JSONException {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
            return null ;
        }
    }



    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(LoginActivity.this, TrialActivity.class)
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }



    private class FacebookModel {

        /**
         * id : 1479168742128308
         * first_name : Samir
         * last_name : Goel
         * email : samirgoel3@gmail.com
         * gender : male
         * birthday : 08/07/1990
         */

        private String id;
        private String first_name;
        private String last_name;
        private String email;
        private String gender;
        private String birthday;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }



}
