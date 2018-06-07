package com.carflash.user.models;

/**
 * Created by lenovo-pc on 9/2/2017.
 */

public class RideEstimate {

    /**
     * result : 1
     * msg : 100.00
     * estimatetime : 1 min
     */

    private int result;
    private String msg;
    private String estimatetime;

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

    public String getEstimatetime() {
        return estimatetime;
    }

    public void setEstimatetime(String estimatetime) {
        this.estimatetime = estimatetime;
    }
}
