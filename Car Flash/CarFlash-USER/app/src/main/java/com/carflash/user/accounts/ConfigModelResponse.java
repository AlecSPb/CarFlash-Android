package com.carflash.user.accounts;

/**
 * Created by lenovo-pc on 5/3/2017.
 */

public class ConfigModelResponse {


    /**
     * result : 1
     * response : {"user_config":{"map_theme":1,"base_url":"http://apporioinfolabs.com/apporiotaxi/api/","image_domain":"http://apporioinfolabs.com/apporiotaxi/","login_base_url":"http://apporioinfolabs.com/","new_rest_api_base":"http://apporioinfolabs.com/Apporiotaxi_new/Rental/Car_Type","PaymentFailedActivity":"1","tail_tracking":"200","refresh_rate_tracking":"3000","padding_factor_tracking_screen_one":"500","padding_factor_tracking_screen_two":"90","set_path_begin_to_end_via_driver_location":true},"driver_config":{"location_update_time_iterval":"5000","map_theme":0,"base_url":"http://apporioinfolabs.com/apporiotaxi/api/","image_domain":"http://apporioinfolabs.com/apporiotaxi/","main_screen_zoom":15,"tracking_tail":"200","track_through_service":true,"track_through_screen":true,"enable_map_camera_touch":true,"account_module":{"new_account_base_url":"http://apporioinfolabs.com/Apporiotaxi_new/","module_type_one":"2","make_driver_photo_maidatory":false}}}
     */

    private int result;
    private ResponseBean response;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * user_config : {"map_theme":1,"base_url":"http://apporioinfolabs.com/apporiotaxi/api/","image_domain":"http://apporioinfolabs.com/apporiotaxi/","login_base_url":"http://apporioinfolabs.com/","new_rest_api_base":"http://apporioinfolabs.com/Apporiotaxi_new/Rental/Car_Type","PaymentFailedActivity":"1","tail_tracking":"200","refresh_rate_tracking":"3000","padding_factor_tracking_screen_one":"500","padding_factor_tracking_screen_two":"90","set_path_begin_to_end_via_driver_location":true}
         * driver_config : {"location_update_time_iterval":"5000","map_theme":0,"base_url":"http://apporioinfolabs.com/apporiotaxi/api/","image_domain":"http://apporioinfolabs.com/apporiotaxi/","main_screen_zoom":15,"tracking_tail":"200","track_through_service":true,"track_through_screen":true,"enable_map_camera_touch":true,"account_module":{"new_account_base_url":"http://apporioinfolabs.com/Apporiotaxi_new/","module_type_one":"2","make_driver_photo_maidatory":false}}
         */

        private UserConfigBean user_config;
        private DriverConfigBean driver_config;

        public UserConfigBean getUser_config() {
            return user_config;
        }

        public void setUser_config(UserConfigBean user_config) {
            this.user_config = user_config;
        }

        public DriverConfigBean getDriver_config() {
            return driver_config;
        }

        public void setDriver_config(DriverConfigBean driver_config) {
            this.driver_config = driver_config;
        }

        public static class UserConfigBean {
            /**
             * map_theme : 1
             * base_url : http://apporioinfolabs.com/apporiotaxi/api/
             * image_domain : http://apporioinfolabs.com/apporiotaxi/
             * login_base_url : http://apporioinfolabs.com/
             * new_rest_api_base : http://apporioinfolabs.com/Apporiotaxi_new/Rental/Car_Type
             * PaymentFailedActivity : 1
             * tail_tracking : 200
             * refresh_rate_tracking : 3000
             * padding_factor_tracking_screen_one : 500
             * padding_factor_tracking_screen_two : 90
             * set_path_begin_to_end_via_driver_location : true
             */

            private int map_theme;
            private String base_url;
            private String image_domain;
            private String login_base_url;
            private String new_rest_api_base;
            private String MainActivity;
            private String tail_tracking;
            private String refresh_rate_tracking;
            private String padding_factor_tracking_screen_one;
            private String padding_factor_tracking_screen_two;
            private boolean set_path_begin_to_end_via_driver_location;

            public int getMap_theme() {
                return map_theme;
            }

            public void setMap_theme(int map_theme) {
                this.map_theme = map_theme;
            }

            public String getBase_url() {
                return base_url;
            }

            public void setBase_url(String base_url) {
                this.base_url = base_url;
            }

            public String getImage_domain() {
                return image_domain;
            }

            public void setImage_domain(String image_domain) {
                this.image_domain = image_domain;
            }

            public String getLogin_base_url() {
                return login_base_url;
            }

            public void setLogin_base_url(String login_base_url) {
                this.login_base_url = login_base_url;
            }

            public String getNew_rest_api_base() {
                return new_rest_api_base;
            }

            public void setNew_rest_api_base(String new_rest_api_base) {
                this.new_rest_api_base = new_rest_api_base;
            }

            public String getMainActivity() {
                return MainActivity;
            }

            public void setMainActivity(String MainActivity) {
                this.MainActivity = MainActivity;
            }

            public String getTail_tracking() {
                return tail_tracking;
            }

            public void setTail_tracking(String tail_tracking) {
                this.tail_tracking = tail_tracking;
            }

            public String getRefresh_rate_tracking() {
                return refresh_rate_tracking;
            }

            public void setRefresh_rate_tracking(String refresh_rate_tracking) {
                this.refresh_rate_tracking = refresh_rate_tracking;
            }

            public String getPadding_factor_tracking_screen_one() {
                return padding_factor_tracking_screen_one;
            }

            public void setPadding_factor_tracking_screen_one(String padding_factor_tracking_screen_one) {
                this.padding_factor_tracking_screen_one = padding_factor_tracking_screen_one;
            }

            public String getPadding_factor_tracking_screen_two() {
                return padding_factor_tracking_screen_two;
            }

            public void setPadding_factor_tracking_screen_two(String padding_factor_tracking_screen_two) {
                this.padding_factor_tracking_screen_two = padding_factor_tracking_screen_two;
            }

            public boolean isSet_path_begin_to_end_via_driver_location() {
                return set_path_begin_to_end_via_driver_location;
            }

            public void setSet_path_begin_to_end_via_driver_location(boolean set_path_begin_to_end_via_driver_location) {
                this.set_path_begin_to_end_via_driver_location = set_path_begin_to_end_via_driver_location;
            }
        }

        public static class DriverConfigBean {
            /**
             * location_update_time_iterval : 5000
             * map_theme : 0
             * base_url : http://apporioinfolabs.com/apporiotaxi/api/
             * image_domain : http://apporioinfolabs.com/apporiotaxi/
             * main_screen_zoom : 15
             * tracking_tail : 200
             * track_through_service : true
             * track_through_screen : true
             * enable_map_camera_touch : true
             * account_module : {"new_account_base_url":"http://apporioinfolabs.com/Apporiotaxi_new/","module_type_one":"2","make_driver_photo_maidatory":false}
             */

            private String location_update_time_iterval;
            private int map_theme;
            private String base_url;
            private String image_domain;
            private int main_screen_zoom;
            private String tracking_tail;
            private boolean track_through_service;
            private boolean track_through_screen;
            private boolean enable_map_camera_touch;
            private AccountModuleBean account_module;

            public String getLocation_update_time_iterval() {
                return location_update_time_iterval;
            }

            public void setLocation_update_time_iterval(String location_update_time_iterval) {
                this.location_update_time_iterval = location_update_time_iterval;
            }

            public int getMap_theme() {
                return map_theme;
            }

            public void setMap_theme(int map_theme) {
                this.map_theme = map_theme;
            }

            public String getBase_url() {
                return base_url;
            }

            public void setBase_url(String base_url) {
                this.base_url = base_url;
            }

            public String getImage_domain() {
                return image_domain;
            }

            public void setImage_domain(String image_domain) {
                this.image_domain = image_domain;
            }

            public int getMain_screen_zoom() {
                return main_screen_zoom;
            }

            public void setMain_screen_zoom(int main_screen_zoom) {
                this.main_screen_zoom = main_screen_zoom;
            }

            public String getTracking_tail() {
                return tracking_tail;
            }

            public void setTracking_tail(String tracking_tail) {
                this.tracking_tail = tracking_tail;
            }

            public boolean isTrack_through_service() {
                return track_through_service;
            }

            public void setTrack_through_service(boolean track_through_service) {
                this.track_through_service = track_through_service;
            }

            public boolean isTrack_through_screen() {
                return track_through_screen;
            }

            public void setTrack_through_screen(boolean track_through_screen) {
                this.track_through_screen = track_through_screen;
            }

            public boolean isEnable_map_camera_touch() {
                return enable_map_camera_touch;
            }

            public void setEnable_map_camera_touch(boolean enable_map_camera_touch) {
                this.enable_map_camera_touch = enable_map_camera_touch;
            }

            public AccountModuleBean getAccount_module() {
                return account_module;
            }

            public void setAccount_module(AccountModuleBean account_module) {
                this.account_module = account_module;
            }

            public static class AccountModuleBean {
                /**
                 * new_account_base_url : http://apporioinfolabs.com/Apporiotaxi_new/
                 * module_type_one : 2
                 * make_driver_photo_maidatory : false
                 */

                private String new_account_base_url;
                private String module_type_one;
                private boolean make_driver_photo_maidatory;

                public String getNew_account_base_url() {
                    return new_account_base_url;
                }

                public void setNew_account_base_url(String new_account_base_url) {
                    this.new_account_base_url = new_account_base_url;
                }

                public String getModule_type_one() {
                    return module_type_one;
                }

                public void setModule_type_one(String module_type_one) {
                    this.module_type_one = module_type_one;
                }

                public boolean isMake_driver_photo_maidatory() {
                    return make_driver_photo_maidatory;
                }

                public void setMake_driver_photo_maidatory(boolean make_driver_photo_maidatory) {
                    this.make_driver_photo_maidatory = make_driver_photo_maidatory;
                }
            }
        }
    }
}
