package com.carflash.user.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.carflash.user.R;
import com.carflash.user.models.firebasemodels.CheckResult;
import com.carflash.user.others.NoInternetDialog;
import com.carflash.user.others.SingletonGson;
import com.devspark.appmsg.AppMsg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by samir on 30/01/17.
 */

public class ApiManager implements NoInternetDialog.NoInternetDialogListener {

    String url;
    HashMap map;
    GsonBuilder gsonBuilder;
    Gson gson;
    APIFETCHER apifetcher;
    Activity mActivity ;
    Context mContext ;
    boolean mMakeDialogWorkable = false ;
    ProgressDialog progressDialog;
    NoInternetDialog noInternetDialog;

    private static final String TAG = "API_MANAGER";

    public static final String ACTION_DO_NOTHING = "do_nothing";
    public static final String ACTION_SHOW_DIALOG = "show_dialog";
    public static final String ACTION_SHOW_TOAST = "show_toast";
    public static final String ACTION_SHOW_TOP_BAR = "show_snackbar";

    AppMsg appMessage  = null ;



    public ApiManager(APIFETCHER apifetcher , Activity activity, Context context  ) {
        mActivity = activity;
        mContext = context ;
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        map = new HashMap();
        noInternetDialog = new NoInternetDialog(context , this);
        this.apifetcher = apifetcher;
        try {progressDialog = new ProgressDialog(context);progressDialog.setCancelable(false);progressDialog.setMessage(""+context.getResources().getString(R.string.loading));}catch (Exception e){}
        try {appMessage  =  AppMsg.makeText(activity, context.getString(R.string.no_internet_cponnection), new AppMsg.Style(AppMsg.LENGTH_STICKY, R.color.icons_8_muted_grey), R.layout.no_internet_layout_one);}catch (Exception e){}

    }



    public void execution_paystack(final String tag, String url, HashMap<String, String> bodyparameter  , boolean showProgressDialog  , String action){
        bodyparameter.put("language_code" , ""+ Locale.getDefault().getLanguage());
        if( mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                paystackMethod( tag,  url,  bodyparameter , false);
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                paystackMethod( tag,  url,  bodyparameter , showProgressDialog);
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                try {switch (action){
                    case ACTION_DO_NOTHING:break;
                    case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                    case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                    case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                }}catch (Exception e){}
            }
        }
    }

    private void paystackMethod(final String tag, String url, HashMap<String, String> bodyparameter , boolean showprogressdialog){
        //  Toast.makeText(mContext, "hello POST", Toast.LENGTH_SHORT).show();
        Log.d("**API Executing API (POST) ", "Hashparameters => " + bodyparameter);
        Log.d("**API Executing API (POST) ", "Name => " + tag + "  " + "URL => " + url);
        try {
            if(showprogressdialog){progressDialog.show();
            }}catch (Exception e){ }
        AndroidNetworking.post("" + url)
                .addBodyParameter(bodyparameter)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("*** RESPONSE "+tag, " " + jsonObject);
                        apifetcher.onFetchComplete(jsonObject, tag);  // important here we need to validate the response first can be later
                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                        Log.e(TAG, "" + anError.getErrorBody());
                        Log.e(TAG, "" + anError.getErrorDetail());
                        Log.e(TAG, "" + anError.getMessage());
                        Log.e(TAG, "" + anError.getStackTrace());
                        Log.e(TAG, "" + anError.getCause());
                    }
                });
    }






    @SuppressLint("LongLogTag")
    public void execution_method_post(final String tag, String url, HashMap<String, String> bodyparameter  , boolean showProgressDialog  , String action ) {
        bodyparameter.put("language_code" , ""+ Locale.getDefault().getLanguage());
        if( mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postMethod( tag,  url,  bodyparameter , false);
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postMethod( tag,  url,  bodyparameter , showProgressDialog);
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                try {switch (action){
                        case ACTION_DO_NOTHING:break;
                        case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                        case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                        case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                    }}catch (Exception e){}
            }
        }

    }


    @SuppressLint("LongLogTag")
    public void execution_method_post_withoutcheck(final String tag, String url, HashMap<String, String> bodyparameter  , boolean showProgressDialog  , String action ) {
        bodyparameter.put("language_code" , ""+ Locale.getDefault().getLanguage());
        if( mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postMethod_without_check( tag,  url,  bodyparameter , false);
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postMethod_without_check( tag,  url,  bodyparameter , showProgressDialog);
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                try {switch (action){
                    case ACTION_DO_NOTHING:break;
                    case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                    case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                    case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                }}catch (Exception e){}
            }
        }

    }



    public void execution_method_get(final String tag, String url , boolean showProgressDialog , String action) {
        Log.d("**API Executing API ", "Name => " + tag + "  " + "URL => " + url);
        if(mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                getterMethod(tag, url+"&language_code="+Locale.getDefault().getLanguage(),false);
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                getterMethod(tag, url+"&language_code="+Locale.getDefault().getLanguage() ,showProgressDialog);
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                switch (action){
                    case ACTION_DO_NOTHING:break;
                    case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                    case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                    case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                }
            }
        }
    }

    public void execution_method_without_result_check(final String tag, String url , boolean showProgressDialog , String action){
        Log.d("**API Executing API ", "Name => " + tag + "  " + "URL => " + url);
        if(mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                execution_method_getter_google_apis(tag, url ,false);
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                execution_method_getter_google_apis(tag, url ,showProgressDialog);
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                switch (action){
                    case ACTION_DO_NOTHING:break;
                    case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                    case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                    case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                }
            }
        }
    }

    public void execution_method_post_single_image(final String tag , String url , String image ,  String image_key , HashMap<String , String > data  , boolean showprogressdialog, String action){
        data.put("language_code" , ""+ Locale.getDefault().getLanguage());if( mActivity == null){
            // this mean that this method is called by some non UI Element
            if(isNetworkConnected(mContext)){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postsingleimage(tag ,url , image , image_key , data , showprogressdialog );
            }
        }else{
            if(isNetworkConnected(mActivity.getBaseContext())){
                try{if(appMessage!= null && appMessage.isFloating()){appMessage.cancel();}}catch (Exception e){}
                postsingleimage(tag ,url , image , image_key , data , showprogressdialog );
            }else {
                Log.d("**Internet Check", "Name => " + tag + "  " + "NO INTERNET");
                try {switch (action){
                    case ACTION_DO_NOTHING:break;
                    case ACTION_SHOW_DIALOG:noInternetDialog.showMessageDialog();break;
                    case ACTION_SHOW_TOAST:Toast.makeText(mContext, ""+mContext.getString(R.string.no_internet_cponnection), Toast.LENGTH_SHORT).show();break;
                    case ACTION_SHOW_TOP_BAR:try{appMessage.show();}catch (Exception e){}break;
                }}catch (Exception e){}
            }
        }
    }

    private void postMethod(final String tag, String url, HashMap<String, String> bodyparameter , boolean showprogressdialog){
      //  Toast.makeText(mContext, "hello POST", Toast.LENGTH_SHORT).show();
        Log.d("**API Executing API (POST) ", "Hashparameters => " + bodyparameter);
        Log.d("**API Executing API (POST) ", "Name => " + tag + "  " + "URL => " + url);
        try {
            if(showprogressdialog){progressDialog.show();
            }}catch (Exception e){ }
        AndroidNetworking.post("" + url)
                .addBodyParameter(bodyparameter)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("*** RESPONSE "+tag, " " + jsonObject);
                        if(validateResult(""+jsonObject)){
                            apifetcher.onFetchComplete(jsonObject, tag);}else{
                        // handle wrong response
                            apifetcher.onFetchResultZero(""+jsonObject);
                        }
                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                        Log.e(TAG, "" + anError.getErrorBody());
                        Log.e(TAG, "" + anError.getErrorDetail());
                        Log.e(TAG, "" + anError.getMessage());
                        Log.e(TAG, "" + anError.getStackTrace());
                        Log.e(TAG, "" + anError.getCause());
                    }
                });
    }


    private void postMethod_without_check(final String tag, String url, HashMap<String, String> bodyparameter , boolean showprogressdialog){
        //  Toast.makeText(mContext, "hello POST", Toast.LENGTH_SHORT).show();
        Log.d("**API Executing API (POST) ", "Hashparameters => " + bodyparameter);
        Log.d("**API Executing API (POST) ", "Name => " + tag + "  " + "URL => " + url);
        try {
            if(showprogressdialog){progressDialog.show();
            }}catch (Exception e){ }
        AndroidNetworking.post("" + url)
                .addBodyParameter(bodyparameter)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("*** RESPONSE "+tag, " " + jsonObject);
                       // if(validateResult(""+jsonObject)){
                            apifetcher.onFetchComplete(jsonObject, tag);
//                        }else{
//                        }
                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                        try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                        Log.e(TAG, "" + anError.getErrorBody());
                        Log.e(TAG, "" + anError.getErrorDetail());
                        Log.e(TAG, "" + anError.getMessage());
                        Log.e(TAG, "" + anError.getStackTrace());
                        Log.e(TAG, "" + anError.getCause());
                    }
                });
    }

    private void getterMethod(final String tag, String url , boolean showprogressDialog){
        Log.d("**API Executing API ", "Name => " + tag + "  " + "URL => " + url);
        try {if(showprogressDialog){progressDialog.show();}}catch (Exception e){ }
        AndroidNetworking.get(url)
                .setTag(this).setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject jsonObject) {
                Log.d("*** RESPONSE "+tag, "" + jsonObject);
                try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                if(validateResult(""+jsonObject)){apifetcher.onFetchComplete(jsonObject, tag);}else {}
            }

            @Override
            public void onError(ANError anError) {
                try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                Log.e(TAG, "" + anError.getErrorBody());
                Log.e(TAG, "" + anError.getErrorDetail());
                Log.e(TAG, "" + anError.getMessage());
                Log.e(TAG, "" + anError.getStackTrace());
                Log.e(TAG, "" + anError.getCause());
            }
        });
    }

    private void execution_method_getter_google_apis(final String tag, String url , boolean showprogressDialog){
        Log.d("**API Executing API ", "Name => " + tag + "  " + "URL => " + url);
        try {if(showprogressDialog){progressDialog.show();}}catch (Exception e){ }
        AndroidNetworking.get(url)
                .setTag(this).setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                }).getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject jsonObject) {
                Log.d("*** RESPONSE "+tag, "" + jsonObject);
                try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                apifetcher.onFetchComplete(jsonObject, tag);
            }

            @Override
            public void onError(ANError anError) {
                try {if(progressDialog.isShowing()){progressDialog.dismiss();}}catch (Exception e){}
                Log.e(TAG, "" + anError.getErrorBody());
                Log.e(TAG, "" + anError.getErrorDetail());
                Log.e(TAG, "" + anError.getMessage());
                Log.e(TAG, "" + anError.getStackTrace());
                Log.e(TAG, "" + anError.getCause());
            }
        });
    }


    @Override
    public void onDialogDismiss() {
        mActivity.finish();
    }

    @Override
    public void onDialogRetry() {
        Toast.makeText(mActivity, "The last qued Api Should run", Toast.LENGTH_SHORT).show();
    }

    public void postsingleimage(final String tag , String url , String image ,  String image_key , HashMap<String , String > data  , boolean showprogressdialog) {
        Log.d("**API Executing API (POST) ", "Hashparameters => " + data);

        Log.d("**API Executing API (POST)" , "Imagekey=> "+image_key );
        Log.d("**API Executing API (POST)" , "Imagepath => "+image );
        Log.d("**API Executing API (POST) ", "Name => " + tag + "  " + "URL => " + url);

        try {if(showprogressdialog){progressDialog.show();}}catch (Exception e){ }
        if (image.equals("")) {
            AndroidNetworking.upload(url).addMultipartParameter(data)
                    .addMultipartFile("" + image_key, new File(image))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("*** RESPONSE " + tag, " " + response);
                            if (validateResult("" + response)) {
                                apifetcher.onFetchComplete(response, tag);
                            } else {
                                apifetcher.onFetchResultZero(""+response);
                            }
                            try {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            try {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (Exception e) {
                            }
                            Log.e(TAG, "" + anError.getErrorBody());
                            Log.e(TAG, "" + anError.getErrorDetail());
                            Log.e(TAG, "" + anError.getMessage());
                            Log.e(TAG, "" + anError.getStackTrace());
                            Log.e(TAG, "" + anError.getCause());
                            apifetcher.onFetchResultZero("Server error");
                        }
                    });

        }
        else {

            AndroidNetworking.upload(url).addMultipartParameter(data)
                    .addMultipartFile("" + image_key, new File(image))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("*** RESPONSE " + tag, " " + response);
                            if (validateResult("" + response)) {
                                apifetcher.onFetchComplete(response, tag);
                            } else {
                            }
                            try {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            try {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (Exception e) {
                            }
                            Log.e(TAG, "" + anError.getErrorBody());
                            Log.e(TAG, "" + anError.getErrorDetail());
                            Log.e(TAG, "" + anError.getMessage());
                            Log.e(TAG, "" + anError.getStackTrace());
                            Log.e(TAG, "" + anError.getCause());
                        }
                    });

        }
    }


    public interface APIFETCHER {
        void onFetchComplete(Object script, String APINAME) ; // This will give the full script void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second);

        void onFetchResultZero(String s);

        //     void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second);
//        void onCheckResult(boolean value);
    }

    private boolean validateResult(String object){
        boolean returning_value = false ;

        // checking integer result
            try {CheckResult.ResultInt check_int = SingletonGson.getInstance().fromJson(""+object , CheckResult.ResultInt.class);
                Log.e(TAG+"@@@" , "Running ResultInt");
                if(check_int.getResult() == 1){
                    returning_value = true;
                } else{
                    returning_value = false;
                }
            }catch (Exception e){Log.e(TAG+" result_parsing_exception_ResultInt" , ""+e.getMessage());}




        // checking string result
            try {CheckResult.ResultString check_string = SingletonGson.getInstance().fromJson(""+object , CheckResult.ResultString.class);
                Log.e(TAG+"@@@" , "Running ResultString");
                if(check_string.getResult().equals("1")){returning_value = true;} else{returning_value = false;}
            }catch (Exception e){Log.e(TAG+" result_parsing_exception_ResultString" , ""+e.getMessage());}




        // checking integer status
            try {CheckResult.StatusInt check_status_int = SingletonGson.getInstance().fromJson(""+object , CheckResult.StatusInt.class);
                Log.e(TAG+"@@@" , "Running statusInt");
                if(check_status_int.getStatus() == 1){returning_value = true;returning_value = true;} else{returning_value = false;}
            }catch (Exception e){Log.e(TAG+" result_parsing_exception_StatusInt" , ""+e.getMessage());returning_value = true;}




        // checking string status
            try {CheckResult.StatusString check_status_string = SingletonGson.getInstance().fromJson(""+object , CheckResult.StatusString.class);
                Log.e(TAG+"@@@" , "Running statusString");
                if(check_status_string.getStatus().equals("1")){returning_value = true;returning_value = true;} else{returning_value = false;}
            }catch (Exception e){Log.e(TAG+" result_parsing_exception_StatusString" , ""+e.getMessage());returning_value = true;}





        return returning_value;
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
