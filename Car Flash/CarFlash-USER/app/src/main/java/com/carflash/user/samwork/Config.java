package com.carflash.user.samwork;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.carflash.user.R;
import com.carflash.user.models.firebasemodels.ChatModel;
import com.carflash.user.rentalmodule.taxirentalmodule.RentalPackageResponse;
import com.carflash.user.rentalmodule.taxirentalmodule.RentalRidenowModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 3/20/2017.
 */



public class Config {


    public static String currency_symbol = "₦";

    public static String Notification_RIDE_ID = "";
    public static String Notification_RIDE_STATUS = "";
    public static String BROADCASTNAME = "broadCastName";
    public static boolean ShowAcceptDialog_is_visible = false;
    public static boolean ShowReject_is_visible = false;
    public static Location currentLocation ;
    public static float MapDefaultZoom = 15;
    public static String ChatReferencetable = "Chat";
    public static ArrayList<LatLng> tracking_data  = new ArrayList<>();
    public static String DriverRefrence = "Drivers_A";
    public static String GeoFireReference = "geofire";
    public static String RideTableReference = "RideTable";

    public static List<ChatModel> cities = new ArrayList<ChatModel>(){{
        add(new ChatModel("NOCHAT" , "NO CHAT" , ""+System.currentTimeMillis()));
    }};


    public static boolean app_loaded_from_initial = false ;
    public static boolean is_TrialRideConfirmCtivityisopen = false ;

    public static String Users = "USER";


    public static String TrackDriver = "Driver Meter";
    public static String AppSalar_APIKEY = "samirgoel3";
    public static String APPSALAR_SECRET = "0fbLRQcL";


    public static String COUPON_CODE = "";


    public static DataSnapshot mDataSnapShot ;
    public static long Location_Updation_Interval = 10000;


    /////////////////rental //////////////////////////////////


    public static String Base_Url = "";
    public static String Image_Base_Url = "";
    public static String City_Id = "";
    public static String User_id = "";
    public static String User_latitude = "";
    public static String User_longitude = "";
    public static String User_location = "";


    public static RentalPackageResponse response ;

    public static  RentalPackageResponse.DetailsBean SELECTED_PACKAGE ;
    public static String SELECTED_PACKAGE_ID = "";
    public static String SELECTED_PACKAGE_NAME = "";
    public static int SELECTED_PACKAGE_POSITION ;

    public static RentalPackageResponse.DetailsBean.RentalPakageCarBean SELECTED_RENTAL_CAR_BEAN ;


    public static boolean is_confirm_rental_activity_is_open = false ;

    public static int Minimum_time_for_ride_later = 15 ;
    public static int rideLoaderActivity;

    public static boolean PostedRequest_RENTAL = false ;
    public static int PostedRentalType = 0 ;
    public static RentalRidenowModel rental_ride_now_response ;
    // need to be removed

    public static String RideId = "";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////



    public interface ApiKeys{
        String
                KEY_UpdateDevice_ID = "KEY_UpdateDevice_ID";
        String KEY_CallSupport = "KEY_CallSupport";
        String KEY_VersionUpdate = "KEY_VersionUpdate";
        String KEY_PaymentOption = "KEY_PaymentOption";
        String Key_View_Cars = "Key_View_Cars";
        String KEY_FLAG = "1";
        String Key_Rice_Now = "Key_Rice_Now";
        String Key_Ride_Later = "Key_Ride_Later";
        String Key_Estimate  = "Key_Estimate";
        String Key_Cancel_by_user  = "Key_Cancel_by_user";
        String Key_NearRoadLocation  = "Key_NearRoadLocation";
        String Key_ViewRideInfo  = "Key_ViewRideInfo";
        String Key_cancelRideReason  = "Key_cancelRideReason";
        String Key_Cancel_Ride  = "Key_Cancel_Ride";
        String Key_Google_Distance_matrix  = "Key_Google_Distance_matrix";
        String Key_Rating_Api = "rating_normal" ;
        String Key_payment_api = "payment_api";
        String Key_view_Done_Ride = "view_done_ride";
        String Key_Virew_Cities = "view_cities";
        String Key_Virew_Rate_Card_Cities = "rate_card_cities";
        String Key_View_car_by_Cities = "viewCarByCities";
        String Key_Customer_Sync = "Key_Customer_Sync";
        String Key_Customer_Sync_End = "Key_Customer_Sync_End";
        String KeyApplyCoupon = "coupon";
        String KeyAboutUs = "aboutUs";
        String Ket_terms_and_condition = "terms_and_condition";
        String KEY_EDIT_PROFILE = "edit_profile";
        String KEY_ACTIVES_RIDES = "actives_rides";
        String KEY_FORGOTPASS_OTP = "forgot_pass_otp";
        String KEY_FORGOTPASS_CONFIRMPASS = "forgot_pass_confirm_pass";
        String KEY_ADD_CREDIT_CARD = "add_credit_card";
        String KEY_SAVED_CARD = "save_cards";

        String KEY_SOS = "KEY_SOS";
        String KEY_REPORT_ISSUE = "KEY_REPORT_ISSUE";
        String KEY_SOS_REQUEST = "SOS_REQUEST";



        ///   rest apis   cartype ket is used as it was before
        String KEY_REST_RIDE_SYNC = "KEY_REST_RIDE_SYNC";
        String KEY_REST_RIDE_INFO = "KEY_REST_RIDE_INFO";
        String KEY_REST_DONE_RIDE_INFO = "KEY_REST_DONE_RIDE_INFO";
        String KEY_REST_RATING = "KEY_REST_RATING";
        String KEY_REST_RIDE_HISTORY = "KEY_REST_RIDE_HISTORY";
        String KEY_REST_RIDE_DETAILS = "KEY_REST_RIDE_DETAILS";
        String KEY_REST_RENTAL_CANCEL_RIDE = "KEY_REST_RENTAL_CANCEL_RIDE";
        String KEY_RENTAL_MAKE_PAYMENT = "KEY_RENTAL_MAKE_PAYMENT";
        String KEY_RENTAL_RATING = "KEY_RENTAL_RATING";
        String KEY_CUSTOMER_SUPPORT = "customer_support";
        String KEY_REST_NOTIFICATIONS = "KEY_NOTIFICATIONS";

        String KEY_PAY_WITH_PAYSTACK = "PAY_WITH_PAYSTACK";
        String CONFIRM_PAYMENT = "CONFIRM_PAYMENT";
        String KEY_VERIFY_OTP = "verify_otp";


        ////////////////// account module

        String KEY_REGISTER = "KEY_REGISTER";
        String KEY_PHONE_LOGIN = "KEY_PHONE_LOGIN";
        String KEY_CHANGE_PASSWORD = "KEY_CHANGE_PASSWORD";
        String KEY_PHONE_INFO = "KEY_PHONE_INFO";
        String KEY_FACEBOOK_SIGNUP = "KEY_FACEBOOK_SIGNUP" ;
        String KEY_FACEBOOK_LOGIN = "KEY_FACEBOOK_LOGIN" ;
        String KEY_GOOGLE_LOGIN = "KEY_GOOGLE_LOGIN";
        String KEY_GOOGLE_SINGNUP = "KEY_GOOGLE_SINGNUP";
        String KEY_UPDATE_EMAIL = "KEY_UPDATE_EMAIL" ;
        String KEY_LOGOUT = "KEY_LOGOUT" ;
        String KEY_DEMO_USER = "KEY_DEMO_USER" ;
        String KEY_CHANGE_DESTINATION  = "change_destination";
        String Key_Wallet_blance = "Walletbalance";
        String key_view_cards = "view_cards";
        String Key_Save_cards = "save_cards";
        String Key_Add_Money_to_Wallet = "add_money_to_wallet";
        String Key_Delete_Cards = "delete_cards";


        /// rental
        String KEY_RENTAl_PACKAGE = "Rental_Package";
        String KEY_RENTAl_Book_car = "Book_Car";
        String KEY_RENTAl_Coupons = "coupon";
        String APP_VERSIONS= "APP_VERSIONS";

        String SOS_REQUEST_NOTIFIER = "Sos_Request";

    }


    public interface IntentKeys{
        String PICK_LATITUDE = "pick_latitude";
        String PICK_LONGITUDE = "pick_longitude";
        String PICK_LOCATION_TXT = "pick_location_txt";
        String DROP_LATITUDE = "drop_latitude";
        String DROP_LONGITUDE = "drop_longitude";
        String DROP_LOCATIOn_TXT = "drop_location_txt";
        String TIME = "time";
        String DATE = "date";
        String CAR_IMAGE = "car_image";
        String CITY_NAME = "city_name";
        String Ride_id = "ride_id";
        String KEY_USER_ID = "user_id";
        String KEY_OLD_PASSWORD = "old_password";

        ////// rental
        String Baseurl = "base_urls";
        String CityId = "cityid";
        String Image_base_url = "image_base_url";
        String user_id = "user_id";
        String user_location = "user_location";
        String user_latitude = "user_latitude";
        String user_longitude = "user_longitude";

        String currency_symboll = "currency_sumbol";
    }

    public interface Status{
        String NORMAL_BOOKING = "1";  // ride booked by user
        String NORMAL_CANCEL_BY_USER = "2";  //  ride cancelled by user
        String NORMAL_ACCEPTED_BY_DRIVER = "3";  // accepted by driver
        String NORMAL_REJECTED_BY_DRIVER = "4";  // Ride cancelled by driver and trying to alloting to other
        String NORMAL_ARRIVED = "5";  // driver arrived on door
        String NORMAL_RIDE_STARTED = "6";  //  Ride started
        String NORMAL_RIDE_END = "7";  ///  ride ended by driver
        String NORMAL_LATER_BOOKINg = "8";  // when user booked ride for later
        String NORMAL_CANCEL_BY_DRIVER = "9";  // ride cancelled by driver

        // for rental approach
        String RENTAL_BOOKED = "10";  // ride booked via user
        String RENTAL_ACCEPTED = "11";  // rental booking acepted by driver
        String RENTAL_ARRIVED = "12";  // rental driver arrived
        String RENTAl_RIDE_STARTED = "13";  // rental ride started by driver
        String RENTAL_RIDE_REJECTED = "14";  // rental ride reject by driver
        String RENTAL_RIDE_CANCEL_BY_USER = "15";  // rental ride cancelled by user
        String RENTAL_RIDE_END = "16";  // rental ride end by driver
        String NORMAL_RIDE_CANCEl_BY_ADMIN = "17";
        String RENTAL_RIDE_CANCEL_BY_DRIVER  = "18";
        String RENTAL_RIDE_CANCEl_BY_ADMIN  = "19";
        String CHANGED_DESTINATION_ADDRESS  = "20";
        String RIDE_EXPIRE_BY_CLICKING_CROSS = "Ride Expire By Clicking Cross";
        String RIDE_EXPIRE_AUTOMATICALLY_VIA_PULSE = "Ride Expire Automatically Via Pulse";
        String PROMOTIONAL_NOTIFICATION = "51";
        String PARTIAL_ACCEPT = "22";
    }


    public static String getStatustext(String val , Context context){
      if(val.equals(""+Status.NORMAL_BOOKING)){
          return  context.getString(R.string.ride_booked);
      } if(val.equals(""+Status.NORMAL_CANCEL_BY_USER)){
            return  context.getString(R.string.ride_cancelled);
        } if(val.equals(""+Status.NORMAL_ACCEPTED_BY_DRIVER)){
            return  context.getString(R.string.driver_on_way);
        } if(val.equals(""+Status.NORMAL_REJECTED_BY_DRIVER)){
            return  context.getString(R.string.booking_failed);
        } if(val.equals(""+Status.NORMAL_ARRIVED)){
            return  context.getString(R.string.ride_arrived);
        } if(val.equals(""+Status.NORMAL_RIDE_STARTED)){
            return  context.getString(R.string.riding_now);
        } if(val.equals(""+Status.NORMAL_RIDE_END)){
            return  context.getString(R.string.ride_ended);
        } if(val.equals(""+Status.NORMAL_LATER_BOOKINg)){
            return  context.getString(R.string.Ride_scheduled);
        } if(val.equals(""+Status.NORMAL_CANCEL_BY_DRIVER)){
            return  context.getString(R.string.ride_cancelled_by_driver);
        }if(val.equals(""+Status.RENTAL_BOOKED)){
            return  context.getString(R.string.ride_booked);
        }if(val.equals(""+Status.RENTAL_ARRIVED)){
            return  context.getString(R.string.ride_arrived);
        }if(val.equals(""+Status.RENTAl_RIDE_STARTED)){
            return  context.getString(R.string.riding_now);
        }if(val.equals(""+Status.RENTAL_ACCEPTED)){
            return  context.getString(R.string.ride_booked);
        }if(val.equals(""+Status.RENTAL_RIDE_END)){
            return  context.getString(R.string.ride_ended);
        }if(val.equals(""+Status.RENTAL_RIDE_REJECTED)){
            return  context.getString(R.string.ride_cancelled);
        }
        if(val.equals(""+Status.RENTAL_RIDE_CANCEL_BY_USER)){
            return  context.getString(R.string.ride_cancelled_by_you);
        }
        if(val.equals(""+Status.NORMAL_RIDE_CANCEl_BY_ADMIN)){
            return  context.getString(R.string.admin_cancel);
        }
        if(val.equals(""+Status.RENTAL_RIDE_CANCEL_BY_DRIVER)){
            return  context.getString(R.string.driver_cancel);
        }
        if(val.equals(""+Status.RENTAL_RIDE_CANCEl_BY_ADMIN)){
            return  context.getString(R.string.admin_cancelled);
        }
        if(val.equals(""+Status.PARTIAL_ACCEPT)){
            return  context.getString(R.string.allotted);
        }
        else {
            return  context.getString(R.string.something_went_wrong);
        }

    }


    interface MarkerSets{


        String CarCode_SEDAN = "1" ;
        String CarCodee_HATCHBACK = "2" ;
        String CarCode_MINI = "3" ;
        String CarCode_LUXUR = "4" ;
        String CarCode_SUV = "5" ;
        String CarCode_PRESTIGE= "6" ;


        int ICON_SEDAN = R.drawable.blue_car;
        int ICON_HATCHBACK = R.drawable.yellow_car;
        int ICON_MINI = R.drawable.red_car;
        int ICON_LUXURY = R.drawable.grey_car;
        int ICON_SUV = R.drawable.blue_car;
        int ICON_PRESTIGE = R.drawable.red_car;
        int ICON_DEFAULT = R.drawable.red_car;
    }

    public static int getMarkerIcon(String CarCode){
        switch (CarCode){
            case MarkerSets.CarCode_SEDAN:
                return MarkerSets.ICON_SEDAN ;
            case MarkerSets.CarCodee_HATCHBACK:
                return MarkerSets.ICON_HATCHBACK ;
            case MarkerSets.CarCode_MINI:
                return MarkerSets.ICON_MINI;
            case MarkerSets.CarCode_LUXUR:
                return MarkerSets.ICON_LUXURY;
            case MarkerSets.CarCode_SUV:
                return MarkerSets.ICON_SUV;
            case MarkerSets.CarCode_PRESTIGE:
                return MarkerSets.ICON_PRESTIGE ;
            default: return MarkerSets.ICON_DEFAULT;
        }
    }



    //////////////////////////  data for API
    public static String polyline_color = "#000000";
    public static int polyline_width = 5;
    public static double tail_value_on_track_ride_screem = 200.0 ;
    public static int map_theme = 1 ;  // 0 for default , 1 for uber silver theme






    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static int Screen_width(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int Screen_height (Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static boolean isEmailValid(String email) {
        Log.d("validating email" , ""+email);
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean ismobileValid(String phone) {
        Log.d("validating email" , ""+phone);
        if (phone == null) {
            return false;
        } else {
            if(phone.length() < 10){
                return false;
            }else{
                return true;
            }

        }
    }

    public static String getTimeFromTimeStamp  (Long timestamp ){

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }


}
