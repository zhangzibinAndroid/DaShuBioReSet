package com.dashubio.ecg_single.ble;

public class JPBleNormalData {
	//时间戳
	public long timestamp = 0L;
	
	// 心率
	public int rhythm = 0;

	// 脉搏
	public int pulse = 0;

	// 收缩压差
	public int sysdif = 0;

	// 收缩压
	public int sys = 0;

	// 舒张压
	public int dias = 0;

	// 平均压
	public int mean = 0;

	// 血氧
	public int oxygen = 0;

	// 呼吸
	public int resp = 0;

	// ST段数值
	public float st = 0.0f;
}