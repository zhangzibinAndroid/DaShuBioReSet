package com.dashubio.ecg_single.ble;

import java.util.HashMap;
import java.util.Map;

public class JPBleUtils {
	public static Map<String, String> BLE_SERVICES = new HashMap<String, String>();
	public static Map<String, String> BLE_CHARACTERISTICS = new HashMap<String, String>();
	
	static {
		BLE_SERVICES.put("0000FFA0-0000-1000-8000-00805F9B34FB", "Get data Service");
		BLE_SERVICES.put("00001802-0000-1000-8000-00805F9B34FB", "Get pressure Service");
		
		BLE_CHARACTERISTICS.put("0000FFA1-0000-1000-8000-00805F9B34FB", "Enable");
		BLE_CHARACTERISTICS.put("0000FFA3-0000-1000-8000-00805F9B34FB", "Data");
		BLE_CHARACTERISTICS.put("0000FFA4-0000-1000-8000-00805F9B34FB", "Wave1");
		BLE_CHARACTERISTICS.put("0000FFA5-0000-1000-8000-00805F9B34FB", "Wave2");
		BLE_CHARACTERISTICS.put("00002A06-0000-1000-8000-00805F9B34FB", "Pressure");
	}
	
	private static JPBleUtils INSTANCE;
	
	private JPBleUtils() {}
	
	public static JPBleUtils getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JPBleUtils();
		}
		return INSTANCE;
	}
}
