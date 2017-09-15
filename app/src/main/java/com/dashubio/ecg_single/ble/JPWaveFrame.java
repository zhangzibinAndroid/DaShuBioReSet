package com.dashubio.ecg_single.ble;

/**
 * 波形帧
 * @author Yi
 *
 */
public class JPWaveFrame {
	
	/**
	 * 构造函数
	 */
	public JPWaveFrame(){
	}
	
	/**
	 * 设置数据
	 * @param byteData
	 */
	public JPWaveFrame setData(final byte[] byteData){
		//将byte转为int
		for (int i = 0; i < byteData.length; i++) {
			mPoints[i] = byteData[i] & 0xff;
		}
		
		mCountID = mPoints[2];
		mPoints[2] = 255;
		
		return this;
	}
	
	/**
	 * 获取计数标签
	 * @return
	 */
	public int getCountID(){
		return mCountID;
	}
	
	/**
	 * 获取点
	 * @return
	 */
	public int[] getPoints(){
		return mPoints;
	}
	
	/**
	 * 帧清空
	 */
	public void clean(){
		mCountID = 0;
		for (int i = 0; i < mPoints.length; i++) {
			mPoints[i] = 0;
		}
	}
	
	public void invalid(){
		for (int i = 0; i < mPoints.length; i++) {
			mPoints[i] = 255;
		}
	}
	
	/**
	 * 拷贝帧
	 * @param frame
	 */
	public void copy(final JPWaveFrame frame){
		mCountID = frame.getCountID();
		
		int[] points = frame.getPoints();
		for (int i = 0; i < mPoints.length; i++) {
			mPoints[i] = points[i];
		}
	}
	
	private int mCountID = 0;			//计数标签
	private int[] mPoints = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};		//点数组
}