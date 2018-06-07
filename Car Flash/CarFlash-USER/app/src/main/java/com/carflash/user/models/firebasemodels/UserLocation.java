package com.carflash.user.models.firebasemodels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lenovo-pc on 5/25/2017.
 */

@IgnoreExtraProperties
public class UserLocation {


    public String user_id;
    public String user_current_latitude;
    public String user_current_longitude;
    public String user_location_text;
    public String timestamp;
    public String device_name;


    public String first_name;
    public String last_name;
    public String phone;
    public String normal_email;
    public String password;
    public String online_offline;
    public String device_id;
    public String facebook_id;
    public String facebook_mail;
    public String facebook_image;
    public String facebook_first_name;
    public String facebook_last_name;
    public String google_id;
    public String google_name;
    public String google_mail ;
    public String google_image;
    public String login_type;


    public UserLocation() {
    }

    public UserLocation(String user_id ,
                        String user_current_latitude ,
                        String user_current_longitude ,
                        String user_location_text,
                        String timestamp ,
                        String device_name,
                        String first_name,
                        String last_name,
                        String phone,
                        String normal_email,
                        String password,
                        String online_offline,
                        String device_id,
                        String facebook_id,
                        String facebook_mail,
                        String facebook_image,
                        String facebook_first_name,
                        String facebook_last_name,
                        String google_id,
                        String google_name,
                        String google_mail,
                        String google_image,
                        String login_type
    ) {



        this.user_id = user_id ;
        this.user_current_latitude = user_current_latitude ;
        this.user_current_longitude = user_current_longitude ;
        this.user_location_text = user_location_text ;
        this.timestamp = timestamp ;
        this.device_name = device_name ;

        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.normal_email = normal_email;
        this.password = password;
        this.online_offline = online_offline;
        this.device_id = device_id;
        this.facebook_id = facebook_id;
        this.facebook_mail = facebook_mail;
        this.facebook_image = facebook_image;
        this.facebook_first_name = facebook_first_name;
        this.facebook_last_name = facebook_last_name;
        this.google_id = google_id;
        this.google_name = google_name;
        this.google_mail = google_mail;
        this.google_image = google_image;
        this.login_type = login_type;

    }



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNormal_email() {
        return normal_email;
    }

    public void setNormal_email(String normal_email) {
        this.normal_email = normal_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOnline_offline() {
        return online_offline;
    }

    public void setOnline_offline(String online_offline) {
        this.online_offline = online_offline;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
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

    public String getFacebook_first_name() {
        return facebook_first_name;
    }

    public void setFacebook_first_name(String facebook_first_name) {
        this.facebook_first_name = facebook_first_name;
    }

    public String getFacebook_last_name() {
        return facebook_last_name;
    }

    public void setFacebook_last_name(String facebook_last_name) {
        this.facebook_last_name = facebook_last_name;
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

    public String getGoogle_image() {
        return google_image;
    }

    public void setGoogle_image(String google_image) {
        this.google_image = google_image;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }












    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_current_latitude() {
        return user_current_latitude;
    }

    public void setUser_current_latitude(String user_current_latitude) {
        this.user_current_latitude = user_current_latitude;
    }

    public String getUser_current_longitude() {
        return user_current_longitude;
    }

    public void setUser_current_longitude(String user_current_longitude) {
        this.user_current_longitude = user_current_longitude;
    }

    public String getUser_location_text() {
        return user_location_text;
    }

    public void setUser_location_text(String user_location_text) {
        this.user_location_text = user_location_text;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }



    public String getGoogle_mail() {
        return google_mail;
    }

    public void setGoogle_mail(String google_mail) {
        this.google_mail = google_mail;
    }





}
