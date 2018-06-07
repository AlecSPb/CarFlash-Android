package com.carflash.user.models;

/**
 * Created by lenovo-pc on 9/2/2017.
 */

public class RatingResponse {

    /**
     * result : 1
     * msg : Rating Successfully!!
     */

    private int result;
    private String msg;

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
