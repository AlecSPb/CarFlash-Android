package com.carflash.user.models;

/**
 * Created by user on 7/6/2017.
 */

public class ViewCurrentBalanceResponse {


    /**
     * result : 1
     * msg : {"wallet_money":"60"}
     */

    private int result;
    private MsgBean msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * wallet_money : 60
         */

        private String wallet_money;

        public String getWallet_money() {
            return wallet_money;
        }

        public void setWallet_money(String wallet_money) {
            this.wallet_money = wallet_money;
        }
    }
}
