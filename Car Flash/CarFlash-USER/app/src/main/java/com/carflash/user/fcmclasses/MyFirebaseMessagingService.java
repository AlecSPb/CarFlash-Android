package com.carflash.user.fcmclasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.carflash.user.NotificationActivity;
import com.carflash.user.PaymentActivity;
import com.carflash.user.R;
import com.carflash.user.RentalReceiptActivity;
import com.carflash.user.RentalTrackActivity;
import com.carflash.user.SplashActivity;
import com.carflash.user.StatusSession.DBHelper;
import com.carflash.user.TripHistoryActivity;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.NewRideSync;
import com.carflash.user.models.NewRideSyncModel;
import com.carflash.user.others.Constants;
import com.carflash.user.TrialActivity;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.trackRideModule.TrackRideAactiviySamir;
import com.carflash.user.urls.Apis;
import com.carflash.user.manager.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements  ApiManager.APIFETCHER {

    Intent intent;
    String pn_message, pn_ride_id, pn_ride_status;

    SessionManager sessionManager;
    LanguageManager languageManager;
    DBHelper dbHelper ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        pn_message = remoteMessage.getData().get("message");
        pn_ride_id = remoteMessage.getData().get("ride_id");
        pn_ride_status = remoteMessage.getData().get("ride_status");
        String app_id = remoteMessage.getData().get("app_id");
        Log.d("***pn_message_customer      "  , ""+ pn_message);
        Log.d("***pn_ride_id_customer       "  , ""+ pn_ride_id);
        Log.d("***pn_ride_status_customer       " , "" + pn_ride_status);
        Log.d("***app_id_customer       " , "" + app_id);

        if (app_id.equals("1")) {
            checkStatus();
        }
    }

    void checkStatus() {
        sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);
        dbHelper = new DBHelper(this);
        if(pn_ride_status.equals("9") && Constants.Track_RideActivity_is_open == 1){
            sendNotification(""+pn_message);
        }
        if (pn_ride_status.equals("3") || pn_ride_status.equals("4") || pn_ride_status.equals("5")  || pn_ride_status.equals("6")||pn_ride_status.equals("9")) {  // ride status = 3(accepted) , 4(Cancelled by user ) , 5(driver Arrived) , 6(ride started) , 9(cancelled by driver)
            new ApiManager(this , null , this ).execution_method_get(Config.ApiKeys.Key_Customer_Sync, ""+ Apis.customerSync+"?ride_id="+pn_ride_id+"&user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID) , false,ApiManager.ACTION_DO_NOTHING);
        }else if (pn_ride_status.equals("7")) {
            new ApiManager(this , null , this ).execution_method_get(Config.ApiKeys.Key_Customer_Sync_End , ""+""+Apis.customerSyncEnd+"?ride_id="+pn_ride_id+"&user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID) , false,ApiManager.ACTION_DO_NOTHING);
        }if(pn_ride_status.equals("11") || pn_ride_status.equals("12") || pn_ride_status.equals("13") ||  pn_ride_status.equals("14") || pn_ride_status.equals("16")){
            HashMap<String , String > data = new HashMap<>();
            data.put("rental_booking_id" , ""+pn_ride_id);
            data.put("app_id" , "1");
            new ApiManager(this , null , this ).execution_method_post(""+Config.ApiKeys.KEY_REST_RIDE_SYNC , ""+ Apis.RideSync, data, false,ApiManager.ACTION_DO_NOTHING);
        }if(pn_ride_status.equals("18")){
            if(Constants.isRentaltrackActivityIsOpen){
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("ride_id",pn_ride_id);

                broadcastIntent.putExtra("ride_status", pn_ride_status);
                broadcastIntent.setAction("com.carflash.user");
                sendBroadcast(broadcastIntent);
            } else {
                sendNotification(pn_message);
            }
        }
        else if (pn_ride_status.equals(""+Config.Status.CHANGED_DESTINATION_ADDRESS)){
            if(Constants.Track_RideActivity_is_open == 0){ // that is activity is open
//                EventBus.getDefault().post(new ChangeLocationEvent(""+pn_ride_status));
            }else {
                sendNotification(pn_message);
            }
        }
        else if (pn_ride_status.equals(""+Config.Status.PROMOTIONAL_NOTIFICATION)){
            sendNotification(pn_message);
        }else if (pn_ride_status.equals(""+Config.Status.PARTIAL_ACCEPT)){
            sendNotification(pn_message);
        }else if (pn_ride_status.equals(""+Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN)){
            sendNotification(pn_message);
        }else if (pn_ride_status.equals(""+Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN)){
            if(!Constants.isRentaltrackActivityIsOpen){
                sendNotification(""+pn_message);
            }
        }
    }

    void sendNotification(String message) {
        if (pn_ride_status.equals("3") || pn_ride_status.equals("4") || pn_ride_status.equals("5")  || pn_ride_status.equals("6")) {
                intent = new Intent(this, TrackRideAactiviySamir.class)
                        .putExtra("ride_id", pn_ride_id)
                        .putExtra("ride_status", pn_ride_status);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals("7")) {
            intent = new Intent(this, PaymentActivity.class)
                    .putExtra("DONE_RIDE_ID", pn_ride_id)
                    .putExtra("ride_status", pn_ride_status);
        } else if (pn_ride_status.equals("9")) {
            intent = new Intent(this, TrialActivity.class)
                    .putExtra("ride_id", pn_ride_id)
                    .putExtra("ride_status", pn_ride_status);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (pn_ride_status.equals(Config.Status.RENTAL_ACCEPTED) ||  pn_ride_status.equals(Config.Status.RENTAL_ARRIVED ) || pn_ride_status.equals(Config.Status.RENTAl_RIDE_STARTED)  ) {
            intent = new Intent(this, RentalTrackActivity.class)
                    .putExtra("ride_id", pn_ride_id)
                    .putExtra("ride_status", pn_ride_status);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(Config.Status.RENTAL_RIDE_REJECTED)) {
            intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(Config.Status.RENTAL_RIDE_END)) {
            intent = new Intent(this, RentalReceiptActivity.class);
            intent.putExtra("ride_id" , ""+pn_ride_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(""+Config.Status.RENTAL_RIDE_CANCEL_BY_DRIVER)){
            intent = new Intent(this, TripHistoryActivity.class);
            intent.putExtra("ride_id" , ""+pn_ride_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(""+Config.Status.CHANGED_DESTINATION_ADDRESS)){
            intent = new Intent(this, TrackRideAactiviySamir.class)
                    .putExtra("ride_id", pn_ride_id)
                    .putExtra("ride_status", pn_ride_status);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(""+Config.Status.PROMOTIONAL_NOTIFICATION)){
            intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if(pn_ride_status.equals(""+Config.Status.PARTIAL_ACCEPT)) {
            intent = new Intent(this, TripHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(""+Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN)){
            intent = new Intent(this, TripHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals(""+Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN)){
            intent = new Intent(this, TripHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_100);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo_100)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_100);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo_100)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
//                    .setColor(Color.parseColor("#0x008000"))
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        }


        //////////
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_100);
//
//        int color = 0x008000;
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.app_logo_100)
//                .setLargeIcon(largeIcon)
//                .setColor(color)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(alarmSound)
//                .setVibrate(pattern)
//                .setContentIntent(pendingIntent);
//        notificationManager.notify(0, notificationBuilder.build());
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (APINAME.equals(""+Config.ApiKeys.Key_Customer_Sync)) {
                NewRideSync newRideSync = gson.fromJson(""+script, NewRideSync.class);
                if (newRideSync.getResult() == 1) {
                    pn_message = newRideSync.getMsg();
                    pn_ride_id = newRideSync.getDetails().getRide_id();
                    pn_ride_status = newRideSync.getDetails().getRide_status();

                    if (pn_ride_status.equals("3") && Constants.is_main_Activity_open == false) {
                        sendNotification(pn_message);
                        dbHelper.insertRow(pn_ride_id , pn_ride_status);
                    }
                    else if (pn_ride_status.equals("4")  && Constants.is_main_Activity_open == false) {
                        sendNotification(pn_message);
                    }
                    else if (pn_ride_status.equals("5") && Constants.Track_RideActivity_is_open == 1) {
                        sendNotification(pn_message);
                        dbHelper.insertRow(pn_ride_id , pn_ride_status);
                    }
                    else if (pn_ride_status.equals("6") && Constants.Track_RideActivity_is_open == 1) {
                        sendNotification(pn_message);
                        dbHelper.insertRow(pn_ride_id , pn_ride_status);
                    }
                    else if (pn_ride_status.equals("9") && Constants.Track_RideActivity_is_open == 0) {
                        sendNotification(pn_message);
                    }
                } else if(newRideSync.getResult() == 0 ){

                    if (Constants.is_main_Activity_open == true) {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra("ride_id", pn_ride_id);
                        broadcastIntent.putExtra("ride_status", pn_ride_status);
                        Config.Notification_RIDE_ID = pn_ride_id ;
                        Config.Notification_RIDE_STATUS = pn_ride_status ;
                        broadcastIntent.setAction("com.carflash.user");
                        sendBroadcast(broadcastIntent);
                    } else if (Constants.is_main_Activity_open == false) {
                        sendNotification(pn_message);
                    } else {
                        sendNotification(pn_message);
                    }

                }
            }

            if (APINAME.equals(""+Config.ApiKeys.Key_Customer_Sync_End)) {

                NewRideSync newRideSync = new NewRideSync();
                newRideSync = gson.fromJson(""+script, NewRideSync.class);

                if (newRideSync.getResult() == 1) {
                    pn_message = newRideSync.getMsg();
                    pn_ride_id = newRideSync.getDetails().getRide_id();
                    pn_ride_status = newRideSync.getDetails().getRide_status();
                    if (pn_ride_status.equals("7") || Constants.Track_RideActivity_is_open == 1) {
                        sendNotification(pn_message);
                    }
                } else {

                }
            }

            if(APINAME.equals(""+Config.ApiKeys.KEY_REST_RIDE_SYNC)){
                NewRideSyncModel ride_sync_response = gson.fromJson(""+script , NewRideSyncModel.class);
                Log.d("******RentalRideinfoMessage isRentalRideloaderIsOpen" , ""+Constants.isRentalRideloaderIsOpen);
                Log.d("******RentalRideinfoMessage isRentaltrackActivityIsOpen" , ""+Constants.isRentaltrackActivityIsOpen);
                switch (ride_sync_response.getDetails().getBooking_status()){
                    case Config.Status.RENTAL_ACCEPTED:
                        if(!Constants.isRentalRideloaderIsOpen) {
                            sendNotification(ride_sync_response.getMessage());
                        }break;
                    case Config.Status.RENTAL_RIDE_REJECTED:
                        if(!Constants.isRentalRideloaderIsOpen){
                            sendNotification(ride_sync_response.getMessage());
                        }
                        break ;
                    case Config.Status.RENTAL_ARRIVED:
                        if(!Constants.isRentaltrackActivityIsOpen){
                            sendNotification(ride_sync_response.getMessage());
                        }
                        break ;
                    case Config.Status.RENTAl_RIDE_STARTED :

                        if(!Constants.isRentaltrackActivityIsOpen){
                            sendNotification(ride_sync_response.getMessage());
                        }
                        break ;
                    case Config.Status.RENTAL_RIDE_END:
                        if(!Constants.isRentaltrackActivityIsOpen){
                            sendNotification(ride_sync_response.getMessage());
                        }
                        break;
                }
            }}catch (Exception e){}
    }

    @Override
    public void onFetchResultZero(String s) {

    }

}