package com.example.ma.mybaidumap;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Main2Activity extends Activity {
private Button btn1,btn2;
private TextView tv1,tv2;


    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setview();

        setlistener();

        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        // mLocationClient.setAccessKey("8mrnaFzKu3DoduLnWuB5Lt2w"); //V4.1
        // mLocationClient.setAK("8mrnaFzKu3DoduLnWuB5Lt2w"); //V4.0
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        setLocationOption();
        mLocationClient.start();// 开始定位
    }


    /**
     * 设置相关参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);// 禁止启用缓存定位
//        option.setPoiNumber(5); // 最多返回POI个数
//        option.setPoiDistance(1000); // poi查询距离
//        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            StringBuffer sb = new StringBuffer(256);
            sb.append("当前时间 : ");
            sb.append(location.getTime());
            sb.append("\n错误码 : ");
            sb.append(location.getLocType());
            sb.append("\n纬度 : ");
            sb.append(location.getLatitude());
            sb.append("\n经度 : ");
            sb.append(location.getLongitude());
            sb.append("\n半径 : ");
            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\n速度 : ");
                sb.append(location.getSpeed());
                sb.append("\n卫星数 : ");
                sb.append(location.getSatelliteNumber());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\n地址 : ");
                sb.append(location.getAddrStr());
//            }
            tv1.setText(sb.toString());
            tv2.setText(location.getCountry()+","+location.getProvince()+","+location.getCity()+","+location.getStreet()+",==="+
            location.getAddress()+","+location.getLocationDescribe()+","+location.getDistrict());
            Log.d("info", "onReceiveLocation " + sb.toString());
        }

        public void onReceivePoi(BDLocation poiLocation) {
            // 将在下个版本中去除poi功能
            if (poiLocation == null) {
                return;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("Poi time : ");
            sb.append(poiLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(poiLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(poiLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(poiLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(poiLocation.getRadius());
            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(poiLocation.getAddrStr());
            }
//            if (poiLocation.hasPoi()) {
//                sb.append("\nPoi:");
//                sb.append(poiLocation.getPoi());
//            } else {
//                sb.append("noPoi information");
//            }
//            tv2.setText(sb.toString());
            Log.d("info", "onReceivePoi " + sb.toString());
        }
    }




    private void setlistener() {
        //定位
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationClient != null && mLocationClient.isStarted())
                    mLocationClient.requestLocation();
                else
                    Log.d("info", "locClient is null or not started");
            }
        });

        //请求定位POI
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求POI数据
//                if (mLocationClient != null && mLocationClient.isStarted())
//                { mLocationClient.requestPoi();
//                }
            }
        });
    }

    private void setview() {
        btn1= (Button) findViewById(R.id.button);
        btn2= (Button) findViewById(R.id.button2);

        tv1= (TextView) findViewById(R.id.textView);
        tv2= (TextView) findViewById(R.id.textView2);

    }
}
