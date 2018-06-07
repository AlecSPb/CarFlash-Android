package com.carflash.user.models;

/**
 * Created by lenovo-pc on 7/3/2017.
 */

public class NewRideDetailsModel {


    /**
     * status : 1
     * message : Ride Details
     * details : {"ride_id":"1507","user_id":"323","coupon_code":"","pickup_lat":"28.41822201153962","pickup_long":"77.04748995602131","pickup_location":"Q1 287, South City II, South City 2, Sector 46, South City II, Sector 49, Gurugram, Haryana 122018, India","drop_lat":"28.596936000000003","drop_long":"77.1647589","drop_location":"Dhaula Kuan","ride_date":"Monday, Aug 28","ride_time":"14:36:00","last_time_stamp":"02:44:41 PM","ride_image":"https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.41822201153962,77.04748995602131&markers=color:red|label:D|,&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4","later_date":"","later_time":"","driver_id":"212","car_type_id":"2","ride_type":"1","ride_status":"6","reason_id":"0","payment_option_id":"1","card_id":"0","ride_admin_status":"1","date":"2017-08-28","car_type_name":"HATCHBACK","car_name_arabic":"","car_type_name_french":"HATCHBACK","car_type_image":"uploads/car/editcar_2.png","ride_mode":"1","car_admin_status":"1","user_type":"1","user_name":"Samir Goel","user_email":"","user_phone":"+919650923089","user_password":"","user_image":"","device_id":"","flag":"0","wallet_money":"960231.68","register_date":"","referral_code":"","free_rides":"0","referral_code_send":"0","phone_verified":"0","email_verified":"0","password_created":"0","facebook_id":"1479168742128308","facebook_mail":"samirgoel3@gmail.com","facebook_image":"http://graph.facebook.com/1479168742128308/picture?type=large","facebook_firstname":"Samir","facebook_lastname":"Goel","google_id":"112279197400670101492","google_name":"samir goel","google_mail":"samirgoel3@gmail.com","google_image":"https://lh3.googleusercontent.com/-gLtgnfXT9p0/AAAAAAAAAAI/AAAAAAAAAGg/gnHaB2Vwqz0/photo.jpg","google_token":"","facebook_token":"","token_created":"0","login_logout":"0","rating":"4.7777777777778","user_delete":"0","unique_number":"","status":"1","total_amount":"0","begin_time":"02:44:41 PM","end_time":"","arrived_time":"02:44:10 PM","payment_status":"0","distance":"","waiting_time":"0","done_ride_time":" Min","time":" Min","amount":"","waiting_price":"0","ride_time_price":"0","coupan_price":"0.00","driver_name":"Samir Goel","driver_image":"uploads/driver/1503124462driver_212.jpg","driver_rating":"4.6818181818182","driver_phone":"9650923089","driver_lat":"28.4121045","driver_long":"77.0433116","car_number":"DL5584","driver_location":"----","payment_option_name":"Cash","peak_time_charge":"0.00","night_time_charge":"0"}
     */

    private int status;
    private String message;
    private DetailsBean details;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * ride_id : 1507
         * user_id : 323
         * coupon_code :
         * pickup_lat : 28.41822201153962
         * pickup_long : 77.04748995602131
         * pickup_location : Q1 287, South City II, South City 2, Sector 46, South City II, Sector 49, Gurugram, Haryana 122018, India
         * drop_lat : 28.596936000000003
         * drop_long : 77.1647589
         * drop_location : Dhaula Kuan
         * ride_date : Monday, Aug 28
         * ride_time : 14:36:00
         * last_time_stamp : 02:44:41 PM
         * ride_image : https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.41822201153962,77.04748995602131&markers=color:red|label:D|,&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4
         * later_date :
         * later_time :
         * driver_id : 212
         * car_type_id : 2
         * ride_type : 1
         * ride_status : 6
         * reason_id : 0
         * payment_option_id : 1
         * card_id : 0
         * ride_admin_status : 1
         * date : 2017-08-28
         * car_type_name : HATCHBACK
         * car_name_arabic :
         * car_type_name_french : HATCHBACK
         * car_type_image : uploads/car/editcar_2.png
         * ride_mode : 1
         * car_admin_status : 1
         * user_type : 1
         * user_name : Samir Goel
         * user_email :
         * user_phone : +919650923089
         * user_password :
         * user_image :
         * device_id :
         * flag : 0
         * wallet_money : 960231.68
         * register_date :
         * referral_code :
         * free_rides : 0
         * referral_code_send : 0
         * phone_verified : 0
         * email_verified : 0
         * password_created : 0
         * facebook_id : 1479168742128308
         * facebook_mail : samirgoel3@gmail.com
         * facebook_image : http://graph.facebook.com/1479168742128308/picture?type=large
         * facebook_firstname : Samir
         * facebook_lastname : Goel
         * google_id : 112279197400670101492
         * google_name : samir goel
         * google_mail : samirgoel3@gmail.com
         * google_image : https://lh3.googleusercontent.com/-gLtgnfXT9p0/AAAAAAAAAAI/AAAAAAAAAGg/gnHaB2Vwqz0/photo.jpg
         * google_token :
         * facebook_token :
         * token_created : 0
         * login_logout : 0
         * rating : 4.7777777777778
         * user_delete : 0
         * unique_number :
         * status : 1
         * total_amount : 0
         * begin_time : 02:44:41 PM
         * end_time :
         * arrived_time : 02:44:10 PM
         * payment_status : 0
         * distance :
         * waiting_time : 0
         * done_ride_time :  Min
         * time :  Min
         * amount :
         * waiting_price : 0
         * ride_time_price : 0
         * coupan_price : 0.00
         * driver_name : Samir Goel
         * driver_image : uploads/driver/1503124462driver_212.jpg
         * driver_rating : 4.6818181818182
         * driver_phone : 9650923089
         * driver_lat : 28.4121045
         * driver_long : 77.0433116
         * car_number : DL5584
         * driver_location : ----
         * payment_option_name : Cash
         * peak_time_charge : 0.00
         * night_time_charge : 0
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
        private String ride_status;
        private String reason_id;
        private String payment_option_id;
        private String card_id;
        private String ride_admin_status;
        private String date;
        private String car_type_name;
        private String car_name_arabic;
        private String car_type_name_french;
        private String car_type_image;
        private String ride_mode;
        private String car_admin_status;
        private String user_type;
        private String user_name;
        private String user_email;
        private String user_phone;
        private String user_password;
        private String user_image;
        private String device_id;
        private String flag;
        private String wallet_money;
        private String register_date;
        private String referral_code;
        private String free_rides;
        private String referral_code_send;
        private String phone_verified;
        private String email_verified;
        private String password_created;
        private String facebook_id;
        private String facebook_mail;
        private String facebook_image;
        private String facebook_firstname;
        private String facebook_lastname;
        private String google_id;
        private String google_name;
        private String google_mail;
        private String google_image;
        private String google_token;
        private String facebook_token;
        private String token_created;
        private String login_logout;
        private String rating;
        private String user_delete;
        private String unique_number;
        private String status;
        private String total_amount;
        private String begin_time;
        private String end_time;
        private String arrived_time;
        private String payment_status;
        private String distance;
        private String waiting_time;
        private String done_ride_time;
        private String time;
        private String amount;
        private String waiting_price;
        private String ride_time_price;
        private String coupan_price;
        private String driver_name;
        private String driver_image;
        private String driver_rating;
        private String driver_phone;
        private String driver_lat;
        private String driver_long;
        private String car_number;
        private String driver_location;
        private String payment_option_name;
        private String peak_time_charge;
        private String night_time_charge;

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

        public String getCar_type_name() {
            return car_type_name;
        }

        public void setCar_type_name(String car_type_name) {
            this.car_type_name = car_type_name;
        }

        public String getCar_name_arabic() {
            return car_name_arabic;
        }

        public void setCar_name_arabic(String car_name_arabic) {
            this.car_name_arabic = car_name_arabic;
        }

        public String getCar_type_name_french() {
            return car_type_name_french;
        }

        public void setCar_type_name_french(String car_type_name_french) {
            this.car_type_name_french = car_type_name_french;
        }

        public String getCar_type_image() {
            return car_type_image;
        }

        public void setCar_type_image(String car_type_image) {
            this.car_type_image = car_type_image;
        }

        public String getRide_mode() {
            return ride_mode;
        }

        public void setRide_mode(String ride_mode) {
            this.ride_mode = ride_mode;
        }

        public String getCar_admin_status() {
            return car_admin_status;
        }

        public void setCar_admin_status(String car_admin_status) {
            this.car_admin_status = car_admin_status;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getWallet_money() {
            return wallet_money;
        }

        public void setWallet_money(String wallet_money) {
            this.wallet_money = wallet_money;
        }

        public String getRegister_date() {
            return register_date;
        }

        public void setRegister_date(String register_date) {
            this.register_date = register_date;
        }

        public String getReferral_code() {
            return referral_code;
        }

        public void setReferral_code(String referral_code) {
            this.referral_code = referral_code;
        }

        public String getFree_rides() {
            return free_rides;
        }

        public void setFree_rides(String free_rides) {
            this.free_rides = free_rides;
        }

        public String getReferral_code_send() {
            return referral_code_send;
        }

        public void setReferral_code_send(String referral_code_send) {
            this.referral_code_send = referral_code_send;
        }

        public String getPhone_verified() {
            return phone_verified;
        }

        public void setPhone_verified(String phone_verified) {
            this.phone_verified = phone_verified;
        }

        public String getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(String email_verified) {
            this.email_verified = email_verified;
        }

        public String getPassword_created() {
            return password_created;
        }

        public void setPassword_created(String password_created) {
            this.password_created = password_created;
        }

        public String getFacebook_id() {
            return facebook_id;
        }

        public void setFacebook_id(String facebook_id) {
            this.facebook_id = facebook_id;
        }

        public String getFacebook_mail() {
            return facebook_mail;
        }

        public void setFacebook_mail(String facebook_mail) {
            this.facebook_mail = facebook_mail;
        }

        public String getFacebook_image() {
            return facebook_image;
        }

        public void setFacebook_image(String facebook_image) {
            this.facebook_image = facebook_image;
        }

        public String getFacebook_firstname() {
            return facebook_firstname;
        }

        public void setFacebook_firstname(String facebook_firstname) {
            this.facebook_firstname = facebook_firstname;
        }

        public String getFacebook_lastname() {
            return facebook_lastname;
        }

        public void setFacebook_lastname(String facebook_lastname) {
            this.facebook_lastname = facebook_lastname;
        }

        public String getGoogle_id() {
            return google_id;
        }

        public void setGoogle_id(String google_id) {
            this.google_id = google_id;
        }

        public String getGoogle_name() {
            return google_name;
        }

        public void setGoogle_name(String google_name) {
            this.google_name = google_name;
        }

        public String getGoogle_mail() {
            return google_mail;
        }

        public void setGoogle_mail(String google_mail) {
            this.google_mail = google_mail;
        }

        public String getGoogle_image() {
            return google_image;
        }

        public void setGoogle_image(String google_image) {
            this.google_image = google_image;
        }

        public String getGoogle_token() {
            return google_token;
        }

        public void setGoogle_token(String google_token) {
            this.google_token = google_token;
        }

        public String getFacebook_token() {
            return facebook_token;
        }

        public void setFacebook_token(String facebook_token) {
            this.facebook_token = facebook_token;
        }

        public String getToken_created() {
            return token_created;
        }

        public void setToken_created(String token_created) {
            this.token_created = token_created;
        }

        public String getLogin_logout() {
            return login_logout;
        }

        public void setLogin_logout(String login_logout) {
            this.login_logout = login_logout;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getUser_delete() {
            return user_delete;
        }

        public void setUser_delete(String user_delete) {
            this.user_delete = user_delete;
        }

        public String getUnique_number() {
            return unique_number;
        }

        public void setUnique_number(String unique_number) {
            this.unique_number = unique_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getArrived_time() {
            return arrived_time;
        }

        public void setArrived_time(String arrived_time) {
            this.arrived_time = arrived_time;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getWaiting_time() {
            return waiting_time;
        }

        public void setWaiting_time(String waiting_time) {
            this.waiting_time = waiting_time;
        }

        public String getDone_ride_time() {
            return done_ride_time;
        }

        public void setDone_ride_time(String done_ride_time) {
            this.done_ride_time = done_ride_time;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getWaiting_price() {
            return waiting_price;
        }

        public void setWaiting_price(String waiting_price) {
            this.waiting_price = waiting_price;
        }

        public String getRide_time_price() {
            return ride_time_price;
        }

        public void setRide_time_price(String ride_time_price) {
            this.ride_time_price = ride_time_price;
        }

        public String getCoupan_price() {
            return coupan_price;
        }

        public void setCoupan_price(String coupan_price) {
            this.coupan_price = coupan_price;
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

        public String getDriver_location() {
            return driver_location;
        }

        public void setDriver_location(String driver_location) {
            this.driver_location = driver_location;
        }

        public String getPayment_option_name() {
            return payment_option_name;
        }

        public void setPayment_option_name(String payment_option_name) {
            this.payment_option_name = payment_option_name;
        }

        public String getPeak_time_charge() {
            return peak_time_charge;
        }

        public void setPeak_time_charge(String peak_time_charge) {
            this.peak_time_charge = peak_time_charge;
        }

        public String getNight_time_charge() {
            return night_time_charge;
        }

        public void setNight_time_charge(String night_time_charge) {
            this.night_time_charge = night_time_charge;
        }
    }
}
