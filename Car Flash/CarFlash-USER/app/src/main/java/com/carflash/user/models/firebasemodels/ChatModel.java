package com.carflash.user.models.firebasemodels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by samirgoel3@gmail.com on 10/10/2017.
 */
@IgnoreExtraProperties
public class ChatModel {
    public String send_via;
    public String message;
    public String timestamp;

    public ChatModel(){

    }


    public ChatModel(String send_via , String message , String timestamp){
        this.send_via = send_via ;
        this.message = message ;
        this.timestamp = timestamp ;
    }
}
