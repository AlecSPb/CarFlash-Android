package com.carflash.user.rentalmodule.taxirentalmodule;

/**
 * Created by lenovo-pc on 6/29/2017.
 */

public class RentalRidenowModel {

    /**
     * status : 1
     * message : Car Booked
     * details : {"rental_booking_id":"29","user_id":"63","rentcard_id":"3","car_type_id":"2","booking_type":"1","driver_id":"0","pickup_lat":"28.412050404626836","pickup_long":"77.04334728419781","pickup_location":"68, Plaza Street,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122018,","booking_date":"Thursday, Jun 29","booking_time":"08:08 AM","user_booking_date_time":"Thursday, Jun 29, 08:08 AM","last_update_time":"","booking_status":"10","booking_admin_status":"1"}
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
         * rental_booking_id : 29
         * user_id : 63
         * rentcard_id : 3
         * car_type_id : 2
         * booking_type : 1
         * driver_id : 0
         * pickup_lat : 28.412050404626836
         * pickup_long : 77.04334728419781
         * pickup_location : 68, Plaza Street,Block S, Uppal Southend, Sector 49,Gurugram, Haryana 122018,
         * booking_date : Thursday, Jun 29
         * booking_time : 08:08 AM
         * user_booking_date_time : Thursday, Jun 29, 08:08 AM
         * last_update_time :
         * booking_status : 10
         * booking_admin_status : 1
         */

        private String rental_booking_id;
        private String user_id;
        private String rentcard_id;
        private String car_type_id;
        private String booking_type;
        private String driver_id;
        private String pickup_lat;
        private String pickup_long;
        private String pickup_location;
        private String booking_date;
        private String booking_time;
        private String user_booking_date_time;
        private String last_update_time;
        private String booking_status;
        private String booking_admin_status;

        public String getRental_booking_id() {
            return rental_booking_id;
        }

        public void setRental_booking_id(String rental_booking_id) {
            this.rental_booking_id = rental_booking_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRentcard_id() {
            return rentcard_id;
        }

        public void setRentcard_id(String rentcard_id) {
            this.rentcard_id = rentcard_id;
        }

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
        }

        public String getBooking_type() {
            return booking_type;
        }

        public void setBooking_type(String booking_type) {
            this.booking_type = booking_type;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
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

        public String getBooking_date() {
            return booking_date;
        }

        public void setBooking_date(String booking_date) {
            this.booking_date = booking_date;
        }

        public String getBooking_time() {
            return booking_time;
        }

        public void setBooking_time(String booking_time) {
            this.booking_time = booking_time;
        }

        public String getUser_booking_date_time() {
            return user_booking_date_time;
        }

        public void setUser_booking_date_time(String user_booking_date_time) {
            this.user_booking_date_time = user_booking_date_time;
        }

        public String getLast_update_time() {
            return last_update_time;
        }

        public void setLast_update_time(String last_update_time) {
            this.last_update_time = last_update_time;
        }

        public String getBooking_status() {
            return booking_status;
        }

        public void setBooking_status(String booking_status) {
            this.booking_status = booking_status;
        }

        public String getBooking_admin_status() {
            return booking_admin_status;
        }

        public void setBooking_admin_status(String booking_admin_status) {
            this.booking_admin_status = booking_admin_status;
        }
    }
}
