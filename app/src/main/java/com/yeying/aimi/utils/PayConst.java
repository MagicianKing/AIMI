package com.yeying.aimi.utils;

public class PayConst {

    public static int WEIXIN_NO_SUCCESS = 0;

    public static int WEIXIN_SUCCESS = 1;

    public static int WEIXIN_CAL = 2;

    private static int pay_status = 0;

    private static int pay_fail = 0; //支付失败

    public static int getPay_fail() {
        return pay_fail;
    }

    public static void setPay_fail(int pay_fail) {
        PayConst.pay_fail = pay_fail;
    }

    public static int getPay_status() {
        return pay_status;
    }

    public static void setPay_status(int pay_status) {
        PayConst.pay_status = pay_status;
    }


}
