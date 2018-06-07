
package com.carflash.user.models;

import java.util.List;

public class ViewCarType {


    /**
     * result : 1
     * msg : [{"car_type_id":"3","car_type_name":"LUXURY","car_name_arabic":"","car_type_name_french":"LUXURY","car_type_image":"uploads/car/editcar_3.png","ride_mode":"1","car_admin_status":"1","city_id":"57"}]
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
         * car_type_id : 3
         * car_type_name : LUXURY
         * car_name_arabic :
         * car_type_name_french : LUXURY
         * car_type_image : uploads/car/editcar_3.png
         * ride_mode : 1
         * car_admin_status : 1
         * city_id : 57
         */

        private String car_type_id;
        private String car_type_name;
        private String car_name_arabic;
        private String car_type_name_french;
        private String car_type_image;
        private String ride_mode;
        private String car_admin_status;
        private String city_id;

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
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

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }
    }
}
