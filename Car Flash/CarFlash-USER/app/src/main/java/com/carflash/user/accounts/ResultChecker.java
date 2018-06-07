package com.carflash.user.accounts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by samir on 30/01/17.
 */

public class ResultChecker {
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private Integer result;





}
