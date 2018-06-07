
package com.carflash.user.models.tokenGenrator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenGenrator {

    @SerializedName("result")
    @Expose
    private int result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
