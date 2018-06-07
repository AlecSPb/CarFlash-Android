
package com.carflash.user.models;

import java.util.List;

public class CancelReasonCustomer {


    /**
     * result : 1
     * msg : [{"reason_id":"2","reason_name":"Driver refuse to come","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"},{"reason_id":"3","reason_name":"Driver is late","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"},{"reason_id":"4","reason_name":"I got a lift","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"},{"reason_id":"5","reason_name":"Driver ask to cancel","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"},{"reason_id":"6","reason_name":"Driver too far","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"},{"reason_id":"7","reason_name":"Other ","reason_name_arabic":"","reason_name_french":"0","reason_type":"1","cancel_reasons_status":"1"}]
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
         * reason_id : 2
         * reason_name : Driver refuse to come
         * reason_name_arabic :
         * reason_name_french : 0
         * reason_type : 1
         * cancel_reasons_status : 1
         */

        private String reason_id;
        private String reason_name;
        private String reason_name_arabic;
        private String reason_name_french;
        private String reason_type;
        private String cancel_reasons_status;

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getReason_name() {
            return reason_name;
        }

        public void setReason_name(String reason_name) {
            this.reason_name = reason_name;
        }

        public String getReason_name_arabic() {
            return reason_name_arabic;
        }

        public void setReason_name_arabic(String reason_name_arabic) {
            this.reason_name_arabic = reason_name_arabic;
        }

        public String getReason_name_french() {
            return reason_name_french;
        }

        public void setReason_name_french(String reason_name_french) {
            this.reason_name_french = reason_name_french;
        }

        public String getReason_type() {
            return reason_type;
        }

        public void setReason_type(String reason_type) {
            this.reason_type = reason_type;
        }

        public String getCancel_reasons_status() {
            return cancel_reasons_status;
        }

        public void setCancel_reasons_status(String cancel_reasons_status) {
            this.cancel_reasons_status = cancel_reasons_status;
        }
    }
}
