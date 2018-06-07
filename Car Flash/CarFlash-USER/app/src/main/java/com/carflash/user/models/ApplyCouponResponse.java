package com.carflash.user.models;

/**
 * Created by lenovo-pc on 9/2/2017.
 */

public class ApplyCouponResponse {

    /**
     * result : 0
     * msg : Invalid coupon code
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
