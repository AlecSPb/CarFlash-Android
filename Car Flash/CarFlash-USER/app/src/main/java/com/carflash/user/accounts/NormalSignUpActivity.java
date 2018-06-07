package com.carflash.user.accounts;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.carflash.user.R;
import com.carflash.user.TrialActivity;
import com.carflash.user.Verify_OTP;
import com.carflash.user.location.SamLocationRequestService;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.others.ImageCompressMode;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NormalSignUpActivity extends Activity implements ApiManager.APIFETCHER {

    TextView  email_verifier_txt    ;
    EditText  your_name_edit  ,edt_pass_signup   , email_edttt,phone_txt ;
    ProgressBar progress_bar ;

 //   private int Email_validator_Hide = 0 , Email_validator_Invalid = 1 , Email_validator_CHECKING = 2 , Email_validator_Available = 3 , Email_validator_Already_exsist = 4 ;

    ProgressDialog loading_dialouge ;

    LinearLayout root , phone_layout;
    GsonBuilder gsonBuilder ;
    Gson gson;
    String email, phone, password, name, getPhone_Number;

    ApiManager apimanager ;
    SessionManager sessionmanager ;
    CountryCodePicker ccp ;
    private static final int KEY_REGISTER = 110;

    String imagePath = ""  , imagePathCompressed = "" ;
    ImageView user_image;

    private static final int RC_CAMERA_PERM = 123;
    private ContentValues values;
    private Bitmap thumbnail;
    private static final int CAMERS_PICKER = 122;
    private Uri imageUri;
    Uri selectedImage;
    Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_sign_up);
        edt_pass_signup = (EditText) findViewById(R.id.edt_pass_signup);
        your_name_edit = (EditText) findViewById(R.id.your_name_edit);
        phone_txt = (EditText) findViewById(R.id.phone_txt);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        email_verifier_txt= (TextView) findViewById(R.id.email_verifier_txt);
        root = (LinearLayout) findViewById(R.id.root);
        phone_layout = (LinearLayout) findViewById(R.id.edt_phone_layout);

        email_edttt  = (EditText) findViewById(R.id.email_edttt);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        apimanager = new ApiManager(this , this , this  );
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        loading_dialouge = new ProgressDialog(this);
        sessionmanager = new SessionManager(NormalSignUpActivity.this);
        loading_dialouge.setTitle(""+this.getResources().getString(R.string.loading));
        user_image=(ImageView) findViewById(R.id.user_image);



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finilalizeActivity();
            }
        });


//        phone_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NormalSignUpActivity.this, Verify_OTP.class);
//                startActivityForResult(intent, KEY_REGISTER);
//            }
//        });


        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(NormalSignUpActivity.this);
                builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                builder.setTitle(R.string.upload_profile_pic);
                builder.setItems(new String[]{getString(R.string.camera), getString(R.string.gallery)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        if(index == 0 ){
                            try{cameraTask();}catch (Exception e){}
                        }else if (index == 1 ){
                            Intent i1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i1.setType("image/*");
                            startActivityForResult(i1, 101);
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }

            @AfterPermissionGranted(RC_CAMERA_PERM)
            public void cameraTask()throws Exception {
                if (EasyPermissions.hasPermissions(NormalSignUpActivity.this, android.Manifest.permission.CAMERA)) {
                    try{ // Have permission, do the thing!
                        values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CAMERS_PICKER);
                    }catch (Exception e){}
                } else {
                    EasyPermissions.requestPermissions(NormalSignUpActivity.this, getString(R.string.rationale_camera), RC_CAMERA_PERM, android.Manifest.permission.CAMERA);
                }
            }

        });







        findViewById(R.id.ll_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(your_name_edit.getText().toString().equals("") || phone_txt.getText().toString().trim().equals("") || email_edttt.getText().toString().equals("") || edt_pass_signup.getText().toString().equals("") ){
                    Toast.makeText(NormalSignUpActivity.this, "Required Field missing !", Toast.LENGTH_SHORT).show();
                }
                else if(imagePathCompressed == null || imagePathCompressed == ""){
                    Toast.makeText(NormalSignUpActivity.this, "please upload image !", Toast.LENGTH_SHORT).show();
                }
                else if(edt_pass_signup.getText().toString().trim().length()<2){
                    Toast.makeText(NormalSignUpActivity.this, "password should be minimum 2 digit !", Toast.LENGTH_SHORT).show();
                }
                else {
                    name = your_name_edit.getText().toString();
                    email = email_edttt.getText().toString();
                    password = edt_pass_signup.getText().toString();
//                    phone = getPhone_Number;
                    phone=ccp.getSelectedCountryCodeWithPlus()+phone_txt.getText().toString().trim();
                    SignUpNormally();}
            }
        });
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME.equals(com.carflash.user.samwork.Config.ApiKeys.KEY_REGISTER)){
                if(rcheck.getResult() == 1 ){
                    RegistrationModel response = gson.fromJson("" + script, RegistrationModel.class);
                    RegistrationModel registration_response = gson.fromJson("" + script, RegistrationModel.class);
                    sessionmanager.createLoginSession(registration_response.getDetails().getUser_id() , registration_response.getDetails().getUser_name() , registration_response.getDetails().getUser_name(), registration_response.getDetails().getUser_email(),registration_response.getDetails().getUser_phone(),registration_response.getDetails().getUser_image(),registration_response.getDetails().getUser_password(),registration_response.getDetails().getLogin_logout(),registration_response.getDetails().getDevice_id(),
                            registration_response.getDetails().getFacebook_id(),registration_response.getDetails().getFacebook_mail(),registration_response.getDetails().getFacebook_image(),registration_response.getDetails().getFacebook_firstname(),registration_response.getDetails().getFacebook_lastname(),
                            registration_response.getDetails().getGoogle_id(),registration_response.getDetails().getGoogle_name(),registration_response.getDetails().getGoogle_mail(),registration_response.getDetails().getGoogle_image(), SessionManager.LOGIN_NORAL, registration_response.getDetails().getUnique_number());
//                LoginActivity.activity.finish();
//                finish();
                    getLocation();
                }else if (rcheck.getResult() == 0 ){
                    ResultCheckMessage rr = gson.fromJson("" + script, ResultCheckMessage.class);

                    Toast.makeText(this, ""+rr.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }


    private void SignUpNormally() {
        HashMap<String , String > bodyparameters  = new HashMap<String, String>();
        bodyparameters.put("first_name" ,""+ name);
        bodyparameters.put("last_name" ,".");
        bodyparameters.put("phone" ,""+phone);
        bodyparameters.put("password" ,""+password);
        bodyparameters.put("user_email" ,""+email);



        if(imagePathCompressed == null || imagePathCompressed == ""){

            apimanager.execution_method_post(com.carflash.user.samwork.Config.ApiKeys.KEY_REGISTER ,""+  Apis.URL_REGISTER, bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);
        }else{

            apimanager.execution_method_post_single_image(com.carflash.user.samwork.Config.ApiKeys.KEY_REGISTER ,""+  Apis.URL_REGISTER,""+imagePathCompressed,"user_image", bodyparameters, true,ApiManager.ACTION_SHOW_TOP_BAR);

//            apiManager.execution_method_post_single_image("" + com.carflash.user.samwork.Config.ApiKeys.KEY_EDIT_PROFILE,Apis.URL_EDIT_PROFILE,""+ imagePathCompressed,"user_image",data_image,true,ApiManager.ACTION_SHOW_TOP_BAR);

        }
    }


    private void getLocation() {
        try {
            new SamLocationRequestService(this, true).executeService(new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location) {
                    try {
                        startActivity(new Intent(NormalSignUpActivity.this, TrialActivity.class)
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LATITUDE, "" + location.getLatitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LONGITUDE, "" + location.getLongitude())
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.PICK_LOCATION_TXT, "No Internet Connectivity")
                                .putExtra(com.carflash.user.samwork.Config.IntentKeys.CITY_NAME, "No City" ));
                        finilalizeActivity();
                    } catch (Exception e) {
                        Toast.makeText(NormalSignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    private void finilalizeActivity() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        try {
            if(sessionmanager.isLoggedIn()){
                LoginActivity.activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch (requestCode){

                case KEY_REGISTER :
                    try{
                        getPhone_Number = data.getExtras().getString("phone_number");
                        phone_txt.setText(getPhone_Number);
                    }catch (Exception e){}
                    break;

                case 101 :
                    selectedImage = data.getData();
                    imagePath = getPath(selectedImage);
                    ImageCompressMode imageCompressMode = new ImageCompressMode(this);
                    imagePathCompressed = imageCompressMode.compressImage(imagePath);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filePath, options);
                    final int REQUIRED_SIZE = 300;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bitmap1 = BitmapFactory.decodeFile(filePath, options);
                    user_image.setImageBitmap(bitmap1);
                    break ;
                case CAMERS_PICKER:
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    user_image.setImageBitmap(thumbnail);
                    imagePath = getRealPathFromURI(imageUri);
                    ImageCompressMode imageCompressModee = new ImageCompressMode(this);
                    imagePathCompressed = imageCompressModee.compressImage(imagePath) ;
                    break;
            }
        } catch (Exception e){}

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
