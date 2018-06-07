package com.carflash.user.accounts;

/**
 * Created by user on 3/9/2017.
 */

public class ResultCheckMessage {
    /**
     * result : 0
     * message : Email Or Phone already registerd!!
     */

    private int result;
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
