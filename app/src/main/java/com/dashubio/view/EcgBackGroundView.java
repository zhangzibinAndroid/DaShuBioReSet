package com.dashubio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/3 0003
 * 时间： 下午 12:08
 * 描述： 心电图背景
 */

public class EcgBackGroundView extends View {
    //画笔
    protected Paint mPaint;
    //折现的颜色
    protected int mLineColor = Color.parseColor("#ffffff");
    //网格颜色
    protected int mGridColor = Color.parseColor("#0a1528");

    //小网格颜色
    protected int mSGridColor = Color.parseColor("#71788c");
    //背景颜色
    protected int mBackgroundColor = Color.parseColor("#32394d");
    //自身的大小
    protected int mWidth,mHeight;

    //网格宽度
    protected int mGridWidth = 50;
    //小网格的宽度
    protected int mSGridWidth = 10;

    //心电图折现
    protected Path mPath ;
    protected Path mPathSecond ;

    public EcgBackGroundView(Context context) {
        this(context,null);

    }

    public EcgBackGroundView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EcgBackGroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
        mPathSecond = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        //画小网格

        //竖线个数
        int vSNum = mWidth /mSGridWidth;

        //横线个数
        int hSNum = mHeight/mSGridWidth;
        mPaint.setColor(mSGridColor);
        mPaint.setStrokeWidth(1);
        //画竖线
        for(int i = 0;i<vSNum+1;i++){
            canvas.drawLine(i*mSGridWidth,0,i*mSGridWidth,mHeight,mPaint);
        }
        //画横线
        for(int i = 0;i<hSNum+1;i++){

            canvas.drawLine(0,i*mSGridWidth,mWidth,i*mSGridWidth,mPaint);
        }

        //竖线个数
        int vNum = mWidth / mGridWidth;
        //横线个数
        int hNum = mHeight / mGridWidth;
        mPaint.setColor(mGridColor);
        mPaint.setStrokeWidth(1);
        //画竖线
        for(int i = 0;i<vNum+1;i++){
            canvas.drawLine(i*mGridWidth,0,i*mGridWidth,mHeight,mPaint);
        }
        //画横线
        for(int i = 0;i<hNum+1;i++){
            canvas.drawLine(0,i*mGridWidth,mWidth,i*mGridWidth,mPaint);
        }
    }

}
