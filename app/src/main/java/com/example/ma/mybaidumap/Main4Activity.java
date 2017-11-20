package com.example.ma.mybaidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class Main4Activity extends Activity {

    MapView mMapView4 = null;
    ImageButton imb_local;
    BaiduMap baiduMap4;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner4 myListener = new MyLocationListenner4();
    boolean isFirstLoc = true; // 是否首次定位


    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        imb_local= (ImageButton) findViewById(R.id.imb_local);
        //初始化 显示地图
        setview1();

    }
    private void setview1() {
        mMapView4= (MapView) findViewById(R.id.baidu_mapView4);
        baiduMap4 = mMapView4.getMap();
        //设置地图缩放级别16 类型普通地图
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        baiduMap4.setMapStatus(msu);
        //开启交通图
//        baiduMap4.setTrafficEnabled(true);

        //开启 热力图
//        baiduMap4.setBaiduHeatMapEnabled(true);

        //普通地图
        baiduMap4.setMapType(BaiduMap.MAP_TYPE_NORMAL);

//卫星地图
//        baiduMap4.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

//空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        baiduMap4.setMapType(BaiduMap.MAP_TYPE_NONE);


        // 将底图标注设置为隐藏，方法如下：
        baiduMap4.showMapPoi(true);


        // 开启定位图层
        baiduMap4.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        setlistener();

    }

    private void setlistener() {
        imb_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跟随 模式
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                baiduMap4
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));

//                //普通模式
//                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
//                baiduMap4
//                        .setMyLocationConfigeration(new MyLocationConfiguration(
//                                mCurrentMode, true, mCurrentMarker));

//                //罗盘模式
//                mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
//                baiduMap4
//                        .setMyLocationConfigeration(new MyLocationConfiguration(
//                                mCurrentMode, true, mCurrentMarker));
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner4 implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView4 == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap4.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap4.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }



    @Override
    protected void onPause() {
        mMapView4.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView4.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap4.setMyLocationEnabled(false);
        mMapView4.onDestroy();
        mMapView4 = null;
        super.onDestroy();
    }

}
