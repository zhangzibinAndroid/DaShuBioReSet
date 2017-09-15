package com.dashubio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 7:29
 * 描述： 心电图折线
 */

public class EcgPathView extends EcgBackGroundView {
    private static final String TAG = "PathView";
    private Context context;

    public ArrayList<Integer> arrast = new ArrayList();
    float tmp = 0;

    public EcgPathView(Context context) {
        super(context);
        this.context = context;
        mPaint = new Paint();
        mPath = new Path();

    }

    public EcgPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EcgPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void addDATA(int data) {
        arrast.add(data);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        drawCanvas(canvas);

    }

    private void drawCanvas(Canvas canvas) {
        canvas.translate(mWidth, mHeight / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        float endY = 0;
        if (arrast.size() != 0) {
            endY = arrast.get(arrast.size() - 1) / 100;//按屏幕比例缩小Y轴比例
            tmp = getWidthes();
            mPath.lineTo(tmp, endY);
            mPath.moveTo(tmp, endY);
        }else {
            initPath();
            invalidate();
            endY = 0;
        }
        canvas.drawPath(mPath, mPaint);
        scrollTo((int) tmp,0);
        invalidate();
    }

    public float getWidthes() {
        return tmp;
    }

    public void setWidthes(float width) {
        this.tmp = width;
    }

    public ArrayList<Integer> getArrast() {
        return arrast;
    }



    //初始化画布
    public void initPath(){
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
    }
}
