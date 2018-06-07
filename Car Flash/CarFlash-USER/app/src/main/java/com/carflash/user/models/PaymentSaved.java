
package com.carflash.user.models;

public class PaymentSaved {

    /**
     * result : 1
     * msg : Success
     * details : {"id":"719","order_id":"945","user_id":"468","payment_id":"1","payment_method":"Cash","payment_platform":"Android","payment_amount":"100","payment_date_time":"02 Sep, 2017","payment_status":"Done"}
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
         * id : 719
         * order_id : 945
         * user_id : 468
         * payment_id : 1
         * payment_method : Cash
         * payment_platform : Android
         * payment_amount : 100
         * payment_date_time : 02 Sep, 2017
         * payment_status : Done
         */

        private String id;
        private String order_id;
        private String user_id;
        private String payment_id;
        private String payment_method;
        private String payment_platform;
        private String payment_amount;
        private String payment_date_time;
        private String payment_status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_platform() {
            return payment_platform;
        }

        public void setPayment_platform(String payment_platform) {
            this.payment_platform = payment_platform;
        }

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        public String getPayment_date_time() {
            return payment_date_time;
        }

        public void setPayment_date_time(String payment_date_time) {
            this.payment_date_time = payment_date_time;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }
    }
}
