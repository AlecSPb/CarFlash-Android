package com.carflash.user.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private String TAG  = "SessionManager";
    private static final String PREF_NAME = "LoginPrefrences";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String USER_ID = "user_id";
    public static final String USER_FIRST_NAME = "user_first_name";
    public static final String USER_LAST_NAME = "user_last_name";
    public static final String USER_PHONE = "user_phone_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_ONLINE_OFFLINE = "online_offline_status";
    public static final String USER_LOGIN_STATUS = "registered_status";
    public static final String USER_DEVICE_ID = "device_id";
    public static final String USER_IMAGE = "user_image";


    public static final String FACEBOOK_ID = "facebook_id";
    public static final String FACEBOOK_MAIL = "facebook_mail";
    public static final String FACEBOOK_IMAGE = "facebook_image";
    public static final String FACEBOOK_FIRSTNAME = "facebook_firstnme";
    public static final String FACEBOOK_LASTNAME = "facebook_lastname";

    public static final String GOOGLE_ID = "google_id";
    public static final String GOOGLE_NAME = "google_name";
    public static final String GOOGLE_MAIL = "googlr_mail";
    public static final String GOOGLE_IMAGE = "google_image";

    public static final String LOGIN_TYPE = "logintype";

    public static final String LOGIN_NORAL= "normal";
    public static final String LOGIN_GOOGLE = "google";
    public static final String LOGIN_FACEBOOK = "facebook";
    public static final String UNIQUE_NUMBER = "unique_number";
    public static final String TAIL_FACTOR = "tail_factor";
    public static final String NOTIFICSTION_IDS = "notification_ids";
    public static final String LANGUAGE = "Languagae";

    public static final String Currency_symbol = "currency_symboll";
    public static final String Currency_ISO_Code = "currency_iso_code";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getLanguage(){
        HashMap<String, String> data = new HashMap<>();
        Log.d("" +TAG, "Getting language = "+pref.getString(LANGUAGE, ""));
        return pref.getString(LANGUAGE, "");
    }


    public void setLanguage (String locale ){
        Log.d("" +TAG, "Setting languge = "+locale);
        editor.putString(LANGUAGE,""+ locale);
        editor.commit();

        Locale myLocale = new Locale(""+locale);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        _context.getResources().updateConfiguration(config, _context.getResources().getDisplayMetrics());

    }


    public void setDeviceId(String device_id){
        editor.putString(USER_DEVICE_ID, device_id);
        editor.commit();
    }


    public void setTailFactor(String tail_factor){
        editor.putString(TAIL_FACTOR, tail_factor);
        editor.commit();
    }

    public void addNotificationId(String id){
        editor.putString(NOTIFICSTION_IDS, ""+id);
        editor.commit();
    }

    public void createNewPassword(String newPassword){
        editor.putString(USER_PASSWORD, newPassword);
        editor.commit();
    }

    public void createLoginSession(String user_id, String user_name,String user_last_name ,  String user_email, String user_phone,String userimage, String password ,String online_offline , String device_id,
                                   String facebook_id , String facebook_mail , String facebook_image , String facebook_firstname , String facebook_lastname,
                                   String google_id , String google_name , String google_mail  , String google_image , String login_type  , String  unique_number) {


        Log.d("SESSION MANAGER  , CREATING SESSION " , ""+user_id+" "+user_name+" "+user_last_name+" "+user_email+" "+user_phone+" "+password+" "+online_offline+" "+device_id+" "+facebook_id+" "+facebook_mail+" "+facebook_image+" "+facebook_firstname+" "+facebook_lastname+" "+google_id+" "+google_image+" "+google_mail+" "+google_name+" ");


        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.putString(USER_FIRST_NAME, user_name);
        editor.putString(USER_LAST_NAME, user_last_name);
        editor.putString(USER_EMAIL, user_email);
        editor.putString(USER_PHONE, user_phone);
        editor.putString(USER_IMAGE, userimage);
        editor.putString(USER_PASSWORD, password);
        editor.putString(USER_ONLINE_OFFLINE, online_offline);
        editor.putString(USER_DEVICE_ID, device_id);

        editor.putString(FACEBOOK_ID, facebook_id);
        editor.putString(FACEBOOK_MAIL, facebook_mail);
        editor.putString(FACEBOOK_IMAGE, facebook_image);
        editor.putString(FACEBOOK_FIRSTNAME, facebook_firstname);
        editor.putString(FACEBOOK_LASTNAME, facebook_lastname);


        editor.putString(GOOGLE_ID, google_id);
        editor.putString(GOOGLE_NAME, google_name);
        editor.putString(GOOGLE_MAIL, google_mail);
        editor.putString(GOOGLE_IMAGE, google_image);
        editor.putString(LOGIN_TYPE, login_type);
        editor.putString(UNIQUE_NUMBER, unique_number);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, pref.getString(USER_ID, ""));
        user.put(USER_FIRST_NAME, pref.getString(USER_FIRST_NAME, ""));
        user.put(USER_LAST_NAME, pref.getString(USER_LAST_NAME, ""));
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, "default@gmail.com"));
        user.put(USER_PHONE, pref.getString(USER_PHONE, ""));
        user.put(USER_PASSWORD, pref.getString(USER_PASSWORD, ""));
        user.put(USER_ONLINE_OFFLINE, pref.getString(USER_ONLINE_OFFLINE, ""));
        user.put(USER_DEVICE_ID, pref.getString(USER_DEVICE_ID, ""));
        user.put(USER_IMAGE, pref.getString(USER_IMAGE, ""));



        user.put(FACEBOOK_ID, pref.getString(FACEBOOK_ID, ""));
        user.put(FACEBOOK_MAIL, pref.getString(FACEBOOK_MAIL, ""));
        user.put(FACEBOOK_IMAGE, pref.getString(FACEBOOK_IMAGE, ""));
        user.put(FACEBOOK_FIRSTNAME, pref.getString(FACEBOOK_FIRSTNAME, ""));
        user.put(FACEBOOK_LASTNAME, pref.getString(FACEBOOK_LASTNAME, ""));



        user.put(GOOGLE_ID, pref.getString(GOOGLE_ID, ""));
        user.put(GOOGLE_NAME, pref.getString(GOOGLE_NAME, ""));
        user.put(GOOGLE_MAIL, pref.getString(GOOGLE_MAIL, ""));
        user.put(GOOGLE_IMAGE, pref.getString(GOOGLE_IMAGE, ""));
        user.put(LOGIN_TYPE, pref.getString(LOGIN_TYPE, ""));
        user.put(UNIQUE_NUMBER, pref.getString(UNIQUE_NUMBER, ""));
        user.put(TAIL_FACTOR, pref.getString(TAIL_FACTOR, "100"));
        user.put(NOTIFICSTION_IDS , pref.getString(NOTIFICSTION_IDS , "0"));
        return user;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }


    public String getEmail (){
        String email = "";
        if(pref.getString(LOGIN_TYPE, "").equals(""+LOGIN_NORAL)){
            email = pref.getString(USER_EMAIL, "") ;
        }if(pref.getString(LOGIN_TYPE, "").equals(""+LOGIN_FACEBOOK)){
            email = pref.getString(FACEBOOK_MAIL, "") ;
        }if(pref.getString(LOGIN_TYPE, "").equals(""+LOGIN_GOOGLE)){
            email = pref.getString(GOOGLE_MAIL, "") ;
        }
        return  email;
    }



    public String getCurrencyCode(){

        if(pref.getString(Currency_symbol, "").equals("0")){
            return ""+pref.getString(Currency_ISO_Code, "ISO") ;
        }if (pref.getString(Currency_ISO_Code, "").equals("0")){
            return ""+getSymbol() ;
        }else{
          return ""+getSymbol();
        }
    }


    public void setCurrencyCode(String iso_code , String symbol ){

        Log.i("" +TAG , "Setting symbol = "+symbol);
        Log.i("" +TAG , "Setting ISO = "+iso_code);
        editor.putString(Currency_ISO_Code, ""+iso_code);
        editor.putString(Currency_symbol, ""+symbol);
        editor.commit();
    }




    public String getSymbol(){
        String currency = "NA";
        switch (pref.getString(Currency_symbol, "")){
            case "0024":
                currency = "\u0024";
                break;
            case "00A2":
                currency = "\u00A2";
                break;
            case "00A3":
                currency = "\u00A3";
                break;
            case "00A5":
                currency = "\u00A5";
                break;
            case "058F":
                currency = "\u058F";
                break;
            case "060B":
                currency = "\u060B";
                break;
            case "09F2":
                currency = "\u09F2";
                break;
            case "20A0":
                currency = "\u20A0";
                break;
            case "20A1":
                currency = "\u20A1";
                break;
            case "20A2":
                currency = "\u20A2";
                break;
            case "20A3":
                currency = "\u20A3";
                break;
            case "20A4":
                currency = "\u20A4";
                break;
            case "20A5":
                currency = "\u20A5";
                break;
            case "20A6":
                currency = "\u20A6";
                break;
            case "20A7":
                currency = "\u20A7";
                break;
            case "20A8":
                currency = "\u20A8";
                break;
            case "20A9":
                currency = "\u20A9";
                break;
            case "20AA":
                currency = "\u20AA";
                break;
            case "20AB":
                currency = "\u20AB";
                break;
            case "20AC":
                currency = "\u20AC";
                break;
            case "20AD":
                currency = "\u20AD";
                break;
            case "20AE":
                currency = "\u20AE";
                break;
            case "20AF":
                currency = "\u20AF";
                break;
            case "20B0":
                currency = "\u20B0";
                break;
            case "20B1":
                currency = "\u20B1";
                break;
            case "20B2":
                currency = "\u20B2";
                break;
            case "20B3":
                currency = "\u20B3";
                break;
            case "20B4":
                currency = "\u20B4";
                break;
            case "20B5":
                currency = "\u20B5";
                break;
            case "20B6":
                currency = "\u20B6";
                break;
            case "20B7":
                currency = "\u20B7";
                break;
            case "20B8":
                currency = "\u20B8";
                break;
            case "20B9":
                currency = "\u20B9";
                break;
            case "20BA":
                currency = "\u20BA";
                break;
            case "20BB":
                currency = "\u20BB";
                break;
            case "20BC":
                currency = "\u20BC";
                break;
            case "20BD":
                currency = "\u20BD";
                break;
            case "20BE":
                currency = "\u20BE";
                break;
            case "20BF":
                currency = "\u20BF";
                break;
            case "A838":
                currency = "\uA838";
                break;
            case "FDFC":
                currency = "\uFDFC";
                break;
            case "FE69":
                currency = "\uFE69";
                break;
            case "FF04":
                currency = "\uFF04";
                break;
            case "FFE0":
                currency = "\uFFE0";
                break;
            case "FFE1":
                currency = "\uFFE1";
                break;
            case "FFE5":
                currency = "\uFFE5";
                break;
            case "FFE6":
                currency = "\uFFE6";
                break;
            case "00024":
                currency = "\u0024";
                break;
            default:
                currency = "##";
                break;
        }

        return currency ;
    }

}
