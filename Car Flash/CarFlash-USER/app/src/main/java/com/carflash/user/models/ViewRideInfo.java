
package com.carflash.user.models;

public class ViewRideInfo {


    /**
     * result : 1
     * msg :
     * details : {"ride_id":"667","user_id":"63","pickup_lat":"28.41117191884366","pickup_long":"77.04211547970772","pickup_location":"1, Sohna Road,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122001,","drop_lat":"17.44362544057825","drop_long":"78.37995640933514","drop_location":"21, Hitech City Main Rd,HUDA Techno Enclave, HITEC City,Hyderabad, Telangana 500081,","ride_date":"Tuesday, May 16","ride_time":"18:14:29","ride_status":"7","ride_type":"1","driver_id":"100","driver_name":"lsllsl","driver_image":"","driver_rating":"3.6666666666667","driver_phone":"6493493466876","driver_lat":"28.412121178592297","driver_long":"77.043268494308","car_number":"gggg","payment_status":"1","car_type_name":"HATCHBACK","car_model_name":"Nano","begin_lat":"28.41117191884366","begin_long":"77.04211547970772","arrived_time":"18:14:56","begin_time":"18:20:43","begin_location":"1, Sohna Road,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122001,","end_location":"68, Plaza Street, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","amount":"50","distance":"0","time":"0 Min","car_type_image":"uploads/car/car_4.png","done_ride_id":"281","driver_location":"68, Plaza Street, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","ride_image":"https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.41117191884366,77.04211547970772&markers=color:red|label:D|17.44362544057825,78.37995640933514&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4","waiting_time":"5 Min","waiting_price":"30","done_ride_time":"0 Min","ride_time_price":"0","coupon_code":"","coupons_price":"","total_amount":80,"end_time":"18:20:51","payment_option_id":"1","payment_option_name":"Cash"}
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
         * ride_id : 667
         * user_id : 63
         * pickup_lat : 28.41117191884366
         * pickup_long : 77.04211547970772
         * pickup_location : 1, Sohna Road,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122001,
         * drop_lat : 17.44362544057825
         * drop_long : 78.37995640933514
         * drop_location : 21, Hitech City Main Rd,HUDA Techno Enclave, HITEC City,Hyderabad, Telangana 500081,
         * ride_date : Tuesday, May 16
         * ride_time : 18:14:29
         * ride_status : 7
         * ride_type : 1
         * driver_id : 100
         * driver_name : lsllsl
         * driver_image :
         * driver_rating : 3.6666666666667
         * driver_phone : 6493493466876
         * driver_lat : 28.412121178592297
         * driver_long : 77.043268494308
         * car_number : gggg
         * payment_status : 1
         * car_type_name : HATCHBACK
         * car_model_name : Nano
         * begin_lat : 28.41117191884366
         * begin_long : 77.04211547970772
         * arrived_time : 18:14:56
         * begin_time : 18:20:43
         * begin_location : 1, Sohna Road,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122001,
         * end_location : 68, Plaza Street, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * amount : 50
         * distance : 0
         * time : 0 Min
         * car_type_image : uploads/car/car_4.png
         * done_ride_id : 281
         * driver_location : 68, Plaza Street, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * ride_image : https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.41117191884366,77.04211547970772&markers=color:red|label:D|17.44362544057825,78.37995640933514&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4
         * waiting_time : 5 Min
         * waiting_price : 30
         * done_ride_time : 0 Min
         * ride_time_price : 0
         * coupon_code :
         * coupons_price :
         * total_amount : 80
         * end_time : 18:20:51
         * payment_option_id : 1
         * payment_option_name : Cash
         */

        private String ride_id;
        private String user_id;
        private String pickup_lat;
        private String pickup_long;
        private String pickup_location;
        private String drop_lat;
        private String drop_long;
        private String drop_location;
        private String ride_date;
        private String ride_time;
        private String ride_status;
        private String ride_type;
        private String driver_id;
        private String driver_name;
        private String driver_image;
        private String driver_rating;
        private String driver_phone;
        private String driver_lat;
        private String driver_long;
        private String car_number;
        private String payment_status;
        private String car_type_name;
        private String car_model_name;
        private String begin_lat;
        private String begin_long;
        private String arrived_time;
        private String begin_time;
        private String begin_location;
        private String end_location;
        private String amount;
        private String distance;
        private String time;
        private String car_type_image;
        private String done_ride_id;
        private String driver_location;
        private String ride_image;
        private String waiting_time;
        private String waiting_price;
        private String done_ride_time;
        private String ride_time_price;
        private String coupon_code;
        private String coupons_price;
        private String total_amount;
        private String end_time;
        private String payment_option_id;
        private String payment_option_name;

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

        public String getRide_status() {
            return ride_status;
        }

        public void setRide_status(String ride_status) {
            this.ride_status = ride_status;
        }

        public String getRide_type() {
            return ride_type;
        }

        public void setRide_type(String ride_type) {
            this.ride_type = ride_type;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(String driver_name) {
            this.driver_name = driver_name;
        }

        public String getDriver_image() {
            return driver_image;
        }

        public void setDriver_image(String driver_image) {
            this.driver_image = driver_image;
        }

        public String getDriver_rating() {
            return driver_rating;
        }

        public void setDriver_rating(String driver_rating) {
            this.driver_rating = driver_rating;
        }

        public String getDriver_phone() {
            return driver_phone;
        }

        public void setDriver_phone(String driver_phone) {
            this.driver_phone = driver_phone;
        }

        public String getDriver_lat() {
            return driver_lat;
        }

        public void setDriver_lat(String driver_lat) {
            this.driver_lat = driver_lat;
        }

        public String getDriver_long() {
            return driver_long;
        }

        public void setDriver_long(String driver_long) {
            this.driver_long = driver_long;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getCar_type_name() {
            return car_type_name;
        }

        public void setCar_type_name(String car_type_name) {
            this.car_type_name = car_type_name;
        }

        public String getCar_model_name() {
            return car_model_name;
        }

        public void setCar_model_name(String car_model_name) {
            this.car_model_name = car_model_name;
        }

        public String getBegin_lat() {
            return begin_lat;
        }

        public void setBegin_lat(String begin_lat) {
            this.begin_lat = begin_lat;
        }

        public String getBegin_long() {
            return begin_long;
        }

        public void setBegin_long(String begin_long) {
            this.begin_long = begin_long;
        }

        public String getArrived_time() {
            return arrived_time;
        }

        public void setArrived_time(String arrived_time) {
            this.arrived_time = arrived_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getBegin_location() {
            return begin_location;
        }

        public void setBegin_location(String begin_location) {
            this.begin_location = begin_location;
        }

        public String getEnd_location() {
            return end_location;
        }

        public void setEnd_location(String end_location) {
            this.end_location = end_location;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCar_type_image() {
            return car_type_image;
        }

        public void setCar_type_image(String car_type_image) {
            this.car_type_image = car_type_image;
        }

        public String getDone_ride_id() {
            return done_ride_id;
        }

        public void setDone_ride_id(String done_ride_id) {
            this.done_ride_id = done_ride_id;
        }

        public String getDriver_location() {
            return driver_location;
        }

        public void setDriver_location(String driver_location) {
            this.driver_location = driver_location;
        }

        public String getRide_image() {
            return ride_image;
        }

        public void setRide_image(String ride_image) {
            this.ride_image = ride_image;
        }

        public String getWaiting_time() {
            return waiting_time;
        }

        public void setWaiting_time(String waiting_time) {
            this.waiting_time = waiting_time;
        }

        public String getWaiting_price() {
            return waiting_price;
        }

        public void setWaiting_price(String waiting_price) {
            this.waiting_price = waiting_price;
        }

        public String getDone_ride_time() {
            return done_ride_time;
        }

        public void setDone_ride_time(String done_ride_time) {
            this.done_ride_time = done_ride_time;
        }

        public String getRide_time_price() {
            return ride_time_price;
        }

        public void setRide_time_price(String ride_time_price) {
            this.ride_time_price = ride_time_price;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public String getCoupons_price() {
            return coupons_price;
        }

        public void setCoupons_price(String coupons_price) {
            this.coupons_price = coupons_price;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

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
    }
}
