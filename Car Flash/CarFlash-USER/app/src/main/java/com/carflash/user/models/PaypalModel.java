package com.carflash.user.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GARG-PC on 6/18/2016.
 */
public class PaypalModel {

    @SerializedName("response")
    public PaymentDetails response = new PaymentDetails();

    public class PaymentDetails {

        @SerializedName("state")
        public String state;

        @SerializedName("id")
        public String ids;

        @SerializedName("create_time")
        public String create_time;

        @SerializedName("intent")
        public String intents;
    }
}
