package com.dashubio.application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.breathhome_ble_sdk.utils.BreathHomeLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.IBle;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/13 0013
 * 时间： 下午 6:50
 * 描述： 初始化配置
 */


public class DashuApplication extends Application {
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DashuApplication sInstance = null; // 单件对象
    public static DashuApplication dashuhdApplication; // 单件对象
    private BleService mService = null;
    private IBle mBle = null;
    private static List<Activity> lists = new ArrayList<>();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {

            mService = ((BleService.LocalBinder) rawBinder).getService();
            mBle = mService.getBle();
            if (mBle != null && !mBle.adapterEnabled()) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };

    /**
     * 创建函数
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        sInstance = this;
        Intent bindIntent = new Intent(this, BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        dashuhdApplication = this;
        BreathHomeLog.isDebug = true;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

    }

    /**
     * 获取单件对象
     *
     * @return JP应用程序对象
     */
    public static DashuApplication getInstance() {
        return sInstance;
    }

    public IBle getIBle() {
        return mBle;
    }

    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }
}
