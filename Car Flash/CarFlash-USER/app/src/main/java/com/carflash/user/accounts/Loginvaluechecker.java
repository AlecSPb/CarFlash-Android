package com.carflash.user.accounts;

/**
 * Created by user on 3/9/2017.
 */

public class Loginvaluechecker {

    /**
     * result : 0
     * message : Phone or password Donot Match !!
     * value : 2
     */

    private int result;
    private String message;
    private int value;

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
