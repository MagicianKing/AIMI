package com.yeying.aimi.utils;

import java.math.BigDecimal;


/**
 * 工具类  包括 double格式的处理
 *
 * @author shahuaitao
 */
public class ToolUtil {

    /**
     * d1 + d2 function:
     *
     * @param d1
     * @param d2
     * @return
     * @since JDK 1.6
     */
    public static double add(double d1, double d2) {

        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.add(b2).doubleValue();
    }

    /**
     * d1 - d2 function:
     *
     * @param d1
     * @param d2
     * @return
     * @since JDK 1.6
     */
    public static double subtract(double d1, double d2) {

        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.subtract(b2).doubleValue();
    }

    /**
     * d1 * d2 ,保留scale 位小数 function:
     *
     * @param d1
     * @param d2
     * @param scale
     * @return
     * @since JDK 1.6
     */
    public static double multiply(double d1, double d2, int scale) {

        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.multiply(b2).divide(new BigDecimal(1), scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * d1 / d2 ,保留scale 位小数 function:
     *
     * @param d1
     * @param d2
     * @param scale
     * @return
     * @since JDK 1.6
     */
    public static double divide(double d1, double d2, int scale) {

        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * d1 * d2 function:
     *
     * @param d1
     * @param d2
     * @return
     * @since JDK 1.6
     */
    public static double multiply(double d1, double d2) {

        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.multiply(b2).doubleValue();
    }

}
