
package com.carflash.user.models;

public class RateCard {


    /**
     * result : 1
     * msg : Rate card
     * details : {"price_id":"5","city_id":"3","distance_unit":"Miles","currency":"","car_type_id":"2","commission":"10","now_booking_fee":"0","later_booking_fee":"0","cancel_fee":"0","cancel_ride_now_free_min":"0","cancel_ride_later_free_min":"0","scheduled_cancel_fee":"0","base_distance":"2","base_distance_price":"0","base_price_per_unit":"8","free_waiting_time":"3","wating_price_minute":"5","free_ride_minutes":"0","price_per_ride_minute":"15","peak_time_charge":"","peak_time_payment_type":"","night_payment_type":"","night_time_charge":""}
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
         * price_id : 5
         * city_id : 3
         * distance_unit : Miles
         * currency :
         * car_type_id : 2
         * commission : 10
         * now_booking_fee : 0
         * later_booking_fee : 0
         * cancel_fee : 0
         * cancel_ride_now_free_min : 0
         * cancel_ride_later_free_min : 0
         * scheduled_cancel_fee : 0
         * base_distance : 2
         * base_distance_price : 0
         * base_price_per_unit : 8
         * free_waiting_time : 3
         * wating_price_minute : 5
         * free_ride_minutes : 0
         * price_per_ride_minute : 15
         * peak_time_charge :
         * peak_time_payment_type :
         * night_payment_type :
         * night_time_charge :
         */

        private String price_id;
        private String city_id;
        private String distance_unit;
        private String currency;
        private String car_type_id;
        private String commission;
        private String now_booking_fee;
        private String later_booking_fee;
        private String cancel_fee;
        private String cancel_ride_now_free_min;
        private String cancel_ride_later_free_min;
        private String scheduled_cancel_fee;
        private String base_distance;
        private String base_distance_price;
        private String base_price_per_unit;
        private String free_waiting_time;
        private String wating_price_minute;
        private String free_ride_minutes;
        private String price_per_ride_minute;
        private String peak_time_charge;
        private String peak_time_payment_type;
        private String night_payment_type;
        private String night_time_charge;

        public String getPrice_id() {
            return price_id;
        }

        public void setPrice_id(String price_id) {
            this.price_id = price_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getDistance_unit() {
            return distance_unit;
        }

        public void setDistance_unit(String distance_unit) {
            this.distance_unit = distance_unit;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getNow_booking_fee() {
            return now_booking_fee;
        }

        public void setNow_booking_fee(String now_booking_fee) {
            this.now_booking_fee = now_booking_fee;
        }

        public String getLater_booking_fee() {
            return later_booking_fee;
        }

        public void setLater_booking_fee(String later_booking_fee) {
            this.later_booking_fee = later_booking_fee;
        }

        public String getCancel_fee() {
            return cancel_fee;
        }

        public void setCancel_fee(String cancel_fee) {
            this.cancel_fee = cancel_fee;
        }

        public String getCancel_ride_now_free_min() {
            return cancel_ride_now_free_min;
        }

        public void setCancel_ride_now_free_min(String cancel_ride_now_free_min) {
            this.cancel_ride_now_free_min = cancel_ride_now_free_min;
        }

        public String getCancel_ride_later_free_min() {
            return cancel_ride_later_free_min;
        }

        public void setCancel_ride_later_free_min(String cancel_ride_later_free_min) {
            this.cancel_ride_later_free_min = cancel_ride_later_free_min;
        }

        public String getScheduled_cancel_fee() {
            return scheduled_cancel_fee;
        }

        public void setScheduled_cancel_fee(String scheduled_cancel_fee) {
            this.scheduled_cancel_fee = scheduled_cancel_fee;
        }

        public String getBase_distance() {
            return base_distance;
        }

        public void setBase_distance(String base_distance) {
            this.base_distance = base_distance;
        }

        public String getBase_distance_price() {
            return base_distance_price;
        }

        public void setBase_distance_price(String base_distance_price) {
            this.base_distance_price = base_distance_price;
        }

        public String getBase_price_per_unit() {
            return base_price_per_unit;
        }

        public void setBase_price_per_unit(String base_price_per_unit) {
            this.base_price_per_unit = base_price_per_unit;
        }

        public String getFree_waiting_time() {
            return free_waiting_time;
        }

        public void setFree_waiting_time(String free_waiting_time) {
            this.free_waiting_time = free_waiting_time;
        }

        public String getWating_price_minute() {
            return wating_price_minute;
        }

        public void setWating_price_minute(String wating_price_minute) {
            this.wating_price_minute = wating_price_minute;
        }

        public String getFree_ride_minutes() {
            return free_ride_minutes;
        }

        public void setFree_ride_minutes(String free_ride_minutes) {
            this.free_ride_minutes = free_ride_minutes;
        }

        public String getPrice_per_ride_minute() {
            return price_per_ride_minute;
        }

        public void setPrice_per_ride_minute(String price_per_ride_minute) {
            this.price_per_ride_minute = price_per_ride_minute;
        }

        public String getPeak_time_charge() {
            return peak_time_charge;
        }

        public void setPeak_time_charge(String peak_time_charge) {
            this.peak_time_charge = peak_time_charge;
        }

        public String getPeak_time_payment_type() {
            return peak_time_payment_type;
        }

        public void setPeak_time_payment_type(String peak_time_payment_type) {
            this.peak_time_payment_type = peak_time_payment_type;
        }

        public String getNight_payment_type() {
            return night_payment_type;
        }

        public void setNight_payment_type(String night_payment_type) {
            this.night_payment_type = night_payment_type;
        }

        public String getNight_time_charge() {
            return night_time_charge;
        }

        public void setNight_time_charge(String night_time_charge) {
            this.night_time_charge = night_time_charge;
        }
    }
}
