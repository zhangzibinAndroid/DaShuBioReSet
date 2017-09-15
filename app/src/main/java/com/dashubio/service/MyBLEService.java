package com.dashubio.service;

import android.content.Intent;
import android.os.IBinder;

import com.breathhome_ble_sdk.service.BLEService;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/16 0016
 * 时间： 下午 12:23
 * 描述： 呼吸机
 */
public class MyBLEService extends BLEService {

	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	
}
