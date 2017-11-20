package com.example.ma.mybaidumap;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class Main3Activity extends Activity {

    MapView mMapView3 = null;
    BaiduMap baiduMap3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mMapView3= (MapView) findViewById(R.id.baidu_mapView3);

        baiduMap3 = mMapView3.getMap();
        //设置地图缩放级别16 类型普通地图
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        baiduMap3.setMapStatus(msu);
        //卫星图
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //普通图
        baiduMap3.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        setView();
        setListener();

    }





    private void setListener() {
        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        baiduMap3.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
                Log.d("info", "333拖拽中");
            }
            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                Log.d("info","333拖拽结束");
            }
            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
                Log.d("info","333开始拖拽");
            }
        });
    }

    private void setView() {
        // 1，显示图标
        //定义Maker坐标点
        LatLng point = new LatLng(39.963175, 116.400244);
//        LatLng point = new LatLng(4.9E-324, 4.9E-324);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap).zIndex(9).draggable(true);
        //在地图上添加Marker，并显示
        baiduMap3.addOverlay(option);
        //将marker添加到地图上
        Marker  marker = (Marker) (baiduMap3.addOverlay(option));

    }
}
