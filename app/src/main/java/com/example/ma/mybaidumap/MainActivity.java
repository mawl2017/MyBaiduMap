package com.example.ma.mybaidumap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class MainActivity extends Activity {
    MapView mMapView = null;
    BaiduMap baiduMap;
    //======================================================================

    //按钮 添加覆盖物
    private Button addOverlayBtn;
    //是否显示覆盖物 1-显示 0-不显示
    private int isShowOverlay = 1;
    //按钮 定位当前位置
    private Button locCurplaceBtn;
    //是否首次定位
    private boolean isFirstLoc = true;
    //定位SDK的核心类
    private LocationClient mLocClient;
    //定位图层显示模式 (普通-跟随-罗盘)
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //定位图标描述
    private BitmapDescriptor mCurrentMarker = null;
    //当前位置经纬度
    private double latitude;
    private double longitude;
    //定位SDK监听函数
    public MyLocationListenner locListener = new MyLocationListenner();

    //查询 地址
    private Button btn_search;
    private EditText et_search;
    SuggestionSearch mSuggestionSearch;


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
//            Log.d(LTAG, "action: " + s);
            TextView text = (TextView) findViewById(R.id.text_Info);
            text.setTextColor(Color.BLACK);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                text.setText("key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
                text.setTextColor(Color.RED);
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                text.setText("key 验证成功! 功能可以正常使用");
                text.setTextColor(Color.GREEN);
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                text.setText("网络出错");
                text.setTextColor(Color.RED);
            }
        }
    }

    private SDKReceiver mReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化 控件
        mMapView = (MapView) findViewById(R.id.bmapView);
        addOverlayBtn = (Button) findViewById(R.id.btn_addpic);
        locCurplaceBtn = (Button) findViewById(R.id.btn_mylocation);

        //查询控件
        et_search= (EditText) findViewById(R.id.et_search);
        btn_search= (Button) findViewById(R.id.btn_search);


        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        baiduMap = mMapView.getMap();
        //设置地图缩放级别16 类型普通地图
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        baiduMap.setMapStatus(msu);
        //卫星图
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //普通图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        setView2();
        //查询地址
        setView3();
        setListener2();
    }

    /**
     * 查询地址
     */
    private void setView3() {
         mSuggestionSearch = SuggestionSearch.newInstance();

        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    Log.d("info","未找到相关结果");
                    return;
                    //未找到相关结果
                }
                Log.d("info","获取在线建议检索结果===="+res.toString());
                //获取在线建议检索结果
            }
        };

        mSuggestionSearch.setOnGetSuggestionResultListener(listener);

    }


    /**
     * 按钮监听方法
     */
    private void setListener2() {
        //Button 添加覆盖物
        addOverlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCircleOverlay();
            }
        });

        //Button 定位当前位置
        locCurplaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyLocation();
            }
        });

        //查询地址
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(et_search.getText().toString())
                            .city("北京"));
                }
            }
        });
    }

    /**
     * 地图定位
     */
    private void setView2() {

        addOverlayBtn.setEnabled(false);
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //定位初始化
        //注意: 实例化定位服务 LocationClient类必须在主线程中声明 并注册定位监听接口
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);              //打开GPS
        option.setCoorType("bd09ll");        //设置坐标类型
        option.setScanSpan(5000);            //设置发起定位请求的间隔时间为5000ms
        mLocClient.setLocOption(option);     //设置定位参数
        mLocClient.start();                  //调用此方法开始定位

    }


    /**
     * 定位SDK监听器 需添加locSDK jar和so文件
     */
    public class MyLocationListenner implements BDLocationListener {

//        @Override
//        public void onReceivePoi(BDLocation location) {
//
//        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapview 销毁后不在处理新接收的位置
            if (location == null || baiduMap == null) {
                return;
            }
            //MyLocationData.Builder定位数据建造器
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            //设置定位数据
            baiduMap.setMyLocationData(locData);
            mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            //获取经纬度
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("info","定位地点=="+latitude+","+longitude);
            //Toast.makeText(getApplicationContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();
            //第一次定位的时候，那地图中心点显示为定位到的位置
            if (isFirstLoc) {
                isFirstLoc = false;
                //地理坐标基本数据结构
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
                //MapStatusUpdate描述地图将要发生的变化
                //MapStatusUpdateFactory生成地图将要反生的变化
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
                baiduMap.animateMapStatus(msu);
                Toast.makeText(getApplicationContext(), location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 定位并添加标注
     */
    private void addMyLocation() {
        //更新
        //跟随定位  FOLLOWING
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        baiduMap.clear();
        addOverlayBtn.setEnabled(true);
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    /**
     * 添加覆盖物
     */
    private void addCircleOverlay() {
        if(isShowOverlay == 1) {  //点击显示
            baiduMap.clear();
            isShowOverlay = 0;
            //DotOptions 圆点覆盖物
            LatLng pt = new LatLng(latitude, longitude);
            CircleOptions circleOptions = new CircleOptions();
            //circleOptions.center(new LatLng(latitude, longitude));
            circleOptions.center(pt);                          //设置圆心坐标
            circleOptions.fillColor(0xAAFFFF00);               //圆填充颜色
            circleOptions.radius(250);                         //设置半径
            circleOptions.stroke(new Stroke(5, 0xAA00FF00));   // 设置边框
            baiduMap.addOverlay(circleOptions);
        }
        else {
            baiduMap.clear();
            isShowOverlay = 1;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocClient.stop();                       //退出时销毁定位
        baiduMap.setMyLocationEnabled(false);   //关闭定位图层
        mMapView.onDestroy();
        mMapView = null;
        mSuggestionSearch.destroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        Log.d("info", "onDestroy");
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        Log.d("info", "onResume");
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        Log.d("info", "onPause");
        mMapView.onPause();
    }
}
