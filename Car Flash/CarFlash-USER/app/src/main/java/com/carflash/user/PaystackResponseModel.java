package com.carflash.user;

/**
 * Created by Lenovo on 9/27/2017.
 */

public class PaystackResponseModel {

    /**
     * result : 1
     *msg " {"Transaction was successful"/"Transaction was unsuccessful"}
     */

    int result;
    String msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
