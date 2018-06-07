package com.carflash.user;

import java.util.List;

/**
 * Created by Lenovo on 11/9/2017.
 */

public class Saved_Card_Model {


    /**
     * result : 1
     * msg : Saved Card!
     * details : [{"paystack_id":"1","authorization_code":"AUTH_7bgdx00mk1","user_id":"15","email":"sam@gmail.com","bin":"408408","last4":"4081","exp_month":"01","exp_year":"2021","channel":"card","card_type":"visa DEBIT","bank":"Test Bank","country_code":"NG","brand":"visa","reusable":"1","signature":"SIG_aoObaZxq0riQvugT78pu"}]
     */

    private int result;
    private String msg;
    private List<DetailsBean> details;

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

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * paystack_id : 1
         * authorization_code : AUTH_7bgdx00mk1
         * user_id : 15
         * email : sam@gmail.com
         * bin : 408408
         * last4 : 4081
         * exp_month : 01
         * exp_year : 2021
         * channel : card
         * card_type : visa DEBIT
         * bank : Test Bank
         * country_code : NG
         * brand : visa
         * reusable : 1
         * signature : SIG_aoObaZxq0riQvugT78pu
         */

        private String paystack_id;
        private String authorization_code;
        private String user_id;
        private String email;
        private String bin;
        private String last4;
        private String exp_month;
        private String exp_year;
        private String channel;
        private String card_type;
        private String bank;
        private String country_code;
        private String brand;
        private String reusable;
        private String signature;

        public String getPaystack_id() {
            return paystack_id;
        }

        public void setPaystack_id(String paystack_id) {
            this.paystack_id = paystack_id;
        }

        public String getAuthorization_code() {
            return authorization_code;
        }

        public void setAuthorization_code(String authorization_code) {
            this.authorization_code = authorization_code;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBin() {
            return bin;
        }

        public void setBin(String bin) {
            this.bin = bin;
        }

        public String getLast4() {
            return last4;
        }

        public void setLast4(String last4) {
            this.last4 = last4;
        }

        public String getExp_month() {
            return exp_month;
        }

        public void setExp_month(String exp_month) {
            this.exp_month = exp_month;
        }

        public String getExp_year() {
            return exp_year;
        }

        public void setExp_year(String exp_year) {
            this.exp_year = exp_year;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getReusable() {
            return reusable;
        }

        public void setReusable(String reusable) {
            this.reusable = reusable;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
