package com.yeying.aimi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 *
 * @author weixiang.qin
 */
public class Utils {

    public static final int MIDDLE_IMG = 0;
    public static final int SMALL_IMG = 1;
    /**
     * 退出client
     *
     * @param mActivity
     */
//	public static void exitClient(Activity mActivity) {
//		((Palmapplication) mActivity.getApplicationContext()).exit();
//	}

    public static String[] patterns = new String[]{"yyyy-MM-dd hh:mm:ss", "yyyyMMddhhmmss"};
    private static double EARTH_RADIUS = 6378137.0;
    private static long lastClickTime;// 上次点击时间
    private static int IMAGE_MAX_WIDTH = 480;
    private static int IMAGE_MAX_HEIGHT = 960;

    public static void initSSLAllWithHttpClient(String url) throws ClientProtocolException, IOException {
        int timeOut = 30 * 1000;
        HttpParams param = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(param, timeOut);
        HttpConnectionParams.setSoTimeout(param, timeOut);
        HttpConnectionParams.setTcpNoDelay(param, true);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", TrustAllSSLSocketFactory.getDefault(), 443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(param, registry);
        DefaultHttpClient client = new DefaultHttpClient(manager, param);

        HttpGet request = new HttpGet(url);
        // HttpGet request = new HttpGet("https://www.alipay.com/");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        Log.e("HTTPS TEST", result.toString());
    }

    /**
     * 判断是否是旧版本
     */
    public static boolean isOldVer(String locVer, String checkVer) {
        boolean isOld = false;
        try {
            if (getIntVer(locVer) < getIntVer(checkVer)) {
                isOld = true;
            } else {
                isOld = false;
            }

        } catch (Exception e) {
        }
        return isOld;
    }

    /**
     * 将版本号有string转为int
     */
    public static int getIntVer(String ver) {
        int num = 0;
        num = Integer.parseInt(ver.replace(".", ""));
        return num;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);

        // 获取文件大小

        long length = file.length();

        if (length > Integer.MAX_VALUE) {

            // 文件太大，无法读取

            throw new IOException("File is to large " + file.getName());

        }

        // 创建一个数据来保存文件数据

        byte[] bytes = new byte[(int) length];

        // 读取数据到byte数组中

        int offset = 0;

        int numRead = 0;

        while (offset < bytes.length

                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {

            offset += numRead;

        }

        // 确保所有数据均被读取

        if (offset < bytes.length) {

            throw new IOException("Could not completely read file "
                    + file.getName());

        }

        // Close the input stream and return bytes

        is.close();

        return bytes;

    }

    /**
     * 根据猫币得到可提现金额
     */
    public static double getPresentMoney(double money) {
        money = money * 0.07;
        double fei = (money * 0.005);
        if (fei < 1) {//支付宝手续费小于1元算作1元
            fei = 1;
        }
        money = money - fei;
        if (money < 0) {
            money = 0;
        }
        return money;
    }

    public static Bitmap decodeFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inSampleSize = 2;//代表容量变为以前容量的1/2
        options.inPreferredConfig = Config.RGB_565;
        try {
            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(
                    options, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        options.inJustDecodeBounds = false;
        if (filePath != null) {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;

    }

    /**
     * 根据原图地址获取中图和小图
     */
    public static String getImage(int type, String url) {
        if (url != null && url.length() > 4) {
            String end = url.substring(url.length() - 4, url.length());
            if (type == MIDDLE_IMG) {
                url = url.replace(end, "-m" + end);
            } else {
                url = url.replace(end, "-s" + end);
            }
        }

        return url;
    }

    //人民币换算猫币(1元等于10猫币)
    public static double getMbNumber(double money) {
        money = money * 10;
        return money;
    }

    /*
     * 计算被嵌套的listview的高度设置LayoutParams
     * */
    public static void setListViewLayoutParams(Adapter adapter, ListView listView) {
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            try {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception e) {

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * double去掉小数点
     */
    public static String doubleFormat(double d) {
//		String s = String.valueOf(d);
//		String newD = s.substring(0,s.indexOf("."));

        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(2);
        ddf1.setGroupingUsed(false);
        DecimalFormat df = new DecimalFormat("#");

        return ddf1.format(d);
    }

    /**
     * double保留两位小数
     */
    public static String doubleFormat2(double d) {

        DecimalFormat df = new DecimalFormat("######0.00");

        return df.format(d);
    }

    public static int getNumByIndex(String str, int index) {
        int num = 0;
        int from = 0;
        int end = 1;
        if (index >= 0) {
            from = index;
            end = index + 1;
        }
        if (!TextUtils.isEmpty(str) && index < str.length()) {
            num = Integer.parseInt(str.substring(from, end));
        }

        return num;
    }

    /**
     * 获取相差天数
     */
    public static long dateDiff(String startTime, String endTime, String format) {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        //获得两个时间的毫秒时间差异
        long diff = 0;
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        return day;
    }

    /**
     * 获得星座
     */
    public static String getStarSeat(int mouth, int day) {
        String starSeat = null;
        if ((mouth == 3 && day >= 21) || (mouth == 4 && day <= 19)) {
            starSeat = "白羊座";
        } else if ((mouth == 4 && day >= 20) || (mouth == 5 && day <= 20)) {
            starSeat = "金牛座";
        } else if ((mouth == 5 && day >= 21) || (mouth == 6 && day <= 21)) {
            starSeat = "双子座";
        } else if ((mouth == 6 && day >= 22) || (mouth == 7 && day <= 22)) {
            starSeat = "巨蟹座";
        } else if ((mouth == 7 && day >= 23) || (mouth == 8 && day <= 22)) {
            starSeat = "狮子座";
        } else if ((mouth == 8 && day >= 23) || (mouth == 9 && day <= 22)) {
            starSeat = "处女座";
        } else if ((mouth == 9 && day >= 23) || (mouth == 10 && day <= 23)) {
            starSeat = "天秤座";
        } else if ((mouth == 10 && day >= 24) || (mouth == 11 && day <= 22)) {
            starSeat = "天蝎座";
        } else if ((mouth == 11 && day >= 23) || (mouth == 12 && day <= 21)) {
            starSeat = "射手座";
        } else if ((mouth == 12 && day >= 22) || (mouth == 1 && day <= 19)) {
            starSeat = "魔蝎座";
        } else if ((mouth == 1 && day >= 20) || (mouth == 2 && day <= 18)) {
            starSeat = "水瓶座";
        } else if ((mouth == 2 && day >= 19) || (mouth == 3 && day <= 20)) {
            starSeat = "双鱼座";
        } else {
            starSeat = "";
        }
        return starSeat;
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    //根据经纬度计算距离（返回单位是m）
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static String compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 70;//个人喜欢从70开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 600) {
            baos.reset();
            options -= 5;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    /**
     * 图片模糊处理s
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * 重新登录环信
     *//*
    public static void ReLoginHuanxin(String userId) {
        if (EMChat.getInstance().isLoggedIn()) {
            Log.i("重新登录环信", "当前是登录状态");
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
        } else {
            EMChatManager.getInstance().login(userId, userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    Log.i("重新登录环信", "重新登录成功");
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.i("重新登录环信", "重登聊天服务器失败！");
                }
            });

        }
    }*/

    /**
     * 返回时间差，负数返回空串
     */
    public static String getDateDiffer(String str) {
        String time = "";
        if (str == null || "".equals(str)) {
            return time;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try

        {
            Date d1 = df.parse(str);
            Date d2 = new Date();
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            time = days + "天" + hours + ":" + minutes + "分";
            if (days < 0 || hours < 0 || minutes < 0) {
                time = "";
            }
        } catch (Exception e) {
            return "";
        }

        return time;
    }

    /**
     * 自适应字体大小
     */
    public static int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        // screenWidth = screenWidth > screenHeight ?
        int rate;
        screenWidth:
        // screenHeight;
        rate = (int) (textSize * (float) screenHeight / 960);
        return rate;
    }

    /**
     * Spinner适配器
     *
     * @param arrayId
     * @return
     */
//	public static ArrayAdapter<CharSequence> createSpinnerAdapter(Activity mActivity, int arrayId) {
//		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(mActivity, R.layout.spinner_item, mActivity
//				.getResources().getStringArray(arrayId));
//		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//		return adapter;
//	}


    /**
     * 是否为null或""
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 默认适配器
     *
     * @param arrayId
     * @return
     */
    public static ArrayAdapter<CharSequence> createAdapter(Activity mActivity, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, arrayId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * 获取状态栏高度
     *
     * @param ctx
     * @return
     */
    public static int getTop(Activity ctx) {
        Rect rect = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 获取屏幕宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx) {
        return ctx.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕宽度  单位  dp
     *
     * @param ctx
     * @return
     */
    public static float getScreenWidthDm(Context ctx) {
        return ctx.getResources().getDisplayMetrics().xdpi;
    }

    /**
     * 获取屏幕高度
     *
     * @param ctx
     * @return
     */
    public static int getScreenHeight(Context ctx) {
        return ctx.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int convertDipOrPx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 对话框LayoutParams
     *
     * @return
     */
    public static LayoutParams getLayoutParams(Context ctx) {
        return new LayoutParams((int) (getScreenWidth(ctx) * 0.9), LayoutParams.WRAP_CONTENT);
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param cls
     */
    @SuppressWarnings("rawtypes")
    public static void sendIntent(Context context, Class cls) {
        context.startActivity(new Intent(context, cls));
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param cls
     */
    @SuppressWarnings("rawtypes")
    public static void sendIntent(Context context, Class cls, Bundle extras) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    /**
     * 替换fragment
     *
     * @param activity
     * @param fragmentId
     * @param fragment
     */
    public static void replaceFragment(FragmentActivity activity, int fragmentId, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentId, fragment);
        transaction.commit();
    }

    /**
     * 替换fragment
     *
     * @param oldFragment
     * @param newFragment
     * @param fragmentId
     */
    public static void replaceFragment(Fragment oldFragment, Fragment newFragment, int fragmentId) {
        FragmentManager manager = oldFragment.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentId, newFragment);
        transaction.commit();
    }

    /**
     * 获取客户端版本号
     *
     * @param ctx
     * @return
     */
    public static String getVersion(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        String versionName = "-1";
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            if (packInfo != null)
                versionName = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 手机是否联网 需要android.permission.ACCESS_NETWORK_STATE权限
     *
     * @param context
     * @return
     */
    public static boolean isNetWorking(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * @param context
     * @param id
     * @param icon
     * @param ticker
     * @param title
     * @param content
     * @param intent
     */
    public static void showNotification(Context context, int id, int icon, String ticker, String title, String content,
                                        Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, ticker, System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // notification.setLatestEventInfo(context, title, content, pendingIntent);
        notificationManager.notify(id, notification);

    }

    /**
     * 获取时间
     *
     * @param time
     * @return
     */
    public static String getNormalTime(String time) {
        String result = "";
        if (isEmpty(time) || time.length() < 10) {
            return result;
        }
        result = time.substring(0, 10);
        return result;
    }

    /**
     * 字符串为空,返回""
     *
     * @param value
     * @return
     */
    public static String getValue(String value) {
        return isEmpty(value) ? "" : value;
    }

    /**
     * ScrollView与ListView兼容问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = view.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param view
     */
    public static void showKeyBoard(Context context, View view) {
        try {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = view.getWindowToken();
            if (windowToken != null) {
                im.showSoftInput(view, 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getDateTime(int year, int month, int day) {
        return year + "-" + appendZero(String.valueOf(month)) + "-" + appendZero(String.valueOf(day));
    }

    /**
     * 获取intent string参数
     *
     * @param mActivity
     * @param savedInstanceState
     * @param paramName
     * @return
     */
    public static String getIntentStringParams(Activity mActivity, Bundle savedInstanceState, String paramName) {
        return savedInstanceState != null ? savedInstanceState.getString(paramName) : mActivity.getIntent()
                .getStringExtra(paramName);
    }

    /**
     * 获取intent boolean参数
     *
     * @param mActivity
     * @param savedInstanceState
     * @param paramName
     * @return
     */
    public static boolean getIntentBooleanParams(Activity mActivity, Bundle savedInstanceState, String paramName,
                                                 boolean defaultValue) {
        return savedInstanceState != null ? savedInstanceState.getBoolean(paramName) : mActivity.getIntent()
                .getBooleanExtra(paramName, defaultValue);
    }

    /**
     * 获取intent参数
     *
     * @param mActivity
     * @param savedInstanceState
     * @param paramName
     * @return
     */
    public static ArrayList<String> getIntentStringArrayList(Activity mActivity, Bundle savedInstanceState,
                                                             String paramName) {
        return savedInstanceState != null ? savedInstanceState.getStringArrayList(paramName) : mActivity.getIntent()
                .getStringArrayListExtra(paramName);
    }

    /**
     * 获取intent Serializable参数
     *
     * @param mActivity
     * @param savedInstanceState
     * @param paramName
     * @return
     */
    public static Serializable getIntentSerializableParams(Activity mActivity, Bundle savedInstanceState,
                                                           String paramName) {
        return savedInstanceState != null ? savedInstanceState.getSerializable(paramName) : mActivity.getIntent()
                .getSerializableExtra(paramName);
    }

    /**
     * 验证验证码是否正确
     *
     * @param intputMobile
     * @param mobile
     * @param inputCode
     * @param mobileCode
     * @return
     */
    public static boolean validateMobileCode(String intputMobile, String mobile, String inputCode, String mobileCode) {
        if (!intputMobile.equals(mobile) || !inputCode.equals(mobileCode)) {
            return false;
        }
        return true;
    }

    /**
     * 输入框获取焦点
     *
     * @param et
     */
    public static void requestFocus(EditText et) {
        et.requestFocus();
        et.setSelection(et.getText().length());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + appendZero(c.get(Calendar.MONTH) + 1 + "") + "-"
                + appendZero(c.get(Calendar.DAY_OF_MONTH) + "");
    }

    /**
     * 隐藏手机号
     *
     * @param mobile
     * @return
     */
//	public static String hideMobile(String mobile) {
//		if (StringUtil.isEmpty(mobile)) {
//			return "";
//		} else if (CheckValidateUtils.isPhoneNum(mobile)) {
//			char[] mobiles = (Utils.getValue(mobile)).toCharArray();
//			for (int i = 3; i < mobiles.length - 4; i++) {
//				mobiles[i] = '*';
//			}
//			return new String(mobiles);
//		} else {
//			return mobile;
//		}
//	}

    /**
     * 前面补零
     *
     * @param str
     * @return
     */
    public static String appendZero(String str) {
        return "00".substring(str.length()) + str;
    }

    /**
     * 身份证字母X转换成大写
     *
     * @param idNoEt
     */
    public static void formatIdNo(final EditText idNoEt) {
        idNoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    String value = s.toString();
                    if (value.indexOf("x") > 0) {
                        idNoEt.removeTextChangedListener(this);
                        idNoEt.setText(StringUtil.parseLowerCaseToUpperCase(s.toString()));
                        idNoEt.setSelection(s.length());
                        idNoEt.addTextChangedListener(this);
                    }
                }
            }
        });
    }

    /**
     * 小写转大写
     *
     */
    public static void formatUpper(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    et.removeTextChangedListener(this);
                    et.setText(StringUtil.parseLowerCaseToUpperCase(s.toString()));
                    et.setSelection(s.length());
                    et.addTextChangedListener(this);
                }
            }
        });
    }

    /**
     * 隐藏真实姓名
     *
     * @param realName
     * @return
     */
    public static String hideRealName(String realName) {
        if (StringUtil.isEmpty(realName)) {
            return "";
        }
        char[] realNames = (Utils.getValue(realName)).toCharArray();
        for (int i = 0; i < realNames.length - 1; i++) {
            realNames[i] = '*';
        }
        return new String(realNames);
    }

    /**
     * 隐藏银行卡号
     *
     * @param bankCard
     * @return
     */
    public static String hideBankCard(String bankCard) {
        if (StringUtil.isEmpty(bankCard)) {
            return "";
        }
        char[] bankCards = (Utils.getValue(bankCard)).toCharArray();
        for (int i = 4; i < bankCards.length - 4; i++) {
            bankCards[i] = '*';
        }
        return new String(bankCards);
    }

    /**
     * 隐藏身份证号
     *
     * @param idCard
     * @return
     */
    public static String hideIdCard(String idCard) {
        if (StringUtil.isEmpty(idCard)) {
            return "";
        }
        char[] idCards = (Utils.getValue(idCard)).toCharArray();
        for (int i = 4; i < idCards.length - 3; i++) {
            idCards[i] = '*';
        }
        return new String(idCards);
    }

    /**
     * 获取开始时间
     *
     * @param startTime
     * @return
     */
    public static String getStartTime(String startTime) {
        if (isEmpty(startTime)) {
            return "";
        }
        return startTime + " 00:00:00";
    }

    /**
     * 获取结束时间
     *
     * @return
     */
    public static String getEndTime(String endTime) {
        if (isEmpty(endTime)) {
            return "";
        }
        return endTime + " 23:59:59";
    }

    /**
     * 是否快速双击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        if (lastClickTime == 0) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD > 0 && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 短信分享
     *
     * @param mActivity
     * @param msgId
     */
    public static void shareSms(Activity mActivity, int msgId) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", mActivity.getResources().getString(msgId));
        mActivity.startActivity(mIntent);
    }

    /**
     * 社交分享
     *
     * @param mActivity
     * @param contentId
     */
    /*public static void shareEmail(Activity mActivity, int contentId) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.more_share));
        emailIntent.putExtra(Intent.EXTRA_TEXT, mActivity.getResources().getString(contentId));
        mActivity.startActivity(Intent.createChooser(emailIntent,
                mActivity.getResources().getString(R.string.more_share)));
    }*/

    /**
     * 拨打电话
     *
     * @param mActivity
     * @param phone
     */
    public static void dialPhone(Activity mActivity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        mActivity.startActivity(intent);
    }

    /**
     * 设置gprs网络
     *
     * @param mActivity
     */
    public static void settingGprsNetwork(Activity mActivity) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT > 19) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
        }
        mActivity.startActivity(intent);
    }

    /**
     * 设置wifi网络
     *
     * @param mActivity
     */
    public static void settingWifiNetwork(Activity mActivity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        mActivity.startActivity(intent);
    }

    @SuppressLint("SimpleDateFormat")
    public static CharSequence format(String time, String pattern) {
        SimpleDateFormat sdfDes = new SimpleDateFormat(pattern);
        String result = time;
        for (String p : patterns) {
            try {
                result = sdfDes.format(new SimpleDateFormat(p).parse(time));
                return result;
            } catch (Exception e) {
                continue;
            }
        }
        return result;
    }

    /**
     * set image src 解决OOM
     *
     * @param imageView
     * @param imagePath
     */
    public static void setImageSrc(ImageView imageView, String imagePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = getImageScale(imagePath);
        Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
        imageView.setImageBitmap(bm);
    }

    /**
     * scale image to fixed height and weight
     *
     * @param imagePath
     * @return
     */
    private static int getImageScale(String imagePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        // set inJustDecodeBounds to true, allowing the caller to query the
        // bitmap info without having to allocate the
        // memory for its pixels.
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, option);

        int scale = 1;
        while (option.outWidth / scale >= IMAGE_MAX_WIDTH || option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
            scale *= 2;
        }
        return scale;
    }

    /**
     * 2个数组合并
     */
    public static String[] combineString(String[] oderStr, String[] desStr) {
        if (oderStr == null || desStr == null) {
            return null;
        }
        String[] result = new String[oderStr.length + desStr.length];
        System.arraycopy(oderStr, 0, result, 0, oderStr.length);
        System.arraycopy(desStr, 0, result, oderStr.length, desStr.length);

        return desStr;

    }

    /**
     * 分割数组
     */
    public static Map<String, String[]> spiltString(String[] oderStr, int length1, int length2, int length3) {
        if (oderStr == null) {
            return null;
        }

        String[] result = new String[length1];
        String[] result2 = new String[length2];
        String[] result3 = new String[length3];

        System.arraycopy(oderStr, 0, result, 0, length1);
        System.arraycopy(oderStr, length1, result2, 0, length2);
        System.arraycopy(oderStr, length1 + length2, result3, 0, length3);

        HashMap<String, String[]> map = new HashMap<String, String[]>();
        map.put("num", result);
        map.put("num2", result2);
        map.put("num3", result3);

        return map;

    }

    public static Map<String, String[]> spiltString(String[] oderStr, int length1, int length2) {
        if (oderStr == null) {
            return null;
        }

        String[] result = new String[length1];
        String[] result2 = new String[length2];

        System.arraycopy(oderStr, 0, result, 0, length1);
        System.arraycopy(oderStr, length1, result2, 0, length2);

        HashMap<String, String[]> map = new HashMap<String, String[]>();
        map.put("num", result);
        map.put("num2", result2);

        return map;

    }

    /**
     * email格式验证
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


	/*
     * 隐藏邮箱账号
	 */
    public static String hideEmail(String eMail) {
        if (StringUtil.isEmpty(eMail)) {
            return "";
        }
        String[] data = Utils.getValue(eMail).split("@");
        char[] F = data[0].toCharArray();
        String S = data[1];
        for (int i = 0; i < F.length - 3; i++) {
            F[i] = '*';
        }
        String result = new String(F);
        result = result + "@" + S;
        return result;
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        if (bitmap == null)
            return null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return b;
        } catch (Exception e) {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e1) {
                }
        }
        return null;
    }


    public static boolean isAppAction(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给handler发送消息
     *
     * @param msg
     */
    public static void sendMessageToHandler(Handler handler, int msg) {
        Message message = new Message();
        message.what = msg;
        handler.sendMessage(message);
    }

    public static void startService(Context context) {
        Intent i = new Intent("com.hexin.agam.view.START_NOTIFYSERVICE");
        context.startService(i);
    }

    public static void stopService(Context context) {
        // Intent i = new Intent("com.ssports.o2o.view.START_NOTIFYSERVICE");
        // context.stopService(i);
    }

//	public static String hideUsername(String userName) {
//		if (StringUtil.isEmpty(userName)) {
//			return "";
//		} else if (CheckValidateUtils.isPhoneNum(userName)) {
//			char[] mobiles = (Utils.getValue(userName)).toCharArray();
//			for (int i = 3; i < mobiles.length - 4; i++) {
//				mobiles[i] = '*';
//			}
//			return new String(mobiles);
//		} else {
//			char[] usernames = userName.toCharArray();
//			int len = usernames.length;
//			for (int i = 0; i < len; i++) {
//				if (i > 0 && i < len - 1) {
//					usernames[i] = '*';
//				}
//			}
//			return new String(usernames);
//		}
//	}

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置窗口背景透明度
     *
     * @param ctx
     * @param alp
     */
    public static void settingsWindowAlpha(Activity ctx, float alp) {
        WindowManager.LayoutParams lp = ctx.getWindow().getAttributes();
        lp.alpha = alp;
        ctx.getWindow().setAttributes(lp);
    }

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }


}
