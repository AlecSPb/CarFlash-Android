package com.carflash.user.models;

/**
 * Created by lenovo-pc on 8/26/2017.
 */

public class QueryResponseModel {
    /**
     * status : 1
     * message : Thanks For Query
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
