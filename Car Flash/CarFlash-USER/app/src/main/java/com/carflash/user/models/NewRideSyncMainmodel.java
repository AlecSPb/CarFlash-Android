package com.carflash.user.models;

/**
 * Created by lenovo-pc on 7/24/2017.
 */

public class NewRideSyncMainmodel {

    /**
     * result : 1
     * msg : Ride Accepted by Driver
     * details : {"ride_id":"158","ride_status":"3"}
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
         * ride_id : 158
         * ride_status : 3
         */

        private String ride_id;
        private String ride_status;

        public String getRide_id() {
            return ride_id;
        }

        public void setRide_id(String ride_id) {
            this.ride_id = ride_id;
        }

        public String getRide_status() {
            return ride_status;
        }

        public void setRide_status(String ride_status) {
            this.ride_status = ride_status;
        }
    }
}
