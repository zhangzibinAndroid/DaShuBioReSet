package com.dashubio.ecg_single.ble;

public class JPBleDataHandler {

	/**
	 * 转换流数据到普通数据
	 *            刘数据
	 * @return 普通数据对象
	 */
	public static JPBleNormalData rawDataToNormalData(final byte[] raw) {
		if (null == raw) {
			return null;
		}

		JPBleNormalData data = new JPBleNormalData();
		data.rhythm = convertByteToInt(raw[3]);
		data.pulse = convertByteToInt(raw[4]);
		data.sysdif = convertByteToInt(raw[5]);
		data.dias = convertByteToInt(raw[6]);
		data.mean = convertByteToInt(raw[7]);
		data.oxygen = convertByteToInt(raw[8]);
		data.resp = convertByteToInt(raw[10]);
		data.st = convertByteToInt(raw[11]) / 100.0f;
		data.sys = data.dias + data.sysdif;
		data.timestamp = System.currentTimeMillis();
		return data;
	}

	/**
	 * 转换流数据到血压数据
	 *
	 * @param data
	 *            刘数据
	 * @return 血压数据对象
	 */
	public static JPBlePressureData rawDataToPressureData(final byte[] raw) {
		if (null == raw) {
			return null;
		}

		JPBlePressureData data = new JPBlePressureData();
		data.rhythm = convertByteToInt(raw[3]);
		data.pulse = convertByteToInt(raw[4]);
		data.sysdif = convertByteToInt(raw[5]);
		data.dias = convertByteToInt(raw[6]);
		data.mean = convertByteToInt(raw[7]);
		data.oxygen = convertByteToInt(raw[8]);
		data.resp = convertByteToInt(raw[10]);
		data.st = convertByteToInt(raw[11]) / 100.0f;
		data.event = convertByteToInt(raw[13]);
		data.sys = data.dias + data.sysdif;
		data.timestamp = System.currentTimeMillis();
		return data;
	}
	
	/**
	 * 转换byte为int
	 * @param data
	 * @return
	 */
	private static int convertByteToInt(final byte data){
		return data & 0xff;
	}
}