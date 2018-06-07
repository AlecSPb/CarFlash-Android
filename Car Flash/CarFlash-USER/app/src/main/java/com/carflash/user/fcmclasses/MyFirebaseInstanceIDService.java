package com.carflash.user.fcmclasses;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static String refreshedToken;


    @Override
    public void onTokenRefresh() {
//        refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Logger.e("REGISTRATION TOKEN            " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
    }

//    void sendRegistrationToServer(String token) {
//    }
}
