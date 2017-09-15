package com.dashubio.broadcast;

import android.content.Context;
import android.content.Intent;

import com.breathhome_ble_sdk.broadreceiver.BleStateBroadcast;
import com.breathhome_ble_sdk.broadreceiver.BroadcastResponse;

public class MyBleStateBroadcast extends BleStateBroadcast {

	@Override
	public void setMyBroadcastResponse(BroadcastResponse myBroadcastResponse) {
		// TODO Auto-generated method stub
		super.setMyBroadcastResponse(myBroadcastResponse);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

	}
}
