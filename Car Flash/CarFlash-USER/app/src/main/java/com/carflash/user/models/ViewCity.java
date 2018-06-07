
package com.carflash.user.models;

import java.util.List;

public class ViewCity {


    /**
     * result : 1
     * msg : [{"city_id":"3","city_name":"Dummy City","city_latitude":"","city_longitude":"","currency":"20AC","distance":"Miles","city_name_arabic":"","city_name_french":"","city_admin_status":"1"},{"city_id":"56","city_name":"Gurugram","city_latitude":"","city_longitude":"","currency":"0930","distance":"Miles","city_name_arabic":"","city_name_french":"","city_admin_status":"1"},{"city_id":"57","city_name":"Delhi","city_latitude":"","city_longitude":"","currency":"0930","distance":"Miles","city_name_arabic":"","city_name_french":"","city_admin_status":"1"},{"city_id":"71","city_name":"Florida","city_latitude":"","city_longitude":"","currency":"00A4","distance":"Miles","city_name_arabic":"","city_name_french":"","city_admin_status":"1"},{"city_id":"77","city_name":"Bursa","city_latitude":"40.1885281","city_longitude":"29.0609636","currency":"TRY","distance":"Kilometers","city_name_arabic":"","city_name_french":"","city_admin_status":"1"},{"city_id":"78","city_name":"Lagos","city_latitude":"6.5243793","city_longitude":"3.3792057","currency":"NGN","distance":"Kilometers","city_name_arabic":"","city_name_french":"","city_admin_status":"1"}]
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
         * city_id : 3
         * city_name : Dummy City
         * city_latitude :
         * city_longitude :
         * currency : 20AC
         * distance : Miles
         * city_name_arabic :
         * city_name_french :
         * city_admin_status : 1
         */

        private String city_id;
        private String city_name;
        private String city_latitude;
        private String city_longitude;
        private String currency;
        private String distance;
        private String city_name_arabic;
        private String city_name_french;
        private String city_admin_status;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_latitude() {
            return city_latitude;
        }

        public void setCity_latitude(String city_latitude) {
            this.city_latitude = city_latitude;
        }

        public String getCity_longitude() {
            return city_longitude;
        }

        public void setCity_longitude(String city_longitude) {
            this.city_longitude = city_longitude;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getCity_name_arabic() {
            return city_name_arabic;
        }

        public void setCity_name_arabic(String city_name_arabic) {
            this.city_name_arabic = city_name_arabic;
        }

        public String getCity_name_french() {
            return city_name_french;
        }

        public void setCity_name_french(String city_name_french) {
            this.city_name_french = city_name_french;
        }

        public String getCity_admin_status() {
            return city_admin_status;
        }

        public void setCity_admin_status(String city_admin_status) {
            this.city_admin_status = city_admin_status;
        }
    }
}
