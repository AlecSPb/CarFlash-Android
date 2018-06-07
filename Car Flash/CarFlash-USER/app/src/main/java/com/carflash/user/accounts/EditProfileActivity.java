package com.carflash.user.accounts;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carflash.user.R;
import com.carflash.user.TrialActivity;
import com.carflash.user.Verify_OTP;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.models.EditProfileResponse;
import com.carflash.user.others.ImageCompressMode;
import com.carflash.user.others.SingletonGson;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditProfileActivity extends Activity implements ApiManager.APIFETCHER {

    TextView  phone_txt ;
    EditText email_address_txt , name_txt ;
    SessionManager sessionmanager ;
    ImageView iv_profile_pic_upload  , ic_camera  ;

    String imagePath = ""  , imagePathCompressed = "" ;
    Gson gson ;
    ApiManager apiManager ;
    Uri selectedImage;
    Bitmap bitmap1;
    String userEmail;
    private int VERIFY_OTP = 899;
    private static final int RC_CAMERA_PERM = 123;
    private ContentValues values;
    private Bitmap thumbnail;
    private static final int CAMERS_PICKER = 122;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this  , this , this );
        gson = new GsonBuilder().create();
        sessionmanager = new SessionManager(EditProfileActivity.this);
        setContentView(R.layout.activity_edit_profile);
        name_txt = (EditText) findViewById(R.id.name_txt);
        email_address_txt = (EditText) findViewById(R.id.email_address_txt);
        phone_txt = (TextView) findViewById(R.id.phone_txt);
        iv_profile_pic_upload = (ImageView) findViewById(R.id.iv_profile_pic_upload);
        ic_camera  = (ImageView) findViewById(R.id.ic_camera);

        if(sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_NORAL) ){
            ic_camera.setVisibility(View.VISIBLE);
        }else {
            ic_camera.setVisibility(View.GONE);
        }

        setDataonView();


       try{if(sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_FACEBOOK)){
           Glide.with(this).load(""+sessionmanager.getUserDetails().get(SessionManager.FACEBOOK_IMAGE)).into(iv_profile_pic_upload);
       }else if (sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_GOOGLE)){
           Picasso.with(this).load(""+sessionmanager.getUserDetails().get(SessionManager.GOOGLE_IMAGE)).placeholder(R.drawable.ic_google).into(iv_profile_pic_upload);
       }else if (sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE).equals(""+SessionManager.LOGIN_NORAL)){
           Picasso.with(this).load(""+sessionmanager.getUserDetails().get(SessionManager.USER_IMAGE)).placeholder(R.drawable.ic_google).into(iv_profile_pic_upload);
       }}catch (Exception e ){}


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });   //   {facebook_id=1479168742128308}
        findViewById(R.id.edt_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(EditProfileActivity.this , EditEmailAddressActivity.class));
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , String > data  = new HashMap<String, String>();
                data.put("user_id" , ""+sessionmanager.getUserDetails().get(SessionManager.USER_ID));
                data.put("unique_id" , ""+ Settings.Secure.getString(EditProfileActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                apiManager.execution_method_post(""+com.carflash.user.samwork.Config.ApiKeys.KEY_LOGOUT , ""+ Apis.URL_LOGOUT , data , true,ApiManager.ACTION_SHOW_TOP_BAR);

            }
        });

       ic_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(EditProfileActivity.this);
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
        });


        findViewById(R.id.ll_done_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_address_txt.getText().toString().equals("") ||name_txt.getText().toString().equals("")){
                    Toast.makeText(EditProfileActivity.this, R.string.please_enter_missing_details, Toast.LENGTH_SHORT).show();
                }if(!email_address_txt.getText().toString().contains("@")){
                    Toast.makeText(EditProfileActivity.this, R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String , String > data_image = new HashMap<>();
                    data_image.put("user_name" , ""+name_txt.getText().toString());
                    data_image.put("user_email" , ""+email_address_txt.getText().toString());
                    data_image.put("user_id" , ""+sessionmanager.getUserDetails().get(SessionManager.USER_ID));
                    data_image.put("phone" , ""+phone_txt.getText().toString());
                    if(imagePathCompressed == null || imagePathCompressed == ""){

                        apiManager.execution_method_post("" +Config.ApiKeys.KEY_EDIT_PROFILE, ""+Apis.URL_EDIT_PROFILE,data_image,true,ApiManager.ACTION_DO_NOTHING);
                    }else{
                        apiManager.execution_method_post_single_image("" + Config.ApiKeys.KEY_EDIT_PROFILE,Apis.URL_EDIT_PROFILE,""+ imagePathCompressed,"user_image",data_image,true,ApiManager.ACTION_SHOW_TOP_BAR);

                    }
                }
            }
        });


        findViewById(R.id.edit_phone_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, Verify_OTP.class);
                startActivityForResult(intent, VERIFY_OTP);
            }
        });
    }


    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask()throws Exception {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)) {
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
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera), RC_CAMERA_PERM, android.Manifest.permission.CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try{cameraTask();}catch (Exception e){}
    }



    private void setDataonView() {
        if((sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME)).equals(sessionmanager.getUserDetails().get(SessionManager.USER_LAST_NAME))){
            name_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME));

        }else{
            name_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_FIRST_NAME)+" "+sessionmanager.getUserDetails().get(SessionManager.USER_LAST_NAME));
        }
        phone_txt.setText(""+sessionmanager.getUserDetails().get(SessionManager.USER_PHONE));
        email_address_txt.setText(""+sessionmanager.getEmail());
        userEmail = sessionmanager.getEmail();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultChecker rcheck = gson.fromJson("" + script, ResultChecker.class);

            if(APINAME.equals(""+Config.ApiKeys.KEY_EDIT_PROFILE)){
                EditProfileResponse editProfileResponse = SingletonGson.getInstance().fromJson(""+script , EditProfileResponse.class);
                sessionmanager.createLoginSession(editProfileResponse.getDetails().getUser_id(),
                        editProfileResponse.getDetails().getUser_name() ,
                        editProfileResponse.getDetails().getUser_name(),
                        editProfileResponse.getDetails().getUser_email(),
                        editProfileResponse.getDetails().getUser_phone(),
                        editProfileResponse.getDetails().getUser_image(),
                        editProfileResponse.getDetails().getUser_password(),
                        editProfileResponse.getDetails().getLogin_logout(),
                        editProfileResponse.getDetails().getDevice_id(),
                        editProfileResponse.getDetails().getFacebook_id(),
                        editProfileResponse.getDetails().getFacebook_mail(),
                        editProfileResponse.getDetails().getFacebook_image(),
                        editProfileResponse.getDetails().getFacebook_firstname(),
                        editProfileResponse.getDetails().getFacebook_lastname(),
                        editProfileResponse.getDetails().getGoogle_id(),
                        editProfileResponse.getDetails().getGoogle_name(),
                        editProfileResponse.getDetails().getGoogle_mail(),
                        editProfileResponse.getDetails().getGoogle_image(),
                        sessionmanager.getUserDetails().get(SessionManager.LOGIN_TYPE),
                        editProfileResponse.getDetails().getUnique_number());

                        showDialogForprofileUpdated();


            }else{
                finilalizeActivity();
                sessionmanager.logoutUser();
            }}catch (Exception E){}

    }

    private void showDialogForprofileUpdated() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_profile_updated);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onFetchResultZero(String s) {

        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }


    private void finilalizeActivity() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        JSONObject no_data = new JSONObject();
        try {
            if(sessionmanager.isLoggedIn()){
                no_data.put("result" , "1");
            }else{
                no_data.put("result" , "0");
            }

            no_data.put("response","EditProfile  Activity Cancelled.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent=new Intent();
        intent.putExtra("MESSAGE",""+no_data);
        setResult(3,intent);
        finish();
        try {
            TrialActivity.mainActivity.finish();
        }catch (Exception e ){

        }
        startActivity(new Intent(this , LoginActivity.class));
    }





    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == Activity.RESULT_OK) {
            try {
                if (req == 101) {
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
                    iv_profile_pic_upload.setImageBitmap(bitmap1);
                }
                if(req == CAMERS_PICKER){
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    iv_profile_pic_upload.setImageBitmap(thumbnail);
                    imagePath = getRealPathFromURI(imageUri);
                    ImageCompressMode imageCompressMode = new ImageCompressMode(this);
                    imagePathCompressed = imageCompressMode.compressImage(imagePath);
                }
                if(req == VERIFY_OTP){
                    phone_txt.setText(""+data.getExtras().getString("phone_number"));
                }
            } catch (Exception e) {}
        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}
