
package com.carflash.user.models;

import java.util.List;

public class ViewCard {


    /**
     * result : 1
     * msg :
     * details : [{"card_id":"73","card_number":"4242","card_type":"Visa"},{"card_id":"74","card_number":"4242","card_type":"Visa"},{"card_id":"75","card_number":"4242","card_type":"Visa"},{"card_id":"76","card_number":"4242","card_type":"Visa"}]
     */

    private int result;
    private String msg;
    private List<DetailsBean> details;

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

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * card_id : 73
         * card_number : 4242
         * card_type : Visa
         */

        private String card_id;
        private String card_number;
        private String card_type;

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }
    }
}
