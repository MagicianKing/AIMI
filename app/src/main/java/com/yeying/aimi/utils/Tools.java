package com.yeying.aimi.utils;import android.Manifest;import android.app.ActivityManager;import android.app.ActivityManager.RunningAppProcessInfo;import android.app.AlertDialog.Builder;import android.app.PendingIntent;import android.content.Context;import android.content.DialogInterface;import android.content.pm.PackageInfo;import android.content.pm.PackageManager;import android.graphics.Bitmap;import android.graphics.Bitmap.Config;import android.graphics.Canvas;import android.graphics.Matrix;import android.graphics.Paint;import android.graphics.PorterDuff.Mode;import android.graphics.PorterDuffXfermode;import android.graphics.Rect;import android.graphics.RectF;import android.os.Environment;import android.os.StatFs;import android.telephony.SmsManager;import android.telephony.TelephonyManager;import android.util.Log;import android.widget.TextView;import java.io.FileInputStream;import java.io.FileNotFoundException;import java.io.FileOutputStream;import java.io.IOException;import java.io.InputStream;import java.io.OutputStream;import java.math.BigInteger;import java.net.MalformedURLException;import java.net.URL;import java.security.MessageDigest;import java.security.NoSuchAlgorithmException;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Collections;import java.util.Date;import java.util.HashMap;import java.util.LinkedList;import java.util.List;import java.util.Map;import static com.yeying.aimi.BuildConfig.BASE_URL;public class Tools {    //http://jiaoningbo.tunnel.qydev.comjiaoningbo.tunnel.qydev.com    public static String BEGIN_URL = BASE_URL;    public static String normal_web = BEGIN_URL + "/hi8_mobile_web/servlet/main.cl";    //public static String normal_web = BEGIN_URL + "/hi8_mobile_web/servlet/main.cl";    /**     * 上传文件请求     */    public static String upload_web = BEGIN_URL + "/hi8_mobile_web/servlet/uploadFile.cl";    public static String HEADURL;//="http://47.93.35.238:10032/hi8_files"    /**     * 本地版本号     */    public static String getClientVersion() {        return "1.1";    }    /**     * generate the md5 key     *     * @param url     * @return     */    public static String genMd5(String url) {        String result = url;        if (url != null) {            try {                MessageDigest md = MessageDigest.getInstance("MD5");                md.update(url.getBytes());                BigInteger hash = new BigInteger(1, md.digest());                result = hash.toString(16);                if ((result.length() % 2) != 0) {                    result = "0" + result;                }            } catch (NoSuchAlgorithmException e) {                e.printStackTrace();            }        }        return result;    }    public static void sendSMS(String phoneNumber, String message) {        /*		 * PendingIntent pi =		 * PendingIntent.getActivity(WhiteSendSmsActivity.this, 0, new		 * Intent(this, WhiteSendSmsActivity.class), 0);		 */        Tools.i("Sending sms:" + phoneNumber + " :" + message);        SmsManager smsManager = SmsManager.getDefault();        ArrayList<String> messages = smsManager.divideMessage(message);        String mServiceCenter = null;        try {            smsManager.sendMultipartTextMessage(phoneNumber, mServiceCenter,                    messages, new ArrayList<PendingIntent>(),                    new ArrayList<PendingIntent>());        } catch (Exception ex) {            ex.printStackTrace();        }    }    public static void stopOtherService(Context ctx) {        PackageManager pm = ctx.getPackageManager();        List<PackageInfo> pkgList = pm                .getInstalledPackages(PackageManager.GET_PERMISSIONS);        List<String> pkgNames = new LinkedList<String>();        if (pkgList != null) {            for (PackageInfo info : pkgList) {                String pkg = info.packageName;                if (pkg.equals("com.viastore.activity")) {                    continue;                }                if (info.requestedPermissions != null)                    for (String permission : info.requestedPermissions) {                        if (permission.equals(Manifest.permission.RECEIVE_SMS)) {                            // stop the package                            Log.i("ViaStore", pkg + " ->" + permission);                            // String packageName=null;                            // packageName=pkg;                            pkgNames.add(pkg);                            // break;                        }                    }            }        }        ActivityManager am = (ActivityManager) ctx                .getSystemService(Context.ACTIVITY_SERVICE);        for (RunningAppProcessInfo service : am.getRunningAppProcesses()) {            if (service.pkgList != null) {                for (String pkg : service.pkgList) {                    if (pkgNames.size() > 0) {                        //                        for (String pkgName : pkgNames) {                            if (pkgName.equals(pkg)) {                                int pid = service.pid;                                am.restartPackage(pkg);                                Log.i("ViaStore", "pkg->" + pkg + " "                                        + service.pid);                                Log.i("ViaStore", " service->"                                        + service.processName);                                Log.i("ViaStore", "should kill the pid:" + pid);                            }                        }                    }                }            }        }    }    public static String getContentType(String filename) {        String ctype = "application/octet-stream";        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")                || filename.endsWith(".JPG") || filename.endsWith(".JPEG")                || filename.endsWith(".png") || filename.endsWith(".PNG")                || filename.endsWith(".bmp") || filename.endsWith(".BMP")                || filename.endsWith(".gif") || filename.endsWith(".GIF")                ) {            int pos = filename.lastIndexOf(".");            String suffix = filename.substring(pos + 1);            ctype = "image/" + suffix;        }        if (filename.endsWith("zip")) {            ctype = "application/octet-stream";        }        return ctype;    }    public static void setText(TextView mText, String content) {        if (content != null && mText != null)            mText.setText(content);    }    public static void i(String msg) {        Log.i("Palm", msg);    }    public static void e(String message) {        Log.e("Palm", message);    }    public static String getMESI(Context mContext) {        String mesi = null;        TelephonyManager manager = (TelephonyManager) mContext                .getSystemService(Context.TELEPHONY_SERVICE);        mesi = manager.getSimSerialNumber();        String subscribledId = manager.getSubscriberId();        Tools.i("SubscirberId:" + subscribledId + "  mesi:" + mesi);        return mesi;    }    public static String getSize(long size) {        // B,K,M        String ret = null;        size = 1024 * size;        if (size < 1024) {            float f = (float) (size / 1024.0);            ret = String.format("%.02f K", f);        } else if (size < 1024 * 1024) {            float f = (float) (size / (1024));            ret = String.format("%.02f K", f);        } else if (size < 1024 * 1024 * 1024) {            float f = (float) (size / (1024 * 1024));            ret = String.format("%.02f M", f);        }        return ret;    }    ;    public static Bitmap resizeBitmap(Bitmap originalBmp, int viewWidth,                                      int viewHeight) {        // DisplayMetrics displaymetrics = new DisplayMetrics();        // ((Activity)        // mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);        // int screenHeight = displaymetrics.heightPixels;        // int screenWidth = displaymetrics.widthPixels;        int bmpWidth = originalBmp.getWidth();        int bmpHeight = originalBmp.getHeight();        double heightScale = (viewHeight * 1.0) / (bmpHeight * 1.0);        double widthScale = (viewWidth * 1.0) / (bmpWidth * 1.0);        // double widthScale = 1.6;        // double heightScale = 1.0;        float scaleWidth = 1;        float scaleHeight = 1;        scaleWidth = (float) (scaleWidth * widthScale);        scaleHeight = (float) (scaleHeight * heightScale);        Matrix matrix = new Matrix();        matrix.postScale(scaleWidth, scaleHeight);        Bitmap resizeBmp = Bitmap.createBitmap(originalBmp, 0, 0, bmpWidth,                bmpHeight, matrix, true);        return resizeBmp;    }    public static void showAlertOk(Context mContext, int resinfo, int restext) {        showAlert(mContext, resinfo, restext, true);    }    public static void showAlertOK(Context context, String title, String text) {        showAlert(context, title, text, true);    }    public static void showAlert(Context mContext, int resinfo, int restext) {        showAlert(mContext, resinfo, restext, false);    }    public static void showAlert(Context context, String title, String text) {        showAlert(context, title, text, false);    }    public static void showAlert(Context mContext, int resinfo, int restext,                                 boolean isOk) {        showAlert(mContext, mContext.getString(resinfo),                mContext.getString(restext), isOk);    }    public static void showAlert(Context context, String title, String text,                                 boolean isOk) {        Builder alertBuilder = new Builder(context);        alertBuilder.setTitle(title);        alertBuilder.setMessage(text);        // android.R.string        alertBuilder.setNegativeButton(isOk ? android.R.string.ok                        : android.R.string.cancel,                new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface arg0, int arg1) {                        // TODO Auto-generated method stub                        arg0.dismiss();                    }                });        alertBuilder.create().show();    }    public static String unEncode(String str) {        if (str != null) {            while (str.indexOf("&amp;") != (-1)) {                str = str.replaceAll("&amp;", "&");            }            str = str.replaceAll("&lt;", "<");            str = str.replaceAll("&gt;", ">");            str = str.replaceAll("&nbsp;", " ");            str = str.replaceAll("&quot;", "\"");            str = str.replaceAll("\r\n", "\n");        }        return str;    }    public static Map<String, String> getQueryMap(String query) {        String[] params = query.split("&");        Map<String, String> map = new HashMap<String, String>();        for (String param : params) {            String name = param.split("=")[0];            String value = param.split("=")[1];            map.put(name, value);        }        return map;    }    public static Map<String, String> getParametersFromUrl(String url) {        Map<String, String> params = null;        try {            URL uri = new URL(url);            String query = uri.getQuery();            params = getQueryMap(query);        } catch (MalformedURLException e) {            // TODO Auto-generated catch block            e.printStackTrace();        }        return params;    }    public static int indexOfRev(String str, char c) {        int pos = -1;        for (int i = str.length() - 1; i >= 0; i--) {            if (str.charAt(i) == c) {                pos = i;                break;            }        }        return pos;    }    public static void chmod(String permission, String path) {        try {            String command = "chmod " + permission + " " + path;            Runtime runtime = Runtime.getRuntime();            Process proc = runtime.exec(command);            // Util.log(proc.get)        } catch (IOException e) {            e.printStackTrace();        }    }    public static void copyFile(String localPath, String string) {        Tools.i("copyFile" + localPath + " to " + string);        // TODO Auto-generated method stub        InputStream is = null;        OutputStream out = null;        try {            is = new FileInputStream(localPath);            out = new FileOutputStream(string);            byte[] buf = new byte[1024];            int size;            while ((size = is.read(buf)) > 0) {                out.write(buf, 0, size);            }            out.flush();        } catch (FileNotFoundException e) {            // TODO Auto-generated catch block            e.printStackTrace();        } catch (IOException e) {            // TODO Auto-generated catch block            e.printStackTrace();        } finally {            if (is != null) {                try {                    is.close();                } catch (IOException e) {                    // TODO Auto-generated catch block                    e.printStackTrace();                }            }            if (out != null) {                try {                    out.close();                } catch (IOException e) {                    // TODO Auto-generated catch block                    e.printStackTrace();                }            }        }    }    // 2012-06-09T19:35:31.267+08:00    public static String parseTimestamp(String timestamp) {        // String dtStart = "2010-10-15T09:27:37Z";        String ret = null;        if (timestamp != null && timestamp.contains("T")) {            ret = timestamp.substring(0, timestamp.length() - 6);            ret = ret.replace("T", " ");            ret = ret.substring(0, 19);        }        return ret;    }    public static String currentTime() {        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");        String date = format.format(new Date());        return date;    }    public static boolean isAvaiableSpace(int sizeMb) {        boolean ishasSpace = false;        if (Environment.getExternalStorageState().equals(                Environment.MEDIA_MOUNTED)) {            String sdcard = Environment.getExternalStorageDirectory().getPath();            StatFs statFs = new StatFs(sdcard);            long blockSize = statFs.getBlockSize();            long blocks = statFs.getAvailableBlocks();            long availableSpare = (blocks * blockSize) / (1024 * 1024);            if (availableSpare > sizeMb) {                ishasSpace = true;            }        } else {            ishasSpace = true;        }        return ishasSpace;    }    public static int[] parseXingYunWuLin(String prizeBall) {        int[] balls = new int[6];        String[] entries = prizeBall.split(",");        Tools.i("prizeBall:" + prizeBall);        if (prizeBall.indexOf(",") > 0)            for (int i = 0; i < entries.length; i++) {                balls[i] = Integer.parseInt(entries[i]);            }        return balls;    }    public static String parseXingYunWuLinTime(String time) {        String returnTime = time;        if (time != null) {            int firstPosition = time.indexOf("-");            int secondPosition = time.lastIndexOf(":");            if (firstPosition > 0 && secondPosition > 0) {                returnTime = time.substring(firstPosition + 1, secondPosition);            }        }        return returnTime;    }    public static long parseTime(String currenttime) {        // TODO Auto-generated method stub        long time = 0L;        if (currenttime != null && currenttime.length() > 18) {            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");            try {                Date date = format.parse(currenttime);                time = date.getTime();            } catch (ParseException e) {                e.printStackTrace();            }        }        return time;    }    public static String parseString(String prizeBall, String spilt) {        String[] balls = prizeBall.split(spilt);        List<String> prizeList = new ArrayList<String>();        for (int i = 0; i < balls.length; i++) {            prizeList.add(balls[i]);        }        Collections.sort(prizeList);        StringBuffer sb = new StringBuffer();        for (int i = 0; i < prizeList.size(); i++) {            sb.append(prizeList.get(i));            if (i < prizeList.size() - 1) {                sb.append(" ");            }        }        return sb.toString();    }    public static String[] parseSSQPrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        List<String> prizeList = new ArrayList<String>();        for (int i = 0; i <= 5; i++) {            prizeList.add(balls[i]);        }        Collections.sort(prizeList);        prizeList.add(balls[6]);        if (balls.length > 7) {            prizeList.add(balls[7]);        }//		prizeList.add("16");        return prizeList.toArray(new String[0]);    }    public static String[] parseQLCPrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        List<String> prizeList = new ArrayList<String>();        for (int i = 0; i <= 6; i++) {            prizeList.add(balls[i]);        }        Collections.sort(prizeList);        prizeList.add(balls[7]);        return prizeList.toArray(new String[0]);    }    public static String[] parseXJXLCPrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        List<String> prizeList = new ArrayList<String>();        for (int i = 0; i <= 6; i++) {            prizeList.add(balls[i]);        }        Collections.sort(prizeList);        return prizeList.toArray(new String[0]);    }    public static String[] parseXJSSCPrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        List<String> prizeList = new ArrayList<String>();        for (int i = 0; i <= 4; i++) {            prizeList.add(balls[i]);        }        Collections.sort(prizeList);        return prizeList.toArray(new String[0]);    }    public static String[] parseD3PrizeBall(String prizeBall) {        String[] balls = prizeBall.split("\\|");        return balls;    }    public static String[] parsePrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        for (int i = 0; i < balls.length - 1; i++) {            for (int j = 0; j < balls.length - 1; j++) {                if (Integer.valueOf(balls[i]) < Integer.valueOf(balls[j])) {                    String temp = null;                    temp = balls[j];                    balls[j] = balls[i];                    balls[i] = temp;                }            }        }        return balls;    }    public static String[] parseK3PrizeBall(String prizeBall) {        String[] balls = prizeBall.split(",");        return new String[]{String.valueOf(Integer.parseInt(balls[0])),                String.valueOf(Integer.parseInt(balls[1])),                String.valueOf(Integer.parseInt(balls[2]))};    }    public static String[] formatSSQandQLCPrizeBall(String prizeBall) {        String[] balls = parsePrizeBall(prizeBall);        String[] redblue = new String[2];        redblue[0] = "";        for (int i = 0; i < balls.length - 1; i++) {            redblue[0] += " " + balls[i];        }        redblue[1] = balls[balls.length - 1];        return redblue;    }    public static String formatD3PrizeBall(String prizeBall) {        String[] balls = prizeBall.split("\\|");        for (int i = 0; i < balls.length; i++) {            for (int j = 0; j < balls.length; j++) {                if (Integer.valueOf(balls[i]) < Integer.valueOf(balls[j])) {                    String temp = null;                    temp = balls[j];                    balls[j] = balls[i];                    balls[i] = temp;                }            }        }        String redblue = "";        for (int i = 0; i < balls.length; i++) {            redblue += " " + balls[i];        }        return redblue;    }    public static String formatK3PrizeBall(String prizeBall) {        return prizeBall.replace(",", " ").replace("0", "");    }    public static String formatDate(String time) {        if (time == null || "".equals(time))            return "";        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");        SimpleDateFormat format2 = new SimpleDateFormat("yyy-MM-dd");        try {            Date date = format.parse(time);            return format2.format(date);        } catch (ParseException e) {            // TODO Auto-generated catch block            e.printStackTrace();        }        return null;    }    /**     * 转换图片成圆�?	 *     *     * @param bitmap 传入Bitmap对象     * @return     */    public static Bitmap toRoundBitmap(Bitmap bitmap) {        int width = bitmap.getWidth();        int height = bitmap.getHeight();        float roundPx;        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;        if (width <= height) {            roundPx = width / 2;            top = 0;            bottom = width;            left = 0;            right = width;            height = width;            dst_left = 0;            dst_top = 0;            dst_right = width;            dst_bottom = width;        } else {            roundPx = height / 2;            float clip = (width - height) / 2;            left = clip;            right = width - clip;            top = 0;            bottom = height;            width = height;            dst_left = 0;            dst_top = 0;            dst_right = height;            dst_bottom = height;        }        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);        Canvas canvas = new Canvas(output);        final int color = 0xff424242;        final Paint paint = new Paint();        final Rect src = new Rect((int) left, (int) top, (int) right,                (int) bottom);        final Rect dst = new Rect((int) dst_left, (int) dst_top,                (int) dst_right, (int) dst_bottom);        final RectF rectF = new RectF(dst);        paint.setAntiAlias(true);        canvas.drawARGB(0, 0, 0, 0);        paint.setColor(color);        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));        canvas.drawBitmap(bitmap, src, dst, paint);        return output;    }}