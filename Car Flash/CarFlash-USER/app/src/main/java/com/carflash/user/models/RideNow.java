
package com.carflash.user.models;

public class RideNow {

    /**
     * result : 1
     * msg : Ride Book Successfully
     * details : {"ride_id":"1832","user_id":"468","coupon_code":"","pickup_lat":"28.41218546490328","pickup_long":"77.04312231391668","pickup_location":"1, Sohna Rd, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122001, India","drop_lat":"28.46582615149554","drop_long":"77.0612171664834","drop_location":"94, Sector Rd, South City I, Sector 30, Gurugram, Haryana 122003, India","ride_date":"Saturday, Sep 2","ride_time":"15:38:34","last_time_stamp":"03:38:34 PM","ride_image":"","later_date":"14/9/2017","later_time":"04:38","driver_id":"0","car_type_id":"2","ride_type":"2","pem_file":"1","ride_status":"1","reason_id":"0","payment_option_id":"1","card_id":"0","ride_admin_status":"1","date":"2017-09-02"}
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
         * ride_id : 1832
         * user_id : 468
         * coupon_code :
         * pickup_lat : 28.41218546490328
         * pickup_long : 77.04312231391668
         * pickup_location : 1, Sohna Rd, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122001, India
         * drop_lat : 28.46582615149554
         * drop_long : 77.0612171664834
         * drop_location : 94, Sector Rd, South City I, Sector 30, Gurugram, Haryana 122003, India
         * ride_date : Saturday, Sep 2
         * ride_time : 15:38:34
         * last_time_stamp : 03:38:34 PM
         * ride_image :
         * later_date : 14/9/2017
         * later_time : 04:38
         * driver_id : 0
         * car_type_id : 2
         * ride_type : 2
         * pem_file : 1
         * ride_status : 1
         * reason_id : 0
         * payment_option_id : 1
         * card_id : 0
         * ride_admin_status : 1
         * date : 2017-09-02
         */

        private String ride_id;
        private String user_id;
        private String coupon_code;
        private String pickup_lat;
        private String pickup_long;
        private String pickup_location;
        private String drop_lat;
        private String drop_long;
        private String drop_location;
        private String ride_date;
        private String ride_time;
        private String last_time_stamp;
        private String ride_image;
        private String later_date;
        private String later_time;
        private String driver_id;
        private String car_type_id;
        private String ride_type;
        private String pem_file;
        private String ride_status;
        private String reason_id;
        private String payment_option_id;
        private String card_id;
        private String ride_admin_status;
        private String date;

        public String getRide_id() {
            return ride_id;
        }

        public void setRide_id(String ride_id) {
            this.ride_id = ride_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public String getPickup_lat() {
            return pickup_lat;
        }

        public void setPickup_lat(String pickup_lat) {
            this.pickup_lat = pickup_lat;
        }

        public String getPickup_long() {
            return pickup_long;
        }

        public void setPickup_long(String pickup_long) {
            this.pickup_long = pickup_long;
        }

        public String getPickup_location() {
            return pickup_location;
        }

        public void setPickup_location(String pickup_location) {
            this.pickup_location = pickup_location;
        }

        public String getDrop_lat() {
            return drop_lat;
        }

        public void setDrop_lat(String drop_lat) {
            this.drop_lat = drop_lat;
        }

        public String getDrop_long() {
            return drop_long;
        }

        public void setDrop_long(String drop_long) {
            this.drop_long = drop_long;
        }

        public String getDrop_location() {
            return drop_location;
        }

        public void setDrop_location(String drop_location) {
            this.drop_location = drop_location;
        }

        public String getRide_date() {
            return ride_date;
        }

        public void setRide_date(String ride_date) {
            this.ride_date = ride_date;
        }

        public String getRide_time() {
            return ride_time;
        }

        public void setRide_time(String ride_time) {
            this.ride_time = ride_time;
        }

        public String getLast_time_stamp() {
            return last_time_stamp;
        }

        public void setLast_time_stamp(String last_time_stamp) {
            this.last_time_stamp = last_time_stamp;
        }

        public String getRide_image() {
            return ride_image;
        }

        public void setRide_image(String ride_image) {
            this.ride_image = ride_image;
        }

        public String getLater_date() {
            return later_date;
        }

        public void setLater_date(String later_date) {
            this.later_date = later_date;
        }

        public String getLater_time() {
            return later_time;
        }

        public void setLater_time(String later_time) {
            this.later_time = later_time;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
        }

        public String getRide_type() {
            return ride_type;
        }

        public void setRide_type(String ride_type) {
            this.ride_type = ride_type;
        }

        public String getPem_file() {
            return pem_file;
        }

        public void setPem_file(String pem_file) {
            this.pem_file = pem_file;
        }

        public String getRide_status() {
            return ride_status;
        }

        public void setRide_status(String ride_status) {
            this.ride_status = ride_status;
        }

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getPayment_option_id() {
            return payment_option_id;
        }

        public void setPayment_option_id(String payment_option_id) {
            this.payment_option_id = payment_option_id;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getRide_admin_status() {
            return ride_admin_status;
        }

        public void setRide_admin_status(String ride_admin_status) {
            this.ride_admin_status = ride_admin_status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
