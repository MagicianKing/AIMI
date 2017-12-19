package com.yeying.aimi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10307;
import com.yeying.aimi.service.bean.PositionBean;
import com.yeying.aimi.storage.LocationCache;
import com.yeying.aimi.storage.SessionCache;

/**
 * 定位service
 *
 * @author shahuaitao
 */
public class PostionService extends Service {

    public BDLocationListener myListener = new MyLocationListener();
    PositionBean position = new PositionBean();
    private Context context;
    private LocationBinder locationBinder = new LocationBinder();
    private LocationClient mLocation;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("postionService", "onBind start");
        return locationBinder;
    }

    @Override
    public void onCreate() {
        Log.i("postionService", "onCreate start");
        context = this;
        mLocation = new LocationClient(this.getApplicationContext());
        mLocation.registerLocationListener(myListener);
        setLocationOption();
        if (!mLocation.isStarted()) {
            mLocation.start();
        }
    }

    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);//打开gps
        option.setLocationMode(LocationMode.Battery_Saving);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(false);
        option.setOpenAutoNotifyMode(3000, 5, 2);//自动检测位置变化,最低间隔三秒,距离5米,敏感程度中等
        mLocation.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        Log.i("postionService", "onDestroy start");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public class LocationBinder extends Binder {

        public PositionBean getPosition() {
            return position;
        }

    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {


            if (location.getLocType() == 161) {
                position.setSet(0);
                SessionCache session = SessionCache.getInstance(context);

                session.dingwei = position.getSet();
                session.save();
            } else {
                position.setSet(1);
                SessionCache session = SessionCache.getInstance(context);

                session.dingwei = position.getSet();
                session.save();
            }
           /* StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());//获得当前时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());//获得erro code得知定位现状
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获得纬度
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//获得经度
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {//通过GPS定位
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());//获得速度
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append(location.getDistrict());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                sb.append(location.getDirection());//获得方位
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//通过网络连接定位
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                sb.append("\ndirection : ");
                sb.append(location.getDistrict());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());//获得经营商？
            }*/
            position.setLatd(location.getLatitude());
            position.setLond(location.getLongitude());
            position.setCucity(location.getCity());
            position.setDistrict(location.getDistrict());
            position.setTime(System.currentTimeMillis());


            LocationCache cache = LocationCache.getLocationCache(context);

            if (cache.positionbean == null) {
                cache.positionbean = new PositionBean();
            }
            cache.positionbean.setLatd(location.getLatitude());
            cache.positionbean.setLond(location.getLongitude());
            cache.positionbean.setCucity(location.getCity());
            cache.positionbean.setDistrict(location.getDistrict());
            cache.positionbean.setTime(System.currentTimeMillis());
            cache.positionbean.setSet(position.getSet());
            cache.save();
            SessionCache session = SessionCache.getInstance(context);

            if (session.userId != null && session.sessionId != null) {
                P10307 p = new P10307();
                p.req.userId = session.userId;
                p.req.sessionId = session.sessionId;
                p.req.location_x = String.valueOf(cache.positionbean.getLond());
                p.req.location_y = String.valueOf(cache.positionbean.getLatd());
                new LocationTask().execute(context, p);
            }


//			latd=location.getLatitude();//纬度
//			lond=location.getLongitude();//经度
//			cuproce=location.getProvince();
//			cucity=location.getCity();
//			distist=location.getDistrict();
//			ad_nametext.setText(location.getAddrStr());	
//			String cy=location.getCity();
//			String ds=location.getDistrict();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    public class LocationTask extends DefaultTask {

        public void onError(DefaultError obj) {
            super.onError(obj);
            Log.i("PostionService", "用户位置定位10307接口调用失败");
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            Log.i("", "用户位置定位10307接口调用成功");
        }
    }

}
