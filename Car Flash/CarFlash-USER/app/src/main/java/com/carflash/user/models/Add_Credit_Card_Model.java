package com.carflash.user.models;

/**
 * Created by Lenovo on 11/7/2017.
 */

public class Add_Credit_Card_Model {
    /**
     * result : 1
     * msg : Url Create!!
     * details : {"status":true,"message":"Authorization URL created","data":{"authorization_url":"https://paystack.com/secure/hj9zxmgslh1onhs","access_code":"hj9zxmgslh1onhs","reference":"5a02aefda3aa4"},"call_back_url":"http://apporio.org/Alakowe/api/thanks.php"}
     */

    private int result;
    private String msg;
    private DetailsBean details;

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

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * status : true
         * message : Authorization URL created
         * data : {"authorization_url":"https://paystack.com/secure/hj9zxmgslh1onhs","access_code":"hj9zxmgslh1onhs","reference":"5a02aefda3aa4"}
         * call_back_url : http://apporio.org/Alakowe/api/thanks.php
         */

        private boolean status;
        private String message;
        private DataBean data;
        private String call_back_url;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getCall_back_url() {
            return call_back_url;
        }

        public void setCall_back_url(String call_back_url) {
            this.call_back_url = call_back_url;
        }

        public static class DataBean {
            /**
             * authorization_url : https://paystack.com/secure/hj9zxmgslh1onhs
             * access_code : hj9zxmgslh1onhs
             * reference : 5a02aefda3aa4
             */

            private String authorization_url;
            private String access_code;
            private String reference;

            public String getAuthorization_url() {
                return authorization_url;
            }

            public void setAuthorization_url(String authorization_url) {
                this.authorization_url = authorization_url;
            }

            public String getAccess_code() {
                return access_code;
            }

            public void setAccess_code(String access_code) {
                this.access_code = access_code;
            }

            public String getReference() {
                return reference;
            }

            public void setReference(String reference) {
                this.reference = reference;
            }
        }
    }


 /*   *//**
     * result : 1
     * msg : Url Create!!
     * details : {"status":false,"message":"Duplicate Transaction Reference","call_back_url":"http://apporio.org/Alakowe/api/thanks.php"}
     *//*

    private int result;
    private String msg;
    private DetailsBean details;

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

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public static class DetailsBean {
        *//**
         * status : false
         * message : Duplicate Transaction Reference
         * call_back_url : http://apporio.org/Alakowe/api/thanks.php
         *//*

        private boolean status;
        private String message;
        private String call_back_url;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCall_back_url() {
            return call_back_url;
        }

        public void setCall_back_url(String call_back_url) {
            this.call_back_url = call_back_url;
        }
    }*/


}
