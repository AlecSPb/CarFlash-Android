
package com.carflash.user.models.tokenGenrator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("authorization_url")
    @Expose
    private String authorizationUrl;
    @SerializedName("access_code")
    @Expose
    private String accessCode;
    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("call_back_url")
    @Expose
    private String call_back_url;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCall_back_url() {
        return call_back_url;
    }

    public void setCall_back_url(String call_back_url) {
        this.call_back_url = call_back_url;
    }
}
