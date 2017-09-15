package com.dashubio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/3 0003
 * 时间： 下午 12:08
 * 描述： 心电图暂时作废2017/09/07
 */

public class EcgPathSecond extends CardiographView {
    public static ArrayList<Float> arrast = new ArrayList();
    public static float moveX = 0;
    public static float moveY = 0;

    public EcgPathSecond(Context context) {
        super(context);
    }

    public EcgPathSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EcgPathSecond(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void addDATA(ArrayList<Float> ecglist) {
        arrast.addAll(ecglist);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth, mHeight/2);
        //用path模拟一个心电图样式
        mPath.moveTo(moveX, moveY);
        //设置画笔style
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        for (int i = 0; i < arrast.size(); i++) {
            moveY =(128- arrast.get(i))*2;//按屏幕比例缩小Y轴比例
            moveX = (float) (moveX + 1);
            mPath.lineTo(moveX, moveY);
            scrollTo((int) moveX,0);
        }
        arrast.clear();
        canvas.drawPath(mPath, mPaint);
        invalidate();
    }

}
