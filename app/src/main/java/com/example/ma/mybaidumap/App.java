package com.example.ma.mybaidumap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

/**
 * Created by ma on 2017/9/14.
 */
public class App extends Application {
    public static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        SDKInitializer.initialize(this);
        x.Ext.init(this);
    }

    public static App getApp(){
        return context;
    }
}
