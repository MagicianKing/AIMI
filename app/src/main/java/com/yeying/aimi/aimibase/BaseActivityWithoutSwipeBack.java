package com.yeying.aimi.aimibase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;
import com.yeying.aimi.R;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.MyLifecycleHandler;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/31.
 */

public class BaseActivityWithoutSwipeBack extends FragmentActivity{

    public static final int REQUEST_PERMISSION_CODE = 1;
    private static PermissionCheckedLister mPermissionCheckedLister;
    public final String TAG = "name:\t" + getClass().getSimpleName() + "\tcontent\t";

    public Activity mActivity;

    //定位参数
    private LocationClientOption option = new LocationClientOption();
    private LocationClient mLocation;
    public BDLocationListener myListener = new MyLocationListener();

    //是否在前台
    public static boolean isForeground = true;
    private SessionCache mSession;

    //权限请求
    public static void requestPermissions(String[] permissions, String explan, PermissionCheckedLister permissionCheckedLister) {
        Activity activity = AIMIApplication.getTopActivity();
        mPermissionCheckedLister = permissionCheckedLister;
        List<String> permissionsList = new ArrayList<>();
        if (permissionCheckedLister != null && permissions != null) {

            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
            if (!permissionsList.isEmpty()) {
                //是否弹出提示信息
                if (!TextUtils.isEmpty(explan)) {
                    Toast.makeText(activity, explan, Toast.LENGTH_SHORT).show();
                }
                //申请权限
                ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PERMISSION_CODE);
            } else {
                //所有权限都被允许
                mPermissionCheckedLister.onAllGranted();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AIMIApplication.addActivity(this);
        SystemBarTintManager manager = new SystemBarTintManager(this);
        manager.setStatusBarTintColor(Color.BLACK);
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = this;
        mLocation = new LocationClient(getApplicationContext());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    //统计被拒绝的权限和同意的权限
                    //同意的权限
                    List<String> grantPermissionList = new ArrayList<>();
                    //拒绝的权限
                    List<String> deniedPermissionList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permission);
                        } else {
                            grantPermissionList.add(permission);
                        }
                    }
                    if (deniedPermissionList.isEmpty()) {
                        //全部同意
                        mPermissionCheckedLister.onAllGranted();
                    } else {
                        //同意的部分
                        mPermissionCheckedLister.onGranted(grantPermissionList);
                        //拒绝的部分
                        mPermissionCheckedLister.onDenied(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPermissionCheckedLister = null;
        AIMIApplication.exitActivity(this);
        mLocation.stop();
        super.onDestroy();
    }



    public interface PermissionCheckedLister {
        //成功回调所有权限
        void onAllGranted();

        //如果有权限被拒绝,调用
        //返回通过的权限
        void onGranted(List<String> grantPermissions);

        //返回拒绝的权限
        void onDenied(List<String> deniedPermissions);
    }

    //设置定位参数
    private void setLocationOption(boolean b) {
        if (b) {
            option.setOpenGps(true);//打开gps
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//高精度定位
        } else {
            option.setOpenGps(false);//打开gps
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//网络定位
        }
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(false);
        option.setOpenAutoNotifyMode(3000, 5, 2);//自动检测位置变化,最低间隔三秒,距离5米,敏感程度中等
        mLocation.setLocOption(option);
    }

    public void startLocation(){
        if (mLocation != null){
            if (mLocation.isStarted()) {
                mLocation.stop();
                mLocation.start();
            } else {
                mLocation.start();
            }
        }
    }

    /**
     * 检查定位权限 并开启定位
     * @param isLocationCheck
     */
    public void checkLocationPermission(final boolean isLocationCheck) {
        mLocation.registerLocationListener(myListener);
        //6.0以下不用请求权限,直接检查 GPS
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGPS2Locate(isLocationCheck);
            return;
        }
        //请求定位权限
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, "我们需要获取您的位置已提供更好的服务", new PermissionCheckedLister() {
            @Override
            public void onAllGranted() {
                openGPS2Locate(isLocationCheck);
            }

            @Override
            public void onGranted(List<String> grantPermissions) {

            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                mSession = SessionCache.getInstance(mActivity);
                mSession.locationX = "";
                mSession.locationY = "";
                mSession.save();
                PromptUtils.showToast(mActivity, "您已拒绝此权限,我们可能无法获取您的位置,无法为您提供更好的服务");
            }
        });
    }

    /**
     * 检测GPS是否开启
     * @return
     */
    private boolean checkGPS() {
        //判断GPS是否打开
        LocationManager locationManager
                = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }

    /**
     * 打开GPS
     * @param isLocationCheck
     */
    private void openGPS2Locate(boolean isLocationCheck){
        if (!checkGPS()) {//GPS 没打开
            mSession = SessionCache.getInstance(mActivity);
            mSession.locationX = "";
            mSession.locationY = "";
            mSession.save();
            if (!isLocationCheck){//跳转 GPS 设置
                skipGPS();
            }
            return;
        }else {//GPS 打开
            setLocationOption(true);
            if (!mLocation.isStarted()) {
                mLocation.start();//开始定位
            }
        }
    }

    /**
     * 跳转GPS
     */
    private void skipGPS() {
        final SessionCache session = SessionCache.getInstance(mActivity);
        //提示设置定位
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setCancelable(false);
        dialog.setMessage("请打开定位服务，允许AIMI使用GPS");
        dialog.setTitle("定位失败");
        //确认
        dialog.setPositiveButton("去设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //跳转设置界面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 598); // 设置完成后返回到原来的界面
                    }
                });
        //取消
        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.show();
        session.dingwei = 1;
        session.save();
    }

    /**
     * 定位服务
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {//GPS 定位方式,才能获取经纬度
                    double latd = location.getLatitude();//纬度
                    double lond = location.getLongitude();//经度
                    SessionCache session = SessionCache.getInstance(mActivity);
                    session.locationX = String.valueOf(lond);
                    session.locationY = String.valueOf(latd);
                    session.save();
                    mLocation.stop();
                    if (Activity_BaPing.flag){
                        intoCatList();
                        Activity_BaPing.flag = false;
                    }
                } else {
                    SessionCache session = SessionCache.getInstance(mActivity);
                    session.locationX = String.valueOf(0);
                    session.locationY = String.valueOf(0);
                    session.save();
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyLifecycleHandler.isApplicationInForeground()){
            isForeground = true;
        }else{
            isForeground = false;
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MyLifecycleHandler.isApplicationInForeground()){
            isForeground = true;
        }else{
            isForeground = false;
        }
    }

    public void intoCatList(){

    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null)
            return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
