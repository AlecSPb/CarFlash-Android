package com.carflash.user.others;

/**
 * Created by samirgoel3@gmail.com on 10/10/2017.
 */

public class FirebaseChatEvent {
    public String message ;
    public String send_via;
    public String timestamp ;

    FirebaseChatEvent(String message , String send_via , String timestamp){
        this.message = message ;
        this.send_via = send_via ;
        this.timestamp = timestamp;
    }
}