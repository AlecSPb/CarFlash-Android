
package com.carflash.user.models;

import java.util.List;

public class ViewPaymentOption {


    /**
     * result : 1
     * msg : [{"payment_option_id":"1","payment_option_name":"Cash","payment_option_name_arabic":"","payment_option_name_french":"","status":"1"},{"payment_option_id":"2","payment_option_name":"Paypal","payment_option_name_arabic":"","payment_option_name_french":"","status":"1"},{"payment_option_id":"3","payment_option_name":"Credit Card","payment_option_name_arabic":"","payment_option_name_french":"","status":"1"},{"payment_option_id":"4","payment_option_name":"Wallet","payment_option_name_arabic":"","payment_option_name_french":"","status":"1"}]
     */

    private int result;
    private List<MsgBean> msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * payment_option_id : 1
         * payment_option_name : Cash
         * payment_option_name_arabic :
         * payment_option_name_french :
         * status : 1
         */

        private String payment_option_id;
        private String payment_option_name;
        private String payment_option_name_arabic;
        private String payment_option_name_french;
        private String status;

        public String getPayment_option_id() {
            return payment_option_id;
        }

        public void setPayment_option_id(String payment_option_id) {
            this.payment_option_id = payment_option_id;
        }

        public String getPayment_option_name() {
            return payment_option_name;
        }

        public void setPayment_option_name(String payment_option_name) {
            this.payment_option_name = payment_option_name;
        }

        public String getPayment_option_name_arabic() {
            return payment_option_name_arabic;
        }

        public void setPayment_option_name_arabic(String payment_option_name_arabic) {
            this.payment_option_name_arabic = payment_option_name_arabic;
        }

        public String getPayment_option_name_french() {
            return payment_option_name_french;
        }

        public void setPayment_option_name_french(String payment_option_name_french) {
            this.payment_option_name_french = payment_option_name_french;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
