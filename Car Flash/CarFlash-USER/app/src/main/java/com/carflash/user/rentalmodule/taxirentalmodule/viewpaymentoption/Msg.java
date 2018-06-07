
package com.carflash.user.rentalmodule.taxirentalmodule.viewpaymentoption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Msg {

    @SerializedName("payment_option_id")
    @Expose
    private String paymentOptionId;
    @SerializedName("payment_option_name")
    @Expose
    private String paymentOptionName;
    @SerializedName("payment_option_name_arabic")
    @Expose
    private String paymentOptionNameArabic;
    @SerializedName("payment_option_name_french")
    @Expose
    private String paymentOptionNameFrench;
    @SerializedName("status")
    @Expose
    private String status;

    public String getPaymentOptionId() {
        return paymentOptionId;
    }

    public void setPaymentOptionId(String paymentOptionId) {
        this.paymentOptionId = paymentOptionId;
    }

    public String getPaymentOptionName() {
        return paymentOptionName;
    }

    public void setPaymentOptionName(String paymentOptionName) {
        this.paymentOptionName = paymentOptionName;
    }

    public String getPaymentOptionNameArabic() {
        return paymentOptionNameArabic;
    }

    public void setPaymentOptionNameArabic(String paymentOptionNameArabic) {
        this.paymentOptionNameArabic = paymentOptionNameArabic;
    }

    public String getPaymentOptionNameFrench() {
        return paymentOptionNameFrench;
    }

    public void setPaymentOptionNameFrench(String paymentOptionNameFrench) {
        this.paymentOptionNameFrench = paymentOptionNameFrench;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
