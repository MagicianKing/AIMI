package com.yeying.aimi.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
    /**
     * 字母，数字与特殊符号
     */

    public static final String STR_SPECIAL = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    public static final String ENG_NUM = "[a-zA-Z0-9]";

    /**
     * 是否为null或""
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

	/*
     * private static boolean isValidName(char c){
	 * if(!String.valueOf(c).matches("[a-zA-Z\u4e00-\u9fa5]")){ return false; }
	 * return true; }
	 */

    /**
     * 验证字符串中只含有汉字
     *
     * @param name
     * @return
     */
    public static boolean validateName(String name) {
        boolean res = true;
        int len = name.length();
        for (int i = 0; i < len; i++) {
            if (!isValidName(name.charAt(i))) {
                res = false;
                break;
            }
        }
        return res;
    }

    // 只能为汉字
    private static boolean isValidName(char c) {
        if (!String.valueOf(c).matches("[\u4e00-\u9fa5]")) {
            return false;
        }
        return true;
    }

    /**
     * 验证开户行地址
     *
     * @param bankName
     * @return
     */
    public static boolean validateBankName(String bankName) {
        boolean res = true;
        int len = bankName.length();
        for (int i = 0; i < len; i++) {
            if (!String.valueOf(bankName.charAt(i)).matches(
                    "[0-9a-zA-Z\u4e00-\u9fa5]")) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 验证身份证号码
     *
     * @param idCard
     * @return
     */
    public static boolean validateIdCard(String idCard) {
        boolean res = true;
        String regex = "^(^\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        if (!idCard.matches(regex)) {
            res = false;
        }
        return res;
    }

    public static String combinateString(String[] vars) {
        String ret = null;
        if (vars != null) {
            StringBuffer sb = new StringBuffer();
            for (String var : vars) {
                sb.append(var).append(",");
            }
            ret = sb.toString();
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /*
     * public static boolean isMobileNumber(String mobile) { Pattern p =
     * Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); Matcher
     * m = p.matcher(mobiles); logger.info(m.matches()+"---"); return
     * m.matches(); }
     */
    // 验证手机号
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // 只能是数字验证手机验证码
    public static boolean isNumber(String code) {
        boolean res = true;
        int len = code.length();
        for (int i = 0; i < len; i++) {
            if (!String.valueOf(code.charAt(i)).matches("[0-9]")) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static boolean isAlphOrNum(String str, int min, int max) {
        String reg = "^(\\w{" + min + "," + max + "}){1}$";
        if (isEmpty(str))
            return false;
        return str.matches(reg);
    }

    public static String formatNum(double value) {
        NumberFormat format = NumberFormat.getInstance();
        return format.format(value);
    }

    // 是否大于限定的长度
    public static boolean larMaxLength(String str, int length) {
        if (str.length() > length) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符长度是否在两个数字之间
     *
     * @param str
     * @param little
     * @param big
     * @return
     */
    public static boolean checkBetween(String str, int little, int big) {
        double lenght = str.length();
        if (lenght >= little && lenght <= big) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
            String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
                valueLength += 2;
            } else {
				/* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    // 验证邮箱
    public static boolean isEmail(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 判断字符串是不是全部是英文字母+数字+符号
     *
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM(String str) {
        boolean res = true;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!String.valueOf(str.charAt(i)).matches(STR_SPECIAL)) {
                res = false;
                break;
            }
        }
        return res;

        // return Regular(str, STR_SPECIAL);
    }

    /**
     * 判断字符串是不是全部是英文字母+数字
     *
     * @param str
     * @return boolean
     */
    public static boolean isEnandNum(String str) {
        boolean res = true;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!String.valueOf(str.charAt(i)).matches(ENG_NUM)) {
                res = false;
                break;
            }
        }
        return res;

        // return Regular(str, STR_SPECIAL);
    }

    /**
     * 小写转大写
     *
     * @param str
     * @return
     */
    public static String parseLowerCaseToUpperCase(String str) {
        int size = str.length();
        char[] chs = str.toCharArray();
        for (int i = 0; i < size; i++) {
            if (chs[i] <= 'z' && chs[i] >= 'a') {
                chs[i] = (char) (chs[i] - 32);
            }
        }
        return new String(chs);
    }

    /**
     * 是否包含空格
     *
     * @return
     */
    public static boolean containSpace(String str) {
        if (str.indexOf(" ") >= 0) {
            return true;
        }
        return false;
    }

    public static String formatDouble(double data) {

        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(data);
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    public static String getDatStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String t1 = format.format(date);
        return t1;

    }

    public static Date parseDatStr(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t1 = null;
        try {
            t1 = format.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t1;
    }

}
